package com.gennlife.platform.service;

import com.gennlife.platform.bean.conf.ConfGroupInfo;
import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import com.google.gson.internal.LinkedHashTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chensong on 2015/12/10.
 */
public class ArkService {
    private static final Logger logger = LoggerFactory.getLogger(ArkService.class);
    private static Conf conf = null;
    private static MongoConf mongoConf = null;
    private static ExecutorService executorService;
    private static String seerField;
    private static String CosmicMutantExport_v70;
    private static Gson gson = GsonUtil.getGson();


    private static JsonParser jsonParser = new JsonParser();

    public void init(String s, Element element) {
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        logger.info("qRecommendURL =" + conf.getqRecommendURL());
        logger.info("searchURL =" + conf.getSearchURL());
        logger.info("fileURL =" + conf.getFileURL());
        logger.info("detailURL =" + conf.getDetailURL());
        logger.info("importURL =" + conf.getImportURL());
        executorService = Executors.newCachedThreadPool();
        parseChain();
        parseProjectDisease();
        logger.info("开始初始化mongoDB相关配置,,,,");
        mongoConf = (MongoConf) context.getBean("com.gennlife.platform.util.MongoConf");
        try {
            MongoManager.init(mongoConf);
            MongoManager.initCollection();
        } catch (UnknownHostException e) {
            logger.error("",e);
            throw new RuntimeException();
        }

        try{
            ConfigurationService.init();
        }catch (Exception e){
            logger.error("ConfigurationService 启动失败");
            logger.error("",e);
            throw new RuntimeException();
        }
    }

    public void destroy() {
        MongoManager.destory();
    }

    public static Conf getConf(){
        if(conf == null){
            ApplicationContext context = SpringContextUtil.getApplicationContext();
            conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        }
        return conf;
    }
    public static ExecutorService getExecutorService(){
        if(executorService == null){
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }



    private static JsonObject chainJson = null;

    public static void parseChain(){
        try {
            String data = FilesUtils.readFile("/chainConf.json");
            chainJson = (JsonObject) jsonParser.parse(data);
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }
    }
    private static JsonObject projectDisease = null;

    private static Map<String,String> diseaseMap = new HashMap<String, String>();
    public static String getDiseaseName(String key){
        return diseaseMap.get(key);
    }
    public static JsonObject getProjectDisease() {
        return projectDisease;
    }

    public static void parseProjectDisease(){
        try {
            String data = FilesUtils.readFile("/projectDisease.json");
            JsonObject object = (JsonObject) jsonParser.parse(data);
            projectDisease = object.getAsJsonObject("disease");
            for(Map.Entry<String, JsonElement> jsonElement:projectDisease.entrySet()){
                String key = jsonElement.getKey();
                String value = projectDisease.get(key).getAsString();
                diseaseMap.put(value,key);
            }
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }
    }


    public static final JsonObject getChainJson(){
        return chainJson;
    }

}
