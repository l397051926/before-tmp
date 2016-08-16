package com.gennlife.platform.processor;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tools.ant.util.ConcatResourceInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info("url="+url);
        String result = HttpRequestUtils.httpGet(url);
        return result;
    }
}
