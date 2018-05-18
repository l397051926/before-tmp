package com.gennlife.platform.ReadConfig;

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
 * @create 2018 18 14:01
 * @desc
 **/
public class ReadConditionByRedis {
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
                RedisUtil.setValue(crf_id,gson.toJson(advancedSearch));
            }
        }catch (Exception e){
            logger.error("", e);
            throw new RuntimeException();
        }
    }

    public static JsonObject getCrfSearch(String crf_id) {
        if(!RedisUtil.isExists(crf_id)){
            loadConfigurationInfo();
        }
        String data = RedisUtil.getValue(crf_id);
        if (data != null) {
            JsonObject target = (JsonObject) jsonParser.parse(data);
            return target;
        }
        return new JsonObject();
    }
}
