package com.gennlife.platform.controller;

import com.gennlife.platform.processor.LaboratoryProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/9/12.
 */
@Controller
@RequestMapping("/bsma")
public class BackstageManagementController {
    private Logger logger = LoggerFactory.getLogger(BackstageManagementController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static LaboratoryProcessor processor = new LaboratoryProcessor();
    @RequestMapping(value="/OrgMapData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postOrgMapData(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取科室组织信息 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.orgMapData(paramObj);
        }catch (Exception e){
            logger.error("获取科室组织信息",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取科室组织信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddOrg",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postChangePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addOrg(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("添加科室 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/DeleteOrg",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postDeleteOrg(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("删除科室 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.deleteOrg(paramObj);
        }catch (Exception e){
            logger.error("删除科室",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除科室 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
