package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.crf.DataBean;
import com.gennlife.platform.bean.crf.SampleListBean;
import com.gennlife.platform.bean.crf.SummaryBean;
import com.gennlife.platform.bean.projectBean.MyProjectList;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import com.mongodb.BasicDBObject;
import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-song on 16/3/18.
 */
public class CrfProcessor {
    private static Logger logger = LoggerFactory.getLogger(CrfProcessor.class);
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 返回模板信息
     * 如果crf_id请求参数为空,
     * 去summary结构中查找,
     * 1.如果summary没有该projectID的信息,
     * 构建默认模型数据,放入meta结构中,生成summary数据,放入summary结构中,返回默认模型
     * 2.如果summary中有这个projectID的信息,获取crf_id
     * 去meta中查找,对应模型信息
     * <p>
     * if()
     *
     * @param paramObj
     */
    public String model(JsonObject paramObj) {
        String projectID = null;
        String uid = null;
        try{
            projectID = paramObj.get("projectID").getAsString();
            uid = paramObj.get("uid").getAsString();
        }catch (Exception e){
            logger.error("",e);
            return  ParamUtils.errorParam("参数错误");
        }
        Map<String, Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID", projectID);
        String category = AllDao.getInstance().getProjectDao().getProjectDisease(confMap);
        if(category == null){
            category = "5";//默认模板类别
        }
        String crf_id = null;
        String crf_name = null;
        if (paramObj.get("crf_name") != null) {
            crf_name = paramObj.get("crf_name").getAsString();
        }
        if(!paramObj.has("crf_id")||"".equals(paramObj.get("crf_id").getAsString())){//没有传crf_id或者传递无效crf_id
            SummaryBean summary = MongoManager.getSummaryByProjectID(projectID);//获取当前项目summary数据
            if (summary ==null || summary.getCrf_id() == null) {//该项目还没有模板
                Set<String> users = new HashSet<String>();
                //获取项目成员
                List<SyUser> memberList = AllDao.getInstance().getSyUserDao().getProjectMemberSList(confMap);
                for (SyUser syUser : memberList) {
                    users.add(syUser.getUid());
                }
                if (users.contains(uid)) {//当前操作者是项目成员
                    //获取默认模板
                    JsonObject modelObj = getDefaultModel(category);//通过默认模板生成新的默认模板
                    //插入mongodb数据库
                    MongoManager.insertNewModel(modelObj);
                    crf_id = modelObj.get("crf_id").getAsString();
                    //生成SummaryBean ,插入数据库
                    String t = time.format(new Date());
                    SummaryBean summaryBean = new SummaryBean();
                    summaryBean.setCrf_id(crf_id);
                    summaryBean.setCreateTime(t);
                    summaryBean.setCrfName(crf_name + "");
                    summaryBean.setUid(uid);
                    summaryBean.setProjectID(projectID);
                    summaryBean.setCrfStatus("创建");
                    summaryBean.setUsers(users);
                    summaryBean.setLastTime(t);
                    MongoManager.insertNewSummary(summaryBean);
                    modelObj = ModelTreeUtils.updateUIData(modelObj);
                    return gson.toJson(modelObj);//返回默认的模板
                }else {//当前用户不是这个项目成员,无法操作
                    return ParamUtils.errorParam("当前用户不是这个项目成员,无法操作");
                }
            }else {//该项目有模板,从数据库中获取的crf_id不空
                //获取模板数据
                JsonObject modelObj = MongoManager.getModel(summary.getCrf_id());
                modelObj = ModelTreeUtils.updateUIData(modelObj);
                return gson.toJson(modelObj);
            }
        }else {//前端请求的crf不空
            crf_id = paramObj.get("crf_id").getAsString();
            JsonObject modelObj = MongoManager.getModel(crf_id);
            modelObj = ModelTreeUtils.updateUIData(modelObj);
            return gson.toJson(modelObj);
        }
    }




    /**
     * 生成默认的模板
     *
     * @param category
     * @return
     */
    public static JsonObject getDefaultModel(String category) {
        JsonObject resultObject = null;
        resultObject = MongoManager.getModel(category);
        resultObject = (JsonObject) jsonParser.parse(gson.toJson(resultObject));
        resultObject.remove("_id");
        UUID uuid = UUID.randomUUID();
        String crf_id = category + "_" + uuid;//生成新的模板
        resultObject.addProperty("crf_id", crf_id);
        return resultObject;
    }


