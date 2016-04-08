package com.gennlife.platform.util;

import com.gennlife.platform.bean.crf.DataBean;
import com.gennlife.platform.bean.crf.MongoResultBean;
import com.gennlife.platform.bean.crf.SampleListBean;
import com.gennlife.platform.bean.crf.SummaryBean;
import com.gennlife.platform.enums.MongoCollectionNames;
import com.gennlife.platform.enums.MongoDBNames;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import org.bson.BSONObject;
import org.bson.BsonDocument;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by chen-song on 16/3/19.
 */
public class MongoManager {
    private static Logger logger = LoggerFactory.getLogger(MongoManager.class);
    private static Mongo mongo = null;
    private static String host;
    private static int port;
    private static int poolSize;
    private static int blockSize;
    private  static MongoConf mongoC;
    public static String defaultModel;
    private static DBCollection summaryCollection = null;
    private static DBCollection metaCollection = null;
    private static DBCollection dataCollection = null;
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    public static void init(MongoConf mongoConf){
        mongoC = mongoConf;
        host = mongoConf.getHost();
        port = mongoConf.getPort();
        poolSize = mongoConf.getPoolSize();
        blockSize = mongoConf.getBlockSize();
        mongo = new Mongo(host,port);
        MongoOptions options = mongo.getMongoOptions();
        options.connectionsPerHost = poolSize;
        options.threadsAllowedToBlockForConnectionMultiplier = blockSize;
    }

    /**
     *
     * @param dbName :
     * @return
     * @throws UnknownHostException
     */
    public static DBCollection get(String dbName, String collectionName){
        if(mongo == null){
            init(mongoC);
        }
        DB db = mongo.getDB(dbName);
        return db.getCollection(collectionName);
    }
    public static void initCollection() throws UnknownHostException {
        summaryCollection = MongoManager.get(MongoDBNames.CRFName.getName(), MongoCollectionNames.SummaryName.getName());
        metaCollection = MongoManager.get(MongoDBNames.CRFName.getName(), MongoCollectionNames.MetaName.getName());
        dataCollection = MongoManager.get(MongoDBNames.CRFName.getName(), MongoCollectionNames.CrfDataName.getName());
    }
    /**
     * 关闭mongo连接
     */
    public static void destory(){
        if(mongo != null){
            mongo.close();
        }
    }
    public static void insertItem(String DBName,String collectionName,DBObject dbObject) throws UnknownHostException {
        DBCollection collection = get(DBName,collectionName);
        WriteResult writeResult = collection.insert(dbObject);
    }

    public static String getDefaultModel() {
        return defaultModel;
    }
    public static void setDefaultModel(String defaultM){
        defaultModel = defaultM;
    }




    /**
     * 更新summery数据
     * @param crf_id
     * @param caseID
     * @return
     */
    public static void updateSummaryCaseID(String crf_id, String caseID) {
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("caseID", caseID);
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
        summaryCollection.update(query, updateObj);
    }


