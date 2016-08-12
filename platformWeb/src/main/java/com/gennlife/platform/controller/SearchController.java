package com.gennlife.platform.controller;


import com.gennlife.platform.processor.SearchProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chensong on 2015/12/12.
 */
@Controller
@RequestMapping("/search")
public class SearchController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(SearchController.class);
    private static SearchProcessor processor = new SearchProcessor();
    private static JsonParser jsonParser = new JsonParser();

    @RequestMapping(value="/SearchMembers",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchMembers(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchMembers(paramObj);
            logger.info("搜索用户列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索用户列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SearchMembers",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSearchMembers(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchMembers(paramObj);
            logger.info("搜索用户列表 get 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索用户列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SearchSetList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postSearchSetList(@RequestBody String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchSetList(paramObj);
            logger.info("搜索用户列表 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索用户列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SearchSetList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSearchSetList(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchSetList(paramObj);
            logger.info("搜索用户列表 get 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索用户列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/StoreSearchCondition",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getStoreSearchCondition(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.storeSearchCondition(paramObj);
            logger.info("搜索条件保存 post 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索条件保存 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SearchConditionList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSearchConditionList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.searchConditionList(paramObj);
            logger.info("搜索条件保存 post 耗时:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索条件保存 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
