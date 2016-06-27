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
    @RequestMapping(value="/Model",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postModel(@RequestParam("param") String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("crf 请求模板 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.model(paramObj);
        }catch (Exception e){
            logger.error("请求模板",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("crf 请求模板 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SelectAttr",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSelectAttr(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("勾选属性,修改模型 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.selectAttr(paramObj);
        }catch (Exception e){
            logger.error("勾选属性,修改模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("勾选属性,修改模型 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/SelectAttr",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getSelectAttr(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("勾选属性,修改模型 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.selectAttr(paramObj);
        }catch (Exception e){
            logger.error("勾选属性,修改模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("勾选属性,修改模型 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/AddGroup",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postAddGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("模型增加组 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addGroup(paramObj);
        }catch (Exception e){
            logger.error("模型增加属性",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("模型增加组 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/AddGroup",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getAddGroup(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("模型增加组 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addGroup(paramObj);
        }catch (Exception e){
            logger.error("模型增加属性",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("模型增加组 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/AddAttr",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postAddAttr(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("模型增加属性 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addAttr(paramObj);
        }catch (Exception e){
            logger.error("模型增加属性",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("模型增加属性 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/AddAttr",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getAddAttr(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("模型增加属性 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.addAttr(paramObj);
        }catch (Exception e){
            logger.error("编辑模型",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("模型增加属性 get 耗时"+(System.currentTimeMillis()-start) +"ms");
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

    @RequestMapping(value="/UpdateGroupName",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpdateGroupName(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("更新组名称 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updateGroupName(paramObj);
        }catch (Exception e){
            logger.error("更新组名称",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新组名称  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/UpdateGroupName",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getUpdateGroupName(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("更新组名称 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updateGroupName(paramObj);
        }catch (Exception e){
            logger.error("更新组名称",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新组名称  post 耗时"+(System.currentTimeMillis()-start) +"ms");
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

    @RequestMapping(value="/ModelTree",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postModelTree(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取模型树,没有叶子节点 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.modelTree(paramObj);
        }catch (Exception e){
            logger.error("获取模型树,没有叶子节点",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取模型树,没有叶子节点 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/ModelTree",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getModelTree(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("获取模型树,没有叶子节点 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.modelTree(paramObj);
        }catch (Exception e){
            logger.error("获取模型树,没有叶子节点",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取模型树,没有叶子节点 get 耗时"+(System.currentTimeMillis()-start) +"ms");
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



    @RequestMapping(value="/DeleteGroup",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除组 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteGroup(paramObj);
        }catch (Exception e){
            logger.error("删除组异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/DeleteGroup",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getDeleteGroup(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            logger.info("删除组 get方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteGroup(paramObj);
        }catch (Exception e){
            logger.error("删除组异常",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除组 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    /**
     *
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
}
