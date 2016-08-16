package com.gennlife.platform.controller;

import com.gennlife.platform.processor.ComputeProcessor;
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

/**
 * Created by chen-song on 16/8/16.
 */
@Controller
@RequestMapping("/cs")
public class ComputeController {
    private Logger logger = LoggerFactory.getLogger(ComputeController.class);
    private static ComputeProcessor processor = new ComputeProcessor();
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/Smg",method= RequestMethod.GET,produces = "application/xml;charset=UTF-8")
    public @ResponseBody
    String postModel(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("计算服务因子图 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.smg(paramObj);
        }catch (Exception e){
            logger.error("计算服务因子图",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("计算服务因子图 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
