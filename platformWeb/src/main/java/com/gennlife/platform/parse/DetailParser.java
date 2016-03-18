package com.gennlife.platform.parse;

import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.DataService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;

import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


/**
 * Created by chensong on 2015/12/18.
 */
public class DetailParser {
    private static Logger logger = LoggerFactory.getLogger(DetailParser.class);
    public static String detailURL= null;
    public static String detailGenURL= null;
    private static Gson gson = GsonUtil.getGson();

    private static JsonParser jsonParser = new JsonParser();
    private static View viewer = new View();
    static{
        detailURL = ArkService.getConf().getDetailURL();
        detailGenURL=ArkService.getConf().getDetailGenURL();
    }
    public String getData(String table,String key,String groupName,String limit){
        String url = null;
        if(groupName == null || "".equals(groupName)){
            groupName = "总览信息";
        }
        Set<String>  fieldsSet = DataService.getGroupIndexFieldNameMap().get(table).get(groupName);
        if("生物标本信息".equals(groupName)){
            fieldsSet = getBioIndexFieldNameSet(table,groupName);
        }
        Set<String> tmpSet = new HashSet<String>();
        for(String index:fieldsSet){
            if(!"".equals(index) && !index.startsWith("#")){
                tmpSet.add(index);
            }
        }

        String fields = gson.toJson(tmpSet);
        String data = null;
        if("tcga_read".equals(table) && "基因突变".equals(groupName)){
            String[] pages = limit.split(",");
            if(pages.length != 2) {
                logger.error("limit 参数不合法");
                return null;
            }
            url = String.format(detailGenURL, ParamUtils.encodeURI(table), ParamUtils.encodeURI(key), ParamUtils.encodeURI(fields), ParamUtils.encodeURI(pages[1]), ParamUtils.encodeURI(pages[0]));
            logger.info(String.format(detailGenURL, table, key, fields,pages[1],pages[0]));
        } else {
            url = String.format(detailURL, ParamUtils.encodeURI(table), ParamUtils.encodeURI(key), ParamUtils.encodeURI(fields));
            logger.info(String.format(detailURL, table, key, fields));
        }
        data = HttpRequestUtils.httpGet(url);
        logger.info("data ="+data);
        //IndexFieldName,value
        Map<String,String> indexResultMap = new HashMap<String, String>();
        //List<IndexFieldName,value>
        List<Map<String,String>> indexResultList = new LinkedList<Map<String, String>>();
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        Boolean succeed = jsonObject.get("succeed").getAsBoolean();
        if(!succeed) {
            logger.error("详情返回结果："+ data);
            //return null;
        }else {
            if("tcga_read".equals(table) && "基因突变".equals(groupName)){
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                for(JsonElement entry:jsonArray){
                    JsonObject object = entry.getAsJsonObject();
                    Map<String,String> map = new HashMap<String, String>();
                    indexResultList.add(map);
                    for(String IndexFieldName:tmpSet){
                        if(object.get(IndexFieldName) !=null) {
                            try {
                                String value = object.get(IndexFieldName).getAsString();
                                map.put(IndexFieldName, value);
                            } catch (Exception e) {
                                map.put(IndexFieldName, object.get(IndexFieldName).toString());
                            }
                        }
                    }
                }
            }else{
                JsonObject jsonResult = jsonObject.getAsJsonObject("data");
                for(String IndexFieldName:tmpSet){
                    jsonResult.get(IndexFieldName);
                    if(jsonResult.get(IndexFieldName) != null){
                        try {
                            String value = jsonResult.get(IndexFieldName).getAsString();
                            indexResultMap.put(IndexFieldName,value);
                        }catch (Exception e) {
                            indexResultMap.put(IndexFieldName,jsonResult.get(IndexFieldName).toString());
                        }
                    }else {
                        indexResultMap.put(IndexFieldName,"");
                    }

                }
            }
        }
        List<String> groupNames = DataService.getDetailGroupNameListMap().get(table);
        if("总览信息".equals(groupName)){
            Map<String,Map<String,String>> dataMap = summary(groupName, table, indexResultMap);
            String str = viewer.viewSummary(dataMap,groupNames,groupName);
            return str;
        }else if("临床信息".equals(groupName) || "随访信息".equals(groupName)){
            Map<String, String> dataMap = clinicalOrFollowup(groupName,table,indexResultMap);
            String str = viewer.viewClinicalOrFollowup(dataMap, groupNames, groupName);
            return str;
        }else if("生物标本信息".equals(groupName)) {
            Map<String,ConfItem> IndexFieldNameConfItemMap = ArkService.getIndexMap().get(table);
            Map<String, Map<String, Map<String, String>>> dataMap = bioBigData(groupName, table, indexResultMap,IndexFieldNameConfItemMap);
            Map<String, String> groupMap = bioGroupData(groupName, table, indexResultMap,IndexFieldNameConfItemMap);
            String str = viewer.ViewBio(dataMap,groupNames,groupMap,groupName);
            return str;
        }else if("tcga_read".equals(table) && "基因突变".equals(groupName)){
            List<String> headList = (List<String>) DataService.getDetailGroupMap().get(table).get(groupName);
            String str = viewer.viewGene(groupNames,groupName,indexResultList,headList);
            return str;
        }else if("seer_read".equals(table) && "基因突变".equals(groupName)){
            List<String> headList = (List<String>) DataService.getDetailGroupMap().get(table).get(groupName);
            String str = viewer.viewGene(groupNames,groupName,indexResultList,headList);
            return str;
        }


        return null;


    }
    private Map<String, Map<String, Map<String, String>>>  bioBigData(String groupName,String table, Map<String,String> indexResultMap,Map<String,ConfItem> IndexFieldNameConfItemMap){
        //原发肿瘤,血样,<UIName,value>:组织成的
        Map<String, Map<String, Map<String, String>>> resutMap = new HashMap<String, Map<String, Map<String, String>>>();
        if("生物标本信息".equals(groupName)) {
            Map<String, Set<String>> groupNameIndexFieldNameMap = DataService.getGroupIndexFieldNameMap().get(table);
            //value,Set<IndexFieldName>:第一层
            Map<String, Set<String>> subIndexGroup1Map = new HashMap<String, Set<String>>();
            Set<String> prefixSet = groupNameIndexFieldNameMap.get(groupName + "#prefix");
            Set<String> group1 = groupNameIndexFieldNameMap.get(groupName + "#group1");
            Set<String> group2 = groupNameIndexFieldNameMap.get(groupName + "#group2");
            Set<String> dataSet = groupNameIndexFieldNameMap.get(groupName + "#data");
            for(String prefix:prefixSet){
                for (String IndexFieldName : group1) {
                    IndexFieldName = prefix + IndexFieldName;
                    String value = indexResultMap.get(IndexFieldName);
                    if(value != null && !"".equals(value)){
                        Set<String> values = subIndexGroup1Map.get(value);
                        if (values == null) {
                            values = new HashSet<String>();
                            subIndexGroup1Map.put(value, values);
                        }
                        values.add(IndexFieldName);
                    }
                }
            }
            //遍历第一层
            for(String value:subIndexGroup1Map.keySet()){
                Map<String, Map<String, String>> tmp = new HashMap<String, Map<String, String>>();
                resutMap.put(value, tmp);
                Set<String> indexs = subIndexGroup1Map.get(value);
                //value,Set<IndexFieldName>:第一层
                Map<String, Set<String>> subIndexGroup2Map = new HashMap<String, Set<String>>();
                for(String index:indexs){
                    String prefix = index.substring(0,index.lastIndexOf(".")) +".";
                    for(String group:group2){
                        String IndexFieldName = prefix + group;
                        String group2Value = indexResultMap.get(IndexFieldName);
                        Map<String, String> tmp2Map = new HashMap<String, String>();
                        tmp.put(group2Value,tmp2Map);
                        Set<String> values = subIndexGroup2Map.get(group2Value);
                        if (values == null) {
                            values = new HashSet<String>();
                            subIndexGroup2Map.put(group2Value, values);
                        }
                        values.add(IndexFieldName);
                        for(String data:dataSet){
                            if(data.startsWith("#")){
                                tmp2Map.put(data.substring(1,index.length()),"");
                            }else{
                                String IndexFieldNameData = prefix + data;
                                ConfItem confItem = IndexFieldNameConfItemMap.get(IndexFieldNameData);
                                if(confItem != null){
                                    String group3Value = indexResultMap.get(IndexFieldNameData) == null?"":indexResultMap.get(IndexFieldNameData);
                                    tmp2Map.put(confItem.getUIFieldName(), group3Value);
                                }
                            }
                        }
                    }
                }
            }
        }
        Map<String,List<String>> sortMap = (Map<String, List<String>>) DataService.getDetailGroupMap().get(table).get(groupName);
        List<String> sort = sortMap.get("data");
        List<String> sort1 = sortMap.get("head2");
        Map<String, Map<String, Map<String, String>>> realMap = new LinkedHashMap<String, Map<String, Map<String, String>>>();
        for(String group1:DataService.getGroup1()){
            if(resutMap.keySet().contains(group1)){
                Map<String, Map<String, String>> map = resutMap.get(group1);
                Map<String, Map<String, String>> tmpMap = new LinkedHashMap<String, Map<String, String>>();
                realMap.put(group1,tmpMap);
                for(String group2:DataService.getGroup2()){
                    if(map.keySet().contains(group2)){
                        Map<String,String> noSortMap = map.get(group2);
                        Map<String,String> sortData = new LinkedHashMap<String, String>();
                        for(String UIIndex:sort){
                            String[] datas = UIIndex.split(",");
                            if(noSortMap.get(datas[1]) == null){
                                sortData.put(datas[0],"");
                            }else {
                                sortData.put(datas[0],noSortMap.get(datas[1]));
                            }
                        }
                        for(String UIIndex:sort1){
                            String[] datas = UIIndex.split(",");
                            if(noSortMap.get(datas[1]) == null){
                                sortData.put(datas[0],"");
                            }else {
                                sortData.put(datas[0],noSortMap.get(datas[1]));
                            }
                        }
                        tmpMap.put(group2,sortData);
                    }
                }
            }
        }
        return realMap;
    }
    private Map<String, String>  bioGroupData(String groupName,String table, Map<String,String> indexResultMap,Map<String,ConfItem> IndexFieldNameConfItemMap){
        Map<String, Set<String>> groupNameIndexFieldNameMap = DataService.getGroupIndexFieldNameMap().get(table);
        List<String> list = new LinkedList<String>();
        Set<String> groupSet = groupNameIndexFieldNameMap.get(groupName + "#group");
        for(String head:groupSet){
            list.add(head);
        }
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        for(String index:list){
            String value = "";
            String name = "";
            if(IndexFieldNameConfItemMap.get(index) != null){
                value = indexResultMap.get(index) == null ? "":indexResultMap.get(index);
                name = IndexFieldNameConfItemMap.get(index).getUIFieldName();
            }else{
                name = index.substring(1,index.length());
                value = "";
            }
            resultMap.put(name,value);
        }
        return resultMap;
    }

