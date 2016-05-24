package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.parse.CaseSearchParser;
import com.gennlife.platform.parse.CaseSuggestParser;
import com.gennlife.platform.parse.QueryServerParser;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by chen-song on 16/5/13.
 */
public class CaseProcessor {
    private Logger logger = LoggerFactory.getLogger(CaseProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();
    private static ExecutorService executorService = ArkService.getExecutorService();


    /**
     * 搜索结果列表展示的集合:done
     *
     * @param req
     * @param resp
     */
    public void searchItemSet(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        JsonObject paramObj = null;
        String searchKey = null;
        String keywords = null;
        String status = null;
        Set<String> set = new HashSet<String>();
        ResultBean resultBean = new ResultBean();
        try {
            param = ParamUtils.getParam(req);
            logger.info("SearchItemSet param=" + param);
            paramObj = (JsonObject) jsonParser.parse(param);
            searchKey = paramObj.get("searchKey").getAsString();//病历搜索的关键词
            keywords = paramObj.get("keywords").getAsString();//属性搜索的关键词
            status = paramObj.get("status").getAsString();
            if (!"0".equals(status) && !"1".equals(status) && !"2".equals(status)) {
                ParamUtils.errorParam("status参数出错", req, resp);
                return;
            }
            JsonArray arrange = paramObj.get("arrange").getAsJsonArray();
            for (JsonElement json : arrange) {
                set.add(json.getAsString());
            }

        } catch (Exception e) {
            ParamUtils.errorParam(req, resp);
            return;
        }
        if ("0".equals(status)) {//默认
            JsonObject result = ConfigurationService.getDefaultObj();
            resultBean.setCode(1);
            resultBean.setData(result);
        } else if ("1".equals(status)) {//可选
            JsonObject all = ConfigurationService.getAllObj();
            JsonObject allNew = new JsonObject();
            for (Map.Entry<String, JsonElement> obj : all.entrySet()) {
                String groupName = obj.getKey();
                JsonArray items = obj.getValue().getAsJsonArray();
                JsonArray newGroup = new JsonArray();
                for (JsonElement json : items) {
                    JsonObject item = json.getAsJsonObject();
                    String IndexFieldName = item.get("IndexFieldName").getAsString();
                    if (set.contains(IndexFieldName)) {
                        newGroup.add(item);
                    }
                }
                if (newGroup.size() > 0) {
                    allNew.add(groupName, newGroup);
                }
            }
            resultBean.setCode(1);
            resultBean.setData(allNew);
        } else if ("2".equals(status)) {//所有
            JsonObject all = ConfigurationService.getAllObj();
            JsonObject allNew = new JsonObject();
            for (Map.Entry<String, JsonElement> obj : all.entrySet()) {
                String groupName = obj.getKey();
                JsonArray items = obj.getValue().getAsJsonArray();
                JsonArray newGroup = new JsonArray();
                for (JsonElement json : items) {
                    JsonObject item = json.getAsJsonObject();
                    String UIFieldName = item.get("UIFieldName").getAsString();
                    if ("".equals(keywords) || UIFieldName.contains(keywords)) {
                        if (!"".equals(keywords)) {
                            UIFieldName = UIFieldName.replaceAll(keywords, "<span style='color:red'>" + keywords + "</span>");
                            item.addProperty("UIFieldName", UIFieldName);
                        }
                        newGroup.add(item);
                    }
                }
                if (newGroup.size() > 0) {
                    allNew.add(groupName, newGroup);
                }
            }
            resultBean.setCode(1);
            resultBean.setData(allNew);
        }

        viewer.viewString(gson.toJson(resultBean), resp, req);
        return;

    }

    /**
     * 搜索关键词提示(包括知识库,搜索):done
     *
     * @param req
     * @param resp
     */
    public void searchTermSuggest(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        String keywords = null;
        String indexName = null;
        String size = null;
        String dicName = null;
        String page = null;
        ResultBean resultBean = new ResultBean();
        try {
            param = ParamUtils.getParam(req);
            logger.info("SearchTermSuggest param=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            if (paramObj.get("indexName") != null) {
                indexName = paramObj.get("indexName").getAsString();
            }
            keywords = paramObj.get("keywords").getAsString();
            if (paramObj.get("size") != null) {
                size = paramObj.get("size").getAsString();
            }
            dicName = paramObj.get("dicName").getAsString();
            if (paramObj.get("page") != null) {
                page = paramObj.get("page").getAsString();
            }
        } catch (Exception e) {
            ParamUtils.errorParam(req, resp);
            return;
        }
        if (indexName == null) {
            indexName = "clinical_cases_dic";
        }
        if (size == null) {
            size = "5";
        }
        if (page == null) {
            page = "1";
        }
        CaseSuggestParser parserIndex = new CaseSuggestParser(indexName, dicName, keywords, size, page);
        Set<String> set = new HashSet<String>();
        int count = 0;
        try {
            String data = parserIndex.parser();
            JsonObject dataObj = (JsonObject) jsonParser.parse(data);
            count = dataObj.get("total").getAsInt();
            JsonArray dataArray = dataObj.getAsJsonArray("words");
            for (JsonElement json : dataArray) {
                String key = json.getAsString();
                set.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            ParamUtils.errorParam("请求出错", req, resp);
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("counter", count);
        resultBean.setCode(1);
        resultBean.setData(set);
        resultBean.setInfo(map);
        viewer.viewString(gson.toJson(resultBean), resp, req);
    }

    /**
     * 高级搜索关键词提示:done
     *
     * @param req
     * @param resp
     */
    public void advancedSearchTermSuggest(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        String keywords = null;
        ResultBean resultBean = new ResultBean();
        try {
            param = ParamUtils.getParam(req);
            logger.info("AdvancedSearchTermSuggest param=" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            keywords = paramObj.get("keywords").getAsString();
        } catch (Exception e) {
            ParamUtils.errorParam(req, resp);
            return;
        }
        List<JsonObject> list = new LinkedList<JsonObject>();

        if (null != keywords) {
            List<JsonObject> set = ConfigurationService.getAllList();
            for (JsonObject jsonObject : set) {
                String UIFieldName = jsonObject.get("UIFieldName").getAsString();
                if (UIFieldName.startsWith(keywords)) {
                    list.add(jsonObject);
                }
            }
        }
        resultBean.setCode(1);
        resultBean.setData(list);
        viewer.viewString(gson.toJson(resultBean), resp, req);

    }

    /**
     * 首页知识库搜索
     *
     * @param req
     * @param resp
     */
    public void searchKnowledgeFirst(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try {
            param = ParamUtils.getParam(req);
            logger.info("SearchKnowledge param=" + param);
        } catch (Exception e) {
            ParamUtils.errorParam(req, resp);
            return;
        }
    }

    /**
     * 搜索病历
     *
     * @param req
     * @param resp
     */
    public void searchCase(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        JsonObject paramObj = null;
        String newParam = null;
        try {
            param = ParamUtils.getParam(req);
            logger.info("SearchCase param=" + param);
            paramObj = (JsonObject) jsonParser.parse(param);
            String query = ParamUtils.buildQuery(paramObj);
            paramObj.addProperty("query", query);
            paramObj.addProperty("hospitalID", "public");
            paramObj.remove("from");
            paramObj.remove("to");
            paramObj.remove("filters");
            paramObj.remove("isAdv");
            newParam = gson.toJson(paramObj);
            logger.info("处理后请求参数=" + newParam);
        } catch (Exception e) {
            ParamUtils.errorParam(req, resp);
            return;
        }
        CaseSearchParser caseSearchParser = new CaseSearchParser(newParam);
        try {
            String searchResultStr = caseSearchParser.parser();
            JsonObject searchResult = (JsonObject) jsonParser.parse(searchResultStr);
            JsonObject result = new JsonObject();
            result.addProperty("code",1);
            result.add("data",searchResult);
            viewer.viewString(gson.toJson(result), resp, req);
        } catch (Exception e) {
            ParamUtils.errorParam("搜索失败", req, resp);
            return;
        }
    }

    /**
     * 返回该疾病相关基因
     * @param req
     * @param resp
     */
    public void diseaseSearchGenes(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        String paramStr = null;
        try {
            param = ParamUtils.getParam(req);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            //检查参数
            String searchKey = paramObj.get("searchKey").getAsString();
            JsonObject newParam = new JsonObject();
            newParam.addProperty("searchKey",searchKey);
            paramStr = gson.toJson(newParam);
        }catch (Exception e){
            ParamUtils.errorParam("请求参数出错", req, resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getKnowledgeDiseaseSearchGenesURL();
        String resultStr = HttpRequestUtils.httpPost(url,paramStr);
        viewer.viewString(resultStr,resp,req);
    }

    /**
     * 导出样本集到项目空间
     * @param req
     * @param resp
     */
    public void sampleImport(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        JsonObject paramObj = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("SearchCase param=" + param);
            paramObj = (JsonObject) jsonParser.parse(param);
            String query = ParamUtils.buildQuery(paramObj);
            paramObj.addProperty("query", query);
            paramObj.addProperty("hospitalID", "public");
            paramObj.remove("from");
            paramObj.remove("to");
            paramObj.remove("filters");
            paramObj.remove("isAdv");
        }catch (Exception e){
            ParamUtils.errorParam(req, resp);
            return;
        }

    }
}
