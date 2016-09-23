package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.parse.CaseSearchParser;
import com.gennlife.platform.parse.CaseSuggestParser;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by chen-song on 16/5/13.
 */
public class CaseProcessor {
    private Logger logger = LoggerFactory.getLogger(CaseProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();


    /**
     * 搜索结果列表展示的集合:done
     *
     * @param paramObj
     */
    public String searchItemSet(JsonObject paramObj) {
        String param = null;
        String searchKey = null;
        String keywords = null;
        String status = null;
        Set<String> set = new HashSet<String>();
        ResultBean resultBean = new ResultBean();
        try {
            //searchKey = paramObj.get("searchKey").getAsString();//病历搜索的关键词
            keywords = paramObj.get("keywords").getAsString();//属性搜索的关键词
            status = paramObj.get("status").getAsString();
            if (!"0".equals(status)
                    && !"1".equals(status)
                    && !"2".equals(status)
                    && !"3".equals(status)) {
                return ParamUtils.errorParam("status参数出错");
            }
            JsonArray arrange = paramObj.get("arrange").getAsJsonArray();
            for (JsonElement json : arrange) {
                set.add(json.getAsString());
            }
        } catch (Exception e) {
            logger.error("",e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if ("0".equals(status)) {//搜索结果,默认
            JsonObject result = ConfigurationService.getDefaultObj();
            resultBean.setCode(1);
            resultBean.setData(result);
        } else if ("1".equals(status)) {//属性可选
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
        } else if ("2".equals(status)) {//高级搜索,所有属性,带有搜索功能
            JsonObject all = ConfigurationService.getAdvancedSearch();
            JsonObject allNew = new JsonObject();
            for (Map.Entry<String, JsonElement> obj : all.entrySet()) {
                String groupName = obj.getKey();
                JsonArray items = obj.getValue().getAsJsonArray();
                JsonArray newGroup = new JsonArray();
                for (JsonElement json : items) {
                    JsonObject item = json.getAsJsonObject();
                    String UIFieldName = item.get("UIFieldName").getAsString();
                    if ("".equals(keywords) || UIFieldName.contains(keywords)) {
                        JsonObject itemNew = (JsonObject) jsonParser.parse(gson.toJson(item));
                        if (!"".equals(keywords)) {
                            UIFieldName = UIFieldName.replaceAll(keywords, "<span style='color:red'>" + keywords + "</span>");
                            itemNew.addProperty("UIFieldName", UIFieldName);
                        }
                        newGroup.add(itemNew);
                    }
                }
                if (newGroup.size() > 0) {
                    allNew.add(groupName, newGroup);
                }
            }
            resultBean.setCode(1);
            resultBean.setData(allNew);
        }else if("3".equals(status)){//更改属性,所有属性,带有搜索功能
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
                        JsonObject itemNew = (JsonObject) jsonParser.parse(gson.toJson(item));
                        if (!"".equals(keywords)) {
                            UIFieldName = UIFieldName.replaceAll(keywords, "<span style='color:red'>" + keywords + "</span>");
                            itemNew.addProperty("UIFieldName", UIFieldName);
                        }
                        newGroup.add(itemNew);
                    }
                }
                if (newGroup.size() > 0) {
                    allNew.add(groupName, newGroup);
                }
            }
            resultBean.setCode(1);
            resultBean.setData(allNew);
        }
        return  gson.toJson(resultBean);

    }

    /**
     * 搜索关键词提示(包括知识库,搜索):done
     *
     * @param paramObj
     */
    public String searchTermSuggest(JsonObject paramObj) {
        String keywords = null;
        String indexName = null;
        String size = null;
        String dicName = null;
        String page = null;
        ResultBean resultBean = new ResultBean();
        try {
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
            logger.error("",e);
            return ParamUtils.errorParam("请求参数出错");
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
            return ParamUtils.errorParam("请求出错");

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("counter", count);
        resultBean.setCode(1);
        resultBean.setData(set);
        resultBean.setInfo(map);
        return gson.toJson(resultBean);
    }

    /**
     * 高级搜索关键词提示:done
     *
     * @param paramObj
     */
    public String advancedSearchTermSuggest(JsonObject paramObj) {
        String param = null;
        String keywords = null;
        ResultBean resultBean = new ResultBean();
        try {
            keywords = paramObj.get("keywords").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数出错");
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
        return gson.toJson(resultBean);

    }

    /**
     * 首页知识库搜索
     *
     * @param paramObj
     */
    public String searchKnowledgeFirst(JsonObject paramObj) {
        return null;
    }

    /**
     * 搜索病历
     *
     * @param newParam
     */
    public String searchCase(String newParam) {
        CaseSearchParser caseSearchParser = new CaseSearchParser(newParam);
        try {
            String searchResultStr = caseSearchParser.parser();
            JsonObject searchResult = (JsonObject) jsonParser.parse(searchResultStr);
            JsonObject result = new JsonObject();
            result.addProperty("code",1);
            result.add("data",searchResult);
            return gson.toJson(result);
        } catch (Exception e) {
            return ParamUtils.errorParam("搜索失败");
        }
    }

    /**
     * 返回该疾病相关基因
     * @param paramObj
     */
    public String diseaseSearchGenes(JsonObject paramObj) {
        String paramStr = null;
        try {
            //检查参数
            String searchKey = paramObj.get("searchKey").getAsString();
            JsonObject newParam = new JsonObject();
            newParam.addProperty("searchKey",searchKey);
            paramStr = gson.toJson(newParam);
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("请求参数出错");
        }
        String url = ConfigurationService.getUrlBean().getKnowledgeDiseaseSearchGenesURL();
        return HttpRequestUtils.httpPost(url,paramStr);
    }

    /**
     * 导出样本集到项目空间
     * @param paramObj
     */
    public String sampleImport(JsonObject paramObj) {
        return null;
    }

    /**
     * 通路查询时,校验基因数组正确性
     * @param paramObj
     * @return
     */
    public String geneVerify(JsonObject paramObj) {
        try{
            String param = gson.toJson(paramObj);
            String url = ConfigurationService.getUrlBean().getCaseGeneErrorURL();
            logger.info("GeneVerify url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("GeneVerify result="+url);
            JsonObject resultObj = (JsonObject) jsonParser.parse(result);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(resultObj);
            return gson.toJson(resultBean);
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }
}