    /**
     * 筛选属性后,修改模型中的select属性
     * @param paramObj
     * @return
     */
    public String selectAttr(JsonObject paramObj) {
        String projectID = null;
        String crf_id = null;
        JsonArray attrsArray = null;
        try{
            projectID = paramObj.get("projectID").getAsString();
            crf_id = paramObj.get("crf_id").getAsString();
            attrsArray = paramObj.get("attrs").getAsJsonArray();
        }catch (Exception e){
            logger.error("",e);
            return  ParamUtils.errorParam("参数错误");
        }
        //检验projectID与crf_id对应关系
        SummaryBean summaryBean = MongoManager.getSummaryByProjectID(projectID);
        if(summaryBean == null){
            String err = "请求参数projectID与crf_id不对应,prjectID=" + projectID + " 对应的crf_id为空";
            return ParamUtils.errorParam(err);
        }else if(!crf_id.equals(summaryBean.getCrf_id())){
            String err = "请求参数projectID与crf_id不对应,prjectID=" + projectID + " 对应的crf_id=" + summaryBean.getCrf_id();
            return ParamUtils.errorParam(err);
        }else{
            //获取这个crf_id对应的model数据
            JsonObject modelJsonObject = MongoManager.getModel(crf_id);
            //获取选中的select的attrid
            Set<String> attrs = new HashSet<String>();
            for (JsonElement entity : attrsArray) {
                String attrID = entity.getAsString();
                attrs.add(attrID);
            }
            JsonArray dataObj = modelJsonObject.getAsJsonArray("data");
            JsonArray newDataObj = new JsonArray();
            List<BSONObject> list = new LinkedList<BSONObject>();
            for (JsonElement entity : dataObj) {
                JsonObject obj = ModelTreeUtils.traversalSelect(null, entity.getAsJsonObject(), attrs);
                newDataObj.add(obj);
                list.add(BasicDBObject.parse(gson.toJson(obj)));
            }
            //生成新的数据模型
            modelJsonObject.add("children", newDataObj);
            //将新的模型数据存入mongo
            MongoManager.updateNewModel(crf_id, list);
            modelJsonObject = ModelTreeUtils.updateUIData(modelJsonObject);
            return gson.toJson(modelJsonObject);
        }
    }


    /**
     * 增加属性组
     *
     * @param paramObj
     */
    public String addGroup(JsonObject paramObj) {
        String crf_id = null;
        String groupName = null;
        String parentsID = null;
        boolean isPackage = false;
        String addUser = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            addUser = paramObj.get("uid").getAsString();
            groupName = paramObj.get("groupName").getAsString();
            if (paramObj.get("package") != null && paramObj.get("package").getAsInt() == 1) {
                isPackage = true;
            }
            parentsID = paramObj.get("parentsID") == null ? "Root" : paramObj.get("parentsID").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if (crf_id == null || groupName == null || parentsID == null) {
            return ParamUtils.errorParam("缺少请求参数出错");
        }
        //获取这个crf_id对应的model数据
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if (modelJsonObject == null) {
            return ParamUtils.errorParam("crf_id:" + crf_id + "不存在");
        }
        JsonArray dataObj = modelJsonObject.getAsJsonArray("children");
        Set<String> RootIDs = new HashSet<String>();
        for(JsonElement entity:dataObj){
            RootIDs.add(entity.getAsJsonObject().get("id").getAsString());
        }
        if("Root".equals(parentsID) && RootIDs.contains(groupName)){
            String err = "无法创建重名的属性组";
            return ParamUtils.errorParam(err);
        }
        JsonObject newGroup = new JsonObject();
        newGroup.addProperty("name", groupName);
        newGroup.addProperty("isParent", true);
        newGroup.addProperty("addUser",addUser);
        if (isPackage) {
            newGroup.addProperty("package", 1);
        }
        newGroup.add("children", new JsonArray());

        JsonArray newDataObj = new JsonArray();
        if ("Root".equals(parentsID)) {
            newGroup.addProperty("rankID", dataObj.size());
            newGroup.addProperty("id", groupName);
        } else {
            newGroup.addProperty("id", parentsID + "_" + groupName);
        }
        List<BSONObject> list = new LinkedList<BSONObject>();
        if("Root".equals(parentsID)){//最底层添加
            for (JsonElement entity : dataObj) {
                JsonObject obj = entity.getAsJsonObject();
                newDataObj.add(obj);
                list.add(BasicDBObject.parse(gson.toJson(obj)));
            }
            newDataObj.add(newGroup);
            list.add(BasicDBObject.parse(gson.toJson(newGroup)));
        }else{
            for (JsonElement entity : dataObj) {
                JsonObject obj = ModelTreeUtils.traversalAddGroup(parentsID, entity.getAsJsonObject(), newGroup);
                newDataObj.add(obj);
                list.add(BasicDBObject.parse(gson.toJson(obj)));
            }
        }
        //生成新的数据模型
        modelJsonObject.add("children", newDataObj);
        //将新的模型数据存入mongo
        MongoManager.updateNewModel(crf_id, list);
        //
        modelJsonObject = ModelTreeUtils.updateUIData(modelJsonObject);
        return gson.toJson(modelJsonObject);
    }



