package com.gennlife.platform.service;

import com.gennlife.platform.bean.conf.SystemDefault;
import com.gennlife.platform.configuration.FileBean;
import com.gennlife.platform.configuration.URLBean;
import com.gennlife.platform.util.ConfigUtils;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 16/5/13.
 */
@Component
public class ConfigurationService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
    private static JsonParser jsonParser = new JsonParser();
    private static URLBean urlBean = null;
    private static FileBean fileBean = null;
    @Autowired
    private ConfigUtils configUtils;
    //<orgID,indexName>
    private static Map<String, String> orgIDIndexNamemap = new HashMap<>();
    //全量属性的jsonobject<病种，List>
    private static Map<String, List<JsonObject>> alldiseaseList = new HashMap<>();
    //<病种，<index name,ui name>>
    private static Map<String, Map<String, String>> nameMap = new HashMap<>();

    //导出可选属性<病种，对应配置>
    private static Map<String, JsonObject> importMap = new HashMap<>();
    //高级搜索
    private static Map<String, JsonObject> advancedSearchMap = new HashMap<>();
    //默认的搜索列表
    private static Map<String, JsonObject> defaultMap = new HashMap<>();
    //全部的搜索列表
    private static Map<String, JsonObject> allMap = new HashMap<>();
    //比较因子属性列表
    private static Map<String, JsonObject> compareMap = new HashMap<>();

    private static JsonArray resourceTypeArray = null;

    private static JsonObject LabIdToNumberObj = null;
    private static String default_crf_id ;
    @Autowired
    public void setDefault_crf_id( SystemDefault systemDefault)
    {
        default_crf_id=systemDefault.getSearchItemSetDefault();
    }

    private static Gson gson = GsonUtil.getGson();


    public static URLBean getUrlBean() {
        return urlBean;
    }

    public  void loadConfigurationInfo() throws IOException {
        String caseStr = configUtils.getRemoteUtfFile("case.json");
        if(StringUtils.isEmpty(caseStr))
        {
            caseStr = FilesUtils.readFile("/case.json");
        }
        //logger.info("case.json = "+caseStr);
        JsonObject allDiseasesObj = (JsonObject) jsonParser.parse(caseStr);
        for (Map.Entry<String, JsonElement> item : allDiseasesObj.entrySet()) {
            String crf_id = item.getKey();
            JsonObject jsonObject = item.getValue().getAsJsonObject();
            JsonObject defaultObj = jsonObject.getAsJsonObject("default");
            JsonObject allObj = jsonObject.getAsJsonObject("all");
            JsonObject advancedSearch = jsonObject.getAsJsonObject("advancedSearch");
            JsonObject compareObj = jsonObject.getAsJsonObject("compare");
            JsonObject importTree = jsonObject.getAsJsonObject("import");
            for (Map.Entry<String, JsonElement> entity : allObj.entrySet()) {
                String key = entity.getKey();
                JsonArray object = allObj.getAsJsonArray(key);
                for (JsonElement jsonElement : object) {
                    JsonObject json = jsonElement.getAsJsonObject();
                    String UIFieldName = json.get("UIFieldName").getAsString();
                    String IndexFieldName = json.get("IndexFieldName").getAsString();
                    if (!nameMap.containsKey(crf_id)) {
                        Map<String, String> tmpName = new HashMap<>();
                        tmpName.put(IndexFieldName, UIFieldName);
                        nameMap.put(crf_id, tmpName);
                        List<JsonObject> tmpList = new LinkedList<>();
                        tmpList.add(json);
                        alldiseaseList.put(crf_id, tmpList);
                    } else {
                        Map<String, String> tmpName = nameMap.get(crf_id);
                        tmpName.put(IndexFieldName, UIFieldName);
                        List<JsonObject> tmpList = alldiseaseList.get(crf_id);
                        tmpList.add(json);
                    }
                }
            }
            importMap.put(crf_id, importTree);
            advancedSearchMap.put(crf_id, advancedSearch);
            defaultMap.put(crf_id, defaultObj);
            allMap.put(crf_id, allObj);
            compareMap.put(crf_id, compareObj);
        }


        String resourceStr = FilesUtils.readFile("/resourceConfig.json");
        JsonObject resourceConfig = (JsonObject) jsonParser.parse(resourceStr);
        resourceTypeArray = resourceConfig.get("resourceTypeArray").getAsJsonArray();

        String orgIDIndexNameStr = FilesUtils.readFile("/UserOrgIDMapIndex.json");

        JsonObject orgIDIndexNameObj = (JsonObject) jsonParser.parse(orgIDIndexNameStr);
        for (Map.Entry<String, JsonElement> entity : orgIDIndexNameObj.entrySet()) {
            String orgID = entity.getKey();
            String indexName = entity.getValue().getAsString();
            orgIDIndexNamemap.put(orgID, indexName);
        }

        String LabIdToNumber = FilesUtils.readFile("/LabIdToNumber.json");
        LabIdToNumberObj = (JsonObject) jsonParser.parse(LabIdToNumber);
    }

    public static JsonObject getAllObj(String crf_id) {
        JsonObject jsonObject = allMap.get(crf_id);
        if (jsonObject == null) {
            jsonObject = allMap.get(default_crf_id);
        }
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            return target;
        }
        return new JsonObject();
    }


    public static JsonObject getDefaultObj(String crf_id) {
        JsonObject jsonObject = defaultMap.get(crf_id);
        if (jsonObject == null) {
            jsonObject = defaultMap.get(default_crf_id);
        }
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            target = removeDiseasePrefix(target);
            return target;
        }
        return new JsonObject();
    }

    public static JsonObject getImportTree(String crf_id) {
        JsonObject jsonObject = importMap.get(crf_id);
        if (jsonObject == null) {
            jsonObject = importMap.get(default_crf_id);
        }
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            return target;
        }
        return new JsonObject();
    }

    public static JsonObject getAdvancedSearch(String crf_id) {
        JsonObject jsonObject = advancedSearchMap.get(crf_id);
        if (jsonObject == null) {
            jsonObject = advancedSearchMap.get(default_crf_id);
        }
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            return target;
        }
        return new JsonObject();
    }


    public static String getUIFieldName(String IndexFieldName, String crf_id) {
        Map<String, String> tmpName = nameMap.get(crf_id);
        if (tmpName == null) {
            return null;
        } else {
            return tmpName.get(IndexFieldName);
        }
    }

    public static String getUIFieldName(String IndexFieldName) {
        for (String crf_id : nameMap.keySet()) {
            Map<String, String> tmpName = nameMap.get(crf_id);
            if (tmpName.containsKey(IndexFieldName)) {
                return tmpName.get(IndexFieldName);
            }
        }
        return null;

    }


    public static JsonArray getResourceTypeArray() {
        return resourceTypeArray;
    }

    public static JsonObject getCompareObj(String crf_id) {
        JsonObject jsonObject = compareMap.get(crf_id);
        if (jsonObject == null) {
            jsonObject = compareMap.get(default_crf_id);
        }
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            return target;
        }
        return null;
    }

    public static FileBean getFileBean() {
        return fileBean;
    }


    public static Map<String, String> getOrgIDIndexNamemap() {
        return orgIDIndexNamemap;
    }

    public static List<JsonObject> getAllList(String crf_id) {

        return alldiseaseList.get(crf_id);
    }

    public static JsonObject removeDiseasePrefix(JsonObject target) {
        JsonObject newTarget = new JsonObject();
        for (Map.Entry<String, JsonElement> item : target.entrySet()) {
            String groupName = item.getKey();
            JsonElement groupValue = item.getValue();
            groupName = groupName.replaceFirst("肺癌.", "");
            groupName = groupName.replaceFirst("肝癌.", "");
            groupName = groupName.replaceFirst("肾癌.", "");
            newTarget.add(groupName, groupValue);
        }
        return newTarget;
    }

    public static JsonObject getLabIdToNumberObj() {
        return LabIdToNumberObj;
    }
    @Autowired
    public  void setUrlBean(URLBean urlBean) {
        ConfigurationService.urlBean = urlBean;
    }
    @Autowired
    public  void setFileBean(FileBean fileBean) {
        ConfigurationService.fileBean = fileBean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            logger.info("ConfigurationService.init启动");
            //搜索相关配置列表
            loadConfigurationInfo();
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
