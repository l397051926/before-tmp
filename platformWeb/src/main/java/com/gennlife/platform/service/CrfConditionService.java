package com.gennlife.platform.service;

import com.gennlife.platform.util.FilesUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liumingxin
 * @create 2018 17 20:06
 * @desc
 **/
public class CrfConditionService {
    private static final Logger logger = LoggerFactory.getLogger(ArkService.class);
    private static JsonParser jsonParser = new JsonParser();

    private static JsonObject crfConditionJson = null;
    public static Map<String,String> crfConditionMap = null;

    static {
        try {
            crfConditionMap = new HashMap<>();
            String data = FilesUtils.readFile("/crfCondition.json");
            JsonObject object = (JsonObject) jsonParser.parse(data);
            crfConditionJson = object.getAsJsonObject("conditions");
            for (Map.Entry<String, JsonElement> jsonElement : crfConditionJson.entrySet()) {
                String key = jsonElement.getKey();
                String value = crfConditionJson.get(key).getAsString();
                crfConditionMap.put(key, value);
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException();
        }
    }

}