    /**
     * 增加属性
     *
     * @param paramObj
     */
    public String addAttr(JsonObject paramObj) {
        String crf_id = null;
        String cNName = null;
        String uIType = null;
        JsonObject valueMap = null;
        String eNName = null;
        String dataType = null;
        String groupID = "Root";
        String uid = null;
        //结果
        String data = null;
        try {
            cNName = paramObj.get("cNName").getAsString();
            groupID = paramObj.get("id").getAsString();
            if (paramObj.get("eNName") != null) {
                eNName = paramObj.get("eNName").getAsString();
            }
            crf_id = paramObj.get("crf_id").getAsString();
            uIType = paramObj.get("uIType").getAsString();
            dataType = paramObj.get("dataType").getAsString();
            if (paramObj.get("valueMap") != null) {
                valueMap = paramObj.get("valueMap").getAsJsonObject();
            }
            uid = paramObj.get("uid").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if (cNName == null || uIType == null || dataType == null || groupID == null) {
            logger.error("请求参数出错");
            return ParamUtils.errorParam("缺少请求参数");
        }
        //生成一个属性节点
        JsonObject newAttr = new JsonObject();
        newAttr.addProperty("name", cNName);
        newAttr.addProperty("uIType", uIType);
        newAttr.addProperty("dataType", dataType);
        newAttr.addProperty("addUser",uid);
        if (valueMap != null) {
            newAttr.add("valueMap", valueMap);
        }
        newAttr.addProperty("checked", true);
        //获取这个crf_id对应的model数据
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if (modelJsonObject == null) {
            return ParamUtils.errorParam("crf_id:" + crf_id + "不存在");
        }
        //获取模型已经存在的attrID
        Set<String> existSet = new HashSet<String>();
        System.out.println("existSet" + gson.toJson(existSet));
        getAttrIDs(modelJsonObject, existSet);
        //给的英文名称不可用
        if (eNName == null || "".equals(eNName) || existSet.contains(eNName)) {
            String newAttrID = ChineseToEnglish.getPingYin(cNName);
            if (!existSet.contains(newAttrID)) {
                newAttr.addProperty("attrID", newAttrID);
            }
            int count = 1;
            while (true) {//加后缀查找,没有被占用的attrID
                if (!existSet.contains(newAttrID + "_" + count)) {
                    newAttr.addProperty("attrID", newAttrID + "_" + count);
                    break;
                } else {
                    count++;
                }
            }
        } else {
            newAttr.addProperty("attrID", eNName);
        }
        JsonArray dataObj = modelJsonObject.getAsJsonArray("children");
        JsonArray newDataObj = new JsonArray();
        List<BSONObject> list = new LinkedList<BSONObject>();
        for (JsonElement entity : dataObj) {
            JsonObject obj = entity.getAsJsonObject();
            obj = ModelTreeUtils.traversalAddGroup(groupID, obj, newAttr);
            newDataObj.add(obj);
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        //生成新的数据模型
        modelJsonObject.add("children", newDataObj);
        //将新的模型数据存入mongo
        MongoManager.updateNewModel(crf_id, list);
        modelJsonObject = ModelTreeUtils.updateUIData(modelJsonObject);
        return gson.toJson(modelJsonObject);
    }


    /**
     * @param model:模型
     * @param set:调用时,放入空的集合,代用完成后,set返回该模型中,attrID的集合
     */
    private static void getAttrIDs(JsonObject model, Set<String> set) {
        if (model != null && model.get("crf_id") != null) {
            JsonArray array = model.getAsJsonArray("children");
            for (JsonElement obj : array) {
                getAttrIDs(obj.getAsJsonObject(), set);
            }
        }
        if (model.get("attrID") == null) {//当前是组
            JsonArray data = model.getAsJsonArray("children");
            for (JsonElement entity : data) {
                JsonObject obj = entity.getAsJsonObject();
                getAttrIDs(obj, set);
            }
        } else {//当前是属性
            if (model != null && !model.isJsonNull() && model.isJsonObject() && model.get("attrID") != null) {
                String attrID = model.get("attrID").getAsString();
                set.add(attrID);
            }

        }
    }


    /**
     * 保存定义好的数据模型
     *
     * @param paramObj
     */
    public String editModel(JsonObject paramObj) {
        String crf_id = null;
        JsonArray children = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            children = paramObj.getAsJsonArray("children");
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        List<BSONObject> list = new LinkedList<BSONObject>();
        for (JsonElement entity : children) {
            JsonObject obj = entity.getAsJsonObject();
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        MongoManager.updateNewModel(crf_id,list);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("保存");
        return gson.toJson(resultBean);

    }

    /**
     * 将模型状态从创建状态,改成保存完成
     * @param paramObj
     */
    public String saveModel(JsonObject paramObj) {
        String crf_id = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        MongoManager.changeModeStatus(crf_id);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("保存成功");
        return gson.toJson(resultBean);
    }

    public String getData(JsonObject paramObj) {
        String crf_id = null;
        String caseID = null;
        JsonObject data = new JsonObject();//最后的返回结果
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            if (paramObj.has("caseID")) {
                caseID = paramObj.get("caseID").getAsString();
            }
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");

        }
        data.addProperty("code",1);
        data.addProperty("crf_id", crf_id);
        SummaryBean summaryBean = MongoManager.getSummary(crf_id);
        if (caseID == null || "".equals(caseID)) {
            if (summaryBean == null) {
                String err = "crf_id 对应的模型数据为空";
                return ParamUtils.errorParam(err);
            }
            UUID uuid = UUID.randomUUID();
            caseID = uuid + "";
            //更新summary结构体
            MongoManager.updateSummaryCaseID(crf_id, caseID);
            data.addProperty("caseID", caseID);
            data.add("children", new JsonArray());
            return gson.toJson(data);
        }else {
            JsonObject d = MongoManager.getCrfData(crf_id, caseID);
            data.addProperty("caseID", caseID);
            if (d == null || !d.has("children") || !d.get("children").isJsonArray()) {
                data.add("children", new JsonArray());
            } else {
                JsonArray children = d.get("children").getAsJsonArray();
                data.add("children", children);
            }
            return gson.toJson(data);

        }

    }

    public String upLoadData(JsonObject paramObj) {
        String crf_id = null;
        String caseID = null;
        JsonArray children = null;
        JsonObject data = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            caseID = paramObj.get("caseID").getAsString();
            children = paramObj.get("children").getAsJsonArray();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");

        }
        JsonObject existData = MongoManager.getCrfData(crf_id, caseID);
        String t = time.format(new Date());
        if (existData == null) {//数据collection中没有这个caseID的数据
            data = new JsonObject();
            data.addProperty("crf_id", crf_id);
            data.addProperty("caseID", caseID);
            data.add("children", children);
            String name = ModelTreeUtils.getUpdateForName(children);
            if(name != null){
                data.addProperty("patientName", name);
            }
            Date today = new Date();
            String todayStr = time.format(today).substring(0,10);
            data.addProperty("createTime",todayStr);
            data.addProperty("status","录入中");
            SummaryBean summaryBean = MongoManager.getSummary(crf_id);
            if(summaryBean == null){//还没有这个crf_id 对应数据,出错
                return ParamUtils.errorParam("没有这个crf_id 对应数据summary,出错");
            }
            Integer maxCaseNo = summaryBean.getMaxCaseNo();
            data.addProperty("patientNo", maxCaseNo+1+"");
            summaryBean.setMaxCaseNo(maxCaseNo+1);
            summaryBean.setLastTime(t);
            MongoManager.updateNewSummary(summaryBean);
            String dataObj = gson.toJson(data);
            MongoManager.insertNewData(dataObj);

        }else{//已经插入一部分,这部分是更新同名的组信息
            JsonArray jsonArr = existData.getAsJsonArray("children");
            String name = ModelTreeUtils.getUpdateForName(children);
            JsonArray newChildren = new JsonArray();
            Map<String,JsonObject> map = new LinkedHashMap<String, JsonObject>();
            //现将原有的数据放入map
            mergeNewData(jsonArr,map);
            //将提交上来的数据放入map,新覆盖旧
            mergeNewData(children,map);
            for(String key:map.keySet()){
                newChildren.add(map.get(key));
            }
            existData.addProperty("crf_id", crf_id);
            existData.addProperty("caseID", caseID);
            if(name != null){
                existData.addProperty("patientName", name);
            }
            existData.add("children",newChildren);
            existData.remove("_id");
            DataBean dataBean = gson.fromJson(gson.toJson(existData), DataBean.class);
            MongoManager.updateNewData(dataBean);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("上传数据成功");
        return gson.toJson(resultBean);
    }
    private static void mergeNewData(JsonArray jsonArray,Map<String,JsonObject> map){
        for(JsonElement en:jsonArray){
            JsonObject group = en.getAsJsonObject();
            String id = group.get("id") != null? group.get("id").getAsString():null;
            if(id != null){
                map.put(id,group);
            }
        }
    }

    /**
     * 更新组名称
     * @param paramObj
     */
    public String updateGroupName(JsonObject paramObj) {
        String crf_id = null;
        String groupID = null;
        String groupName = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            groupID = paramObj.get("id").getAsString();
            groupName = paramObj.get("groupName").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if(modelJsonObject == null){
            String err = "crf_id 对应模型 为空";
            return ParamUtils.errorParam(err);
        }
        ModelTreeUtils.traversalUpdateGroupName(modelJsonObject,groupID,groupName);
        JsonArray children = modelJsonObject.get("children").getAsJsonArray();
        List<BSONObject> list = new LinkedList<BSONObject>();
        for(JsonElement ent:children){
            JsonObject obj = ent.getAsJsonObject();
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        MongoManager.updateNewModel(crf_id,list);
        modelJsonObject = ModelTreeUtils.updateUIData(modelJsonObject);
        return gson.toJson(modelJsonObject);
    }

    /**
     * crf数据录入完成后,点击录入完成出发的接口,请求参数crf_id,caseID,返回crf_id,新的caseID,空的数据格式
     * @param paramObj
     */
    public String saveData(JsonObject paramObj) {
        String crf_id = null;
        String caseID = null;
        JsonObject data = new JsonObject();
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            caseID = paramObj.get("caseID").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        SummaryBean summary = MongoManager.getSummary(crf_id);
        if(summary == null ){
            String err = "没有对应crf_id的CRF数据";
            return ParamUtils.errorParam(err);
        }
        MongoManager.updateNewDataStatus(crf_id,caseID,"录入完成");
        UUID uuid = UUID.randomUUID();
        String newCaseID = uuid +"";
        summary.setCaseID(newCaseID);
        summary.setMaxCaseNo(summary.getMaxCaseNo()+1);
        MongoManager.updateNewSummary(summary);
        data.addProperty("code",1);
        data.addProperty("crf_id",crf_id);
        data.addProperty("caseID",newCaseID);
        return gson.toJson(data);
    }

    /**
     * 返回没有叶子节点的模型树
     * @param paramObj
     */
    public String modelTree(JsonObject paramObj) {
        String crf_id = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");

        }
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if(modelJsonObject == null){
            String err = "没有crf_id:"+crf_id +"对应的模型数据";
            return ParamUtils.errorParam(err);
        }
        JsonObject modelTree = ModelTreeUtils.traversalCutDownLeaves(modelJsonObject);
        return gson.toJson(modelTree);
    }

    /**
     *返回病历列表数据
     * @param paramObj
     */
    public String sampleCaseList(JsonObject paramObj) {
        String crf_id = null;
        String limit = null;
        int[] result = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            limit = paramObj.get("limit").getAsString();
            result = ParamUtils.parseLimit(limit);
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        List<SampleListBean> list = MongoManager.getSampleListData(crf_id,result[0],result[1]);
        int count = MongoManager.getSampleListCount(crf_id);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("count",count);
        resultBean.setInfo(map);
        return gson.toJson(resultBean);
    }

    public String deleteSample(JsonObject paramObj) {
        String crf_id = null;
        Set<String> caseIDSet = new HashSet<String>();
        String key = null;
        String limit = null;
        int[] result = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            JsonArray caseIDArray = paramObj.get("caseIDSet").getAsJsonArray();
            key = paramObj.get("key").getAsString();
            limit = paramObj.get("limit").getAsString();
            result = ParamUtils.parseLimit(limit);
            for(JsonElement entity:caseIDArray){
                String caseID = entity.getAsString();
                caseIDSet.add(caseID);
            }
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        if(caseIDSet.size() == 0){
            String err = "删除caseID为空";
            return ParamUtils.errorParam(err);
        }
        for(String caseID:caseIDSet){
            MongoManager.deleteSample(crf_id,caseID);
        }
        List<SampleListBean> list = MongoManager.searchSampleList(crf_id,result[0],result[1],key);
        int count = MongoManager.getSampleListCount(crf_id);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("count",count);
        resultBean.setInfo(map);
        return gson.toJson(resultBean);
    }

    public String searchSampleList( JsonObject paramObj) {
        String crf_id = null;
        String key = null;
        String limit = null;
        int[] result = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            key = paramObj.get("key").getAsString();
            limit = paramObj.get("limit").getAsString();
            result = ParamUtils.parseLimit(limit);
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        List<SampleListBean> list = MongoManager.searchSampleList(crf_id,result[0],result[1],key);
        int count = MongoManager.searchSampleListintCount(crf_id,key);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("count",count);
        resultBean.setInfo(map);
        return gson.toJson(resultBean);
    }


    public String deleteGroup(JsonObject paramObj) {
        String crf_id = null;
        String groupID = null;
        try {
            crf_id = paramObj.get("crf_id").getAsString();
            groupID = paramObj.get("id").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");

        }
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if(modelJsonObject == null){
            String err = "crf_id 对应模型 为空";
            return ParamUtils.errorParam(err);
        }
        String parentID = getParentID(groupID);
        ModelTreeUtils.traversalDeleteGroup(modelJsonObject,groupID,parentID);
        JsonArray children = modelJsonObject.get("children").getAsJsonArray();
        List<BSONObject> list = new LinkedList<BSONObject>();
        for(JsonElement ent:children){
            JsonObject obj = ent.getAsJsonObject();
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        MongoManager.updateNewModel(crf_id,list);
        modelJsonObject = ModelTreeUtils.updateUIData(modelJsonObject);
        return gson.toJson(modelJsonObject);
    }
    private static String getParentID(String id){
        String[] groups = id.split("_");
        if(groups.length == 1){
            return id;
        }else {
            String parentID = "";
            for(int i=0;i<groups.length -1;i++){
                if("".equals(parentID)){
                    parentID = groups[i];
                }else {
                    parentID = parentID + "_" + groups[i];
                }
            }
            return parentID;
        }
    }

    /**
     * 用户相关项目的crf模版列表
     * @param paramObj
     * @return
     */
    public String projectCrfList(JsonObject paramObj) {
        String uid = null;
        try {
            uid = paramObj.get("uid").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
        Map<String,Object> conf = new HashMap<>();
        conf.put("uid",uid);
        try {
            List<MyProjectList> list = AllDao.getInstance().getSyUserDao().getProjectList(conf);
            List<JsonObject> paramList = new LinkedList<>();
            for(MyProjectList myProjectList:list){
                JsonObject newParamObj = new JsonObject();
                String projectID = myProjectList.getProjectID();
                String projectName = myProjectList.getProjectName();
                newParamObj.addProperty("projectID",projectID);
                newParamObj.addProperty("projectName",projectName);
                paramList.add(newParamObj);
            }
            logger.info("请求CRF Service ProjectCRFList接口参数="+gson.toJson(paramList));
            String url = ConfigurationService.getUrlBean().getCRFProjectCRFListURL();
            String result = HttpRequestUtils.httpPost(url,gson.toJson(paramList));
            return result;
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }

    }
}
