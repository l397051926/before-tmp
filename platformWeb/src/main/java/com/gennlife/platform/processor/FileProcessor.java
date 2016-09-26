package com.gennlife.platform.processor;

import com.gennlife.platform.util.DataFormatConversion;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by chensong on 2015/12/9.
 */
public class FileProcessor {
    private static Logger logger = LoggerFactory.getLogger(FileProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer= new View();
    /**
     * 图片上传的
     * @param request
     * @param resps
     */
    public void pictureUp(HttpServletRequest request, HttpServletResponse resps){

    }

    /**
     *
     * @param request
     * @param resps
     */
    public void projectInfo(HttpServletRequest request, HttpServletResponse resps){

    }

    public String fsRec(JsonObject jsonObject){
        String host = "";
        StringBuffer newParam = new StringBuffer();
        for(Map.Entry<String, JsonElement> entry:jsonObject.entrySet()){
            String key = entry.getKey();
            String value = null;
            if(entry.getValue().isJsonObject()){
                value = entry.getValue().getAsJsonObject().toString();
            }else if(entry.getValue().isJsonArray()){
                value = entry.getValue().getAsJsonArray().toString();
            }else if(entry.getValue().getAsBoolean()){
                value = entry.getValue().getAsBoolean()+"";
            }else {
                value = entry.getValue().getAsString();
            }
            if("url".equals(key)){
                host = value;
            }else {
                newParam.append("&").append(key).append("=").append(ParamUtils.encodeURI(value));
            }
        }
        return HttpRequestUtils.httpGet(host + "?" + newParam.toString());
    }

    public String systemInfo(){
        JsonObject jsonObject = new JsonObject();
        double msize = 1024.0 * 1024;
        double total = (Runtime.getRuntime().totalMemory()) / msize;
        double max = (Runtime.getRuntime().maxMemory()) / msize;
        double free = (Runtime.getRuntime().freeMemory()) / msize;
        int AvailableProcessors = Runtime.getRuntime().availableProcessors();
        jsonObject.addProperty("total",total + "MB");
        jsonObject.addProperty("max",max + "MB");
        jsonObject.addProperty("free", free + "MB");
        jsonObject.addProperty("accessible", (max - total + free) + "MB");
        jsonObject.addProperty("availableProcessors", AvailableProcessors + "个");
        return gson.toJson(jsonObject);
    }
    
    public String reloadConfig() {
        try{
        	DataFormatConversion.reload();
            return "{\"code\":\"1\"}";
        }catch (Exception e){
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }

    }




}
