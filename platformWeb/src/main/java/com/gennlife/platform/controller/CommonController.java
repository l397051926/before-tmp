package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CommonProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by chen-song on 16/9/6.
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    private static String labImportsuffix = "导入科室历史.csv";
    private static String staffImportsuffix = "导入人员历史.csv";
    private static JsonParser jsonParser = new JsonParser();
    private static CommonProcessor processor = new CommonProcessor();
    private static Gson gson = GsonUtil.getGson();
    //存放文件的位置
    private static String FilePath = "/home/tomcat_demo2_web/update/";//默认位置
    static{
        FilePath = ConfigurationService.getFileBean().getManageFileLocation();
    }
    //后缀
    private static String suffix = ".csv";
    @RequestMapping(value="/UploadFileForImportLab",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUploadFileForImportLab(@RequestParam("name") MultipartFile file,HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = "";
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr = processor.uploadFileForImportLab(file,user);
            logger.info("上传文件导入科室 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传文件导入科室 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DownloadFileForImportLabHistory",method= RequestMethod.GET)
    public void getDownloadFileForImportLabHistory(HttpServletRequest paramRe,HttpServletResponse response){
        User user = (User)paramRe.getAttribute("currentUser");
        String file = FilePath+user.getOrg_name() + labImportsuffix;
        processor.downLoadFile(file,response,"最近组织导入结果.csv");
    }

    @RequestMapping(value="/UploadFileForImportStaff",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUploadFileForImportStaff(@RequestParam("name") MultipartFile file,HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = "";
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            resultStr = processor.uploadFileForImportStaff(file,user);
            logger.info("上传文件导入人员 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传文件导入人员 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/DownloadFileForStaffHistory",method= RequestMethod.GET)
    public void getDownloadFileForStaffHistory(HttpServletRequest paramRe,HttpServletResponse response){
        User user = (User)paramRe.getAttribute("currentUser");
        String file = FilePath+user.getOrg_name() + staffImportsuffix;
        processor.downLoadFile(file,response,"最近成员导入结果.csv");
    }

    @RequestMapping(value="/DownloadFileForStaffModel",method= RequestMethod.GET)
    public void DownloadFileForStaffModel(HttpServletRequest paramRe,HttpServletResponse response){
        String file = FilePath+"人员导入模版"+ suffix;
        processor.downLoadFile(file,response,"人员导入模版.csv");
    }
    @RequestMapping(value="/DownloadFileForLabModel",method= RequestMethod.GET)
    public void DownloadFileForLabModel(HttpServletRequest paramRe,HttpServletResponse response){
        String file = FilePath+"组织导入模版"+ suffix;
        processor.downLoadFile(file,response,"组织导入模版.csv");
    }


    @RequestMapping(value="/DownloadFileForExplainCRFImport",method= RequestMethod.GET)
    public void DownloadFileForExplainCRFImport(HttpServletRequest paramRe, HttpServletResponse response){

        String oparam = ParamUtils.getParam(paramRe);
        JsonObject json = (JsonObject)jsonParser.parse(oparam);
        String file = FilePath;
        String liver_cancer = "映射模型字段说明-肝癌-V1.0.xlsx";
        String lung_cancer = "映射模型字段说明-肺癌-V1.0.xlsx";
        String kidney_cancer = "映射模型字段说明-肾癌-V1.0.xlsx";
        String crfId = null;
        try {
            crfId = json.get("crf_id").getAsString().trim();
            if (StringUtils.isEmpty(crfId)) {
                return ;
            }
        } catch (Exception e) {
            return ;
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
}
