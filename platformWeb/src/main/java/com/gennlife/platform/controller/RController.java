package com.gennlife.platform.controller;

import com.gennlife.platform.processor.RProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-shuai on 16/7/29.
 */
@Controller
@RequestMapping("/rscript")
public class RController {
    private Logger logger = LoggerFactory.getLogger(CrfController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static RProcessor processor = new RProcessor();
    private static Gson gson = GsonUtil.getGson();

    @RequestMapping(value="/r_run",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postr_run(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("R执行 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.r_run(paramObj);
        }catch (Exception e){
            logger.error("R执行",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("R执行  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/r_stop",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postr_stop(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("R停止 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.r_stop(paramObj);
        }catch (Exception e){
            logger.error("R停止",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("R停止  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/r_save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postr_save(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("R代码保存 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.r_save(paramObj);
        }catch (Exception e){
            logger.error("R代码保存",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("R代码保存  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/r_load",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postr_load(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("从项目空间载入R代码 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.r_load(paramObj);
        }catch (Exception e){
            logger.error("从项目空间载入R代码",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("从项目空间载入R代码  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/r_list",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postr_list(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("列出项目下的所有R代码 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.r_list(paramObj);
        }catch (Exception e){
            logger.error("列出项目下的所有R代码",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("列出项目下的所有R代码 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

}
