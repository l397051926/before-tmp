package com.gennlife.platform.processor;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luoxupan on 25/10/2017.
 */
public class RwsProcessor {
    private static Logger logger = LoggerFactory.getLogger(RwsProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    public String PreLiminary(String reqStr){
        try {
            String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
            url += "?" + reqStr;
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String PreAggregation(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPreAggregationUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("RWS请求图形接口发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String PreFindForProjectData(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPreFindForProjectData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("RWS图形下面列表接口", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String FindByProjectId(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getFindByProjectId();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目下所有已定义的事件/指标列表", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }
}
