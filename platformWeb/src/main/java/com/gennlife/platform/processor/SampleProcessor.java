package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.projectBean.ProLog;
import com.gennlife.platform.bean.projectBean.ProSample;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.model.Group;
import com.gennlife.platform.model.User;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by chensong on 2015/12/9.
 */
public class SampleProcessor {
    private static Logger logger = LoggerFactory.getLogger(SampleProcessor.class);
    private static View viewer = new View();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 搜索关键词
     *
     * @param jsonObject
     */
    public String importSample(JsonObject jsonObject,User user) {
        try {
            String projectID = jsonObject.get("projectID").getAsString();
            String sampleName = null;
            String uid = jsonObject.get("uid").getAsString();
            if (jsonObject.get("sampleName") != null) {
                sampleName = jsonObject.get("sampleName").getAsString();
            }
            String sampleDesc = null;
            if (jsonObject.get("sampleDesc").getAsString() != null) {
                sampleDesc = jsonObject.get("sampleDesc").getAsString();
            }
            JsonObject query = jsonObject.get("query").getAsJsonObject();
            JsonElement roles = jsonObject.get("roles");
            query.add("roles",roles);
            JsonElement power = jsonObject.get("power");
            query.add("power",power);
            JsonArray groups = jsonObject.get("groups").getAsJsonArray();
            query.add("groups",groups);

            logger.info("原始搜索条件="+gson.toJson(query));
            String withSid = CaseProcessor.transformSid(gson.toJson(query),user);
            JsonObject queryNew = (JsonObject) jsonParser.parse(withSid);
            logger.info("sid 处理后搜索条件="+gson.toJson(queryNew));
            JsonArray source = query.getAsJsonArray("source");
            String url = ConfigurationService.getUrlBean().getSampleImportIURL();
            JsonObject param = new JsonObject();
            param.add("query",queryNew);
            if(queryNew.has("code") && queryNew.get("code").getAsInt() ==0){
                return gson.toJson(queryNew);
            }
            String data = HttpRequestUtils.httpPostForSampleImport(url,gson.toJson(param));
            Long startTime = System.currentTimeMillis();
            logger.info("data = " + data);
            if(data == null || "".equals(data)){
                return ParamUtils.errorParam("FS 返回空");
            }
            JsonObject resultMap = jsonParser.parse(data).getAsJsonObject();
            logger.info("时间======" + (System.currentTimeMillis() - startTime));
            Boolean succeed = resultMap.get("success").getAsBoolean();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultBean resultBean = new ResultBean();
            if (succeed) {
                //项目对应的样本集数量增加1
                AllDao.getInstance().getProjectDao().autoAddSetCountOne(projectID);
                String uri = resultMap.get("data_id").getAsString();
                Integer total = resultMap.get("TOTAL").getAsInt();
                Map<String, Object> conf = new HashMap<String, Object>();
                conf.put("projectID", projectID);
                Integer batchID = AllDao.getInstance().getProjectDao().getMaxBatchID(conf);
                if (batchID == null) {
                    batchID = 1;
                } else {
                    batchID++;
                }
                if (sampleName == null) {
                    sampleName = "数据集-" + batchID;
                }
                ProSample proSample = new ProSample();
                proSample.setProjectID(projectID);
                proSample.setOpTime(new Date());
                proSample.setBstatus(1);
                proSample.setOperator(uid);
                proSample.setSampleURI(uri);
                proSample.setSampleName(sampleName);
                proSample.setBatchID(batchID);
                proSample.setTotal(total);
                proSample.setItems(gson.toJson(source));
                proSample.setSampleDesc(sampleDesc);
                int counter = AllDao.getInstance().getProjectDao().insertProSample(proSample);
                ProLog proLog = new ProLog();
                proLog.setAction(LogActionEnum.ImportSamples.getName());
                proLog.setLogTime(new Date());
                proLog.setLogText("<" + sampleName + ">");
                proLog.setSampleURI(uri);
                proLog.setProjectID(projectID);
                proLog.setUid(uid);
                proLog.setSampleName(sampleName);
                counter = AllDao.getInstance().getProjectDao().insertProLog(proLog);
                resultBean.setCode(1);
                Map<String,Object> info = new HashMap<>();
                info.put("counter",total);
                resultBean.setInfo(info);
            } else {
                resultBean.setCode(0);

            }
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    public String sampleDetail(JsonObject jsonObject) {
        try{
            String sampleURI =  jsonObject.get("sampleURI").getAsString();
            jsonObject.remove("sampleURI");
            jsonObject.addProperty("data_id",sampleURI);
            String url = ConfigurationService.getUrlBean().getSampleDetailURL();
            String dataStr = HttpRequestUtils.httpPost(url,gson.toJson(jsonObject));
            JsonObject json = (JsonObject) jsonParser.parse(dataStr);
            boolean succeed = json.get("success").getAsBoolean();
            if (succeed) {
                JsonArray schemaJson = json.getAsJsonArray("SCHEMA");
                JsonObject schema = new JsonObject();
                List<String> list = new LinkedList<>();
                for(JsonElement schemaElement:schemaJson){
                    String index = schemaElement.getAsString();
                    String uiName = ConfigurationService.getUIFieldName(index);
                    schema.addProperty(index,uiName);
                    list.add(index);
                }
                JsonArray data = new JsonArray();
                JsonArray DATAArray = json.getAsJsonArray("DATA");
                for(JsonElement dataItemArray:DATAArray){
                    JsonArray oneDataArray = dataItemArray.getAsJsonArray();
                    JsonObject entity = new JsonObject();
                    for(int i=0;i<list.size();i++){
                        String index = list.get(i);
                        JsonPrimitive dataValue = oneDataArray.get(i).getAsJsonPrimitive();
                        entity.add(index,dataValue);
                    }
                    data.add(entity);
                }
                JsonObject result = new JsonObject();
                JsonObject info = new JsonObject();
                JsonElement counter = json.get("TOTAL");
                info.add("schema",schema);
                info.add("counter",counter);
                result.addProperty("code",1);
                result.add("info",info);
                result.add("data",data);
                return gson.toJson(result);
            }else{
                return ParamUtils.errorParam("FS出现异常");
            }

        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    /**
     * 编译样本集
     *
     * @param paramObj
     */
    public String editSet(JsonObject paramObj,User syUser) {
        String uid = null;
        String projectID = null;
        String sampleName = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();

        try {
            projectID = paramObj.get("projectID").getAsString();
            String sampleURI = paramObj.get("sampleURI").getAsString();
            sampleName = paramObj.get("sampleName").getAsString();
            String sampleDesc = paramObj.get("sampleDesc").getAsString();
            uid = paramObj.get("uid").getAsString();
            map.put("projectID", projectID);
            map.put("sampleURI", sampleURI);
            map.put("sampleName", sampleName);
            map.put("sampleDesc", sampleDesc);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数异常");
        }

        int count = AllDao.getInstance().getProjectDao().updateSetInfo(map);
        if (count >= 1) {
            ProLog proLog = new ProLog();
            proLog.setProjectID(projectID);
            proLog.setUid(uid);
            proLog.setAction(LogActionEnum.CreatePlan.getName());
            proLog.setLogText(syUser.getUname() + LogActionEnum.UpdateSetInfo + ":" + sampleName);
            proLog.setLogTime(new Date());
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
            resultBean.setCode(1);
        } else {
            resultBean.setCode(0);
            logger.error("更新样本集信息失败");
        }
        return gson.toJson(resultBean);


    }

    public String importTree(JsonObject paramObj) {
        String crf_id = "kidney_cancer";
        try {
            crf_id = paramObj.get("crf_id").getAsString();
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        return gson.toJson(ConfigurationService.getImportTree(crf_id));
    }

    /**
     *
     * @param param
     * @return
     */
    public String sampleSetDirectoryList(String param) {
        String sampleURI = null;
        String key = null;
        String crf_id = "kidney_cancer";
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            sampleURI = paramObj.get("sampleURI").getAsString();
            key = paramObj.get("keywords").getAsString();
            if(paramObj.has("crf_id")){
                crf_id = paramObj.get("crf_id").getAsString();
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        ProSample proSample = AllDao.getInstance().getProjectDao().getSampleDataBySampleURI(sampleURI);
        String itemsStr = proSample.getItems();
        JsonArray itemArray = (JsonArray) jsonParser.parse(itemsStr);
        Set<String> set = new HashSet<>();
        for (JsonElement jsonElement:itemArray){
            set.add(jsonElement.getAsString());
        }
        ResultBean resultBean = new ResultBean();
        JsonObject all = ConfigurationService.getAllObj(crf_id);
        JsonObject allCopy = (JsonObject) jsonParser.parse(gson.toJson(all));
        JsonObject allNew = new JsonObject();
        for (Map.Entry<String, JsonElement> obj : allCopy.entrySet()) {
            String groupName = obj.getKey();
            JsonArray items = obj.getValue().getAsJsonArray();
            JsonArray newGroup = new JsonArray();
            for (JsonElement json : items) {
                JsonObject item = json.getAsJsonObject();
                item.remove("relatedItems");
                String IndexFieldName = item.get("IndexFieldName").getAsString();
                String UIFieldName = item.get("UIFieldName").getAsString();
                if (set.contains(IndexFieldName)) {
                    if(key == null ||"".equals(key) || UIFieldName.contains(key)){
                        newGroup.add(item);
                    }
                }

            }
            if (newGroup.size() > 0) {
                allNew.add(groupName, newGroup);
            }
        }
        resultBean.setCode(1);
        resultBean.setData(allNew);
        return gson.toJson(resultBean);
    }

    public String sampleSetSearch(String param) {
        String url = ConfigurationService.getUrlBean().getSampleDetailSearchURL();
        logger.info("sampleSetSearch url="+url);
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        String sampleURI = paramObj.get("sampleURI").getAsString();
        String limitStr = paramObj.get("limit").getAsString();
        int[] ls = ParamUtils.parseLimit(limitStr);
        paramObj.addProperty("page",ls[0]);
        paramObj.addProperty("size",ls[1]);
        paramObj.remove("sampleURI");
        paramObj.remove("limit");
        paramObj.addProperty("data_id",sampleURI);
        String paramNew = gson.toJson(paramObj);
        logger.info("转化后的请求参数="+paramNew);
        String reStr = HttpRequestUtils.httpPost(url ,paramNew);
        logger.info("sampleSetSearch result="+reStr);
        if(reStr == null || "".equals(reStr)){
            return ParamUtils.errorParam("FS 返回空");
        }
        try{
            JsonObject json = (JsonObject) jsonParser.parse(reStr);
            boolean succeed = json.get("success").getAsBoolean();
            if (succeed) {
                JsonArray Use = json.getAsJsonArray("USE");
                Map<String,String> useMap = new HashMap<>();
                for(JsonElement jsonElement:Use){
                    JsonObject obj = jsonElement.getAsJsonObject();
                    String __SAMPLE_ID = obj.get("__SAMPLE_ID").getAsString();
                    String use = obj.get("use").getAsString();
                    useMap.put(__SAMPLE_ID,use);
                }

                JsonArray PROPERTY = json.getAsJsonArray("PROPERTY");
                JsonElement RANGE = json.get("RANGE");
                JsonArray schemaJson = json.getAsJsonArray("SCHEMA");
                JsonObject schema = new JsonObject();
                List<String> list = new LinkedList<>();
                for(JsonElement schemaElement:schemaJson){
                    String index = schemaElement.getAsString();
                    if("__SAMPLE_ID".equals(index)){
                        schema.addProperty(index,"样本id");
                    }else{
                        String uiName = ConfigurationService.getUIFieldName(index);
                        schema.addProperty(index,uiName);
                    }
                    list.add(index);
                }
                schema.addProperty("__use","是否使用");
                JsonArray data = new JsonArray();
                JsonArray DATAArray = json.getAsJsonArray("DATA");

                for(JsonElement dataItemArray:DATAArray){
                    JsonArray oneDataArray = dataItemArray.getAsJsonArray();
                    JsonObject entity = new JsonObject();
                    for(int i=0;i<list.size();i++){
                        String index = list.get(i);
                        JsonPrimitive dataValue = oneDataArray.get(i).getAsJsonPrimitive();
                        entity.add(index,dataValue);
                    }
                    String __SAMPLE_ID = entity.get("__SAMPLE_ID").getAsString();
                    String use = useMap.get(__SAMPLE_ID);
                    entity.addProperty("__use",use);
                    data.add(entity);

                }
                JsonObject result = new JsonObject();
                JsonObject info = new JsonObject();
                JsonElement TOTAL = json.get("TOTAL");
                JsonElement SEARCH_TOTAL = json.get("SEARCH_TOTAL");
                JsonElement USE_TOTAL = json.get("USE_TOTAL");
                info.add("schema",schema);
                info.add("TOTAL",TOTAL);
                info.add("SEARCH_TOTAL",SEARCH_TOTAL);
                info.add("USE_TOTAL",USE_TOTAL);
                info.add("property",PROPERTY);
                info.add("RANGE",RANGE);
                result.addProperty("code",1);
                result.add("info",info);
                result.add("data",data);
                return gson.toJson(result);
            }else{
                return ParamUtils.errorParam("FS出现异常");
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String uploadAdaptTag(String param) {
        String url = ConfigurationService.getUrlBean().getSampleUploadAdaptTagURL();
        logger.info("uploadAdaptTag url="+url);
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        String sampleURI = paramObj.get("sampleURI").getAsString();
        paramObj.addProperty("data_id",sampleURI);
        paramObj.remove("sampleURI");
        String paramNew = gson.toJson(paramObj);
        logger.info("转化后的请求参数="+paramNew);
        String reStr = HttpRequestUtils.httpPost(url ,paramNew);
        logger.info("uploadAdaptTag result="+reStr);
        if(reStr == null || "".equals(reStr)){
            return ParamUtils.errorParam("FS 返回空");
        }
        try{
            JsonObject json = (JsonObject) jsonParser.parse(reStr);
            boolean succeed = json.get("success").getAsBoolean();
            ResultBean resultBean = new ResultBean();
            if(succeed){
                resultBean.setCode(1);
                return gson.toJson(resultBean);
            }else {
                resultBean.setCode(0);
                return gson.toJson(resultBean);
            }
        }catch (Exception e){
            return ParamUtils.errorParam("出现异常");
        }
    }

    public String importSampleCheck(JsonObject jsonObject, User user) {
        try {

            JsonObject query = jsonObject.get("query").getAsJsonObject();
            JsonElement roles = jsonObject.get("roles");
            query.add("roles",roles);
            JsonElement power = jsonObject.get("power");
            query.add("power",power);
            JsonArray groups = jsonObject.get("groups").getAsJsonArray();
            query.add("groups",groups);

            logger.info("原始搜索条件="+gson.toJson(query));
            String withSid = CaseProcessor.transformSid(gson.toJson(query),user);
            JsonObject queryNew = (JsonObject) jsonParser.parse(withSid);
            logger.info("sid 处理后搜索条件="+gson.toJson(queryNew));
            String url = ConfigurationService.getUrlBean().getSampleImportChecKIURL();
            JsonObject param = new JsonObject();
            param.add("query",queryNew);
            if(queryNew.has("code") && queryNew.get("code").getAsInt() ==0){
                return gson.toJson(queryNew);
            }
            String data = HttpRequestUtils.httpPostForSampleImport(url,gson.toJson(param));
            if(data == null){
                return ParamUtils.errorParam("FS 返回为空");
            }else {
                JsonObject resultBean = new JsonObject();
                JsonObject dataObj = (JsonObject) jsonParser.parse(data);
                resultBean.addProperty("code",1);
                resultBean.add("data",dataObj);
                return gson.toJson(resultBean);
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }

    }
}
