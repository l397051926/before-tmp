package com.gennlife.platform.service;

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
        String result = null;
        JsonObject resultObj = new JsonObject();
        boolean flag = paramObj.has("crfId");
        try {
            String crfName = "", crfId = "";
            String projectId = paramObj.get("projectId").getAsString();
            String dataSource = projectMapper.getDataSource(projectId);

            //不是单病种需要获取indexName
            if (!flag){
                paramObj.addProperty("indexName", ConfigUtils.getSearchIndexName());
                crfName="EMR";
                logger.info("emr的indexName" + ConfigUtils.getSearchIndexName());
            } else {
                crfId = paramObj.get("crfId").getAsString();
                crfName = projectMapper.getCrfName(crfId);
                if (!StringUtils.isEmpty(dataSource) && !dataSource.equals("EMR")){
                    //去掉单病种-
                    dataSource = dataSource.substring(4);
                }
            }
            logger.info("dataSource: "+dataSource);
            if(StringUtils.isEmpty(crfId)){
                crfId = "EMR";
            }
            paramObj.addProperty("crfName",crfName);
            paramObj.addProperty("crfId",crfId);

            if (flag){
                if (StringUtils.isEmpty(dataSource) || dataSource.equals(crfName)){
                    logger.info("单病种透传rws："+ dataSource);
                    String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
                    result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                } else {
                    logger.info("当前请求：" +crfName+ ", DB：" + dataSource+"----没有透传");
                    resultObj.addProperty("code","0");
                    resultObj.addProperty("info","所选项目状态有更新，请重新选择");
                    return gson.toJson(resultObj);
                }
            } else {
                if (StringUtils.isEmpty(dataSource) || dataSource.equals("EMR")){
                    logger.info("EMR透传："+dataSource);
                    String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
                    result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
                } else {
                    logger.info("当前请求：EMR" + ", DB：" + dataSource+"----没有透传");
                    resultObj.addProperty("code","0");
                    resultObj.addProperty("info","所选项目状态有更新，请重新选择");
                    return gson.toJson(resultObj);
                }
            }
            resultObj = (JsonObject) jsonParser.parse(result);

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
}
