package com.gennlife.platform.service;

import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.bean.list.ColumnBean;
import com.gennlife.platform.bean.list.ColumnPropetity;
import com.gennlife.platform.bean.list.ColumnValue;
import com.gennlife.platform.util.FilesUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by chensong on 2016/1/13.
 */
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);
    private static String listRank;
    private static String detailRank;
    private static JsonParser jsonParser = new JsonParser();
    public static void init(){
        searchReusltConf();
        searchReusltDefaultList();
        detailConf();
    }
    //<库名，分组，List<ConfItem>>:搜索结果分组排序逻辑
    private static Map<String,Map<String,List<ConfItem>>> listRankMap = new HashMap<String,Map<String,List<ConfItem>>>();
    //<库名，分组，List<IndexFieldName>>:搜索结果分组排序逻辑索引
    private static Map<String,Map<String,List<String>>> listRankIndexMap = new HashMap<String,Map<String,List<String>>>();
    //<库名，List<ColumnBean>>:搜索结果的默认表头结构
    private static Map<String,List<ColumnBean>> listDefaultMap = new HashMap<String, List<ColumnBean>>();

    /**
     * 解析配置文件:search_list_column_rank.json
     */
    private static void searchReusltConf(){
        InputStream inputStream4 =  ArkService.class.getResourceAsStream("/search_list_column_rank.json");
        try {
            listRank = FilesUtils.readString(inputStream4, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException();
        }finally {
            try {
                inputStream4.close();
            } catch (IOException e) {
                logger.error("", e);
                throw new RuntimeException();
            }
        }

        JsonObject listRankJsonObject = jsonParser.parse(listRank).getAsJsonObject();
        for(Map.Entry<String, JsonElement> entry:listRankJsonObject.entrySet()){
            String indexName = entry.getKey();
            Map<String,ConfItem> indexFieldsMap = ArkService.getIndexMap().get(indexName);
            Map<String,List<ConfItem>> indexMap = listRankMap.get(indexName);
            if(indexMap == null){
                indexMap = new LinkedHashMap<String, List<ConfItem>>();
                listRankMap.put(indexName,indexMap);
            }
            Map<String,List<String>> tmp = listRankIndexMap.get(indexName);
            if(tmp == null){
                tmp= new LinkedHashMap<String, List<String>>();
                listRankIndexMap.put(indexName,tmp);
            }
            JsonObject indexObject = entry.getValue().getAsJsonObject();
            for(Map.Entry<String, JsonElement> indexEntry:indexObject.entrySet()){
                String groupName = indexEntry.getKey();
                List<ConfItem> list = indexMap.get(groupName);
                if(list == null){
                    list = new LinkedList<ConfItem>();
                    indexMap.put(groupName,list);
                }

                List<String> tmpList = tmp.get(groupName);
                if(tmpList == null){
                    tmpList = new LinkedList<String>();
                    tmp.put(groupName,tmpList);
                }

                JsonArray jsonArray = indexEntry.getValue().getAsJsonArray();
                for(JsonElement valueEntry : jsonArray) {
                    JsonObject v = valueEntry.getAsJsonObject();
                    for(Map.Entry<String, JsonElement> vEntry:v.entrySet()){
                        String UIFieldName = vEntry.getKey();
                        String IndexFieldName = vEntry.getValue().getAsString();
                        if(indexFieldsMap.get(IndexFieldName) != null){
                            indexFieldsMap.get(IndexFieldName).setColumnStatus("0");
                        }
                        tmpList.add(IndexFieldName);
                        ConfItem confItem = new ConfItem();
                        confItem.setIndexFieldName(IndexFieldName);
                        confItem.setUIFieldName(UIFieldName);
                        confItem.setColumnStatus("0");
                        list.add(confItem);
                    }
                }
            }
        }
    }

    private static void searchReusltDefaultList(){
        for(String tableName:listRankMap.keySet()){
            List<ColumnBean> list = new LinkedList<ColumnBean>();
            listDefaultMap.put(tableName,list);
            Map<String,List<ConfItem>> groupMap = listRankMap.get(tableName);
            for(String groupName:groupMap.keySet()){
                ColumnBean columnBean = new ColumnBean();
                list.add(columnBean);
                columnBean.setParentTitle(groupName);
                List<ColumnValue> columnValues = new LinkedList<ColumnValue>();
                columnBean.setColumnValue(columnValues);
                for(ConfItem confItem:groupMap.get(groupName)){
                    ColumnPropetity columnPropetity = new ColumnPropetity();
                    columnPropetity.setDataType(confItem.getDataType());
                    columnPropetity.setIndexFieldName(confItem.getIndexFieldName());
                    columnPropetity.setUIFieldName(confItem.getUIFieldName());
                    ColumnValue columnValue = new ColumnValue();
                    columnValue.setColumnStatus("0");
                    confItem.setColumnStatus("0");
                    columnValue.setColumnPropetity(columnPropetity);
                    columnValues.add(columnValue);
                }
            }
        }
    }
    //<库名,组名List>:有序
    private static Map<String,List<String>> detailGroupNameListMap = new HashMap<String, List<String>>();
    //<库名,<组名，Object>> 所有详情页的汇总
    private static Map<String,Map<String,Object>> detailGroupMap = new HashMap<String, Map<String, Object>>();
    /*
        库名:总览信息:子组名:List<UIFieldName+","+IndexFieldName>
             临床信息:List<UIFieldName+","+IndexFieldName>
             随访信息:List<UIFieldName+","+IndexFieldName>
             基因突变:List<UIFieldName+","+IndexFieldName>
             生物标本信息:子组名:List<UIFieldName+","+IndexFieldName>

     */
    //每组请求详情时,IndexFieldName集合
    //<库名，组名，Set<IndexFieldName>>:
    private static  Map<String,Map<String,Set<String>>> groupIndexFieldNameMap = new HashMap<String, Map<String, Set<String>>>();

    /**
     * 解析配置文件:search_detail_column_rank.json
     */
    private static void detailConf(){
        InputStream inputStream2 =  ArkService.class.getResourceAsStream("/search_detail_column_rank.json");
        try {
            detailRank =  FilesUtils.readString(inputStream2, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException();
        }finally {
            try {
                inputStream2.close();
            } catch (IOException e) {
                logger.error("", e);
                throw new RuntimeException();
            }
        }
        JsonObject detailJsonObject = jsonParser.parse(detailRank).getAsJsonObject();
        for(Map.Entry<String, JsonElement> tableEntery:detailJsonObject.entrySet()){
            String tableName = tableEntery.getKey();
            Map<String,Object> tmpGroup = new LinkedHashMap<String, Object>();
            detailGroupMap.put(tableName,tmpGroup);
            List<String> tmpGroupNameList = new LinkedList<String>();
            detailGroupNameListMap.put(tableName, tmpGroupNameList);
            Map<String,Set<String>> tmpgGroupIndexFieldNameMap = new LinkedHashMap<String, Set<String>>();
            groupIndexFieldNameMap.put(tableName,tmpgGroupIndexFieldNameMap);//表名
            JsonObject tableObject = tableEntery.getValue().getAsJsonObject();
            for(Map.Entry<String, JsonElement> groupEntry:tableObject.entrySet()){
                String groupName = groupEntry.getKey();
                Set<String> set = new HashSet<String>();
                tmpgGroupIndexFieldNameMap.put(groupName,set);//组名
                tmpGroupNameList.add(groupName);
                if(groupEntry.getValue().isJsonArray()){
                    List<String> tmpList = new LinkedList<String>();
                    tmpGroup.put(groupName,tmpList);
                    JsonArray dataArray = groupEntry.getValue().getAsJsonArray();
                    for(JsonElement dataEntry:dataArray){
                        JsonObject dataOject = dataEntry.getAsJsonObject();
                        for(Map.Entry<String,JsonElement> item:dataOject.entrySet()){
                            String UIName = item.getKey();
                            String IndexFieldName = item.getValue().getAsString();
                            tmpList.add(UIName + "," + IndexFieldName);
                            set.add(IndexFieldName);
                        }
                    }
                }else if(groupEntry.getValue().isJsonObject()){
                    Map<String,Object> tmpMap = new LinkedHashMap<String, Object>();
                    tmpGroup.put(groupName,tmpMap);
                    JsonObject subGroupObject = groupEntry.getValue().getAsJsonObject();
                    for(Map.Entry<String, JsonElement> dataEntry:subGroupObject.entrySet()){
                        Set<String> subSet = new HashSet<String>();
                        String subGroupName = dataEntry.getKey();
                        tmpgGroupIndexFieldNameMap.put(groupName + "#" + subGroupName,subSet);//组名
                        JsonArray dataArray = dataEntry.getValue().getAsJsonArray();
                        List<String> tmpList = new LinkedList<String>();
                        tmpMap.put(subGroupName,tmpList);
                        for(JsonElement itemEntry:dataArray){
                            JsonObject dataOject = itemEntry.getAsJsonObject();
                            for(Map.Entry<String, JsonElement> item:dataOject.entrySet()){
                                String UIName = item.getKey();
                                String IndexFieldName = item.getValue().getAsString();
                                tmpList.add(UIName + "," + IndexFieldName);
                                set.add(IndexFieldName);
                                subSet.add(IndexFieldName);
                            }
                        }
                    }

                }
            }
        }
    }

    public static final Map<String, List<ColumnBean>> getListDefaultMap() {
        return listDefaultMap;
    }

    public static final Map<String, Map<String, List<ConfItem>>> getListRankMap() {
        return listRankMap;
    }

    public static final Map<String, Map<String, List<String>>> getListRankIndexMap() {
        return listRankIndexMap;
    }

    public static Map<String, Map<String, Set<String>>> getGroupIndexFieldNameMap() {
        return groupIndexFieldNameMap;
    }

    public static Map<String, List<String>> getDetailGroupNameListMap() {
        return detailGroupNameListMap;
    }

    public static Map<String, Map<String, Object>> getDetailGroupMap() {
        return detailGroupMap;
    }


    private static List<String> group1 = new LinkedList<String>();
    private static List<String> group2 = new LinkedList<String>();
    static{
        group1.add("原发肿瘤");
        group1.add("复发肿瘤");
        group1.add("转移肿瘤");
        group2.add("血样");
        group2.add("正常组织");
        group2.add("肿瘤组织");
    }


    public static List<String> getGroup2() {
        return group2;
    }

    public static List<String> getGroup1() {
        return group1;
    }
}
