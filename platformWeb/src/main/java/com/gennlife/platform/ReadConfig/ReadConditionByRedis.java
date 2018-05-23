package com.gennlife.platform.ReadConfig;

import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.RedisUtil;
import com.google.gson.*;
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
            //读取 emrrws
            String EmrRwsConfig = FilesUtils.readFile("/rws/emr_rws.json");
            RedisUtil.setValue("emr_rws",EmrRwsConfig);
            //读取 crf rws
            String crf_rws_CVD = FilesUtils.readFile("/rws/crf_rws_CVD.json");
            RedisUtil.setValue("crf_rws_CVD",crf_rws_CVD);

        }catch (Exception e){
            logger.error("读取配置文件 异常", e);
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

    public static JsonArray getEmrRws(){
        if(!RedisUtil.isExists("emr_rws")){
            loadConfigurationInfo();
        }
        String data = RedisUtil.getValue("emr_rws");
        if(data !=null){
            JsonArray target = (JsonArray) jsonParser.parse(data);
            return target;
        }
        return new JsonArray();
    }

    public static JsonArray getCrfRws(String crfId){
        if(!RedisUtil.isExists("crf_rws_"+crfId)){
            loadConfigurationInfo();
        }
        String data = RedisUtil.getValue("crf_rws_"+crfId);
        if(data !=null){
            JsonArray target = (JsonArray) jsonParser.parse(data);
            return target;
        }
        return new JsonArray();
    }

}
