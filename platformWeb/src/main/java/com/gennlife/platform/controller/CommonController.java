package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CommonProcessor;
import com.gennlife.platform.util.FileUploadUtil;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by chen-song on 16/9/6.
 */
@Controller
@RequestMapping("/common")
public class CommonController  {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    private static String labImportsuffix = "导入科室历史.csv";
    private static String staffImportsuffix = "导入人员历史.csv";
    private static CommonProcessor processor = new CommonProcessor();
    private static Gson gson = GsonUtil.getGson();
    private static View view = new View();
    //存放文件的位置
    private static String FilePath = "/home/tomcat_demo2_web/update/";
    //后缀
    private static String suffix = ".csv";
    @RequestMapping(value="/UploadFileForImportLab",method= RequestMethod.POST)
    public @ResponseBody String postChain(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = "";
        FileUploadUtil fileUploadUtil = new FileUploadUtil(FilePath,suffix,paramRe);
        try{
            resultStr = fileUploadUtil.Upload("导入科室",labImportsuffix);
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
        HttpSession session = paramRe.getSession();
        if(session == null){
            view.viewString(ParamUtils.errorParam("当前session已经失效"),response);
            return;
        }
        User user = gson.fromJson((String)session.getAttribute("user"),User.class);
        String file = FilePath+user.getOrg_name() + labImportsuffix;
        processor.downLoadFile(file,response);
    }

    @RequestMapping(value="/UploadFileForImportStaff",method= RequestMethod.POST)
    public @ResponseBody String postUploadFileForImportStaff(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = "";
        FileUploadUtil fileUploadUtil = new FileUploadUtil(FilePath,suffix,paramRe);
        try{
            resultStr = fileUploadUtil.Upload("导入人员",staffImportsuffix);
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
        HttpSession session = paramRe.getSession();
        if(session == null){
            view.viewString(ParamUtils.errorParam("当前session已经失效"),response);
            return;
        }
        User user = gson.fromJson((String)session.getAttribute("user"),User.class);
        String file = FilePath+user.getOrg_name() + staffImportsuffix;
        processor.downLoadFile(file,response);
    }

    @RequestMapping(value="/DownloadFileForStaffModel",method= RequestMethod.GET)
    public void DownloadFileForStaffModel(HttpServletRequest paramRe,HttpServletResponse response){
        HttpSession session = paramRe.getSession();
        if(session == null){
            view.viewString(ParamUtils.errorParam("当前session已经失效"),response);
            return;
        }
        User user = gson.fromJson((String)session.getAttribute("user"),User.class);
        String file = FilePath+"人员导入模版"+ suffix;
        processor.downLoadFile(file,response);
    }
    @RequestMapping(value="/DownloadFileForLabModel",method= RequestMethod.GET)
    public void DownloadFileForLabModel(HttpServletRequest paramRe,HttpServletResponse response){
        HttpSession session = paramRe.getSession();
        if(session == null){
            view.viewString(ParamUtils.errorParam("当前session已经失效"),response);
            return;
        }
        User user = gson.fromJson((String)session.getAttribute("user"),User.class);
        String file = FilePath+"科室导入模版"+ suffix;
        processor.downLoadFile(file,response);
    }

}
