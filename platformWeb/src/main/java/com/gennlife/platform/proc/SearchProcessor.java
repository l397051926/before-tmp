package com.gennlife.platform.proc;

import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.conf.ConfGroupInfo;
import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.bean.list.ColumnBean;
import com.gennlife.platform.bean.list.ColumnPropetity;
import com.gennlife.platform.bean.list.ColumnValue;
import com.gennlife.platform.bean.projectBean.HistoryWords;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.parse.*;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.DataService;
import com.gennlife.platform.util.Conf;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.SpringContextUtil;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by chensong on 2015/12/9.
 */
public class SearchProcessor {
    private static Logger logger = LoggerFactory.getLogger(SearchProcessor.class);
    private static String qRecommendURL = null;
    private static String searchURL = null;
    private static String fileURL = null;
    private static DetailParser detailParser = new DetailParser();
    private static  ExecutorService executorService = ArkService.getExecutorService();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer = new View();
    static{
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        Conf conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        qRecommendURL = conf.getqRecommendURL();
        searchURL = conf.getSearchURL();
        fileURL = conf.getFileURL();
    }
    /**
     * 提示词 + 历史搜索词
     * @param req
     * @param resp
     */
    public void qRecommend(HttpServletRequest req, HttpServletResponse resp) {
        try{
            String param = ParamUtils.getParam(req);
            logger.info("qRecommend param=" + param);
            JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
            String keywords = jsonObject.get("keywords").getAsString();
            String uid = jsonObject.get("loginname").getAsString();
            String indexName = jsonObject.get("indexName").getAsString();
            List<Future<String>> result = new ArrayList<Future<String>>();
            QRecommendParser parserIndex2 = new QRecommendParser(indexName,keywords);
            result.add(executorService.submit(parserIndex2));
            Map<String,Object> conf = new HashMap<String, Object>();
            conf.put("uid", uid);
            conf.put("keywords", keywords);
            conf.put("indexName", indexName);
            List<String> list = AllDao.getInstance().getSyUserDao().getHistoryWords(conf);
            List<String> resultList = new ArrayList<String>();
            for(Future<String> future:result){
                String data = future.get();
                JsonArray array = jsonParser.parse(data).getAsJsonArray();
                for(JsonElement entry:array){
                    String key = entry.getAsString();
                    if(!resultList.contains(key)){
                        resultList.add(key);
                    }
                }
            }
            list.addAll(resultList);
            viewer.viewList(list, null, true, resp, req);
        }catch(Exception e){
            logger.error("",e);
        }
    }

