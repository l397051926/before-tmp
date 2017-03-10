package com.gennlife.platform.controller;


import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.SampleProcessor;
import com.gennlife.platform.util.GsonUtil;
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
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            logger.info("Import param="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.importSample(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本导入 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ImportCheck",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String ImportCheck(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            logger.info("ImportCheck param="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.importSampleCheck(paramObj,user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("样本导入校验 耗时"+(System.currentTimeMillis()-start) +"ms");
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
            JsonObject paramObj = (JsonObject)jsonParser.parse(param);
            int count = AllDao.getInstance().getProjectDao().isExistProjectID(paramObj.get("projectID").getAsString());
            if (count <= 0) {
                return SampleProcessor.projectIDIsNotExists();
            } else {
                User user = (User)paramRe.getAttribute("currentUser");
                resultStr =  processor.editSet(paramObj,user);
            }
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
            User user = (User)paramRe.getAttribute("currentUser");
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
    public @ResponseBody String getImportTree(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = new JsonObject();
            if(param == null || "".equals(param)){
                paramObj.addProperty("crf_id","kidney_cancer");
            }else {
                paramObj = (JsonObject) jsonParser.parse(param);
            }

            resultStr =  processor.importTree(paramObj);
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
