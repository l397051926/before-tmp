package com.gennlife.platform.ReadConfig;

import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.RedisUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liumingxin
 * @create 2018 17 20:50
 * @desc
 **/
public class LoadCrfCondition {
    private static final Logger logger = LoggerFactory.getLogger(LoadCrfCondition.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    //crf HashMap
    private static Map<String, JsonObject> CrfSearchMap = new HashMap<>();

    public static void loadConfigurationInfo(){
        try {
            String allCRFDiseases = FilesUtils.readFile("/crf/angiocardiopathy.json");
            JsonObject allCRFConfig = (JsonObject) jsonParser.parse(allCRFDiseases);
            for (Map.Entry<String, JsonElement> item : allCRFConfig.entrySet()) {
                String crf_id = item.getKey();
                JsonObject jsonObject = item.getValue().getAsJsonObject();
                JsonObject defaultObj = jsonObject.getAsJsonObject("default");
                JsonObject allObj = jsonObject.getAsJsonObject("all");
                JsonObject advancedSearch = jsonObject.getAsJsonObject("advancedSearch");
                JsonObject compareObj = jsonObject.getAsJsonObject("compare");
                JsonObject importTree = jsonObject.getAsJsonObject("import");
                CrfSearchMap.put(crf_id,advancedSearch);
            }
        }catch (Exception e){
            logger.error("", e);
            throw new RuntimeException();
        }
    }

    public static Map<String, JsonObject> getCrfSearchMap() {
        if(CrfSearchMap.size() ==0){
            loadConfigurationInfo();
        }
        return CrfSearchMap;
    }

    public static JsonObject getCrfSearch(String crf_id) {
        JsonObject jsonObject = getCrfSearchMap().get(crf_id);
        if (jsonObject != null) {
            String copy = gson.toJson(jsonObject);
            JsonObject target = (JsonObject) jsonParser.parse(copy);
            return target;
        }
        return new JsonObject();
    }


}
