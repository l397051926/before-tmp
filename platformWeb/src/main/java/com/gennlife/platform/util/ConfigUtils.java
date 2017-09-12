package com.gennlife.platform.util;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Chenjinfeng on 2017/9/9.
 */
@Component
@Scope("singleton")
public class ConfigUtils {
    private static String config_uri;
    private static String appName;
    private static final Logger logger= LoggerFactory.getLogger(ConfigUtils.class);
    public static String  getConfig_uri() {
        return config_uri;
    }
    @Value("${spring.cloud.config.uri}")
    public void setConfig_uri(String config_uri) {
        this.config_uri = config_uri;
    }

    public String getAppName() {
        return appName;
    }

    @Value("${spring.application.name}")
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public static String getRemoteUtfFile(String file)
    {
        String url="";
        if(config_uri.endsWith("/")) url=config_uri;
        else url=config_uri+"/";
        url=url+"files/string/"+appName;
        JsonObject json=new JsonObject();
        json.addProperty("filename",file);
        try {
            String result = HttpRequestUtils.httpPost(url, GsonUtil.getGson().toJson(json));
            return result;
        }
        catch (Exception e)
        {
            logger.error("config util error ",e);
        }
        return null;
    }
}
