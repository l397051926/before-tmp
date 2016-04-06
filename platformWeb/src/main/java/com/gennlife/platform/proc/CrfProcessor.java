package com.gennlife.platform.proc;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.bean.crf.DataBean;
import com.gennlife.platform.bean.crf.MongoResultBean;
import com.gennlife.platform.bean.crf.SampleListBean;
import com.gennlife.platform.bean.crf.SummaryBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.util.ChineseToEnglish;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.MongoManager;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import com.google.gson.internal.ObjectConstructor;
import com.mongodb.*;
import org.apache.tools.ant.util.XMLFragment;
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
    private static View viewer = new View();


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
     * @param request
     * @param resps
     */
    public void model(HttpServletRequest request, HttpServletResponse resps) {
        String param = ParamUtils.getParam(request);
        String category = "5";//默认模板类别
        String crf_id = null;
        logger.info("model =" + param);
        ResultBean resultBean = new ResultBean();
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        String projectID = paramObj.get("projectID").getAsString();
        String crf_name = null;
        if (paramObj.get("crf_name") != null) {
            crf_name = paramObj.get("crf_name").getAsString();
        }
        if (paramObj.get("category") != null) {
            category = paramObj.get("category").getAsString();
        }
        Map<String, Object> confMap = new HashMap<String, Object>();
        confMap.put("projectID", projectID);
        String uid = paramObj.get("uid").getAsString();
        String data = null;
        if (paramObj.get("crf_id") == null || "".equals(paramObj.get("crf_id").getAsString())) {//客户端还不知道,crf_id
            SummaryBean summary = MongoManager.getSummaryByProjectID(projectID);//去summary中查找该项目是否已经存在模板数据
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
                    MongoManager.insertNewModel(modelObj);
                    crf_id = modelObj.get("crf_id").getAsString();
                    //SummaryBean
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
                    data = gson.toJson(modelObj);
                    viewer.viewString(data, resps, request);
                    return;
                } else {//当前用户不是这个项目成员,无法操作
                    resultBean.setCode(0);
                    resultBean.setInfo("当前用户不是这个项目成员,无法操作");
                    data = gson.toJson(resultBean);
                    viewer.viewString(data, resps, request);
                    return;
                }
            } else {//该项目有模板,从数据库中获取的crf_id不空
                JsonObject modelObj = MongoManager.getModel(summary.getCrf_id());
                data = gson.toJson(modelObj);
                viewer.viewString(data, resps, request);
                return;
            }

        } else {//前端请求的crf不空
            crf_id = paramObj.get("crf_id").getAsString();
            JsonObject modelObj = MongoManager.getModel(crf_id);
            data = gson.toJson(modelObj);
            viewer.viewString(data, resps, request);
            return;
        }


    }

    /**
     * 递归遍历模型,将set<attrID>的属性为勾选状态,如果set为空,将全部属性勾选
     *
     * @param parent
     * @param obj
     * @param set
     * @return
     */
    public static JsonObject traversalSelect(String parent, JsonObject obj, Set<String> set) {
        if (obj == null || obj.isJsonNull()) {
            return null;
        }
        if (obj.get("attrID") == null) {//组
            String id = obj.get("id").getAsString();
            JsonArray jsonArray = obj.getAsJsonArray("children");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonObject = jsonArray.get(i);
                traversalSelect(id, jsonObject.getAsJsonObject(), set);
            }
        } else {
            if (set == null) {
                obj.addProperty("checked", true);
            } else {
                String attrID = obj.get("attrID").getAsString();
                if (set.contains(attrID)) {
                    obj.addProperty("checked", true);
                } else {
                    if ("autoId".equals(attrID) || "name".equals(attrID)) {
                        obj.addProperty("checked", true);
                    } else {
                        obj.addProperty("checked", false);
                    }
                }
            }
        }
        return obj;
    }

    /**
     * 递归遍历模型,将新定义组或者属性的信息放入响应的位置
     *
     * @param parent:父节点ID
     * @param obj,当前的模型节点
     * @param group,新的组,或者属性
     * @return
     */
    public static JsonObject traversalAddGroup(String parent, JsonObject obj, JsonObject group) {
        if (obj == null || obj.isJsonNull()) {
            return null;
        }
        if (obj.get("attrID") == null) {//当前是组
            String id = obj.get("id").getAsString();
            JsonArray jsonArray = obj.getAsJsonArray("children");
            if (id.equals(parent)) {//找到父节点
                int rankID = jsonArray.size();
                group.addProperty("rankID", rankID);
                jsonArray.add(group);
            } else {//
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement jsonObject = jsonArray.get(i);
                    traversalAddGroup(parent, jsonObject.getAsJsonObject(), group);
                }
            }
        } else {//当前是属性,什么也不做

        }
        return obj;
    }

    /**
     * 递归查找,未被选中的属性,
     *
     * @param obj,当前节点
     * @param set,保存选中的节点的路径
     * @return
     */
    public static JsonObject traversalSelectAttr(String parent, JsonObject obj, Set<String> set) {
        if (obj == null || obj.isJsonNull()) {
            return null;
        }
        if (obj.get("attrID") == null) {//当前是组
            String id = obj.get("id").getAsString();
            JsonArray jsonArray = obj.getAsJsonArray("children");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonObject = jsonArray.get(i);
                traversalSelectAttr(id, jsonObject.getAsJsonObject(), set);
            }
        } else {//当前是属性
            if (obj.get("checked") != null && obj.get("checked").getAsBoolean()) {
                set.add(parent + "_" + obj.get("attrID").getAsString());
            }
        }
        return obj;
    }


    /**
     *深度优先遍历去除掉叶子节点
     * @param parent
     * @return
     */
    public static JsonObject traversalCutDownLeaves(JsonObject parent){
        if(parent == null||parent.isJsonNull()){
            return null;
        }
        if(parent.get("attrID") != null || parent.get("children") == null || parent.get("children").isJsonNull()){
            return null;
        }
        JsonArray children = parent.get("children").getAsJsonArray();
        JsonArray newChildren = new JsonArray();
        for(JsonElement child:children){
            JsonObject childObj = child.getAsJsonObject();
            if(childObj.get("attrID") == null){//是组
                JsonObject newchildObj = traversalCutDownLeaves(childObj);
                if(newchildObj != null){
                    newChildren.add(newchildObj);
                }
            }
        }
        if(newChildren.size() == 0){
            parent.remove("children");
        }else{
            parent.add("children",newChildren);
        }

        return parent;
    }




    /**
     * 生成默认的模板
     *
     * @param category
     * @return
     */
    public static JsonObject getDefaultModel(String category) {
        MongoResultBean mongoResultBean = null;
        JsonObject resultObject = null;
        if (MongoManager.getDefaultModel() == null) {
            resultObject = MongoManager.getModel(category);
            MongoManager.setDefaultModel(gson.toJson(resultObject));
            resultObject = (JsonObject) jsonParser.parse(gson.toJson(resultObject));
        } else {
            mongoResultBean = gson.fromJson(MongoManager.getDefaultModel(), MongoResultBean.class);
            resultObject = (JsonObject) jsonParser.parse(gson.toJson(mongoResultBean));
        }
        UUID uuid = UUID.randomUUID();
        String crf_id = category + "_" + uuid;//生成新的模板
        resultObject.addProperty("crf_id", crf_id);
        return resultObject;
    }




    /**
     * 筛选属性后,修改模型中的select属性
     *
     * @param req
     * @param resp
     */
    public void selectAttr(HttpServletRequest req, HttpServletResponse resp) {
        String projectID = null;
        String crf_id = null;
        JsonArray attrsArray = null;
        String data = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("selectAttr =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            projectID = paramObj.get("projectID").getAsString();
            crf_id = paramObj.get("crf_id").getAsString();
            attrsArray = paramObj.get("attrs").getAsJsonArray();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        //检验projectID与crf_id对应关系
        SummaryBean summaryBean = MongoManager.getSummaryByProjectID(projectID);
        if(summaryBean == null){
            String err = "请求参数projectID与crf_id不对应,prjectID=" + projectID + " 对应的crf_id为空";
            logger.error(err);
            errorParam(err, req, resp);
            return;
        }
        if (!crf_id.equals(summaryBean.getCrf_id())) {
            String err = "请求参数projectID与crf_id不对应,prjectID=" + projectID + " 对应的crf_id=" + summaryBean.getCrf_id();
            logger.error(err);
            errorParam(err, req, resp);
            return;
        }
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
            JsonObject obj = traversalSelect(null, entity.getAsJsonObject(), attrs);
            newDataObj.add(obj);
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        //生成新的数据模型
        modelJsonObject.add("children", newDataObj);
        //将新的模型数据存入mongo
        MongoManager.updateNewModel(crf_id, list);
        data = gson.toJson(modelJsonObject);
        viewer.viewString(data, resp, req);
    }



    /**
     * 增加属性组
     *
     * @param req
     * @param resp
     */
    public void addGroup(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String groupName = null;
        String parentsID = null;
        boolean isPackage = false;
        String data = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("addGroup =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            groupName = paramObj.get("groupName").getAsString();
            if (paramObj.get("package") != null && paramObj.get("package").getAsInt() == 1) {
                isPackage = true;
            }
            parentsID = paramObj.get("parentsID") == null ? "Root" : paramObj.get("parentsID").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        if (crf_id == null || groupName == null || parentsID == null) {
            errorParam("缺少请求参数出错", req, resp);
            return;
        }
        //获取这个crf_id对应的model数据
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if (modelJsonObject == null) {
            errorParam("crf_id:" + crf_id + "不存在", req, resp);
            return;
        }
        JsonArray dataObj = modelJsonObject.getAsJsonArray("children");

        JsonObject newGroup = new JsonObject();
        newGroup.addProperty("name", groupName);
        newGroup.addProperty("isParent", true);
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
        for (JsonElement entity : dataObj) {
            JsonObject obj = null;
            if (!"Root".equals(parentsID)) {
                obj = traversalAddGroup(parentsID, entity.getAsJsonObject(), newGroup);
            } else {
                obj = entity.getAsJsonObject();
            }
            newDataObj.add(obj);
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        //生成新的数据模型
        modelJsonObject.add("children", newDataObj);
        //将新的模型数据存入mongo
        MongoManager.updateNewModel(crf_id, list);
        data = gson.toJson(modelJsonObject);
        viewer.viewString(data, resp, req);
    }

    /**
     * 增加属性
     *
     * @param req
     * @param resp
     */
    public void addAttr(HttpServletRequest req, HttpServletResponse resp) {
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
            String param = ParamUtils.getParam(req);
            logger.info("addAttr =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            cNName = paramObj.get("cNName").getAsString();
            groupID = paramObj.get("groupID").getAsString();
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
            errorParam("请求参数出错", req, resp);
            return;
        }
        if (cNName == null || uIType == null || dataType == null || groupID == null) {
            logger.error("请求参数出错");
            errorParam("缺少请求参数", req, resp);
            return;
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
            errorParam("crf_id:" + crf_id + "不存在", req, resp);
            return;
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
            obj = traversalAddGroup(groupID, obj, newAttr);
            newDataObj.add(obj);
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        //生成新的数据模型
        modelJsonObject.add("children", newDataObj);
        //将新的模型数据存入mongo
        MongoManager.updateNewModel(crf_id, list);
        data = gson.toJson(modelJsonObject);
        viewer.viewString(data, resp, req);

    }

    private static void errorParam(String info, HttpServletRequest req, HttpServletResponse resp) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setInfo(info);
        String data = gson.toJson(resultBean);
        viewer.viewString(data, resp, req);
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
     * @param req
     * @param resp
     */
    public void editModel(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        JsonObject paramObj = null;
        JsonArray children = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("editModel =" + param);
            paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            children = paramObj.getAsJsonArray("children");
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        List<BSONObject> list = new LinkedList<BSONObject>();
        for (JsonElement entity : children) {
            JsonObject obj = entity.getAsJsonObject();
            list.add(BasicDBObject.parse(gson.toJson(obj)));
        }
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        String status = modelJsonObject.get("status").getAsString();
        if ("创建".equals(status)) {
            MongoManager.updateNewModel(crf_id,list);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("保存成功");
            String data = gson.toJson(resultBean);
            viewer.viewString(data, resp, req);
        } else {
            errorParam("当前crf_id对应的模板已经保存,不可以再次编辑", req, resp);
            return;
        }


    }

    /**
     * 将模型状态从创建状态,改成保存完成
     * @param req
     * @param resp
     */
    public void saveModel(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        JsonObject paramObj = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("saveModel =" + param);
            paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        MongoManager.changeModeStatus(crf_id);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("保存成功");
        String data = gson.toJson(resultBean);
        viewer.viewString(data, resp, req);

    }

    public void getData(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String caseID = null;
        JsonObject data = new JsonObject();//最后的返回结果
        try {
            String param = ParamUtils.getParam(req);
            logger.info("getData =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            if (paramObj.has("caseID")) {
                caseID = paramObj.get("caseID").getAsString();
            }
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        data.addProperty("code",1);
        data.addProperty("crf_id", crf_id);

        if (caseID == null || "".equals(caseID)) {
            SummaryBean summaryBean = MongoManager.getSummary(crf_id);
            if(summaryBean != null && summaryBean.getCaseID() != null){//summary结构中记录caseID的不为null
                caseID = summaryBean.getCaseID();
                data.addProperty("caseID", caseID);
                JsonObject d = MongoManager.getCrfData(crf_id, caseID);
                if (d == null || !d.has("children") || !d.get("children").isJsonArray()) {
                    data.add("children", new JsonArray());
                } else {
                    JsonArray children = d.get("children").getAsJsonArray();
                    data.add("children", children);
                }
            }else{
                //依旧是null,生成新的caseID
                if(caseID == null || "".equals(caseID)){
                    UUID uuid = UUID.randomUUID();
                    caseID = uuid+"";
                    //更新summary结构体
                    MongoManager.updateSummaryCaseID(crf_id, caseID);
                }
                data.addProperty("caseID", caseID);
                data.add("children", new JsonArray());
            }
        }
        String jsonStr = gson.toJson(data);
        viewer.viewString(jsonStr, resp, req);
    }

    public void upLoadData(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String caseID = null;
        JsonArray children = null;
        JsonObject data = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("upLoadData =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            caseID = paramObj.get("caseID").getAsString();
            children = paramObj.get("children").getAsJsonArray();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }

        JsonObject existData = MongoManager.getCrfData(crf_id, caseID);
        String t = time.format(new Date());
        if (existData == null) {
            data = new JsonObject();
            data.addProperty("crf_id", crf_id);
            data.addProperty("caseID", caseID);
            data.add("children", children);
            String name = getUpdateForName(children);
            if(name != null){
                data.addProperty("patientName", name);
            }
            Date today = new Date();
            String todayStr = time.format(today).substring(0,10);
            data.addProperty("createTime",todayStr);
            SummaryBean summaryBean = MongoManager.getSummary(crf_id);
            if(summaryBean == null){//还没有这个crf_id 对应数据,出错
                errorParam("没有这个crf_id 对应数据summary,出错", req, resp);
                return;
            }
            if(crf_id.equals(summaryBean.getCrf_id())){//更新
                Integer maxCaseNo = summaryBean.getMaxCaseNo();
                data.addProperty("patientNo", maxCaseNo);
                MongoManager.updateNewSummary(summaryBean);
                DataBean dataBean = gson.fromJson(gson.toJson(data), DataBean.class);
                MongoManager.updateNewData(dataBean);
            }else{//插入
                Integer maxCaseNo = summaryBean.getMaxCaseNo();
                data.addProperty("patientNo", maxCaseNo+1);
                summaryBean.setMaxCaseNo(maxCaseNo+1);
                summaryBean.setCaseID(caseID);
                summaryBean.setLastTime(t);
                MongoManager.updateNewSummary(summaryBean);
                String dataObj = gson.toJson(data);
                MongoManager.insertNewData(dataObj);
            }

        }else{//已经插入一部分,这部分是更新同名的组信息
            if(existData.get("children")!= null || !existData.get("children").isJsonArray()){
                errorParam("上传的数据格式有误,无children或者children不是数组", req, resp);
                return;
            }
            JsonArray jsonArr = existData.getAsJsonArray("children");
            JsonArray newChildren = new JsonArray();
            for(JsonElement entity:jsonArr){
                JsonObject group = entity.getAsJsonObject();
                String name = group.get("name") != null? group.get("name").getAsString():null;
                for(JsonElement ent:children){
                    JsonObject newGroup = ent.getAsJsonObject();
                    String newName = newGroup.get("name") != null ? newGroup.get("name").getAsString():null;
                    if(name != null && newName != null && name.equals(newName)){
                        newChildren.add(newGroup);
                    }else if(name != null && newName != null && !name.equals(newName)){
                        newChildren.add(group);
                    }
                }
            }
            existData.add("children",newChildren);
            DataBean dataBean = gson.fromJson(gson.toJson(existData), DataBean.class);
            MongoManager.updateNewData(dataBean);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("上传数据成功");
        viewer.viewString(gson.toJson(resultBean),resp,req);
    }


    /**
     *
     * @param children
     * @return
     */
    public static String getUpdateForName(JsonArray children){
        String name = null;
        for (JsonElement entity : children) {
            JsonObject obj = entity.getAsJsonObject();
            if (obj.get("name") != null && "患者基本信息".equals(obj.get("name").getAsString())) {
                if (obj.get("children").isJsonArray()) {
                    JsonArray child = obj.getAsJsonArray("children");
                    for (JsonElement en : child) {
                        JsonObject object = en.getAsJsonObject();
                        if (object.get("name") != null && "基本信息".equals(object.get("name").getAsString())) {
                            JsonArray child1 = object.getAsJsonArray("children");
                            for (JsonElement ent : child1) {
                                JsonObject object1 = ent.getAsJsonObject();
                                if (object1.get("attrID") != null && "name".equals(object1.get("attrID").getAsString())) {
                                    name = object1.get("data").getAsString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return name;
    }

    /**
     * crf数据录入完成后,点击录入完成出发的接口,请求参数crf_id,caseID,返回crf_id,新的caseID,空的数据格式
     * @param req
     * @param resp
     */
    public void saveData(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String caseID = null;
        JsonObject data = new JsonObject();
        try {
            String param = ParamUtils.getParam(req);
            logger.info("upLoadData =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            caseID = paramObj.get("caseID").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        SummaryBean summary = MongoManager.getSummary(crf_id);
        if(summary == null ){
            String err = "没有对应crf_id的CRF数据";
            logger.error(err);
            errorParam(err, req, resp);
            return;
        }
        if(summary.getCaseID() == null || !caseID.equals(summary.getCaseID())){
            String err = "crf_id当前录入的数据caseID与录入完成传入的caseID的不对应";
            logger.error(err);
            errorParam(err, req, resp);
            return;
        }
        UUID uuid = UUID.randomUUID();
        String newCaseID = uuid +"";
        summary.setCaseID(newCaseID);
        summary.setMaxCaseNo(summary.getMaxCaseNo()+1);
        MongoManager.updateNewSummary(summary);
        data.addProperty("code",1);
        data.addProperty("crf_id",crf_id);
        data.addProperty("caseID",newCaseID);
        viewer.viewString(gson.toJson(data),resp,req);
    }

    /**
     * 返回没有叶子节点的模型树
     * @param req
     * @param resp
     */
    public void modelTree(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("modelTree =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        JsonObject modelJsonObject = MongoManager.getModel(crf_id);
        if(modelJsonObject == null){
            String err = "没有crf_id:"+crf_id +"对应的模型数据";
            errorParam(err, req, resp);
            return;
        }
        JsonObject modelTree = traversalCutDownLeaves(modelJsonObject);
        viewer.viewString(gson.toJson(modelTree),resp,req);
    }

    /**
     *返回病历列表数据
     * @param req
     * @param resp
     */
    public void sampleCaseList(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String limit = null;
        int[] result = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("sampleCaseList =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            limit = paramObj.get("limit").getAsString();
            result = ParamUtils.parseLimit(limit);
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        List<SampleListBean> list = MongoManager.getSampleListData(crf_id,result[0],result[1]);
        int count = MongoManager.getSampleListCount(crf_id);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("count",count);
        resultBean.setInfo(map);
        viewer.viewString(gson.toJson(resultBean),resp,req);
    }

    public void deleteSample(HttpServletRequest req, HttpServletResponse resp) {
        String crf_id = null;
        String caseID = null;
        try {
            String param = ParamUtils.getParam(req);
            logger.info("sampleCaseList =" + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            crf_id = paramObj.get("crf_id").getAsString();
            caseID = paramObj.get("caseID").getAsString();
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            errorParam("请求参数出错", req, resp);
            return;
        }
        MongoManager.deleteSample(crf_id,caseID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setInfo("删除成功");
        viewer.viewString(gson.toJson(resultBean),resp,req);
    }
}
