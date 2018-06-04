package com.gennlife.platform.service;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.ProjectMapper;
import com.gennlife.platform.processor.RwsProcessor;
import com.gennlife.platform.util.ConfigUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class RwsService implements RwsServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(RwsProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public String PreLiminary(JsonObject paramObj) {
        int counter;
        String result = null, crfId = null, crfName = null, projectId = null, dataSource = null;
        JsonObject resultObj = null;
        boolean flag = paramObj.has("crfId");
        try {
            projectId = paramObj.get("projectId").getAsString();
            dataSource = projectMapper.getDataSource(projectId);
            logger.info("=====数据源dataSource: "+dataSource);
            if (!flag){
                paramObj.addProperty("indexName", ConfigUtils.getSearchIndexName());
                logger.info("emr的indexName" + ConfigUtils.getSearchIndexName());
            } else {
                crfId = paramObj.get("crfId").getAsString();
                //获取单病种对应的名称
                crfName = projectMapper.getCrfName(crfId);
                if (!StringUtils.isEmpty(dataSource)){
                    //如果有值，去掉单病种-
                    dataSource = dataSource.substring(4);
                    logger.info("crf-dataSource:"+dataSource);
                }
            }

            //是否可以透传
            if (StringUtils.isEmpty(dataSource) || dataSource.equals(crfName)) {
                String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
                result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                resultObj = (JsonObject) jsonParser.parse(result);
                //getResult(result,flag,projectId,crfName,crfId,resultObj);
                if (resultObj.get("status").getAsString().equals("200")){
                    counter = insertProCrfId(projectId,"单病种-"+crfName,crfId);
                    logger.info("插入数据源counter;"+counter);
                }
            } else if (StringUtils.isEmpty(dataSource) || dataSource.equals("EMR")){
                String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
                result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                resultObj = (JsonObject) jsonParser.parse(result);
                if (resultObj.get("status").getAsString().equals("200")){
                    counter = insertProCrfId(projectId,"EMR","");
                    logger.info("插入数据源counter;"+counter);
                }
            }
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
        return gson.toJson(resultObj);
    }

    //如果是crf项目，需要写入p_project的crfId字段中，然后存入对应名字到datasource
    public int insertProCrfId(String projectID,String dataSource,String crfId){
        Map<String,String> map = new HashMap<>();
        map.put("projectID",projectID);
        map.put("dataSource",dataSource);
        map.put("crfId",crfId);
        long start = System.currentTimeMillis();
        int count = projectMapper.insertProCrfId(map);
        long end = System.currentTimeMillis();
        logger.debug("insertProCrfId(map) mysql耗时"+(end-start)+"ms");
        return count;
    }

    public void getResult(String result,boolean flag,String projectId,String crfName,String crfId,JsonObject resultObj){
        int counter = 0;
        if (resultObj.get("status").getAsString().equals("200")){
            if (flag){
                counter = insertProCrfId(projectId,"单病种-"+crfName,crfId);
                logger.info("插入数据源counter;"+counter);
            } else {
                counter = insertProCrfId(projectId,"EMR","");
                logger.info("插入数据源counter;"+counter);
            }
        }
    }
}
