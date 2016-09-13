package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Organization;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.ChineseToEnglish;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-song on 16/9/12.
 */
public class LaboratoryProcessor {
    private Logger logger = LoggerFactory.getLogger(LaboratoryProcessor.class);
    private Gson gson = GsonUtil.getGson();
    private static SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static JsonParser jsonParser = new JsonParser();
    /**
     * 获取科室组织信息
     *
     * @param paramObj
     * @return
     */
    public String orgMapData(JsonObject paramObj) {
        String uid = null;
        try {
            uid = paramObj.get("uid").getAsString();
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("参数出错");
        }
        User user = UserProcessor.getUserByUid(uid);
        String orgID = user.getOrgID();
        boolean isAdmin = isAdmin(uid,orgID);
        Organization organization = getOrganization(isAdmin,orgID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public static List<Lab> generateLabTree(List<Lab> labs,String key,int maxLevel) {
        List<Lab> result = new LinkedList<>();
        for(Lab lab:labs){
            if(lab.getLab_parent().equals(key)){
                lab.setOrgID(null);
                result.add(lab);
                if(maxLevel > lab.getLab_level()){
                    List<Lab> subLabs = generateLabTree(labs,lab.getLabID(),maxLevel);
                    if(subLabs.size() > 0){
                        lab.setSubLabs(subLabs);
                    }
                }
            }

        }
        return result;
    }

    /**
     * 添加科室
     * @param paramObj
     * @return
     */
    public String addOrg(JsonObject paramObj) {
        String uid = null;
        String lab_parent = null;
        String lab_name = null;
        String lab_leader = null;
        try{
            uid = paramObj.get("uid").getAsString();
            lab_parent = paramObj.get("lab_parent").getAsString();
            lab_name = paramObj.get("lab_name").getAsString();
            if(paramObj.has("lab_leader")){
                lab_leader = paramObj.get("lab_leader").getAsString();
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        User user = UserProcessor.getUserByUid(uid);
        String orgID = user.getOrgID();
        boolean isAdmin = isAdmin(uid,orgID);
        Organization organization = null;
        if(isAdmin){
            Map<String,Object> map = new HashMap<>();
            List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
            if(labNames.contains(lab_name)){
                return ParamUtils.errorParam(lab_name+"已经存在");
            }
            String labID = orgID+"-"+ChineseToEnglish.getPingYin(lab_name);
            map.put("orgID",orgID);
            map.put("labID",lab_parent);
            Integer lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            if(lab_level == null){
                return ParamUtils.errorParam("无法获取"+lab_parent+"对应的科室信息");
            }
            Lab lab = new Lab();
            lab.setOrgID(orgID);
            lab.setLab_leader(lab_leader);
            lab.setLab_level(lab_level+1);
            lab.setLab_name(lab_name);
            lab.setAdd_time(time.format(new Date()));
            lab.setAdd_user(uid);
            lab.setLabID(labID);
            lab.setLab_parent(lab_parent);
            int counter = AllDao.getInstance().getOrgDao().insertOneLab(lab);
            if(counter == 1){
                organization = getOrganization(isAdmin,orgID);
            }else {
                return ParamUtils.errorParam("插入失败");
            }
        }else{
            return ParamUtils.errorParam("当前用户无权限");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public static boolean isAdmin(String uid,String orgID){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("orgID", orgID);
        List<Admin> admins = AllDao.getInstance().getSyUserDao().getAdmins(map);
        boolean isAdmin = false;
        for (Admin admin : admins) {
            if (admin.getPrivilegeType().equals("admin")
                    && admin.getPrivilegeValue().equals("admin")) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }
    public static Organization getOrganization(boolean isAdmin,String orgID){
        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        if (isAdmin) {
            List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
            Integer maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
            List<Lab> treeLabs = generateLabTree(labs,orgID,maxLevel);
            organization.setLabs(treeLabs);
        }
        return organization;
    }

    /**
     * 删除科室信息
     * @param paramObj
     * @return
     */
    public String deleteOrg(JsonObject paramObj) {
        String uid = null;
        List<String> labIDsList = null;
        try{
            uid = paramObj.get("uid").getAsString();
            JsonArray labIDsArray= paramObj.get("labIDs").getAsJsonArray();
            labIDsList = gson.fromJson(labIDsArray,LinkedList.class);
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        String[] labIDs = labIDsList.toArray(new String[labIDsList.size()]);
        User user = UserProcessor.getUserByUid(uid);
        String orgID = user.getOrgID();
        boolean isAdmin = isAdmin(uid,orgID);
        Organization organization = null;
        if(isAdmin){
            int counter = AllDao.getInstance().getOrgDao().deleteLabs(labIDs);
            int fail = labIDsList.size()-counter;
            logger.info("成功删除"+counter+"个科室信息,失败"+fail+"个");
            organization = getOrganization(isAdmin,orgID);
        }else {
            return ParamUtils.errorParam("当前用户没有权限");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);

        return gson.toJson(resultBean);
    }

    /**
     * 更新科室信息
     * @param paramObj
     * @return
     */
    public String updateOrg(JsonObject paramObj) {
        String uid = null;
        String labID = null;
        String lab_name = null;
        String lab_leader = null;
        String lab_leaderName = null;
        String lab_parent = null;
        try {
            uid = paramObj.get("uid").getAsString();
            labID = paramObj.get("labID").getAsString();
            lab_name = paramObj.get("lab_name").getAsString();
            if(paramObj.has("lab_leader")){
                lab_leader = paramObj.get("lab_leader").getAsString();
            }
            if(paramObj.has("lab_leaderName")){
                lab_leaderName = paramObj.get("lab_leaderName").getAsString();
            }
            lab_parent = paramObj.get("lab_parent").getAsString();
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        User user = UserProcessor.getUserByUid(uid);
        String orgID = user.getOrgID();
        boolean isAdmin = isAdmin(uid,orgID);
        if(isAdmin){
            Map<String,Object> map = new HashMap<>();
        }else{
            return ParamUtils.errorParam("当前用户没有权限");
        }
        return "";
    }
}
