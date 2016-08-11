package com.gennlife.platform.controller;

import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chensong on 2015/12/5.
 */
@Controller
@RequestMapping("/user")
public class UserController{
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserProcessor processor = new UserProcessor();
    private static JsonParser jsonParser = new JsonParser();
    @RequestMapping(value="/Login",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postLogin(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.login(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("登录 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/Login",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getLogin(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.login(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("登录 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/Update",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postUpdate(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.update(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/Update",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getUpdate(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{

            resultStr =  processor.update(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ChangePWD",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postChangePWD(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.changePwdSender(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/ChangePWD",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getChangePWD(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.changePwdSender(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

}
