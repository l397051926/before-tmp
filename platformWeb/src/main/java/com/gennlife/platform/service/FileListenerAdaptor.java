package com.gennlife.platform.service;

import com.gennlife.platform.model.GennZipLog;
import com.gennlife.platform.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class FileListenerAdaptor extends FileAlterationListenerAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(FileListenerAdaptor.class);
    private GeneDataService geneDataService;


    @Override
    public void onFileCreate(File file) {
        execUnZip(file);
        super.onFileCreate(file);

    }

    public void execUnZip(File file) {
        GennZipLog zipLog = new GennZipLog();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        zipLog.setOpTime(simpleDateFormat.format(new Date()));
        zipLog.setZipName(file.getName());
        try {
            logger.info("start unZip " + file.getAbsolutePath());
            long s = System.currentTimeMillis();
            geneDataService.unZip(file);
            FileUtils.deleteQuietly(file);
            logger.info("unzip " + file.getAbsolutePath() + " " + (System.currentTimeMillis() - s) + " ms");
            zipLog.setZipResult("success");
        } catch (Exception e) {
            zipLog.setZipResult("error :" + e.getMessage());
            logger.error(file.getAbsolutePath() + " unzip error :"+e.getMessage());
        } finally {
            geneDataService.getDao().addZipLog(zipLog);
        }
    }

    @Override
    public void onFileChange(File file) {
        execUnZip(file);
        super.onFileChange(file);
    }

    public void init(GeneDataService geneDataService, String listenDir) {
        logger.info("unzip listen dir " + listenDir);
        this.geneDataService = geneDataService;
        LinkedList<File> list = ZipUtils.getAllZip(listenDir);
        if (list != null) {
            for (File zip : list) {
                execUnZip(zip);
            }
        }
    }
}