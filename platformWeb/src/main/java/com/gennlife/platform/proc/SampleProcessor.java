package com.gennlife.platform.proc;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.projectBean.ProLog;
import com.gennlife.platform.bean.projectBean.ProSample;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.enums.LogActionEnum;
import com.gennlife.platform.parse.SampleImportParser;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chensong on 2015/12/9.
 */
public class SampleProcessor {
    private static Logger logger = LoggerFactory.getLogger(SampleProcessor.class);
    private static View viewer = new View();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static String setHeadUrl = null;
    private static String setDataUrl = null;
    static{
        setHeadUrl = ArkService.getConf().getSetDetailURL();
        setDataUrl = ArkService.getConf().getSetDetailDataURL();
    }
    /**
     * 搜索关键词
     * @param req
     * @param resp
     */
    public void importSample(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("importSample param="+param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String projectID = jsonObject.get("projectID").getAsString();
        String indexName = jsonObject.get("indexName").getAsString();
        String sampleName = null;
        String uid = jsonObject.get("uid").getAsString();
        if(jsonObject.get("sampleName") != null){
            sampleName = jsonObject.get("sampleName").getAsString();
        }
        String sampleDesc = null;
        if(jsonObject.get("sampleDesc").getAsString() != null){
            sampleDesc = jsonObject.get("sampleDesc").getAsString();
        }

        Object query =  jsonObject.get("query").getAsJsonObject();

        JsonArray itemColumnName = jsonObject.get("itemColumnName").getAsJsonArray();
        logger.info("itemColumnName ="+ gson.toJson(itemColumnName));
        String data = SampleImportParser.getURI(projectID, itemColumnName, query, indexName);
        String itemMap = gson.toJson(itemColumnName);
        Long startTime = System.currentTimeMillis();
        logger.info("data = " + data);
        JsonObject resultMap = jsonParser.parse(data).getAsJsonObject();
        logger.info("时间======"+(System.currentTimeMillis()-startTime));
        Boolean succeed = resultMap.get("succeed").getAsBoolean();
        Map<String,Object> dataMap = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();
        if(succeed){
            String uri = resultMap.get("uri").getAsString();
            Integer total = resultMap.get("total").getAsInt();
            Map<String,Object> conf = new HashMap<String, Object>();
            conf.put("projectID",projectID);
            Integer batchID = AllDao.getInstance().getProjectDao().getMaxBatchID(conf);
            if(batchID == null){
                batchID = 1;
            }else{
                batchID ++;
            }
            if(sampleName == null){
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
            proSample.setItems(itemMap);
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
            dataMap.put("total", resultMap.get("total"));
        }else{
            resultBean.setCode(0);

        }
        viewer.viewString(gson.toJson(resultBean),resp,req);
    }

    public void sampleDetail(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("sampleDetail param="+param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String uri = jsonObject.get("uri").getAsString();
        logger.info("url =" + String.format(setHeadUrl, uri));
        String url = String.format(setHeadUrl, ParamUtils.encodeURI(uri));
        String headData = HttpRequestUtils.httpGet(url);
        JsonObject jsonHead = jsonParser.parse(headData).getAsJsonObject();
        boolean succeed = jsonHead.get("succeed").getAsBoolean();
        if(succeed){
            JsonObject schemaJson = jsonHead.getAsJsonObject("schema");
            Map<String,JsonObject> headMap = new HashMap<String, JsonObject>();
            for(Map.Entry<String, JsonElement> entry:schemaJson.entrySet()){
                JsonObject tableHead = new JsonObject();
                String key = entry.getKey();
                JsonObject valueOject = entry.getValue().getAsJsonObject();
                String UIFieldName = key;
                if(valueOject.get("UIFieldName") != null){
                    UIFieldName = valueOject.get("UIFieldName").getAsString();
                }
                String type = valueOject.get("type").getAsString();
                tableHead.addProperty("key",key);
                tableHead.addProperty("UIFieldName",UIFieldName);
                tableHead.addProperty("type",type);
                headMap.put(key,tableHead);
            }
            logger.info(String.format(setDataUrl, uri));
            url = String.format(setDataUrl, ParamUtils.encodeURI(uri));
            String tableData = HttpRequestUtils.httpGet(url);
            JsonObject jsonData = jsonParser.parse(tableData).getAsJsonObject();
            succeed = jsonData.get("succeed").getAsBoolean();
            if(succeed){
                JsonArray dataArray = jsonData.getAsJsonArray("data");
                String str = viewer.ViewDetailSet(headMap,dataArray);
                viewer.viewString(str,resp,req);
            }
        }
    }

    /**
     * 编译样本集
     * @param req
     * @param resp
     */
    public void editSet(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("editSet param="+param);
        String uid = null;
        String projectID = null;
        String sampleName = null;
        Map<String,Object> map = new HashMap<String, Object>();
        ResultBean resultBean = new ResultBean();

        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            projectID = paramObj.get("projectID").getAsString();
            String sampleURI = paramObj.get("sampleURI").getAsString();
            sampleName = paramObj.get("sampleName").getAsString();
            String sampleDesc = paramObj.get("sampleDesc").getAsString();
            uid = paramObj.get("uid").getAsString();
            map.put("projectID",projectID);
            map.put("sampleURI",sampleURI);
            map.put("sampleName",sampleName);
            map.put("sampleDesc",sampleDesc);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }

        int count = AllDao.getInstance().getProjectDao().updateSetInfo(map);
        if(count >= 1){
            ProLog proLog = new ProLog();
            proLog.setProjectID(projectID);
            proLog.setUid(uid);
            proLog.setAction(LogActionEnum.CreatePlan.getName());
            SyUser syUser = UserProcessor.getUser(uid);
            proLog.setLogText(syUser.getUname() + LogActionEnum.UpdateSetInfo +":"+sampleName);
            proLog.setLogTime(new Date());
            AllDao.getInstance().getProjectDao().insertProLog(proLog);
            resultBean.setCode(1);
        }else{
            resultBean.setCode(0);
            logger.error("更新样本集信息失败");
        }
        viewer.viewString(gson.toJson(resultBean),resp,req);


    }
}
