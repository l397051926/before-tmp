package com.gennlife.platform.service;

import com.gennlife.platform.dao.GennMapper;
import com.gennlife.platform.model.GennDataModel;
import com.gennlife.platform.model.GennImage;
import com.gennlife.platform.util.DecryptionZipUtil;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.JsonUtils;
import com.gennlife.platform.util.ZipUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Chenjinfeng on 2017/12/19.
 */
@Service
public class GeneDataService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(GeneDataService.class);
    @Value("${ui.gene.zip.pwd}")
    private String pwd;
    @Value("${ui.gene.zip.workPath}")
    private String workPath;
    @Value("${ui.gene.zip.imagePath}")
    private String imgBaseDir;
    @Value("${ui.gene.zip.pdfPath}")
    private String pdfBaseDir;


    @Autowired
    private GennMapper dao;

    public GennMapper getDao() {
        return dao;
    }

    @Transactional
    public void unZip(File sourcefile) throws Exception {
        String target = "unZip" + UUID.randomUUID().toString();
        String outPath = workPath + "/" + target;
        File md5File = null;
        File subZip = null;
        try {
            DecryptionZipUtil.unzip(sourcefile.getAbsolutePath(), outPath, pwd);
            File file = new File(outPath);
            File[] files = file.listFiles();
            if (files == null || files.length != 1 || !files[0].isDirectory()) {
                logger.error("outpath :" + outPath);
                logger.error("files ==null " + (files == null));
                if (files != null) {
                    logger.error("files.length " + (files.length));
                    for (File tmp : files) {
                        logger.error("item " + tmp.getAbsolutePath());
                    }
                }
                throw new RuntimeException(outPath + " 压缩文件格式不对");
            }
            File subPath = files[0];
            md5File = new File(subPath.getAbsolutePath() + "/md5.txt");
            subZip = new File(subPath.getAbsolutePath() + "/sub.zip");
            String md5 = FileUtils.readFileToString(md5File, "utf-8");
            String targetMd5 = ZipUtils.getMd5ByFile(subZip);
            if (md5 == null || !md5.equalsIgnoreCase(targetMd5)) {
                throw new RuntimeException("md5 校验失败");
            }
            boolean flag = ZipUtils.unZipFiles(subZip, outPath);
            if (flag == false) {
                throw new RuntimeException("zip 解压失败");
            }
            File imgJsonFile = new File(outPath + "/sub/img.json");
            File synJsonFile = new File(outPath + "/sub/syn.json");
            String data = FileUtils.readFileToString(synJsonFile, "utf-8");
            LinkedList<GennDataModel> synList = JsonUtils.fromJson(JsonUtils.toJsonElement(data), new TypeToken<LinkedList<GennDataModel>>() {
            }.getType());
            String[] uniqueIds = getUniqueIds(synList);
            List<GennImage> oldImg = null;
            if (imgJsonFile.exists()) {
                String imgData = FileUtils.readFileToString(imgJsonFile, "utf-8");
                LinkedList<GennImage> list = JsonUtils.fromJson(JsonUtils.toJsonElement(imgData), new TypeToken<LinkedList<GennImage>>() {
                }.getType());
                if (list != null) {
                    oldImg = dao.getAllImage(uniqueIds);
                    dao.deleteAllImage(uniqueIds);
                    for (GennImage img : list) {
                        img.setImgPath(imgBaseDir + "/" + img.getImgId());
                        dao.saveGennImage(img);
                    }
                }
            }

            List<String> pdfs = dao.getAllPdfPath(uniqueIds);
            if (synList != null) {
                for (GennDataModel model : synList) {
                    model.setPdfPath(pdfBaseDir + "/" + model.getPdfId() + ".pdf");
                    dao.upsert(model);
                }
            }
            if (oldImg != null) {
                for (GennImage img : oldImg) {
                    FileUtils.deleteQuietly(new File(img.getImgPath()));
                }
            }
            if (pdfs != null) {
                pdfs.forEach(item -> FileUtils.deleteQuietly(new File(item)));
            }
            File imgPath = new File(outPath + "/sub/img");
            if (!imgPath.exists()) {
                imgPath = new File(outPath + "\\sub\\img");
            }
            File pdfPath = new File(outPath + "/sub/pdf");
            if (!pdfPath.exists()) {
                pdfPath = new File(outPath + "\\sub\\pdf");
            }
            if (imgPath.exists()) {
                FileUtils.copyDirectory(imgPath, new File(imgBaseDir));
            }
            if (pdfPath.exists()) {
                FileUtils.copyDirectory(pdfPath, new File(pdfBaseDir));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // FileUtils.deleteQuietly(new File(outPath));
        }
    }

    public String[] getUniqueIds(Collection<GennDataModel> list) {
        if (list == null || list.size() == 0) return null;
        String[] uniqueIds = new String[list.size()];
        int i = 0;
        for (GennDataModel item : list) {
            uniqueIds[i] = item.getUniqueId();
            i++;
        }
        return uniqueIds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FilesUtils.mkdir(imgBaseDir);
        FilesUtils.mkdir(pdfBaseDir);
        FilesUtils.mkdir(workPath);
    }
}
