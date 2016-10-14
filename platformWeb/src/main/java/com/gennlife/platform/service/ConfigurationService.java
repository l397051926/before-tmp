package com.gennlife.platform.service;

import com.gennlife.platform.configuration.FileBean;
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
    private static FileBean fileBean = null;
    //<orgID,indexName>
    private static Map<String,String> orgIDIndexNamemap = new HashMap<>();
    //全量属性的jsonobject
    private static List<JsonObject> allList = new LinkedList<JsonObject>();
    //index name,ui name
    private static Map<String,String> nameMap = new HashMap<>();

    private static JsonObject importTree = new JsonObject();

    private static JsonObject advancedSearch = new JsonObject();
    private static JsonArray resourceTypeArray = null;
    //默认的搜索列表
    private static JsonObject defaultObj = null;
    //全部的搜索列表
    private static JsonObject allObj = null;
    //比较因子属性列表
    private static JsonObject compareObj = null;
    private static Gson gson = GsonUtil.getGson();
    public static void init() {
        try{
            ApplicationContext context = SpringContextUtil.getApplicationContext();
            urlBean = (URLBean) context.getBean("com.gennlife.platform.configuration.URLBean");
            fileBean = (FileBean) context.getBean("FileLocation");
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
        String caseStr = FilesUtils.readFile("/case.json");
        //logger.info("case.json="+caseStr);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(caseStr);
        defaultObj = jsonObject.getAsJsonObject("default");
        allObj = jsonObject.getAsJsonObject("all");
        advancedSearch = jsonObject.getAsJsonObject("advancedSearch");
        compareObj =  jsonObject.getAsJsonObject("compare");
        importTree = jsonObject.getAsJsonObject("import");
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

        String resourceStr = FilesUtils.readFile("/resourceConfig.json");
        JsonObject resourceConfig = (JsonObject) jsonParser.parse(resourceStr);
        resourceTypeArray = resourceConfig.get("resourceTypeArray").getAsJsonArray();

        String orgIDIndexNameStr = FilesUtils.readFile("/UserOrgIDMapIndex.json");

        JsonObject orgIDIndexNameObj = (JsonObject) jsonParser.parse(orgIDIndexNameStr);
        for(Map.Entry<String, JsonElement> entity:orgIDIndexNameObj.entrySet()){
            String orgID = entity.getKey();
            String indexName = entity.getValue().getAsString();
            orgIDIndexNamemap.put(orgID,indexName);
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


    public static JsonArray getResourceTypeArray(){
        return resourceTypeArray;
    }

    public static JsonObject getCompareObj() {
        return compareObj;
    }

    public static FileBean getFileBean(){
        return fileBean;
    }


    public static Map<String, String> getOrgIDIndexNamemap() {
        return orgIDIndexNamemap;
    }
}
