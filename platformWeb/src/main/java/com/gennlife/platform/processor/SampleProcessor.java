package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.ProLog;
import com.gennlife.platform.bean.projectBean.ProSample;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.service.ArkService;
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
    public String importSample(JsonObject jsonObject) {
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
            JsonArray source = query.getAsJsonArray("source");
            String url = ConfigurationService.getUrlBean().getSampleImportIURL();
            JsonObject param = new JsonObject();
            param.add("query",query);
            String data = HttpRequestUtils.httpPost(url,gson.toJson(param));
            Long startTime = System.currentTimeMillis();
            logger.info("data = " + data);
            JsonObject resultMap = jsonParser.parse(data).getAsJsonObject();
            logger.info("时间======" + (System.currentTimeMillis() - startTime));
            Boolean succeed = resultMap.get("success").getAsBoolean();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultBean resultBean = new ResultBean();
            if (succeed) {
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
            jsonObject.addProperty("sampleURI",sampleURI);
            String url = ConfigurationService.getUrlBean().getSampleDetailURL();
            String dataStr = HttpRequestUtils.httpPost(url,gson.toJson(jsonObject));
            JsonObject json = (JsonObject) jsonParser.parse(dataStr);
            boolean succeed = json.get("succeed").getAsBoolean();
            if (succeed) {
                JsonArray schemaJson = json.getAsJsonArray("SCEHMA");
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
                info.add("schema",schema);
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
    public String editSet(JsonObject paramObj) {
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
            SyUser syUser = UserProcessor.getUser(uid);
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

    public String importTree() {
        return gson.toJson(ConfigurationService.getImportTree());
    }
}