    public static void insertNewModel(JsonObject jsonObject) {
        try {
            BsonDocument bsonDocument = BsonDocument.parse(gson.toJson(jsonObject));
            metaCollection.insert(new BasicDBObject(bsonDocument));
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void insertNewSummary(SummaryBean jsonObject) {
        try {
            BsonDocument bsonDocument = BsonDocument.parse(gson.toJson(jsonObject));
            summaryCollection.insert(new BasicDBObject(bsonDocument));
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    public static void insertNewData(String jsonObject) {
        try {
            BsonDocument bsonDocument = BsonDocument.parse(jsonObject);
            dataCollection.insert(new BasicDBObject(bsonDocument));
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static JsonObject getModel(String crf_id) {
        JsonObject resultObject = null;
        try {
            DBObject baseModel = null;
            BasicDBObject query = new BasicDBObject();
            query.put("crf_id", crf_id);//获取模板
            DBCursor cursor = metaCollection.find(query);
            while (cursor.hasNext()) {
                baseModel = cursor.next();
            }
            if(baseModel == null){
                return null;
            }
            MongoResultBean mongoResultBean = gson.fromJson(baseModel.toString(), MongoResultBean.class);
            mongoResultBean.set_id(null);
            MongoManager.setDefaultModel(baseModel.toString());
            resultObject = (JsonObject) jsonParser.parse(gson.toJson(mongoResultBean));
        } catch (Exception e) {
            logger.error("", e);
        }


        return resultObject;
    }

    public static void updateNewModel(String crf_id, List<BSONObject> list) {
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("children", list);
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
        metaCollection.update(query, updateObj);
    }

    public static void updateNewSummary(SummaryBean summaryBean){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", summaryBean.getCrf_id());
        BasicDBObject newDocument = BasicDBObject.parse(gson.toJson(summaryBean));
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
        dataCollection.update(query, updateObj);
    }
    public static void updateNewData(DataBean dataBean){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", dataBean.getCrf_id());
        query.put("caseID", dataBean.getCaseID());
        BasicDBObject newDocument = BasicDBObject.parse(gson.toJson(dataBean));
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
        dataCollection.update(query, updateObj);
    }


    public static JsonObject getCrfData(String crf_id, String caseID) {
        JsonObject data = new JsonObject();
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);//获取项目模板
        query.put("caseID", caseID);//获取项目模板
        DBCursor cursor = dataCollection.find(query);
        DBObject baseModel = null;
        while (cursor.hasNext()) {
            baseModel = cursor.next();
        }
        if (baseModel == null) {
            return null;
        } else {
            if (baseModel.containsKey("children")) {
                DataBean dataBean = gson.fromJson(baseModel.toString(), DataBean.class);
                JsonObject dataObj = (JsonObject) jsonParser.parse(gson.toJson(dataBean));
                JsonArray children = dataObj.getAsJsonArray("children");
                String patientNo = baseModel.get("patientNo").toString();
                String patientName = baseModel.get("patientName").toString();
                data.addProperty("caseNo", patientNo);
                data.addProperty("name", patientNo);
                data.add("children", children);
            }
        }
        return data;
    }


    public static SummaryBean getSummary(String crf_id) {
        SummaryBean summaryBean = null;
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        DBCursor cursor = summaryCollection.find(query);
        DBObject baseModel = null;
        while (cursor.hasNext()) {
            baseModel = cursor.next();
        }
        if (baseModel == null) {
            return null;
        } else {
            summaryBean = gson.fromJson(baseModel.toString(), SummaryBean.class);
        }
        return summaryBean;
    }

    public static SummaryBean getSummaryByProjectID(String projectID) {
        SummaryBean summaryBean = null;
        BasicDBObject query = new BasicDBObject();
        query.put("projectID", projectID);
        DBCursor cursor = summaryCollection.find(query);
        DBObject baseModel = null;
        while (cursor.hasNext()) {
            baseModel = cursor.next();
        }
        if (baseModel == null) {
            return null;
        } else {
            summaryBean = gson.fromJson(baseModel.toString(), SummaryBean.class);
        }
        return summaryBean;
    }

    public static void changeModeStatus(String crf_id){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", "保存完成");
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
        WriteResult w = metaCollection.update(query, updateObj);
        return;
    }


    public static void deleteSample(String crf_id,String caseID){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        query.put("caseID", caseID);
        dataCollection.findAndRemove(query);
        return;
    }

    public static List<SampleListBean> getSampleListData(String crf_id,int start,int pageNo){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        DBCursor cursor = dataCollection.find(query).skip((start - 1) * pageNo).limit(pageNo);
        DBObject baseModel = null;
        List<SampleListBean> list = new LinkedList<SampleListBean>();
        while (cursor.hasNext()) {
            baseModel = cursor.next();
            if(baseModel != null){
                SampleListBean sampleListBean = gson.fromJson(baseModel.toString(), SampleListBean.class);
                list.add(sampleListBean);
            }
        }
        return list;
    }


    public static int getSampleListCount(String crf_id){
        BasicDBObject query = new BasicDBObject();
        query.put("crf_id", crf_id);
        int count = dataCollection.find(query).count();
        return count;
    }

    public static List<SampleListBean> searchSampleList(String crf_id,int start,int pageNo,String key){
        BasicDBObject query = getSampleSearchQuery(crf_id,key);
        DBCursor cursor = dataCollection.find(query).skip((start - 1) * pageNo).limit(pageNo);;
        List<SampleListBean> list = new LinkedList<SampleListBean>();
        DBObject baseModel = null;
        while (cursor.hasNext()){
            baseModel = cursor.next();
            if(baseModel == null){
                continue;
            }
            SampleListBean sampleListBean = gson.fromJson(baseModel.toString(), SampleListBean.class);
            list.add(sampleListBean);

        }
        return list;
    }

    public static int searchSampleListintCount(String crf_id,String key){
        BasicDBObject query = getSampleSearchQuery(crf_id,key);
        int count = dataCollection.find(query).count();;
        return count;
    }

    public static BasicDBObject getSampleSearchQuery(String crf_id,String key){
        BasicDBObject query = new BasicDBObject();
        BasicDBList queryList = new BasicDBList();
        BasicDBObject patientName = new BasicDBObject();
        BasicDBObject regexName = new BasicDBObject();
        regexName.put("$regex",key);
        patientName.put("patientName",regexName);
        BasicDBObject patientNo = new BasicDBObject();
        patientNo.put("patientNo",regexName);
        queryList.add(patientName);
        queryList.add(patientNo);
        query.put("$or",queryList);
        query.put("crf_id",crf_id);
        return query;
    }
}
