package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CrfProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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


    @RequestMapping(value="/GetData",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String getGetData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = AuthorityUtil.addAuthority(paramRe);
            logger.info("数据录入时,请求某个case数据 get方式 参数="+param);
            User user = (User)paramRe.getAttribute("currentUser");
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.getData(paramObj,user.getOrgID());
        }catch (Exception e){
            logger.error("数据录入时,请求某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("数据录入时,请求某个case数据 get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    /**
     * 录入上传
     * @param paramRe
     * @return
     */
    @RequestMapping(value="/UpLoadData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpLoadData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if(indexName == null){
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            logger.info("上传crf数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName",indexName);
            resultStr = processor.upLoadData(paramObj);
        }catch (Exception e){
            logger.error("上传crf数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传crf数据  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/SaveData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postSaveData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if(indexName == null){
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            logger.info("录入完成接口 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName",indexName);
            resultStr = processor.saveData(paramObj);
        }catch (Exception e){
            logger.error("录入完成接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("录入完成接口 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    /**
     * 删除CRF数据
     * @param paramRe
     * @return
     */
    @RequestMapping(value="/DeleteSample",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postDeleteSample(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            logger.info("删除某个case数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteSample(paramObj,user.getOrgID());
        }catch (Exception e){
            logger.error("删除某个case数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除某个case数据 post 耗时"+(System.currentTimeMillis()-start) +"ms");
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

    @RequestMapping(value="/ModelForTraceByCRFID",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public @ResponseBody String getModelForTraceByCRFID(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = AuthorityUtil.addAuthority(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            logger.info("溯源简化的模型接口 参数="+param);
            resultStr = processor.modelForTraceByCRFID(param,user.getOrgID());
        }catch (Exception e){
            logger.error("溯源简化的模型接口",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("溯源简化的模型接口 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    //UpLoadDataForCheck
    @RequestMapping(value="/UpLoadDataForCheck",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public @ResponseBody String postUpLoadDataForCheck(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if(indexName == null){
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            logger.info("自动映射检验上传crf数据 post方式 参数="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName",indexName);
            resultStr = processor.upLoadDataForCheck(paramObj);
        }catch (Exception e){
            logger.error("自动映射检验上传crf数据",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("自动映射检验上传crf数据  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }




    @RequestMapping(value="/UploadFileForImportCRF",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public @ResponseBody String UploadFileForImportCRF(@RequestParam(value="file") CommonsMultipartFile file,HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = AuthorityUtil.addAuthority(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            String crf_id = paramObj.get("crf_id").getAsString();
            JsonArray roles = paramObj.getAsJsonArray("roles");
            User user = (User)paramRe.getAttribute("currentUser");
            if(file.isEmpty()){
                return ParamUtils.errorParam("文件为空");
            }
            resultStr = processor.uploadFileForImportCRF(file,crf_id,roles,user.getOrgID());
        }catch (Exception e){
            logger.error("上传CRF数据文件",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("上传CRF数据文件  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ImportCRFMap",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public @ResponseBody String ImportCRFMap(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String indexName = ConfigurationService.getOrgIDIndexNamemap().get(user.getOrgID());
            if(indexName == null){
                return ParamUtils.errorParam("用户所在的组织无法建立索引");
            }
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            paramObj.addProperty("indexName",indexName);
            resultStr = processor.importCRFMap(paramObj);
        }catch (Exception e){
            logger.error("导入CRF导入配置",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("导入CRF导入配置  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/CsvImportResult",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public @ResponseBody String CsvImportResult(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            resultStr = processor.csvImportResult(param);
        }catch (Exception e){
            logger.error("查询导入状态",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查询导入状态  post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }



    @RequestMapping(value="/IsExistPatient",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public @ResponseBody String IsExistPatient(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            resultStr = processor.isExistPatient(param);
        }catch (Exception e){
            logger.error("病人编号是否存在",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("病人编号是否存在 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
