package com.gennlife.platform.parse;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by chen-song on 16/5/16.
 */
public class CaseSearchParser implements Callable<String> {
    private static Logger logger = LoggerFactory.getLogger(CaseSearchParser.class);
    private String queryStr;
    public CaseSearchParser(String queryStr){
        logger.info("搜索请求参数="+queryStr);
        this.queryStr = queryStr;

    }
    public String call() throws Exception {
        return null;
    }

    public String parser() throws Exception {
        String url = ConfigurationService.getUrlBean().getCaseSearchURL();
        return HttpRequestUtils.httpPost(url,queryStr);
    }
}
