package com.gennlife.platform.parse;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.JsonElement;
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
    private boolean isOk = false;
    JsonObject queryjson;

    public CaseSearchParser(String queryStr) {
        queryjson = JsonUtils.getJsonObject(queryStr);
    }

    public CaseSearchParser(String queryStr, String addquery) {
        queryjson = JsonUtils.getJsonObject(queryStr);
        addQuery(addquery);
    }

    /**
     * 临时处理郑州中心 放开所有权限
     * @param addquery
     */
    public void addQuery(String addquery) {
        if (StringUtils.isEmpty(addquery)) return;
        if (queryjson.has("query")) {
            String query = queryjson.get("query").getAsString();
            if (StringUtils.isEmpty(query)) queryjson.addProperty("query", "[患者基本信息.患者编号] EXIST TRUE");
//            else {
//                queryjson.addProperty("query", "( " + query + " ) and " + addquery);
//            }
        } else queryjson.addProperty("query", "[患者基本信息.患者编号] EXIST TRUE");
        logger.info("query " + queryjson.get("query"));
    }

    public String call() throws Exception {
        return null;
    }

    /**
     * emr搜索！
     * @return
     * @throws Exception
     */
    public String parser() throws Exception {
        isOk = false;
        String url = ConfigurationService.getUrlBean().getCaseSearchURL();

        if (queryjson == null) return ParamUtils.errorParam("非法json");
        JsonElement item = null;
        if (queryjson.has("query")) {
            item = queryjson.get("query");

        } else if (queryjson.has("keywords")) {
            item = queryjson.get("keywords");

        }
        if (item == null) return ParamUtils.errorParam("查询条件为空");
        else {
            if (item.isJsonPrimitive()) {
                if (StringUtils.isEmpty(item.getAsString())) {
                    return ParamUtils.errorParam("查询条件为空");
                }
            } else if (item.isJsonArray()) {
                if (item.getAsJsonArray().size() == 0) {
                    return ParamUtils.errorParam("查询条件为空");
                }
            } else return ParamUtils.errorParam("错误的查询条件");
        }
        isOk = true;
        queryjson.addProperty("indexName", ConfigUtils.getSearchIndexName());
        logger.info("搜索请求参数=" + queryjson);
        return HttpRequestUtils.httpPost(url, GsonUtil.getGson().toJson(queryjson));
    }

    public boolean isOk() {
        return isOk;
    }

    public String getQuery() {
        return GsonUtil.getGson().toJson(queryjson);
    }
}
