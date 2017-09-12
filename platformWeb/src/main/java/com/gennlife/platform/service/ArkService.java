package com.gennlife.platform.service;

import com.gennlife.platform.util.FilesUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chensong on 2015/12/10.
 */
@Component
public class ArkService {
    private static final Logger logger = LoggerFactory.getLogger(ArkService.class);
    private static JsonParser jsonParser = new JsonParser();
    @Autowired
    private ConfigurationService configurationService;

    public void init(String s, Element element) {
        //crf 属性关联配置文件
        logger.info("crf 属性关联配置文件 ");
        parseChain();
        //项目疾病下拉菜单接口
        logger.info("项目疾病下拉菜单接口 ");
        parseProjectDisease();
    }

    public void destroy() {

    }


    private static JsonObject chainJson = null;

    public static void parseChain() {
        try {
            String data = FilesUtils.readFile("/chainConf.json");
            chainJson = (JsonObject) jsonParser.parse(data);
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException();
        }
    }

    private static JsonObject projectDisease = null;

    private static Map<String, String> diseaseMap = new HashMap<String, String>();

    public static String getDiseaseName(String key) {
        return diseaseMap.get(key);
    }

    public static JsonObject getProjectDisease() {
        return projectDisease;
    }

    public static void parseProjectDisease() {
        try {
            String data = FilesUtils.readFile("/projectDisease.json");
            JsonObject object = (JsonObject) jsonParser.parse(data);
            projectDisease = object.getAsJsonObject("disease");
            for (Map.Entry<String, JsonElement> jsonElement : projectDisease.entrySet()) {
                String key = jsonElement.getKey();
                String value = projectDisease.get(key).getAsString();
                diseaseMap.put(value, key);
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException();
        }
    }


    public static final JsonObject getChainJson() {
        return chainJson;
    }

}
