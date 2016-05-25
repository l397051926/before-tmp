package com.gennlife.platform.parse;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by chen-song on 16/5/16.
 */
public class QueryServerParser implements Callable<String> {
    private static Logger logger = LoggerFactory.getLogger(QueryServerParser.class);
    private String url ;
    private String key ;
    private static JsonParser jsonParser = new JsonParser();
    public QueryServerParser(String key){
        logger.info("url="+ConfigurationService.getUrlBean().getSynQueryURL()+key);
        url = String.format(
                ConfigurationService.getUrlBean().getSynQueryURL(),
                ParamUtils.encodeURI(key)
        );
        this.key = key;
    }

    public String call() throws Exception {
        return null;
    }

    public Set<String> parser() throws Exception {
        String re = HttpRequestUtils.httpGet(url);
        Set<String> set = new HashSet<String>();
        try{
            JsonObject jsonObject = (JsonObject) jsonParser.parse(re);
            JsonArray syn = jsonObject.getAsJsonArray("syn");
            for(JsonElement jsonElement:syn){
                String k = jsonElement.getAsString();
                set.add(k);
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return set;
    }
}
