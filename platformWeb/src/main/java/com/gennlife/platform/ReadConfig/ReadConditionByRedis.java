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

    public static void loadCrfConfiguration(String crfId){
        try {
            String allCRFDiseases = FilesUtils.readFile("/crf/"+crfId+".json");
//            String allCRFDiseases = FilesUtils.readCrfFile("src/main/resources/crf/"+crfId+".json");
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
            logger.error("读取配置文件 异常", e);
        }
    }

    public static void loadConfigurationInfo(String crfId){
        try {
            //读取 emrrws
            String EmrRwsConfig = FilesUtils.readFile("/rws/emr_rws.json");
            RedisUtil.setValue("emr_rws",EmrRwsConfig);
            //读取 crf rws
            String crf_rws= FilesUtils.readFile("/rws/crf_rws_"+crfId+".json");
            RedisUtil.setValue("crf_rws_"+crfId,crf_rws);

        }catch (Exception e){
            logger.error("读取配置文件 异常", e);
        }
    }

    public static void loadCrfMapping(){
        try{
            String crf_mapping = FilesUtils.readFile("/rws/crf_mapping.json");
            JsonObject jsonObject = (JsonObject) jsonParser.parse(crf_mapping);
            for(Map.Entry<String,JsonElement> entry:jsonObject.entrySet()){
                String key = entry.getKey();
                String value = gson.toJson(entry.getValue());
                RedisUtil.setValue("crf_"+key+"_mapping",value);
            }
            logger.info("crf_mapping 配置文件读取完毕"+crf_mapping);
        }catch (Exception e ){
            logger.error("crf_mapping 配置文件读取 异常", e);
        }
    }

    public static void loadCrfHitSort(){
        try{
            String crf_mapping = FilesUtils.readFile("/crf/crf_hit_sort.json");
            JsonObject jsonObject = (JsonObject) jsonParser.parse(crf_mapping);
            for(Map.Entry<String,JsonElement> entry:jsonObject.entrySet()){
                String key = entry.getKey();
                RedisUtil.setValue("crf_"+key+"_sort",entry.getValue().getAsString());
            }
            logger.info("crf_hit_sort 配置文件读取完毕");
        }catch (Exception e ){
            logger.error("crf_hit_sort 配置文件读取异常", e);
        }
    }
    public static void loadSearchDefinedEventListConfig(String crfId){
        try{
            String search_defined = FilesUtils.readFile("/rws/searchDefinedEventListConfig"+crfId+".json");
            RedisUtil.setValue("search_defined_"+crfId,search_defined);
            logger.info("crf_hit_sort 配置文件读取完毕");
        }catch (Exception e ){
            logger.error("crf_hit_sort 配置文件读取异常", e);
        }
    }
    public static String getLoadSearchDefinedEventListConfig(String crf_id) {
        if(!RedisUtil.isExists("search_defined_"+crf_id)){
            loadSearchDefinedEventListConfig(crf_id);
        }
        String data = RedisUtil.getValue("search_defined_"+crf_id);
        return data;
    }


    //先放到 crf 查询那里吧
    public static void getCrfMapping(String crfId){
        if(!RedisUtil.isExists("crf_"+crfId+"_mapping")){
            loadCrfMapping();
        }

    }

    public static JsonObject getCrfSearch(String crf_id) {
        if(!RedisUtil.isExists("SEARCH_"+crf_id)){
            loadCrfConfiguration(crf_id);
        }
        String data = RedisUtil.getValue("SEARCH_"+crf_id);
        if (data != null) {
            JsonObject target = (JsonObject) jsonParser.parse(data);
            return target;
        }
        logger.info("crf 映射rws 映射成功 + "+ data);
        return new JsonObject();
    }

    public static JsonArray getEmrRws(){
        if(!RedisUtil.isExists("emr_rws")){
            loadConfigurationInfo("CVD");
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
            loadConfigurationInfo(crfId);
        }
        String data = RedisUtil.getValue("crf_rws_"+crfId);
        if(data !=null){
            JsonArray target = (JsonArray) jsonParser.parse(data);
            return target;
        }
        return new JsonArray();
    }

    public static String getCrfHitSort(String crfId) {
        if(!RedisUtil.isExists("crf_"+crfId+"_sort")){
            loadCrfHitSort();
        }
        String data = RedisUtil.getValue("crf_"+crfId+"_sort");
        return data;
    }
}
