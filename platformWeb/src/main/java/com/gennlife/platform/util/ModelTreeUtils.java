package com.gennlife.platform.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

/**
 * Created by chen-song on 16/4/15.
 */
public class ModelTreeUtils {

    public static JsonObject updateUIData(JsonObject model){
        JsonArray children = model.get("children").getAsJsonArray();
        model = traversalUpdateUI(model,children);
        return model;
    }
    public static JsonObject traversalUpdateUI(JsonObject parant, JsonArray children){
        for(JsonElement entity:children){
            JsonObject child = entity.getAsJsonObject();
            if(child.get("attrID") != null){//是属性
                parant.addProperty("hasAttr",true);
            }else{//是组
                JsonArray subChildren = child.get("children").getAsJsonArray();
                traversalUpdateUI(child,subChildren);
            }
        }
        if(parant.get("hasAttr") == null){
            parant.addProperty("hasAttr",false);
        }
        return parant;
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
     * 删除属性组
     * @param parent
     * @param id
     * @param parentID
     */
    public static void traversalDeleteGroup(JsonObject parent,String id,String parentID){
        if(id.equals(parentID)){//删除的是最底层的组
            JsonArray children = parent.getAsJsonArray("children");
            JsonArray newchildren = new JsonArray();
            for(JsonElement entity:children){
                JsonObject obj = entity.getAsJsonObject();
                if(obj.get("id") == null ||(obj.get("id") != null && !id.equals(obj.get("id").getAsString()))){
                    newchildren.add(obj);
                }
            }
            parent.add("children",newchildren);
        }else{//删除不是最底层的组
            if(parent.get("children") != null){
                String groupID = parent.get("id") == null ? "Root":parent.get("id").getAsString();
                JsonArray children = parent.getAsJsonArray("children");
                if(groupID.equals(parentID)){
                    JsonArray newchildren = new JsonArray();
                    for(JsonElement entity:children){
                        JsonObject obj = entity.getAsJsonObject();
                        if(obj.get("id") == null || (obj.get("id") != null && !id.equals(obj.get("id").getAsString()))){
                            newchildren.add(obj);
                        }
                    }
                    parent.add("children",newchildren);
                }else{
                    for(JsonElement entity:children) {
                        JsonObject obj = entity.getAsJsonObject();
                        traversalDeleteGroup(obj,id,parentID);
                    }
                }
            }
        }

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
            if (obj.get("id") != null && "患者基本信息".equals(obj.get("id").getAsString())) {
                if (obj.get("children").isJsonArray()) {
                    JsonArray child = obj.getAsJsonArray("children");
                    for (JsonElement en : child) {
                        JsonObject object = en.getAsJsonObject();
                        if (object.get("id") != null && "患者基本信息_基本信息".equals(object.get("id").getAsString())) {
                            JsonArray child1 = object.getAsJsonArray("children");
                            for (JsonElement ent : child1) {
                                JsonObject object1 = ent.getAsJsonObject();
                                if (object1.get("attrID") != null && "name".equals(object1.get("attrID").getAsString())) {
                                    name = object1.get("data").getAsString();
                                    return name;
                                }
                            }
                        }
                    }
                }
            }
        }
        return name;
    }


    public static void traversalUpdateGroupName(JsonObject parent,String id,String newName){
        if(parent.get("children") != null){//是组
            String groupID = null;
            if(parent.get("id") != null){//有children,有id,是组
                groupID = parent.get("id").getAsString();
                if(groupID.equals(id)){//找到组,重新命名
                    parent.addProperty("name",newName);
                    String newID = getNewGroupID(id,newName);
                    parent.addProperty("id",newID);
                }else{//当前不是要找组,遍历子节点
                    if(groupID.startsWith(id)){//当前组是要找组的子组
                        String newID = getNewGroupID(id,newName);
                        groupID = groupID.replace(id,newID);
                        parent.addProperty("id",groupID);
                    }
                    JsonArray children = parent.get("children").getAsJsonArray();
                    for(JsonElement entity:children){
                        JsonObject json = entity.getAsJsonObject();
                        traversalUpdateGroupName(json,id,newName);
                    }
                }
            }else {//有children 无id,是根节点
                JsonArray children = parent.get("children").getAsJsonArray();
                for(JsonElement entity:children){
                    JsonObject json = entity.getAsJsonObject();
                    traversalUpdateGroupName(json,id,newName);
                }
            }

        }
    }
    private static String getNewGroupID(String oldID,String newName){
        String[] groups = oldID.split("_");
        String groupID = "";
        if(groups.length == 1){
            groupID = newName;//新的组id
        }else{
            for(int i= 0;i <= groups.length -2;i++){
                if(i == 0){
                    groupID = groups[i];
                }else{
                    groupID = groupID + "_" + groups[i];
                }
            }
            groupID = groupID + "_" + newName;
        }
        return groupID;
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

}
