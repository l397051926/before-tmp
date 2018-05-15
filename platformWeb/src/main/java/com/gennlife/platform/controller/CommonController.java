package com.gennlife.platform.controller;

import com.gennlife.platform.configuration.FileBean;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CommonProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.StringReader;


/**
 * Created by chen-song on 16/9/6.
 */
@Controller
@RequestMapping("/common")
public class CommonController implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    private static String labImportsuffix = "导入科室历史.csv";
    private static String staffImportsuffix = "导入人员历史.csv";
    private static JsonParser jsonParser = new JsonParser();
    private static CommonProcessor processor = CommonProcessor.getCommonProcessor();
    private static Gson gson = GsonUtil.getGson();
    //存放文件的位置
    private static String FilePath = "/home/tomcat_demo2_web/update/";//默认位置
    @Autowired
    private FileBean fileBean;


    //后缀
    private static String suffix = ".csv";

    /**
     *
     * @param file
     * @param paramRe
     * @return
     */
    @RequestMapping(value = "/UploadFileForImportLab", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
    public
    @ResponseBody
    String postUploadFileForImportLab(@RequestParam("name") MultipartFile file, HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = "";
        try {
            logger.info("*-----------开始导入科室");
            User user = (User) paramRe.getAttribute("currentUser");
            resultStr = processor.uploadFileForImportLab(file, user);
            logger.info("上传文件导入科室 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传文件导入科室 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DownloadFileForImportLabHistory", method = RequestMethod.GET)
    public void getDownloadFileForImportLabHistory(HttpServletRequest paramRe, HttpServletResponse response) {
        User user = (User) paramRe.getAttribute("currentUser");
        String file = FilePath + user.getOrg_name() + labImportsuffix;
        processor.downLoadFile(file, response, "最近组织导入结果.csv");
    }

    @RequestMapping(value = "/UploadFileForImportStaff", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
    public
    @ResponseBody
    String postUploadFileForImportStaff(@RequestParam("name") MultipartFile file, HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = "";
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            resultStr = processor.uploadFileForImportStaff(file, user);
            logger.info("上传文件导入人员 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传文件导入人员 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/DownloadFileForStaffHistory", method = RequestMethod.GET)
    public void getDownloadFileForStaffHistory(HttpServletRequest paramRe, HttpServletResponse response) {
        User user = (User) paramRe.getAttribute("currentUser");
        String file = FilePath + user.getOrg_name() + staffImportsuffix;
        processor.downLoadFile(file, response, "最近成员导入结果.csv");
    }

    @RequestMapping(value = "/DownloadFileForStaffModel", method = RequestMethod.GET)
    public void DownloadFileForStaffModel(HttpServletRequest paramRe, HttpServletResponse response) {
        String file = FilePath + "人员导入模版" + suffix;
        logger.info(file);
        processor.downLoadFile(file, response, "人员导入模版.csv");
    }

    @RequestMapping(value = "/DownloadFileForLabModel", method = RequestMethod.GET)
    public void DownloadFileForLabModel(HttpServletRequest paramRe, HttpServletResponse response) {
        String file = FilePath + "组织导入模版" + suffix;
        processor.downLoadFile(file, response, "组织导入模版.csv");
    }


    @RequestMapping(value = "/DownloadFileForExplainCRFImport", method = RequestMethod.GET)
    public void DownloadFileForExplainCRFImport(HttpServletRequest paramRe, HttpServletResponse response) {

        String oparam = ParamUtils.getParam(paramRe);
        JsonObject json = (JsonObject) jsonParser.parse(oparam);
        String file = FilePath;
        String liver_cancer = "映射模型字段说明-肝癌-V1.0.xlsx";
        String lung_cancer = "映射模型字段说明-肺癌-V1.1.xlsx";
        String kidney_cancer = "映射模型字段说明-肾癌-V1.0.xlsx";
        String crfId = null;
        try {
            crfId = json.get("crf_id").getAsString().trim();
            if (StringUtils.isEmpty(crfId)) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        String fileName = null;
        if (crfId.equals("liver_cancer")) {
            file += liver_cancer;
            fileName = liver_cancer;
        } else if (crfId.equals("lung_cancer")) {
            file += lung_cancer;
            fileName = lung_cancer;
        } else {
            file += kidney_cancer;
            fileName = kidney_cancer;
        }
        logger.info("DownloadFileForExplainCRFImport: " + crfId + " : " + fileName);
        processor.downLoadFile(file, response, fileName);
    }
    @RequestMapping(value = "/DownloadDetailImage",method = {RequestMethod.POST,RequestMethod.GET} )
    public void DownloadDetailImage( HttpServletRequest paramRe, HttpServletResponse response){
        Long start = System.currentTimeMillis();
        String type = null;
        String svg = null;
        try {
            paramRe.setCharacterEncoding("utf-8");
            String oparam = ParamUtils.getParam(paramRe);
            String[] dataArgs = oparam.split("------");
            for(int i = 0;i<dataArgs.length; i++){
                String temp = dataArgs[i];
                if(temp.contains("name=\"type\"")){
                    String[] tmparrg = temp.split("\"");
                    type = tmparrg[tmparrg.length-1];
                }
                if(temp.contains("name=\"svg\"")){
                    String[] tmparrg = temp.split("name=\"svg\"");
                    svg = tmparrg[tmparrg.length-1];
                }
            }
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            if (null != type && null != svg){
                svg = svg.replaceAll(":rect", "rect");
                String ext = "";
                Transcoder t = null;
                if (type.equals("image/png")) {
                    ext = "png";
                    t = new PNGTranscoder();
                } else if (type.equals("image/jpeg")) {
                    ext = "jpg";
                    t = new JPEGTranscoder();
                } else if (type.equals("application/pdf")) {
                    ext = "pdf";
                    t = new PDFTranscoder();
                } else if (type.equals("image/svg+xml")) {
                    ext = "svg";
                }
                response.addHeader("Content-Disposition", "attachment; filename=chart."+ext);
                response.addHeader("Content-Type", type);
                if (null != t){
                    TranscoderInput input = new TranscoderInput(new StringReader(svg));
                    TranscoderOutput output = new TranscoderOutput(out);
                    try {
                        t.transcode(input,output);
                    } catch (TranscoderException e){
                        logger.error("编码流错误", e);
                    }
                } else if (ext == "svg"){
                    OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                    writer.append(svg);
                    writer.close();
                } else {
                    out.print("Invalid type: " + type);
                }
            } else {
                response.addHeader("Content-Type", "text/html");
            }
            out.flush();
            out.close();
            logger.info("下载图片 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("下载图片 耗时" + (System.currentTimeMillis() - start) + "ms");


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FilePath = fileBean.getManageFileLocation();
    }
}
