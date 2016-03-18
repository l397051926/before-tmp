package com.gennlife.platform.service;

import com.gennlife.platform.bean.conf.ConfGroupInfo;
import com.gennlife.platform.bean.conf.ConfItem;
import com.gennlife.platform.util.Conf;
import com.gennlife.platform.util.FilesUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.SpringContextUtil;
import com.google.gson.*;
import com.google.gson.internal.LinkedHashTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chensong on 2015/12/10.
 */
public class ArkService {
    private static final Logger logger = LoggerFactory.getLogger(ArkService.class);
    private static Conf conf = null;
    private static ExecutorService executorService;
    private static String seerField;
    private static String CosmicMutantExport_v70;
    private static Gson gson = GsonUtil.getGson();


    private static JsonParser jsonParser = new JsonParser();

    public void init(String s, Element element) {
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        logger.info("qRecommendURL =" + conf.getqRecommendURL());
        logger.info("searchURL =" + conf.getSearchURL());
        logger.info("fileURL =" + conf.getFileURL());
        logger.info("detailURL =" + conf.getDetailURL());
        logger.info("importURL =" + conf.getImportURL());
        executorService = Executors.newCachedThreadPool();
        //解析tcga配置文件
        TcgaConfigParser();
        //解析seer配置文件
        SeerConfigParser();
        //解析Cosmic配置文件
        CosmicMutantExportConfigParser();
        //过滤tcga搜索结果
        TcgaMapParser();
        DataService.init();

    }

    public void destroy() {

    }

