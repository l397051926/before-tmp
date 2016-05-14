package com.gennlife.platform.service;

import com.gennlife.platform.configuration.URLBean;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.SpringContextUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;

/**
 * Created by chen-song on 16/5/13.
 */
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
    private static JsonParser jsonParser = new JsonParser();
    private static URLBean urlBean = null;
    //全量属性的jsonobject
    private static List<JsonObject> allList = new LinkedList<JsonObject>();
    //默认的搜索列表
    private static JsonObject defaultObj = null;
    //全部的搜索列表
    private static JsonObject allObj = null;
    private static Gson gson = GsonUtil.getGson();
    public static void init() {
        try{
            ApplicationContext context = SpringContextUtil.getApplicationContext();
            urlBean = (URLBean) context.getBean("com.gennlife.platform.configuration.URLBean");
            logger.info("新的搜索后端接口:"+urlBean.getCaseSearchURL());
            logger.info("详情页患者基础信息接口:"+urlBean.getCaseDetailPatientBasicInfoURL());
            logger.info("基本统计图形&筛选条件:"+urlBean.getCaseDetailPatientBasicFigureURL());
            logger.info("详情页时间轴信息接口:"+urlBean.getCasePatientBasicTimeAxisURL());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("",e);
            throw new RuntimeException();
        }
        try{
            loadConfigurationInfo();
        }catch (Exception e){

        }
    }

    public static URLBean getUrlBean() {
        return urlBean;
    }

    public static void loadConfigurationInfo() throws IOException {
        String caseStr = FilesUtils.readFile("/case.json");
        logger.info("case.json="+caseStr);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(caseStr);
        JsonObject caseObj = jsonObject.getAsJsonObject("case");
        defaultObj = caseObj.getAsJsonObject("default");
        allObj = caseObj.getAsJsonObject("all");
        for(Map.Entry<String, JsonElement> entity:allObj.entrySet()){
            String key = entity.getKey();
            JsonObject object = allObj.getAsJsonObject(key);
            allList.add(object);
        }
    }

    public static JsonObject getAllObj() {
        String copy = gson.toJson(allObj);
        JsonObject target = (JsonObject) jsonParser.parse(copy);
        return target;
    }

    public static List<JsonObject> getAllList() {
        return allList;
    }

    public static JsonObject getDefaultObj() {
        return defaultObj;
    }
}
