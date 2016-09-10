package com.gennlife.platform.controller;

import com.gennlife.platform.util.FileUploadUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/9/6.
 */
@Controller
@RequestMapping("/common")
public class CommonController  {
    private Logger logger = LoggerFactory.getLogger(CommonController.class);
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/UploadFile",method= RequestMethod.POST)
    public @ResponseBody String postChain(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = "";
        FileUploadUtil fileUploadUtil = new FileUploadUtil("/home/tmp","/home/file",paramRe);
        try{
            fileUploadUtil.Upload();
            logger.info("上传文件 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传文件 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


}