    public static Conf getConf(){
        if(conf == null){
            ApplicationContext context = SpringContextUtil.getApplicationContext();
            conf = (Conf) context.getBean("com.gennlife.platform.util.Conf");
        }
        return conf;
    }
    public static ExecutorService getExecutorService(){
        if(executorService == null){
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }

    //<库名,pageName,group,List<ConfItem>>
    private static Map<String, Map<String,Map<String,List<ConfItem>>>> GroupMap = new HashMap<String, Map<String, Map<String, List<ConfItem>>>>();
    //<库名,<IndexFieldName,ConfItem>> 所有库的索引Map
    private static Map<String, Map<String,ConfItem>> IndexMap = new HashMap<String,  Map<String,ConfItem>>();
    //<库名,<UIFieldName,Set<ConfItem>>> 所有库的索引Map
    private static Map<String,Map<String,Set<String>>> ReIndexMap = new HashMap<String, Map<String,Set<String>>>();
    /**
     * 解析tcga初始配置文件：tcga_read_field.json
     */
    private static void TcgaConfigParser(){
        InputStream inputStream5 =  ArkService.class.getResourceAsStream("/tcga_read_field.json");
        String data = null;
        try {
            data = FilesUtils.readString(inputStream5, "utf-8");
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }finally {
            try {
                inputStream5.close();
            } catch (IOException e) {
                logger.error("",e);
                throw new RuntimeException();
            }
        }
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        JsonObject tcgaJsonObject = jsonObject.getAsJsonObject("tcga_read");
        Map<String,ConfItem> tmpIndexMap = new HashMap<String, ConfItem>();
        IndexMap.put("tcga_read",tmpIndexMap);
        Map<String,Map<String,List<ConfItem>>> tmpGroupMap = new HashMap<String, Map<String, List<ConfItem>>>();
        GroupMap.put("tcga_read",tmpGroupMap);
        Map<String,Set<String>> tmpReIndex = new HashMap<String, Set<String>>();
        ReIndexMap.put("tcga_read",tmpReIndex);
        for(Map.Entry<String, JsonElement> entry:tcgaJsonObject.entrySet()) {
            String name = entry.getKey();
            JsonObject valueObject = entry.getValue().getAsJsonObject();
            if("gene".equals(name)){
                JsonObject tmpValueMap = entry.getValue().getAsJsonObject();
                valueObject = tmpValueMap.get("READ.Mutation_Packager_Oncotated_Raw_Calls.Level_3").getAsJsonObject();
            }
            for(Map.Entry<String, JsonElement> itemEntry:valueObject.entrySet()){
                JsonObject item = itemEntry.getValue().getAsJsonObject();
                ConfItem confItem = new ConfItem();
                String dataType = item.get("dataType").getAsString();
                String IndexFieldName = item.get("IndexFieldName").getAsString();
                String UIFieldName = item.get("UIFieldName").getAsString();
                tmpIndexMap.put(IndexFieldName, confItem);
                Set<String> set = tmpReIndex.get(UIFieldName);
                if(set == null){
                    set = new HashSet<String>();
                    tmpReIndex.put(UIFieldName,set);
                }
                set.add(IndexFieldName);
                confItem.setDataType(dataType);
                confItem.setIndexFieldName(IndexFieldName);
                confItem.setUIFieldName(UIFieldName);
                List<ConfGroupInfo> list = new LinkedList<ConfGroupInfo>();
                confItem.setGroupInfoList(list);
                Set<String> pageNameSet = new HashSet<String>();
                if(item.get("groupInfo")!= null && item.get("groupInfo").isJsonArray()){
                    JsonArray groupInfoArray = item.getAsJsonArray("groupInfo");
                    for(JsonElement groupItem:groupInfoArray){
                        String pageName = groupItem.getAsJsonObject().get("pageName").getAsString();
                        String groupName = groupItem.getAsJsonObject().get("groupName").getAsString();
                        pageNameSet.add(pageName);
                        ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                        confGroupInfo.setGroupName(groupName);
                        confGroupInfo.setPageName(pageName);
                        list.add(confGroupInfo);
                        Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                        if(tmpMap == null){
                            tmpMap = new HashMap<String,List<ConfItem>>();
                            tmpGroupMap.put(pageName,tmpMap);
                        }
                        List<ConfItem> tmpList = tmpMap.get(groupName);
                        if(tmpList == null){
                            tmpList = new LinkedList<ConfItem>();
                            tmpMap.put(groupName, tmpList);
                        }
                        tmpList.add(confItem);
                    }
                    if(!pageNameSet.contains("searchReuslt")){
                        String groupName = "其他";
                        String pageName = "searchReuslt";
                        ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                        confGroupInfo.setPageName(pageName);
                        confGroupInfo.setGroupName(groupName);
                        list.add(confGroupInfo);
                        Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                        if(tmpMap == null){
                            tmpMap = new HashMap<String,List<ConfItem>>();
                            tmpGroupMap.put(pageName,tmpMap);
                        }
                        List<ConfItem> tmpList = tmpMap.get(groupName);
                        if(tmpList == null){
                            tmpList = new LinkedList<ConfItem>();
                            tmpMap.put(groupName, tmpList);
                        }
                        tmpList.add(confItem);
                    }
                }else{
                    String groupName = "其他";
                    String pageName = "searchReuslt";
                    ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                    confGroupInfo.setPageName(pageName);
                    confGroupInfo.setGroupName(groupName);
                    list.add(confGroupInfo);
                    Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                    if(tmpMap == null){
                        tmpMap = new HashMap<String,List<ConfItem>>();
                        tmpGroupMap.put(pageName,tmpMap);
                    }
                    List<ConfItem> tmpList = tmpMap.get(groupName);
                    if(tmpList == null){
                        tmpList = new LinkedList<ConfItem>();
                        tmpMap.put(groupName, tmpList);
                    }
                    tmpList.add(confItem);
                }
            }
        }

        logger.info(gson.toJson(GroupMap.get("tcga_read").containsKey("组学信息")));
    }

    /**
     * 解析配置文件：seer_read_field.json
     */
    private static void SeerConfigParser() {
        InputStream inputStream1 = ArkService.class.getResourceAsStream("/seer_read_field.json");
        try {
            seerField = FilesUtils.readString(inputStream1, "utf-8");
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }finally {
            try {
                inputStream1.close();
            } catch (IOException e) {
                logger.error("",e);
                throw new RuntimeException();
            }
        }
        Map<String,ConfItem> tmpIndexMap = new HashMap<String, ConfItem>();
        IndexMap.put("seer_read",tmpIndexMap);
        Map<String,Map<String,List<ConfItem>>> tmpGroupMap = new HashMap<String, Map<String, List<ConfItem>>>();
        GroupMap.put("seer_read",tmpGroupMap);
        Map<String,Set<String>> tmpReIndex = new HashMap<String, Set<String>>();
        ReIndexMap.put("seer_read",tmpReIndex);
        JsonObject jsonObject = jsonParser.parse(seerField).getAsJsonObject();
        JsonObject seerJsonObject = jsonObject.getAsJsonObject("seer_read");
        for(Map.Entry<String, JsonElement> seerNameEntry:seerJsonObject.entrySet()){
            JsonObject item = seerNameEntry.getValue().getAsJsonObject();
            ConfItem confItem = new ConfItem();
            String dataType = item.get("dataType").getAsString();
            String IndexFieldName = item.get("IndexFieldName").getAsString();
            String UIFieldName = item.get("UIFieldName").getAsString();
            tmpIndexMap.put(IndexFieldName, confItem);
            Set<String> set = tmpReIndex.get(UIFieldName);
            if(set == null){
                set = new HashSet<String>();
                tmpReIndex.put(UIFieldName,set);
            }
            set.add(IndexFieldName);
            confItem.setDataType(dataType);
            confItem.setIndexFieldName(IndexFieldName);
            confItem.setUIFieldName(UIFieldName);
            List<ConfGroupInfo> list = new LinkedList<ConfGroupInfo>();
            confItem.setGroupInfoList(list);
            if(item.get("groupInfo")!= null && item.get("groupInfo").isJsonArray()){
                JsonArray groupInfoArray = item.getAsJsonArray("groupInfo");
                for(JsonElement groupItem:groupInfoArray){
                    String pageName = groupItem.getAsJsonObject().get("pageName").getAsString();
                    String groupName = groupItem.getAsJsonObject().get("groupName").getAsString();
                    ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                    confGroupInfo.setGroupName(groupName);
                    confGroupInfo.setPageName(pageName);
                    list.add(confGroupInfo);
                    Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                    if(tmpMap == null){
                        tmpMap = new HashMap<String,List<ConfItem>>();
                        tmpGroupMap.put(pageName,tmpMap);
                    }
                    List<ConfItem> tmpList = tmpMap.get(groupName);
                    if(tmpList == null){
                        tmpList = new LinkedList<ConfItem>();
                        tmpMap.put(groupName, tmpList);
                    }
                    tmpList.add(confItem);
                }
            }else{
                String groupName = "其他";
                String pageName = "searchReuslt";
                ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                confGroupInfo.setPageName(pageName);
                confGroupInfo.setGroupName(groupName);
                list.add(confGroupInfo);
                Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                if(tmpMap == null){
                    tmpMap = new HashMap<String,List<ConfItem>>();
                    tmpGroupMap.put(pageName,tmpMap);
                }
                List<ConfItem> tmpList = tmpMap.get(groupName);
                if(tmpList == null){
                    tmpList = new LinkedList<ConfItem>();
                    tmpMap.put(groupName, tmpList);
                }
                tmpList.add(confItem);
            }
        }
        logger.info(gson.toJson(GroupMap.get("seer_read")));
    }

    /**
     * 解析配置文件：CosmicMutantExport_v70.json
     */
    private static void CosmicMutantExportConfigParser(){
        InputStream inputStream3 =  ArkService.class.getResourceAsStream("/CosmicMutantExport_v70.json");
        try {
            CosmicMutantExport_v70 = FilesUtils.readString(inputStream3,"utf-8");
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }finally {
            try {
                inputStream3.close();
            } catch (IOException e) {
                logger.error("",e);
                throw new RuntimeException();
            }
        }

        Map<String,ConfItem> tmpIndexMap = new HashMap<String, ConfItem>();
        IndexMap.put("CosmicMutantExport_v70",tmpIndexMap);
        Map<String,Map<String,List<ConfItem>>> tmpGroupMap = new HashMap<String, Map<String, List<ConfItem>>>();
        GroupMap.put("CosmicMutantExport_v70",tmpGroupMap);
        Map<String,Set<String>> tmpReIndex = new HashMap<String, Set<String>>();
        ReIndexMap.put("CosmicMutantExport_v70",tmpReIndex);
        JsonObject CosmicMutantExportJsonObject = jsonParser.parse(CosmicMutantExport_v70).getAsJsonObject();
        JsonObject jsonObject = CosmicMutantExportJsonObject.getAsJsonObject("CosmicMutantExport_v70");
        for(Map.Entry<String, JsonElement> seerNameEntry:jsonObject.entrySet()){
            JsonObject item = seerNameEntry.getValue().getAsJsonObject();
            ConfItem confItem = new ConfItem();
            String dataType = item.get("dataType").getAsString();
            String IndexFieldName = item.get("IndexFieldName").getAsString();
            String UIFieldName = item.get("UIFieldName").getAsString();
            tmpIndexMap.put(IndexFieldName, confItem);
            Set<String> set = tmpReIndex.get(UIFieldName);
            if(set == null){
                set = new HashSet<String>();
                tmpReIndex.put(UIFieldName,set);
            }
            set.add(IndexFieldName);
            confItem.setDataType(dataType);
            confItem.setIndexFieldName(IndexFieldName);
            confItem.setUIFieldName(UIFieldName);
            List<ConfGroupInfo> list = new LinkedList<ConfGroupInfo>();
            confItem.setGroupInfoList(list);
            if(item.get("groupInfo")!= null && item.get("groupInfo").isJsonArray()){
                JsonArray groupInfoArray = item.getAsJsonArray("groupInfo");
                for(JsonElement groupItem:groupInfoArray){
                    if(groupItem.getAsJsonObject().get("pageName") != null && groupItem.getAsJsonObject().get("groupName")!=null){
                        String pageName = groupItem.getAsJsonObject().get("pageName").getAsString();
                        String groupName = groupItem.getAsJsonObject().get("groupName").getAsString();
                        ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                        confGroupInfo.setGroupName(groupName);
                        confGroupInfo.setPageName(pageName);
                        list.add(confGroupInfo);
                        Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                        if(tmpMap == null){
                            tmpMap = new HashMap<String,List<ConfItem>>();
                            tmpGroupMap.put(pageName,tmpMap);
                        }
                        List<ConfItem> tmpList = tmpMap.get(groupName);
                        if(tmpList == null){
                            tmpList = new LinkedList<ConfItem>();
                            tmpMap.put(groupName, tmpList);
                        }
                        tmpList.add(confItem);
                    }

                }
            }else{
                String groupName = "其他";
                String pageName = "searchReuslt";
                ConfGroupInfo confGroupInfo = new ConfGroupInfo();
                confGroupInfo.setPageName(pageName);
                confGroupInfo.setGroupName(groupName);
                list.add(confGroupInfo);
                Map<String,List<ConfItem>> tmpMap = tmpGroupMap.get(pageName);
                if(tmpMap == null){
                    tmpMap = new HashMap<String,List<ConfItem>>();
                    tmpGroupMap.put(pageName,tmpMap);
                }
                List<ConfItem> tmpList = tmpMap.get(groupName);
                if(tmpList == null){
                    tmpList = new LinkedList<ConfItem>();
                    tmpMap.put(groupName, tmpList);
                }
                tmpList.add(confItem);
            }
        }

    }

    private static Map<String,Map<String,String>> mapConf = new HashMap<String, Map<String, String>>();
    /**
     * 解析部分属性值映射文件
     */
    private static void TcgaMapParser(){
        InputStream inputStream5 =  ArkService.class.getResourceAsStream("/tcga_coadread_field.mapping.json");
        String data = null;
        try {
            data = FilesUtils.readString(inputStream5, "utf-8");
        } catch (IOException e) {
            logger.error("",e);
            throw new RuntimeException();
        }finally {
            try {
                inputStream5.close();
            } catch (IOException e) {
                logger.error("",e);
                throw new RuntimeException();
            }
        }
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("tcga_coadread");
        for(Map.Entry<String, JsonElement> entry:dataObject.entrySet()){
            String key = entry.getKey();
            Map<String,String> tmpMap = mapConf.get(key);
            if(tmpMap == null){
                tmpMap = new HashMap<String, String>();
                mapConf.put(key,tmpMap);
            }
            JsonObject valueObj = entry.getValue().getAsJsonObject();
            for(Map.Entry<String, JsonElement> subEntry:valueObj.entrySet()){
                String subKey = subEntry.getKey();
                String subValue = subEntry.getValue().getAsString();
                tmpMap.put(subKey,subValue);
            }
        }
    }

    public static Map<String, Map<String, String>> getMapConf() {
        return mapConf;
    }

    public static final Map<String, Map<String, ConfItem>> getIndexMap() {
        return IndexMap;
    }

    public static final Map<String, Map<String, Map<String, List<ConfItem>>>> getGroupMap() {
        return GroupMap;
    }

    public static final Map<String, Map<String, Set<String>>> getReIndexMap() {
        return ReIndexMap;
    }

}
