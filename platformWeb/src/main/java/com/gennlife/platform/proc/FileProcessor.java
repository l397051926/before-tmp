package com.gennlife.platform.proc;

import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
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

    public void fsRec(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        logger.info("fsRec =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
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
        String data =  HttpRequestUtils.httpGet(host + "?" + newParam.toString());
        viewer.viewString(data,resps,request);
    }

    public void systemInfo(HttpServletRequest request, HttpServletResponse resps){
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
        String data = gson.toJson(jsonObject);
        viewer.viewString(data,resps,request);
    }

}
