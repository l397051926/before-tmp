package com.gennlife.platform.service;

import com.gennlife.platform.configuration.URLBean;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.SpringContextUtil;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 16/5/13.
 */
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
    private static JsonParser jsonParser = new JsonParser();
    private static URLBean urlBean = null;
    //全量属性的jsonobject
    private static List<JsonObject> allList = new LinkedList<JsonObject>();
    //index name,ui name
    private static Map<String,String> nameMap = new HashMap<>();

    private static JsonObject importTree = new JsonObject();

    private static JsonObject advancedSearch = new JsonObject();

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
            //搜索相关配置列表
            loadConfigurationInfo();
        }catch (Exception e){
            logger.error("",e);
        }
    }

    public static URLBean getUrlBean() {
        return urlBean;
    }

    public static void loadConfigurationInfo() throws IOException {
        String caseStr = FilesUtils.readFile("/case819bak.json");
        logger.info("case.json="+caseStr);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(caseStr);
        JsonObject caseObj = jsonObject.getAsJsonObject("case");
        defaultObj = caseObj.getAsJsonObject("default");
        allObj = caseObj.getAsJsonObject("all");
        advancedSearch = caseObj.getAsJsonObject("advancedSearch");
        for(Map.Entry<String, JsonElement> entity:allObj.entrySet()){
            String key = entity.getKey();
            JsonArray object = allObj.getAsJsonArray(key);
            for(JsonElement jsonElement:object){
                JsonObject json = jsonElement.getAsJsonObject();
                String UIFieldName = json.get("UIFieldName").getAsString();
                String IndexFieldName = json.get("IndexFieldName").getAsString();
                nameMap.put(IndexFieldName,UIFieldName);
                allList.add(json);
            }

        }
        importTree = caseObj.getAsJsonObject("import");

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

    public static JsonObject getImportTree() {
        return importTree;
    }

    public static JsonObject getAdvancedSearch() {
        return advancedSearch;
    }

    public static void setAdvancedSearch(JsonObject advancedSearch) {
        ConfigurationService.advancedSearch = advancedSearch;
    }

    public  static String getUIFieldName(String IndexFieldName){
        return nameMap.get(IndexFieldName);
    }
}