    private Map<String,Map<String,String>> summary(String groupName, String table, Map<String, String> indexResultMap){
        Map<String,Map<String,String>> resultMap = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Object> map = (Map<String, Object>) DataService.getDetailGroupMap().get(table).get(groupName);
        for(String subGroupName:map.keySet()){
            Map<String,String> subMap = new LinkedHashMap<String, String>();
            resultMap.put(subGroupName,subMap);
            List<String> list = (List<String>) map.get(subGroupName);
            for(String max:list){
                String[] keys = max.split(",");
                String UIName = keys[0];
                String IndexFieldName = keys[1];
                String value = indexResultMap.get(IndexFieldName);
                subMap.put(UIName,value);
            }
        }
        return resultMap;
    }

    private Map<String,String> clinicalOrFollowup(String groupName, String table, Map<String, String> indexResultMap){
        Map<String,String> resultMap = new LinkedHashMap<String, String>();
        List<String> list = (List<String>) DataService.getDetailGroupMap().get(table).get(groupName);
        for(String max:list){
            String[] keys = max.split(",");
            String UIName = keys[0];
            String IndexFieldName = keys[1];
            String value = indexResultMap.get(IndexFieldName);
            resultMap.put(UIName, value);
        }
        return resultMap;
    }

    private Set<String> getBioIndexFieldNameSet(String table ,String groupName){
        Set<String> set = new HashSet<String>();
        Map<String, Set<String>> groupNameIndexFieldNameMap = DataService.getGroupIndexFieldNameMap().get(table);
        Set<String> prefixSet = groupNameIndexFieldNameMap.get(groupName + "#prefix");
        Set<String> group1 = groupNameIndexFieldNameMap.get(groupName + "#group1");
        Set<String> group2 = groupNameIndexFieldNameMap.get(groupName + "#group2");
        Set<String> dataSet = groupNameIndexFieldNameMap.get(groupName + "#data");
        Set<String> head2Set = groupNameIndexFieldNameMap.get(groupName + "#head2");
        Set<String> groupSet = groupNameIndexFieldNameMap.get(groupName + "#group");
        for(String prefix:prefixSet){
            for(String group:group1){
                set.add(prefix + group);
            }
            for(String group:group2){
                set.add(prefix + group);
            }
            for(String group:dataSet){
                set.add(prefix + group);
            }
        }
        for(String head:head2Set){
            set.add(head);
        }
        for(String head:groupSet){
            set.add(head);
        }
        return set;
    }



}
