package com.gennlife.platform.processor;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.conf.SystemDefault;
import com.gennlife.platform.model.Group;
import com.gennlife.platform.model.User;
import com.gennlife.platform.parse.CaseSearchParser;
import com.gennlife.platform.parse.CaseSuggestParser;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by chen-song on 16/5/13.
 */
public class CaseProcessor {
    private static Logger logger = LoggerFactory.getLogger(CaseProcessor.class);
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
        String crf_id = ((SystemDefault) SpringContextUtil.getBean("systemDefault")).getSearchItemSetDefault();
        Set<String> set = new HashSet<String>();
        ResultBean resultBean = new ResultBean();
        try {
            //searchKey = paramObj.get("searchKey").getAsString();//病历搜索的关键词
            keywords = paramObj.get("keywords").getAsString();//属性搜索的关键词
            status = paramObj.get("status").getAsString();
            if (!"0".equals(status)
                    && !"1".equals(status)
                    && !"2".equals(status)
                    && !"3".equals(status)
                    && !"4".equals(status)) {
                return ParamUtils.errorParam("status参数出错");
            }
            JsonArray arrange = paramObj.get("arrange").getAsJsonArray();
            for (JsonElement json : arrange) {
                set.add(json.getAsString());
            }
            if (paramObj.has("crf_id")) {
                crf_id = paramObj.get("crf_id").getAsString();
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if ("0".equals(status)) { // 搜索结果,默认
            JsonObject result = ConfigurationService.getDefaultObj(crf_id);
            resultBean.setCode(1);
            resultBean.setData(result);
        } else if ("1".equals(status)) {//属性可选
            JsonObject all = ConfigurationService.getAllObj(crf_id);
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
            JsonObject all = ConfigurationService.getAdvancedSearch(crf_id);
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
                    if (paramObj.has("filterPath")) {
                        String filterPath = paramObj.get("filterPath").getAsString();
                        if (!StringUtils.isEmpty(filterPath)) {
                            if (groupName.equals(filterPath)) {
                                allNew.add(groupName, newGroup);
                            }
                        } else {
                            allNew.add(groupName, newGroup);
                        }
                    } else {
                        allNew.add(groupName, newGroup);
                    }
                }
            }
            resultBean.setCode(1);
            resultBean.setData(allNew);
        } else if ("3".equals(status)) {//更改属性,所有属性,带有搜索功能
            JsonObject all = ConfigurationService.getImportTree(crf_id);
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
        } else if ("4".equals(status)) {//比较因子属性,所有属性,带有搜索功能
            JsonObject all = ConfigurationService.getCompareObj(crf_id);
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
        return gson.toJson(resultBean);
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
            if (StringUtils.isEmpty(keywords)) return ParamUtils.errorParam("查询条件为空");
            if (paramObj.get("size") != null) {
                size = paramObj.get("size").getAsString();
            }
            dicName = paramObj.get("dicName").getAsString();
            if (paramObj.get("page") != null) {
                page = paramObj.get("page").getAsString();
            }
        } catch (Exception e) {
            logger.error("", e);
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
        // String crf_id = "kidney_cancer";
        String crf_id = ((SystemDefault) SpringContextUtil.getBean("systemDefault")).getSearchItemSetDefault();
        try {
            keywords = paramObj.get("keywords").getAsString();
            if (paramObj.has("crf_id")) {
                crf_id = paramObj.get("crf_id").getAsString();
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数出错");
        }
        List<JsonObject> list = new LinkedList<JsonObject>();

        if (null != keywords) {
            List<JsonObject> set = ConfigurationService.getAllList(crf_id);
            if (set != null) {
                for (JsonObject jsonObject : set) {
                    String UIFieldName = jsonObject.get("UIFieldName").getAsString();
                    if (UIFieldName.startsWith(keywords)) {
                        list.add(jsonObject);
                    }
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
     * 搜索接口，sid 转化
     *
     * @param param
     * @return
     */
    public static String transformSid(String param, User user) {
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        return transformSid(paramObj, user);

    }

    public static String transformSid(JsonObject paramObj, User user) {
        if (paramObj.has("sid") && paramObj.has("power")) {
            String sid = paramObj.get("sid").getAsString();
            paramObj.remove("groups"); // 选择科室后，工号权限小时
            paramObj.remove("sid");
            JsonObject power = paramObj.getAsJsonObject("power");
            JsonArray has_searchArray = power.getAsJsonArray("has_search");
            JsonArray newHas_searchArray = new JsonArray();
            for (JsonElement item : has_searchArray) {
                JsonObject has_searchObj = item.getAsJsonObject();
                String tmpSid = has_searchObj.get("sid").getAsString();
                if (tmpSid.equals(sid)) {
                    newHas_searchArray.add(has_searchObj);
                }
            }
            if (newHas_searchArray.size() == 0) {
                return ParamUtils.errorParam("无搜索权限");
            } else {
                power.add("has_search", newHas_searchArray);
            }
            paramObj.add("power", power);
            //logger.info("通过sid转化后，搜索请求参数 = " + gson.toJson(paramObj));
            return gson.toJson(paramObj);
        } else if (paramObj.has("power")) { // 角色,完成小组扩展
            ///buildGroup(paramObj, user);
            return gson.toJson(paramObj);
        } else {
            return gson.toJson(paramObj);
        }

    }

    @Deprecated
    private static void buildGroup(JsonObject paramObj, User user) {
        JsonArray groups = paramObj.getAsJsonArray("groups");
        if (groups == null) {
            groups = new JsonArray();
        } else if (groups.size() > 0) {
            return;
        }
        //构建虚拟小组，确保工号权限生效
        Group group = new Group();
        group.setGroupDesc("无小组信息时，补充个人工号");
        group.setHas_search("有");
        group.setHas_searchExport("有");
        List<User> userList = new LinkedList<>();
        User newUser = new User();
        newUser.setUnumber(user.getUnumber());
        newUser.setFrontEndPower(null);
        userList.add(newUser);
        group.setMembers(userList);
        groups.add(gson.toJsonTree(group));
        paramObj.add("groups", groups);
    }

    /**
     * 导出接口，sid 转化
     *
     * @param param
     * @return
     */
    public static String transformSidForImport(String param, User user) {
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        if (paramObj.has("sid") && paramObj.has("power")) {
            String sid = paramObj.get("sid").getAsString();
            paramObj.remove("groups");//选择科室后，工号权限小时
            paramObj.remove("sid");
            JsonObject power = paramObj.getAsJsonObject("power");
            JsonArray has_searchExportArray = power.getAsJsonArray("has_searchExport");
            JsonArray newhas_searchExportArray = new JsonArray();
            JsonArray has_searchArray = power.getAsJsonArray("has_search");
            JsonArray newHas_searchArray = new JsonArray();
            for (JsonElement item : has_searchExportArray) {
                JsonObject has_searchExportObj = item.getAsJsonObject();
                String tmpSid = has_searchExportObj.get("sid").getAsString();
                if (tmpSid.equals(sid)) {
                    newhas_searchExportArray.add(has_searchExportObj);
                }
            }
            for (JsonElement item : has_searchArray) {
                JsonObject has_searchObj = item.getAsJsonObject();
                String tmpSid = has_searchObj.get("sid").getAsString();
                if (tmpSid.equals(sid)) {
                    newHas_searchArray.add(has_searchObj);
                }
            }
            power.add("has_search", newHas_searchArray);
            power.add("has_searchExport", newhas_searchExportArray);
            paramObj.add("power", power);
            logger.info("通过sid转化后，搜索请求参数 = " + gson.toJson(paramObj));
            return gson.toJson(paramObj);
        } else if (paramObj.has("power")) { // 角色,完成小组扩展
            //buildGroup(paramObj, user);
            return gson.toJson(paramObj);
        } else {
            return param;
        }
    }

    /**
     * 搜索病历
     *
     * @param newParam
     */
    public String searchCase(String newParam, User user) {
        return searchCaseWithAddQuery(newParam, user, null);
    }

    public String SearchCaseRole(String newParam, User user) {
        return transformSid(newParam, user);
    }
/*
{"uid":"13157887-ff05-47a2-b34a-bcf09312be8f",
"indexName":"yantai_hospital_clinical_patients",
"query":"诊断",
"from":"cases",
"to":"cases",
"isAdv":false,
"hospitalID":"public",
"size":10,"page":1,
"topGeneSymbol":[],
"source":["patient_info.PATIENT_SN"编号,"patient_info.GENDER"性别,"patient_info.BLOOD_ABO ABO血型","patient_info.BLOOD_RH","patient_info.BIRTH_PLACE","patient_info.BIRTH_DATE","patient_info.NATIONALITY","patient_info.MARITAL_STATUS","patient_info.NATIVE_PLACE","patient_info.ETHNIC","patient_info.EDUCATION_DEGREE","patient_info.OCCUPATION"],
"aggs":{"terms_aggs":[{"field":"patient_info.GENDER","topN":0},
{"field":"patient_info.ETHNIC","topN":0},{"field":"patient_info.MARITAL_STATUS","topN":0}],
"range_aggs":[]},
"sort":[],"isHighlightAllfields":true}:
 */
    public String searchCaseWithAddQuery(String newParam, User user, String addQuery) {
        if (newParam == null) {
            logger.error("searchCase缺失参数");
            return ParamUtils.errorSessionLosParam();
        }
        String param = transformSid(newParam, user);
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        if (paramObj.has("code") && paramObj.get("code").getAsInt() == 0) {
            return param;
        }
        CaseSearchParser caseSearchParser = new CaseSearchParser(param);
        if (!StringUtils.isEmpty(addQuery)) {
            caseSearchParser.addQuery(addQuery);
        }
        try {
            String searchResultStr = caseSearchParser.parser();
            if (StringUtils.isEmpty(searchResultStr)) {
                logger.error("search empty " + caseSearchParser.getQuery());
                return ParamUtils.errorParam("搜索无结果");
            }
            JsonObject searchResult = (JsonObject) jsonParser.parse(searchResultStr);
            JsonObject result = new JsonObject();
            result.addProperty("code", 1);
            result.add("data", searchResult);
            return gson.toJson(result);
        } catch (Exception e) {
            logger.error("error", e);
            return ParamUtils.errorParam("搜索失败");
        }
    }

    public String searchCaseByCurrentDept(String newParam, User user) {
        return searchCaseWithAddQuery(newParam, user, AuthorityUtil.getCurrentDeptQuery(user));
    }

    /**
     * 返回该疾病相关基因
     *
     * @param paramObj
     */
    public String diseaseSearchGenes(JsonObject paramObj) {
        String paramStr = null;
        try {
            //检查参数
            String searchKey = paramObj.get("searchKey").getAsString();
            JsonObject newParam = new JsonObject();
            newParam.addProperty("searchKey", searchKey);
            paramStr = gson.toJson(newParam);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        String url = ConfigurationService.getUrlBean().getKnowledgeDiseaseSearchGenesURL();
        return HttpRequestUtils.httpPost(url, paramStr);
    }

    /**
     * 导出样本集到项目空间
     *
     * @param paramObj
     */
    public String sampleImport(JsonObject paramObj) {
        return null;
    }

    /**
     * 通路查询时,校验基因数组正确性
     *
     * @param paramObj
     * @return
     */
    public String geneVerify(JsonObject paramObj) {
        try {
            String param = gson.toJson(paramObj);
            String url = ConfigurationService.getUrlBean().getCaseGeneErrorURL();
            logger.info("GeneVerify url = " + url);
            String result = HttpRequestUtils.httpPost(url, param);
            logger.info("GeneVerify result = " + url);
            JsonObject resultObj = (JsonObject) jsonParser.parse(result);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(resultObj);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String searchHighlight(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getHighlight();
            logger.info("搜索详情高亮 url=" + url);
            paramObj.addProperty("indexName", ConfigUtils.getSearchIndexName());
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            logger.info("搜索详情高亮 SS返回结果： " + result);
            return result;
        } catch (Exception e) {
            return ParamUtils.errorParam("请求出错");
        }
    }
}
