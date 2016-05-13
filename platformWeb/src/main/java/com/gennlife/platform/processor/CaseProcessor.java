package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.parse.CaseSuggestParser;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by chen-song on 16/5/13.
 */
public class CaseProcessor {
    private Logger logger = LoggerFactory.getLogger(CaseProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer= new View();
    private static  ExecutorService executorService = ArkService.getExecutorService();
    /**
     * 知识库搜索关键词提示:
     * @param req
     * @param resp
     */
    public void knowledgeTermSuggestion(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("KnowledgeTermSuggestion param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
    }

    /**
     * 搜索结果列表展示的集合
     * @param req
     * @param resp
     */
    public void searchItemSet(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        JsonObject paramObj = null;
        String disease = null;
        String key = null;
        String status = null;
        Set<String> set = new HashSet<String>();
        try{
            param = ParamUtils.getParam(req);
            logger.info("SearchItemSet param="+param);
            paramObj = (JsonObject) jsonParser.parse(param);
            //disease = paramObj.get("disease").getAsString();
            key = paramObj.get("key").getAsString();
            status = paramObj.get("status").getAsString();
            if(!"0".equals(status) && !"1".equals(status) && !"2".equals(status)){
                ParamUtils.errorParam("status参数出错",req,resp);
                return;
            }
            JsonArray arrange =  paramObj.get("arrange").getAsJsonArray();
            for(JsonElement json:arrange){
                set.add(json.getAsString());
            }

        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        if("0".equals(status)){//默认

        }else if("1".equals(status)){//可选

        }else if ("2".equals(status)){//所有

        }

    }

    /**
     * 搜索关键词提示:done
     * @param req
     * @param resp
     */
    public void searchTermSuggest(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        String keywords = null;
        String indexName = null;
        String size = null;
        ResultBean resultBean = new ResultBean();
        try{
            param = ParamUtils.getParam(req);
            logger.info("SearchTermSuggest param="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            if(paramObj.get("indexName") != null){
                indexName = paramObj.get("indexName").getAsString();
            }
            keywords = paramObj.get("keywords").getAsString();
            if(paramObj.get("size") != null){
                size = paramObj.get("size").getAsString();
            }
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        if(indexName == null){
            indexName = "clinical_cases_dic";
        }
        if(size == null){
            size = "5";
        }
        CaseSuggestParser parserIndex = new CaseSuggestParser(indexName,"icd",keywords,size);
        Set<String> set = new HashSet<String>();
        try {
            String data = parserIndex.parser();
            JsonArray dataArray = (JsonArray) jsonParser.parse(data);
            for(JsonElement json:dataArray){
                String key = json.getAsString();
                set.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("",e);
            ParamUtils.errorParam("请求出错",req,resp);
            return;
        }
        resultBean.setCode(1);
        resultBean.setData(set);
        viewer.viewString(gson.toJson(resultBean),resp,req);
    }

    /**
     *高级搜索关键词提示:done
     * @param req
     * @param resp
     */
    public void advancedSearchTermSuggest(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        String keywords = null;
        ResultBean resultBean = new ResultBean();
        try{
            param = ParamUtils.getParam(req);
            logger.info("AdvancedSearchTermSuggest param="+param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            keywords = paramObj.get("keywords").getAsString();
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        List<JsonObject> list = new LinkedList<JsonObject>();
        if(null != keywords){
            Set<JsonObject> set = ConfigurationService.getUINameSet();
            for(JsonObject jsonObject:set){
                String ui_field_name = jsonObject.get("").getAsString();
                if(ui_field_name.startsWith(keywords)){
                    list.add(jsonObject);
                }
            }
        }
        resultBean.setCode(1);
        resultBean.setData(list);
        viewer.viewString(gson.toJson(resultBean),resp,req);

    }

    /**
     * 首页知识库搜索
     * @param req
     * @param resp
     */
    public void searchKnowledge(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("SearchKnowledge param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
    }

    /**
     * 搜索病历
     * @param req
     * @param resp
     */
    public void searchCase(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        JsonObject paramObj = null;

        try{
            param = ParamUtils.getParam(req);
            //logger.info("SearchCase param="+param);
            paramObj = (JsonObject) jsonParser.parse(param);
            logger.info(gson.toJson(paramObj));
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }

    }
}