    /**
     *
     * @param req
     * @param resp
     */
    public void search(HttpServletRequest req, HttpServletResponse resp) {
        String param = ParamUtils.getParam(req);
        logger.info("search  param=" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        logger.info("query=" + jsonObject.get("query").getAsString());
        String keywords = null;
        keywords = jsonObject.get("query").getAsString();
        String uid = jsonObject.get("uid").getAsString();
        Map<String,Future<String>> result= new HashMap<String, Future<String>>();
        String index = jsonObject.get("indexName").getAsString();
        logger.info("indexName="+index);
        SearchKeyParser searchKeyParser = new SearchKeyParser(index,param);
        result.put(index, executorService.submit(searchKeyParser));
        for(String indexName:result.keySet()){
            try {
                String data = result.get(indexName).get();
                if( data != null && jsonParser.parse(data).isJsonObject()){
                    Map<String,Map<String,String>> map = ArkService.getMapConf();
                    JsonObject searchResultObj = jsonParser.parse(data).getAsJsonObject();
                    JsonObject hitsObj = searchResultObj.getAsJsonObject("hits");
                    if(hitsObj == null){
                        logger.error("search 出错:"+data);
                        viewer.viewString(err0Item, resp, req);
                    }else {
                        JsonArray hitsArray = hitsObj.getAsJsonArray("hits");
                        if(hitsArray == null){
                            logger.error("search 出错:"+data);
                            viewer.viewString(err0Item, resp, req);
                        }else{
                            for(JsonElement obj:hitsArray){
                                JsonObject hit = obj.getAsJsonObject();
                                JsonObject sourceObj = hit.getAsJsonObject("_source");
                                if(sourceObj == null){
                                    viewer.viewString(data, resp, req);
                                }else{
                                    JsonObject clinicalObj = sourceObj.getAsJsonObject("clinical_followup_biospecimen");
                                    if(clinicalObj == null){
                                        viewer.viewString(data, resp, req);
                                    }else{
                                        for(Map.Entry<String, JsonElement> entry:clinicalObj.entrySet()){
                                            String subKey = entry.getKey();
                                            if(map.keySet().contains(subKey)){
                                                Map<String,String> tmpMap = map.get(subKey);
                                                String realKey = null;
                                                if(entry.getValue() != null){
                                                    try{
                                                        realKey = entry.getValue().getAsString();
                                                    }catch (Exception e){
                                                        try{
                                                            realKey = entry.getValue().getAsInt() + "";
                                                        }catch (Exception e1){
                                                            logger.error("",e1);
                                                        }
                                                    }
                                                }
                                                if(realKey != null && tmpMap.get(realKey) != null){
                                                    clinicalObj.addProperty(subKey,tmpMap.get(realKey));
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                            String jsonStr = gson.toJson(searchResultObj);
                            viewer.viewString(jsonStr, resp, req);
                        }
                    }
                }else{
                    logger.error("search 出错:"+data);
                    viewer.viewString(err0Item, resp, req);
                }
            } catch (InterruptedException e) {
                logger.error("", e);
            } catch (ExecutionException e) {
                logger.error("",e);
            }
        }
        Map<String,Object> conf = new HashMap<String, Object>();
        conf.put("uid",uid);
        conf.put("keywords", keywords);
        if(uid == null || keywords == null || keywords.contains("AND")){
            return;
        }
        HistoryWords historyWords = new HistoryWords();
        historyWords.setKeywords(keywords);
        historyWords.setUid(uid);
        Integer counter = AllDao.getInstance().getSyUserDao().getHistoryWordsCounter(conf);
        if(counter == null){
            historyWords.setCounter(1);
            AllDao.getInstance().getSyUserDao().insertHistoryWords(historyWords);
        }else {
            conf.put("counter", counter + 1);
            AllDao.getInstance().getSyUserDao().updateHistoryWordsCounter(conf);
        }
    }


    /**
     * 返回样本详情页展示的属性列表
     * columnStatus:0代表默认，1代表可供查询选择的，-1代表返回全部
     * @param req
     * @param resp
     */
    public void listFields(HttpServletRequest req, HttpServletResponse resp){
        String param =  ParamUtils.getParam(req);
        logger.info("listFields param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String indexName = jsonObject.get("indexName").getAsString();
        String columnStatus = jsonObject.get("columnStatus").getAsString();
        JsonArray searchColumn = jsonObject.get("searchColumn").getAsJsonArray();
        List<String> searchColumnList = new LinkedList<String>();
        if("1".equals(columnStatus)){
            for(JsonElement entry:searchColumn){
                searchColumnList.add(entry.getAsString());
            }
        }
        logger.info("index=" + indexName + ",columnStatus=" + columnStatus + ",searchColumn=" + gson.toJson(searchColumnList));
        List<ColumnBean> result = new LinkedList<ColumnBean>();
        //分组，List<ConfItem>>
        Map<String,List<ConfItem>> listMap = DataService.getListRankMap().get(indexName);
        if("0".equals(columnStatus)){
            result = DataService.getListDefaultMap().get(indexName);
        }else if("1".equals(columnStatus)){
            //<IndexFieldName,ConfItem>
            Map<String, ConfItem> tmpMap = ArkService.getIndexMap().get(indexName);
            //<IndexFieldName,ColumnValue>
            Map<String, ColumnValue> tmpResultMap = new HashMap<String, ColumnValue>();
            //<groupName,List<IndexFieldName>>
            Map<String,Set<String>> tmpGroupIndexMap = new HashMap<String, Set<String>>();
            for(String IndexFieldName:searchColumnList){
                ConfItem confItem = tmpMap.get(IndexFieldName);
                if(confItem != null){
                    ColumnValue columnValue = new ColumnValue();
                    List<ConfGroupInfo> groupInfoList = confItem.getGroupInfoList();
                    for(ConfGroupInfo confGroupInfo:groupInfoList){
                        if(confGroupInfo.getPageName().equals("searchReuslt")){
                            String groupName = confGroupInfo.getGroupName();
                            Set<String> set = tmpGroupIndexMap.get(groupName);
                            if(set == null){
                                set = new HashSet<String>();
                                tmpGroupIndexMap.put(groupName, set);
                            }
                            set.add(IndexFieldName);
                        }
                    }
                    ColumnPropetity columnPropetity = new ColumnPropetity();
                    columnPropetity.setDataType(confItem.getDataType());
                    columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                    columnPropetity.setUIFieldName(confItem.getUIFieldName());
                    columnValue.setColumnPropetity(columnPropetity);
                    tmpResultMap.put(IndexFieldName,columnValue);
                }
            }
            Map<String, List<ConfItem>> defaultList = ArkService.getGroupMap().get(indexName).get("searchReuslt");
            Map<String, List<String>> defaultIndexList = DataService.getListRankIndexMap().get(indexName);
            for(String groupName:defaultList.keySet()){
                ColumnBean columnBean = new ColumnBean();
                columnBean.setParentTitle(groupName);
                List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
                columnBean.setColumnValue(columnValues);
                Set<String> set = tmpGroupIndexMap.get(groupName);
                if(set == null){
                    continue;
                }
                for(ConfItem confItem:defaultList.get(groupName)){
                    //选中属性，在默认组中
                    if(set.contains(confItem.getIndexFieldName())){
                        if(!result.contains(columnBean)){
                            result.add(columnBean);
                        }
                        if(!columnValues.contains(tmpResultMap.get(confItem.getIndexFieldName()))){
                            tmpResultMap.get(confItem.getIndexFieldName()).setColumnStatus("0");
                            columnValues.add(tmpResultMap.get(confItem.getIndexFieldName()));
                        }

                    }
                }
                for(String IndexFieldName:set){
                    //所选的属性，不在默认中
                    if(defaultIndexList.get(groupName) != null &&!defaultIndexList.get(groupName).contains(IndexFieldName)){
                        if(!result.contains(columnBean)){
                            result.add(columnBean);
                        }
                        if(!columnValues.contains(tmpResultMap.get(IndexFieldName))){
                            columnValues.add(0,tmpResultMap.get(IndexFieldName));
                        }
                    }
                }
            }

        }else if("-1".equals(columnStatus)){
            Map<String,List<ConfItem>> groupMap = ArkService.getGroupMap().get(indexName).get("searchReuslt");
            Map<String, List<ConfItem>> defaultList = DataService.getListRankMap().get(indexName);
            for(String groupName:defaultList.keySet()){
                List<ConfItem> list = groupMap.get(groupName);
                if(list == null){
                    continue;
                }
                ColumnBean columnBean = new ColumnBean();
                result.add(columnBean);
                columnBean.setParentTitle(groupName);
                List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
                columnBean.setColumnValue(columnValues);
                for(ConfItem confItem:list){
                    ColumnValue columnValue = new ColumnValue();
                    ColumnPropetity columnPropetity = new ColumnPropetity();
                    columnPropetity.setDataType(confItem.getDataType());
                    columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                    columnPropetity.setUIFieldName(confItem.getUIFieldName());
                    columnValue.setColumnPropetity(columnPropetity);
                    if(confItem.getColumnStatus() == null){
                        columnValue.setColumnStatus("1");
                    }else {
                        columnValue.setColumnStatus(confItem.getColumnStatus());
                    }
                    columnValues.add(columnValue);
                }
            }
            String groupName = "其他";
            List<ConfItem> list = groupMap.get(groupName);
            ColumnBean columnBean = new ColumnBean();
            columnBean.setParentTitle(groupName);
            List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
            columnBean.setColumnValue(columnValues);
            if(list != null){
                for(ConfItem confItem:list){
                    ColumnValue columnValue = new ColumnValue();
                    ColumnPropetity columnPropetity = new ColumnPropetity();
                    columnPropetity.setDataType(confItem.getDataType());
                    columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                    columnPropetity.setUIFieldName(confItem.getUIFieldName());
                    columnValue.setColumnPropetity(columnPropetity);
                    if(confItem.getColumnStatus() == null){
                        columnValue.setColumnStatus("1");
                    }else {
                        columnValue.setColumnStatus(confItem.getColumnStatus());
                    }
                    columnValues.add(columnValue);
                }
            }
            if(!columnValues.isEmpty()){
                result.add(columnBean);
            }
        }
        List<ColumnBean> tmp = new LinkedList<ColumnBean>();
        for(ColumnBean columnBean : result){
            ColumnBean columnBean1 = new ColumnBean();
            tmp.add(columnBean1);
            List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
            columnBean1.setParentTitle(columnBean.getParentTitle());
            columnBean1.setColumnValue(columnValues);
            for(ColumnValue columnValue:columnBean.getColumnValue()){
                ColumnValue columnValue1 = new ColumnValue();
                columnValues.add(columnValue1);
                columnValue1.setColumnStatus(columnValue.getColumnStatus());
                ColumnPropetity columnPropetity = new ColumnPropetity();
                columnValue1.setColumnPropetity(columnPropetity);
                columnPropetity.setDataType(columnValue.getColumnPropetity().getDataType());
                columnPropetity.setUIFieldName(columnValue.getColumnPropetity().getUIFieldName());
                columnPropetity.setIndexFieldName(columnValue.getColumnPropetity().getIndexFieldName());
                if(columnValue.getColumnPropetity().getIndexFieldName().startsWith("#")){
                    columnPropetity.setIndexFieldName(columnValue.getColumnPropetity().getIndexFieldName().substring(1));
                }else {
                    columnPropetity.setIndexFieldName(columnValue.getColumnPropetity().getIndexFieldName());
                }

            }
        }


        String reusltStr = gson.toJson(tmp);
        viewer.viewString(reusltStr, resp, req);

    }


    public void detailSample(HttpServletRequest req, HttpServletResponse resp){
        String param =  ParamUtils.getParam(req);
        logger.info("listFields param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String indexName = jsonObject.get("indexName").getAsString();
        String keywords = jsonObject.get("sampleID").getAsString();
        String groupName = jsonObject.get("groupName").getAsString();
        String limit = jsonObject.get("limit").getAsString();
        String jsonStr = detailParser.getData(indexName, keywords, groupName, limit);
        viewer.viewString(jsonStr, resp, req);
    }


    public void searchFields(HttpServletRequest req, HttpServletResponse resps){
        String param =  ParamUtils.getParam(req);
        logger.info("searchFields param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String indexName = jsonObject.get("indexName").getAsString();
        String keywords = jsonObject.get("keywords").getAsString();
        logger.info("indexName="+indexName + ",keywords=" + keywords );
        List<ColumnBean> list = SearchFieldsParser.parse2(indexName, keywords);
        String jsonStr = gson.toJson(list);
        viewer.viewString(jsonStr,resps,req);
    }

    public void recommendFields(HttpServletRequest req, HttpServletResponse resps){
        String param =  ParamUtils.getParam(req);
        logger.info("recommendFields param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String indexName = jsonObject.get("indexName").getAsString();
        String keywords = jsonObject.get("keywords").getAsString();
        logger.info("indexName=" + indexName + ",keywords=" + keywords);
        List<ColumnPropetity> list = RecommendFieldsParser.parse(indexName, keywords);
        String jsonStr = gson.toJson(list);
        viewer.viewString(jsonStr, resps, req);
    }

    /**
     * 搜索项目中成员
     * @param req
     * @param resps
     */
    public void searchMembers(HttpServletRequest req, HttpServletResponse resps){
        String param =  ParamUtils.getParam(req);
        logger.info("searchProjectMembers param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        String searchMemberkey = jsonObject.get("searchMemberkey").getAsString();
        String limit = jsonObject.get("limit").getAsString();
        String projectID = jsonObject.get("projectID").getAsString();
        logger.info("searchProjectMembers searchMemberkey=" + searchMemberkey + ",limit=" + limit);
        int[] ls = ParamUtils.parseLimit(limit);
        Map<String,Object> confMap = new HashMap<String, Object>();
        confMap.put("startIndex",ls[0] * ls[1]);
        confMap.put("maxNum", ls[1]);
        confMap.put("searchMemberkey",searchMemberkey);
        confMap.put("projectID", projectID);
        List<SyUser> list = AllDao.getInstance().getSyUserDao().searchMemberList(confMap);
        int counter = AllDao.getInstance().getSyUserDao().searchMemberCounter(confMap);
        Map<String,Integer> info = new HashMap<String,Integer>();
        info.put("counter",counter);
        viewer.viewList(list, info, true, resps, req);
    }

    /**
     * 二维属性
     * @param req
     * @param resp
     */
    public void searchStatV2(HttpServletRequest req, HttpServletResponse resp) {
        String param =  ParamUtils.getParam(req);
        logger.info("searchStatV2 param =" + param);
        JsonObject jsonObject = jsonParser.parse(param).getAsJsonObject();
        logger.info("query=" + jsonObject.get("query").getAsString());
        String queryStr = jsonObject.get("query").getAsString();
        String index = jsonObject.get("indexName").getAsString();
        String column1 = jsonObject.get("column1").getAsString();
        String column2 = jsonObject.get("column2").getAsString();
        JsonArray range = null;
        if(jsonObject.get("range") != null && jsonObject.get("range").isJsonArray()){
            range = jsonObject.getAsJsonArray("range");
        }
        JsonObject query = new JsonObject();
        query.addProperty("indexName",index);
        query.addProperty("query", queryStr);
        JsonArray source = new JsonArray();
        source.add(column1);
        source.add(column2);
        query.add("source",source);
        JsonObject aggs = new JsonObject();
        query.add("aggs",aggs);
        JsonArray terms_aggs = new JsonArray();
        JsonObject item = new JsonObject();
        item.addProperty("field",column1);
        if("AGE_DX".equals(column1) || "clinical_followup_biospecimen.patient.age_at_initial_pathologic_diagnosis".equals(column1)){
            item.add("range",range);
        }
        JsonArray sub_terms_aggs = new JsonArray();
        JsonObject subItem = new JsonObject();
        subItem.addProperty("field", column2);
        if("AGE_DX".equals(column2) || "clinical_followup_biospecimen.patient.age_at_initial_pathologic_diagnosis".equals(column2)){
            subItem.add("range",range);
        }
        sub_terms_aggs.add(subItem);
        terms_aggs.add(item);
        if("AGE_DX".equals(column2) || "clinical_followup_biospecimen.patient.age_at_initial_pathologic_diagnosis".equals(column2)){
            item.add("sub_range_aggs", sub_terms_aggs);
        }else{
            item.add("sub_terms_aggs", sub_terms_aggs);
        }
        if("AGE_DX".equals(column1) || "clinical_followup_biospecimen.patient.age_at_initial_pathologic_diagnosis".equals(column1)){
            aggs.add("range_aggs", terms_aggs);
        }else {
            aggs.add("terms_aggs", terms_aggs);
        }
        String newParam =  gson.toJson(query);
        logger.info("newParam=" + newParam);
        SearchKeyParser searchKeyParser = new SearchKeyParser(index,newParam);
        ConfItem columnConfItem1 = ArkService.getIndexMap().get(index).get(column1);
        ConfItem columnConfItem2 = ArkService.getIndexMap().get(index).get(column2);
        Future<String> result = executorService.submit(searchKeyParser);
        try {
            String resultData = result.get();
            logger.info("resultData=" + resultData);
            JsonObject buildObject = new JsonObject();
            JsonObject chart = new JsonObject();
            chart.addProperty("type","column");
            buildObject.add("chart", chart);
            JsonObject title = new JsonObject();
            title.addProperty("text", columnConfItem1.getUIFieldName() + "-" + columnConfItem2.getUIFieldName() + "统计");
            buildObject.add("title", title);
            JsonObject xAxis = new JsonObject();
            buildObject.add("xAxis",xAxis);
            JsonArray categories = new JsonArray();
            xAxis.add("categories",categories);
            JsonArray series = new JsonArray();
            buildObject.add("series",series);
            //一维取值，二维取值，count
            Map<String,List<Integer>> tmpMap = new LinkedHashMap<String, List<Integer>>();
            JsonObject resultObject = jsonParser.parse(resultData).getAsJsonObject();
            JsonObject aggregations = resultObject.getAsJsonObject("aggregations");
            for(Map.Entry<String, JsonElement> name:aggregations.entrySet()){
                String column = name.getKey();
                if(column.startsWith(column1)){
                    JsonObject column1Object = aggregations.getAsJsonObject(column);
                    JsonArray buckets = column1Object.getAsJsonArray("buckets");
                    for(JsonElement keyItem:buckets){
                        JsonObject keyObject = keyItem.getAsJsonObject();
                        String key = keyObject.get("key").getAsString();
                        categories.add(key);
                        for(Map.Entry<String, JsonElement> subName:keyObject.entrySet()){
                            String subColumn = subName.getKey();
                            if(subColumn.startsWith(column2)){
                                JsonObject column2Object = subName.getValue().getAsJsonObject();
                                JsonArray subBuckets = column2Object.getAsJsonArray("buckets");
                                for(JsonElement subKeyItem:subBuckets){
                                    String subKey = subKeyItem.getAsJsonObject().get("key").getAsString();
                                    Integer docCount = subKeyItem.getAsJsonObject().get("doc_count").getAsInt();
                                    List<Integer> tmpList = tmpMap.get(subKey);
                                    if(tmpList == null){
                                        tmpList = new LinkedList<Integer>();
                                        tmpMap.put(subKey,tmpList);
                                    }
                                    tmpList.add(docCount);
                                }
                            }
                        }
                    }
                }
            }
            for(String key:tmpMap.keySet()){
                JsonObject dataObj = new JsonObject();
                dataObj.addProperty("name",key);
                List<Integer> tmpList = tmpMap.get(key);
                JsonArray data = new JsonArray();
                for(int i:tmpList){
                    data.add(i);
                }
                dataObj.add("data",data);
                series.add(dataObj);
            }
            String jsonStr = gson.toJson(buildObject);
            viewer.viewString(jsonStr,resp,req);
        } catch (InterruptedException e) {
            logger.error("",e);
        } catch (ExecutionException e) {
            logger.error("",e);
        }
    }

    private String err0Item = "{\n" +
            "\"took\":880," +
            "\"timed_out\":false," +
            "\"_shards\":{" +
            "\"total\":5," +
            "\"successful\":5," +
            "\"failed\":0" +
            "}," +
            "\"hits\":{" +
            "\"total\":0," +
            "\"max_score\":null," +
            "\"hits\":[" +
            "]" +
            "}," +
            "\"aggregations\":{" +
            "}" +
            "}";

}
