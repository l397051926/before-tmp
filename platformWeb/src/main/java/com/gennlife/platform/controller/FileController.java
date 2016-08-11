package com.gennlife.platform.controller;

import com.gennlife.platform.processor.FileProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chensong on 2015/12/9.
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    private static FileProcessor processor = new FileProcessor();
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/Rec",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postRec(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.fsRec(paramObj);
            logger.info("跳转 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("跳转 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/Rec",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getRec(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.fsRec(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("跳转 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SystemInfo",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSystemInfo(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.systemInfo();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SystemInfo",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSystemInfo(){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.systemInfo();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/KnowledgeReload",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postKnowledgeReload(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.reloadConfig();
            logger.info("系统信息 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("系统信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/KnowledgeReload",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getKnowledgeReload(){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.reloadConfig();
            logger.info("更新配置 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新配置 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

}
