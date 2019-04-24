package com.gennlife.platform.processor;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liumingxin
 * @create 2019 11 13:52
 * @desc
 **/
public class MessageCenterProcessor {
    
    private static Logger logger = LoggerFactory.getLogger(MessageCenterProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();


    public String msgUpdate(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getMsgUpdate();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("标识消息为已读或删除此条消息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }

    public String msgUpdateAll(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getMsgUpdateAll();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("指定用户消息全部标记已读或删除 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String msgQuery(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getMsgQuery();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("查询指定条件的消息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String updateBatch(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getUpdateBatch();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("查询指定条件的消息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }
}
