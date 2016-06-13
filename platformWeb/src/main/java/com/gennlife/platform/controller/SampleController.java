package com.gennlife.platform.controller;


import com.gennlife.platform.processor.SampleProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chensong on 2015/12/14.
 */
@Controller
@RequestMapping("/sample")
public class SampleController {
    private Logger logger = LoggerFactory.getLogger(SampleController.class);
    private static SampleProcessor processor = new SampleProcessor();
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/Import",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postImport(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.importSample(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("组织机构列表 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/Import",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getImport(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.importSample(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("组织机构列表 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SetDetail",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSetDetail(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.sampleDetail(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集合数据详情查看 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SetDetail",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSetDetail(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.sampleDetail(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集合数据详情查看 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/EditSet",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postEditSet(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editSet(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑样本集  耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/EditSet",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getEditSet(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editSet(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑样本集 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

}
