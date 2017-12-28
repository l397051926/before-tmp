package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AccessUtils;
import com.gennlife.platform.bean.GennUpResultBean;
import com.gennlife.platform.bean.IpBean;
import com.gennlife.platform.dao.GennMapper;
import com.gennlife.platform.enums.GennMappingEnum;
import com.gennlife.platform.model.GennDataModel;
import com.gennlife.platform.service.FileListenerAdaptor;
import com.gennlife.platform.service.GeneDataService;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chenjinfeng on 2017/12/13.
 */
@Controller
@RequestMapping("/genn")
public class GennController implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(GennController.class);
    private static final String KEY = "code";
    private static final String FS_KEY = "success";
    private static final int SUCCESS = 1;
    private static final boolean FS_SUCCESS = true;
    private static final boolean FS_FAIL = false;
    private static final String ERR_KEY = "error";
    private static final int FAIL = 0;
    @Value("${ui.gene.zip.listenDir}")
    private String listenDir;
    Gson gson = GsonUtil.getGson();
    @Autowired
    GennMapper gennMapper;
    @Autowired
    IpBean ipBean;
    @Autowired
    AccessUtils accessUtils;
    View view = new View();
    @Autowired
    GeneDataService geneDataService;

    @RequestMapping("/img")
    public void image(HttpServletResponse response, HttpServletRequest paramRe,
                      @RequestParam("imageId") String imageId, @RequestParam("patientSn") String patientSn) throws IOException {
        if (checkFail(response, paramRe, imageId, patientSn)) return;
        String match = gennMapper.getOneImagePath(imageId, patientSn);
        if (StringUtils.isEmpty(match)) {
            JsonObject jsonObject = createUnAccess();
            view.setHttpServletResponse(response);
            view.writeResult(GsonUtil.toJsonStr(jsonObject), response);
            return;
        }
        String fileUrl = match;
        File file = new File(fileUrl);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int length = inputStream.read(data);
        inputStream.close();
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }

    private boolean checkFail(HttpServletResponse response, HttpServletRequest paramRe, String id, String patientSn) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(patientSn)) {
            if (response != null) view.viewString("请求参数存在空", response);
            return true;
        }
        if (!accessUtils.checkAccessPatientSn(patientSn, paramRe)) {
            if (response != null) view.viewString("患者编号无权限访问或不存在", response);
            return true;
        }
        return false;
    }

    @RequestMapping("/pdf")
    public ResponseEntity<byte[]> pdf(HttpServletResponse response, HttpServletRequest paramRe,
                                      @RequestParam("pdfId") String pdfId, @RequestParam("patientSn") String patientSn) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        if (checkFail(response, paramRe, pdfId, patientSn)) return new ResponseEntity<byte[]>(null,
                headers, HttpStatus.FORBIDDEN);
        GennDataModel match = gennMapper.getPdfInfo(pdfId, patientSn);
        if (match == null) {
            return new ResponseEntity<byte[]>(null,
                    headers, HttpStatus.FORBIDDEN);
        }
        String fileUrl = match.getPdfPath();
        File file = new File(fileUrl);
        headers.setContentType(MediaType.APPLICATION_PDF);
        String charset = new String(match.getPdfName().getBytes("utf-8"), "iso-8859-1");
        headers.setContentDispositionFormData("file", charset);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.OK);
    }

    public JsonObject createUnAccess() {
        JsonObject jsonObject = new JsonObject();
        setFail(jsonObject);
        jsonObject.addProperty(ERR_KEY, "无权限或无相关记录");
        return jsonObject;

    }

    private void setFail(JsonObject jsonObject) {
        jsonObject.addProperty(KEY, FAIL);
        jsonObject.addProperty(FS_KEY,FS_FAIL);
    }
    private void setSuccess(JsonObject jsonObject) {
        jsonObject.addProperty(KEY, SUCCESS);
        jsonObject.addProperty(FS_KEY,FS_SUCCESS);
    }
    @RequestMapping("/json")
    public
    @ResponseBody
    String json(HttpServletRequest paramRe,
                @RequestParam("uniqueId") String uniqueId, @RequestParam("patientSn") String patientSn) throws IOException {
        if (checkFail(null, paramRe, uniqueId, patientSn)) return GsonUtil.toJsonStr(createUnAccess());
        String match = gennMapper.getJsonData(uniqueId, patientSn);
        if (StringUtils.isEmpty(match)) {
            return GsonUtil.toJsonStr(createUnAccess());
        }
        JsonObject result = new JsonObject();
        result.add("data", GsonUtil.toJsonObject(match));
        setSuccess(result);
        return GsonUtil.toJsonStr(result);
    }

    @RequestMapping("/list")
    public
    @ResponseBody
    String list(@RequestParam("patientSn") String patientSn,
                @RequestParam(value = "visitSn", required = false) String visitSn,
                @RequestParam("page") int page,
                @RequestParam("size") int size, HttpServletRequest paramRe
    ) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;
        JsonObject result = new JsonObject();
        if (!accessUtils.checkAccessPatientSn(patientSn, paramRe)) {
            result.addProperty(ERR_KEY, "患者编号无权限访问或不存在");
            setFail(result);
            return GsonUtil.toJsonStr(result);
        }
        int from = (page - 1) * size;
        if (StringUtils.isEmpty(visitSn)) visitSn = null;
        List<GennDataModel> list = gennMapper.getGennData(from, size, patientSn, visitSn);
        if (list == null || list.size() == 0) {
            result.addProperty(ERR_KEY, "no data");
            setFail(result);
            return GsonUtil.toJsonStr(result);
        }
        for (GennDataModel dataModel : list) {
            dataModel.setPdfPath(null);
            dataModel.setJsonData(null);
        }
        setSuccess(result);
        result.add("data", GsonUtil.toJsonTree(list));
        return GsonUtil.toJsonStr(result);
    }

    public boolean checkVisitSnFail(Map<String, GennUpResultBean> upResult, String id, String visitSn, JsonObject patData) {
        LinkedList<JsonElement> visits = GsonUtil.getJsonArrayAllValue("visits.visit_info.VISIT_SN", patData);
        if (visits == null || visits.size() == 0) {
            upResult.put(id, new GennUpResultBean(GennMappingEnum.MAPPING_ERR, "visitSn不匹配"));
            return true;
        }
        boolean find = false;
        for (JsonElement element : visits) {
            if (element.getAsString().equals(visitSn)) {
                find = true;
                break;
            }
        }
        if (!find) {
            upResult.put(id, new GennUpResultBean(GennMappingEnum.MAPPING_ERR, "visitSn不匹配"));
            return true;
        }
        return false;
    }

    @Override
    public void destroy() throws Exception {
        fileMonitor.stop();
    }

    FileAlterationMonitor fileMonitor;

    @Override
    public void afterPropertiesSet() throws Exception {
        FilesUtils.mkdir(listenDir);
        FileAlterationObserver observer = new FileAlterationObserver(new File(listenDir), new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile() && pathname.getName().endsWith(".zip"))
                    return true;
                return false;
            }
        });
        FileListenerAdaptor listener = new FileListenerAdaptor();
        listener.init(geneDataService, listenDir);
        observer.addListener(listener);
        fileMonitor = new FileAlterationMonitor(5000, new FileAlterationObserver[]{observer});
        fileMonitor.start();
    }

}
