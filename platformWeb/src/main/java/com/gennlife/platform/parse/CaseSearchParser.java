package com.gennlife.platform.parse;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.JsonUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.Callable;

/**
 * Created by chen-song on 16/5/16.
 */
public class CaseSearchParser implements Callable<String> {
    private static Logger logger = LoggerFactory.getLogger(CaseSearchParser.class);
    private String queryStr;
    private boolean isOk=false;
    public CaseSearchParser(String queryStr){
        logger.info("搜索请求参数="+queryStr);
        this.queryStr = queryStr;

    }
    public String call() throws Exception {
        return null;
    }

    public String parser() throws Exception {
        isOk=false;
        String url = ConfigurationService.getUrlBean().getCaseSearchURL();
        JsonObject queryjson=JsonUtils.getJsonObject(queryStr);
        if(queryjson==null) return ParamUtils.errorParam("非法json");
        if(queryjson.has("query"))
        {
            if(StringUtils.isEmpty(queryjson.get("query").toString().trim()))
                return ParamUtils.errorParam("查询条件为空");
        }
        else if(queryjson.has("keywords"))
        {
            if(StringUtils.isEmpty(queryjson.get("keywords").toString().trim()))
                return ParamUtils.errorParam("查询条件为空");
        }
        isOk=true;
        return HttpRequestUtils.httpPost(url,queryStr);
    }
    public boolean isOk()
    {
        return isOk;
    }
}
