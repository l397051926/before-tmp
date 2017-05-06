package com.gennlife.platform.processor;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by chen-song on 16/8/16.
 */
public class ComputeProcessor {
    private Logger logger = LoggerFactory.getLogger(ComputeProcessor.class);
    private static Gson gson = GsonUtil.getGson();
    /**
     * 计算服务因子图
     * @param paramObj
     * @return
     */

    public String smg(JsonObject paramObj) {
        String param = gson.toJson(paramObj);
        String url = ConfigurationService.getUrlBean().getCSSmg()+"?param="+ParamUtils.encodeURI(param);
        logger.info("smg url="+url);
        try{
            String result = HttpRequestUtils.httpGet(url,600000);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("超时");
        }
    }

    /**
     * 基线统计
     * @param paramObj
     * @return
     */
    public String baseline(JsonObject paramObj) {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, JsonElement> item:paramObj.entrySet()){
            String key = item.getKey();
            JsonElement jsonElement = item.getValue();
            if(jsonElement.isJsonPrimitive()){
                String value = jsonElement.getAsString();
                sb.append(key).append("=").append(ParamUtils.encodeURI(value)).append("&");
            }else{
                sb.append(key).append("=").append(ParamUtils.encodeURI(gson.toJson(jsonElement))).append("&");
            }

        }
        String param = sb.toString().substring(0,sb.toString().length()-1);
        String url = ConfigurationService.getUrlBean().getCSBaseline()+"?"+param;
        logger.info("baseline url="+url);
        String reStr = HttpRequestUtils.httpGet(url,600000);
        logger.info("baseline result="+reStr);
        if(reStr == null || "".equals(reStr)){
            return ParamUtils.errorParam("计算服务返回空");
        }else{
            return reStr;
        }
    }
}
