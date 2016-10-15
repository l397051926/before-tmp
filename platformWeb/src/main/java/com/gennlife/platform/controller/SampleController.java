package com.gennlife.platform.controller;


import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.SampleProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.MemCachedUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
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
import javax.servlet.http.HttpSession;

/**
 * Created by chensong on 2015/12/14.
 */
@Controller
@RequestMapping("/sample")
public class SampleController {
    private Logger logger = LoggerFactory.getLogger(SampleController.class);
    private static SampleProcessor processor = new SampleProcessor();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    @RequestMapping(value="/Import",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String postImport(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
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
    String postSetDetail(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
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
    String postEditSet(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            String sessionID = session.getId();
            String uid = MemCachedUtil.get(sessionID);
            if(uid == null){
                return ParamUtils.errorSessionLosParam();
            }
            User user = MemCachedUtil.getUser(uid);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editSet(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑样本集  耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/EditSet",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getEditSet(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            HttpSession session = paramRe.getSession();
            String sessionID = session.getId();
            String uid = MemCachedUtil.get(sessionID);
            if(uid == null){
                return ParamUtils.errorSessionLosParam();
            }
            User user = MemCachedUtil.getUser(uid);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editSet(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑样本集 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ImportTree",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getImportTree(){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            resultStr =  processor.importTree();
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("组织机构列表 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SampleSetDirectoryList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String SampleDirectoryList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("SampleSetDirectoryList param="+param);
            resultStr =  processor.sampleSetDirectoryList(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本集目录请求 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SampleSetSearch",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String SampleSetSearch(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("SampleSetSearch param="+param);
            resultStr =  processor.sampleSetSearch(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本搜索请求 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/UploadAdaptTag",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String UploadAdaptTag(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("UploadAdaptTag param="+param);
            resultStr =  processor.uploadAdaptTag(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传采用/不采用标签 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
