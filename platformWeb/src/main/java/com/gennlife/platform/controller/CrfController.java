package com.gennlife.platform.controller;

import com.gennlife.platform.processor.CrfProcessor;
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
 * Created by chen-song on 16/6/3.
 */
@Controller
@RequestMapping("/crf")
public class CrfController {
    private Logger logger = LoggerFactory.getLogger(CrfController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static CrfProcessor processor = new CrfProcessor();
    private static Gson gson = GsonUtil.getGson();
    @RequestMapping(value="/ModelByProjectID",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postModel(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("通过projectID 获取crf模板 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.modelByProjectID(paramObj);
        }catch (Exception e){
            logger.error("通过projectID 获取crf模板",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("通过projectID 获取crf模板 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ModelByCRFID",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSelectAttr(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("通过CRFID获取crf模板  参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.modelByCRFID(paramObj);
        }catch (Exception e){
            logger.error("通过CRFID获取crf模板",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("通过CRFID获取crf模板 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/EditModel",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postEditModel(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("编辑模型 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.editModel(paramObj);
        }catch (Exception e){
            logger.error("编辑模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑模型  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SaveModel",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSaveModel(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("保存模型 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.saveModel(paramObj);
        }catch (Exception e){
            logger.error("保存模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("保存模型  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SaveModel",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSaveModel(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("保存模型 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.saveModel(paramObj);
        }catch (Exception e){
            logger.error("保存模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("保存模型 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postGetData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("数据录入时,请求某个case数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getData(paramObj);
        }catch (Exception e){
            logger.error("数据录入时,请求某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据录入时,请求某个case数据  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/GetData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetData(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("数据录入时,请求某个case数据 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getData(paramObj);
        }catch (Exception e){
            logger.error("数据录入时,请求某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据录入时,请求某个case数据 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/UpLoadData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpLoadData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("上传crf数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.upLoadData(paramObj);
        }catch (Exception e){
            logger.error("上传crf数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传crf数据  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/UpLoadData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getUpLoadData(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("上传crf数据 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.upLoadData(paramObj);
        }catch (Exception e){
            logger.error("上传crf数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传crf数据 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SaveData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSaveData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("录入完成接口 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveData(paramObj);
        }catch (Exception e){
            logger.error("录入完成接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("录入完成接口 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SaveData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSaveData(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("录入完成接口 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveData(paramObj);
        }catch (Exception e){
            logger.error("录入完成接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("录入完成接口 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/SampleCaseList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSampleCaseList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("病历列表数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.sampleCaseList(paramObj);
        }catch (Exception e){
            logger.error("病历列表数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病历列表数据 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SampleCaseList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSampleCaseList(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("病历列表数据 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.sampleCaseList(paramObj);
        }catch (Exception e){
            logger.error("病历列表数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病历列表数据 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DeleteSample",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteSample(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除某个case数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteSample(paramObj);
        }catch (Exception e){
            logger.error("删除某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除某个case数据 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/DeleteSample",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getDeleteSample(@RequestParam("param")  String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("删除某个case数据 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteSample(paramObj);
        }catch (Exception e){
            logger.error("删除某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除某个case数据 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SearchSampleList",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSearchSampleList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("搜索病历列表 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchSampleList(paramObj);
        }catch (Exception e){
            logger.error("搜索病历列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索病历列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/SearchSampleList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSearchSampleList(@RequestParam("param")  String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("搜索病历列表 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchSampleList(paramObj);
        }catch (Exception e){
            logger.error("搜索病历列表",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索病历列表 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    /**
     * 请求uid对应的模版接口
     * @param param
     * @return
     */
    @RequestMapping(value="/ProjectCrfList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getProjectCrfList(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("用户相关项目的crf模版 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.projectCrfList(paramObj);
        }catch (Exception e){
            logger.error("用户相关项目的crf模版异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户相关项目的crf模版异常 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/AutoMap",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postProjectCrfList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("将搜索到的病例导入crf接口 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.autoMap(paramObj);
        }catch (Exception e){
            logger.error("将搜索到的病例导入crf接口异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("将搜索到的病例导入crf接口 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    /**
     * 获取一个新的唯一的属性id
     * @param param
     * @return
     */
    @RequestMapping(value="/GetAttrID",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetAttrID(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取一个新的唯一的属性id 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getAttrID(paramObj);
        }catch (Exception e){
            logger.error("获取一个新的唯一的属性id异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取一个新的唯一的属性id 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    /**
     * 获取一个新的唯一的组id
     * @param param
     * @return
     */
    @RequestMapping(value="/GetGroupID",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetGroupID(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取一个新的唯一的组id 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getGroupID(paramObj);
        }catch (Exception e){
            logger.error("获取一个新的唯一的组id异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取一个新的唯一的组id 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    /*
    *获取病人基础信息
    * @param param
    * return
     */
    @RequestMapping(value="/PatientInfo",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public @ResponseBody String getPatientInfo(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取病人基础信息 参数="+param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientInfo(param);
        }catch (Exception e){
            logger.error("获取病人基础信息异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取病人基础信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/PatientVisitDetail",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public @ResponseBody String getPatientVisitDetail(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("溯源页病人详细信息 参数="+param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientVisitDetail(param);
        }catch (Exception e){
            logger.error("获取溯源页病人详细信息",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取溯源页病人详细信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/PatientAllVisitsDetail",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public @ResponseBody String getPatientAllVisitDetail(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("溯源全部病人详细信息 参数="+param);
            //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PatientAllVisitDetail(param);
        }catch (Exception e){
            logger.error("获取溯源全部病人详细信息",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取溯源全部病人详细信息 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
