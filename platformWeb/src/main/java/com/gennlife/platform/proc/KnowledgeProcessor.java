package com.gennlife.platform.proc;

import com.gennlife.platform.build.KnowledgeBuilder;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chen-song on 16/5/6.
 */
public class KnowledgeProcessor {
    private Logger logger = LoggerFactory.getLogger(KnowledgeProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static KnowledgeBuilder builder = new KnowledgeBuilder();
    private static View viewer= new View();
    private static Set<String> set = new HashSet<String>();
    static{
        set.add("phenotype");
        set.add("gene");
        set.add("disease");
        set.add("drug");
        set.add("variation");
        set.add("protein");
    }
    public void search(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("search =" + param);
        String from = null;
        String to = null;
        String limit = null;
        String query = null;
        int currentPage = 1;
        int pageSize = 12;
        String tableName = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            from = paramObj.get("from").getAsString().toLowerCase();
            to = paramObj.get("to").getAsString().toLowerCase();
            limit = paramObj.get("limit").getAsString();
            query = paramObj.get("query").getAsString();
            int[] li = ParamUtils.parseLimit(limit);
            currentPage = li[0];
            pageSize = li[1];
            if("drug".equals(to)){
                tableName = paramObj.get("tableName").getAsString();
            }
        }catch (Exception e){
            logger.error("请求参数出错", e);
            ParamUtils.errorParam("请求参数出错", req, resp);
            return;
        }
        if(from.equals("case") || to.equals("case")){
            //涉及到病历搜索
            return;
        }
        if(!isCheckParam(from)){
            ParamUtils.errorParam("请求参数from非法", req, resp);
            return;
        }
        if(!isCheckParam(to)){
            ParamUtils.errorParam("请求参数to非法", req, resp);
            return;
        }
        JsonObject newJson = buildQueryJson(from,to,query,currentPage,pageSize,tableName);
        String newParam = gson.toJson(newJson);
        logger.info("knowledge req=" + newParam);
        String url = ConfigurationService.getUrlBean().getKnowledgeURL();
        logger.info("knowledge url=" + url);
        String resultStr = HttpRequestUtils.httpPost(url,newParam);
        logger.info("knowledge result=" + resultStr);
        JsonObject tmpResult = (JsonObject) jsonParser.parse(resultStr);
        JsonArray resultArray = builder.build(newJson,tmpResult);
        viewer.viewString(gson.toJson(resultArray),resp,req);
    }

    private JsonObject buildQueryJson(String from, String to, String query, int currentPage, int pageSize, String tableName) {
        JsonObject queryObj = new JsonObject();
        queryObj.addProperty("from",from);
        queryObj.addProperty("to",to);
        queryObj.addProperty("query",query);
        queryObj.addProperty("currentPage",currentPage);
        queryObj.addProperty("pageSize",pageSize);
        queryObj.addProperty("tableName",tableName);
        queryObj.addProperty("DEBUG",false);
        return queryObj;
    }

    private boolean isCheckParam(String from){
        return set.contains(from);
    }

}
