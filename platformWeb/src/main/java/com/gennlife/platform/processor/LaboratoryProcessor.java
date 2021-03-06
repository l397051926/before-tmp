package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.OrgMapper;
import com.gennlife.platform.dao.SyUserMapper;
import com.gennlife.platform.model.*;
import com.gennlife.platform.rocketmq.ProducerService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by chen-song on 16/9/12.
 */
public class LaboratoryProcessor {
    private static Logger logger = LoggerFactory.getLogger(LaboratoryProcessor.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Part3Processor part3Processor = new Part3Processor();
    /**
     * 获取科室组织信息
     *
     * @param user
     * @return
     */
    public String orgMapData(User user, String key, String isParentLab, String isLabCast, String lab_id) {
        String orgID = user.getOrgID();
        //通过科室数据，初始化的数据结构
        Organization organization = getOrganization(orgID, key, isParentLab, isLabCast, lab_id, null);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        if (StringUtils.isEmpty(organization.getLabs())) {
            System.out.println("aaa");
        }
        if (organization.isEmpty()) {
            resultBean.setData(new Organization());
        } else {
            resultBean.setData(organization);
        }
        return gson.toJson(resultBean);
    }

    public static List<Lab> generateLabTree(List<Lab> labs, String key, int maxLevel, String isParentLab, String lab_id) {
        List<Lab> result = new LinkedList<>();

        if (labs != null) {
            for (Lab lab : labs) {
                if ("true".equals(isParentLab) && "一线临床类".equals(lab.getDepart_name())) {
                    continue;
                }
                if (lab.getLabID().equals(lab_id)) {
                    continue;
                }
                if (lab.getLab_parent().equals(key)) {
                    lab.setOrgID(null);
                    result.add(lab);
                    if (maxLevel >= lab.getLab_level()) {
                        List<Lab> subLabs = generateLabTree(labs, lab.getLabID(), maxLevel, isParentLab, lab_id);
                        if (subLabs != null && subLabs.size() > 0) {
                            lab.setSubLabs(subLabs);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 添加科室
     *
     * @param paramObj
     * @return
     */
    public String addOrg(JsonObject paramObj) {
        String uid = null;
        String lab_parent = null;
        String lab_name = null;
        String lab_leader = null;
        String depart_name = null;//部门类别
        try {
            uid = paramObj.get("uid").getAsString();
            lab_parent = paramObj.get("lab_parent").getAsString();
            lab_name = paramObj.get("lab_name").getAsString();
            if (paramObj.has("depart_name")) {
                depart_name = paramObj.get("depart_name").getAsString();
            }
            if (paramObj.has("lab_leader")) {
                lab_leader = paramObj.get("lab_leader").getAsString();
            }
            if (!("行政管理类".equals(depart_name) || "业务管理类".equals(depart_name) || "一线临床类".equals(depart_name))) {
                return ParamUtils.errorParam("请求参数有错误");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        User user = UserProcessor.getUserByUids(uid);
        String orgID = user.getOrgID();
        if (orgID.equals(lab_parent) && "一线临床类".equals(depart_name)) {
            return ParamUtils.errorParam("科室类别不符合要求");
        }
        Organization organization = null;
        Map<String, Object> map = new HashMap<>();
        List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
        if (labNames.contains(lab_name)) {
            return ParamUtils.errorParam(lab_name + "已经存在");
        }
        // 生成科室id
        String labID = orgID + "-" + ChineseToEnglish.getPingYin(lab_name);
        //获取當前科室信息
        Lab exLab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
        if (exLab != null) {
            int count = 1;
            while (true) {
                labID = labID + "_" + count;
                exLab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
                if (exLab != null) {
                    count++;
                } else {
                    break;
                }
            }
        }
        Lab lab = new Lab();
        lab.setOrgID(orgID);
        if (lab_parent.equals(orgID)) { // 一级科室
            lab.setLab_level(1);
        } else {
            map.put("orgID", orgID);
            map.put("labID", lab_parent);
            Integer lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            if (lab_level == null) {
                return ParamUtils.errorParam("无法获取" + lab_parent + "对应的科室信息");
            } else {
                lab.setLab_level(lab_level + 1);
            }
        }
        lab.setDepart_name(depart_name);
        lab.setLab_leader(lab_leader);
        lab.setLab_name(lab_name);
        lab.setAdd_time(LogUtils.getStringTime());
        lab.setAdd_user(uid);
        lab.setLabID(labID);
        lab.setLab_parent(lab_parent);
        int counter = AllDao.getInstance().getOrgDao().insertOneLab(lab);
        // 同步增加资源
        addResource(lab);
        if (counter == 1) {
            organization = getOrganization(orgID);
        } else {
            return ParamUtils.errorParam("插入失败");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public static void addResource(Lab lab) {
        if ("一线临床类".equals(lab.getDepart_name()) || "行政管理类".equals(lab.getDepart_name())) {
            return;
        }
        LabResource labResource = new LabResource();
        labResource.setSorgID(lab.getOrgID());
        labResource.setSdesc(lab.getLab_name() + "病例数据资源");
        labResource.setSid(lab.getLabID());
        labResource.setSlab_parent(lab.getLab_parent());
        labResource.setSlab_type(lab.getLab_level() + "");
        labResource.setStype("病例数据");
        labResource.setSname(lab.getLab_name() + "资源");
        labResource.setSlab_name(lab.getLab_name());
        labResource.setStype_role("0"); // 初始化的是普通科室
        LabResource labResourceTmp = AllDao.getInstance().getSyResourceDao().getLabResourcesBySid(labResource.getSid());
        if (labResourceTmp != null) {
            return;
        }
        AllDao.getInstance().getSyResourceDao().insertOneResource(labResource);
        List<String> uids = AllDao.getInstance().getSyUserDao().getUserIDByLabID(lab.getLabID(), lab.getOrgID());
        RedisUtil.updateUserOnLine(uids);
    }


    /**
     * 获取某个医院的组织结构
     *
     * @param orgID
     * @return
     */
    public static Organization getOrganization(String orgID) {

        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs = null;
        Integer maxLevel = null;

        labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);

        if (maxLevel == null) {
            return organization;
        }
        List<Lab> treeLabs = generateLabTree(labs, orgID, maxLevel, null, null);
        organization.setLabs(treeLabs);
        return organization;
    }

    public static Organization getOrganization(String orgID, String roleid) {

        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs = null;
        Integer maxLevel = null;

        labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
        if (!StringUtils.isEmpty(roleid)) {
            List<String> sids = AllDao.getInstance().getSyResourceDao().getSidsByRoleid(roleid);
            for (Lab lab : labs) {
                if (sids.contains(lab.getLabID())) {
                    lab.setChecked("true");
                    setLabChecked(labs, lab.getLab_parent());
                }
            }
        }
        if (maxLevel == null) {
            return organization;
        }
        List<Lab> treeLabs = generateLabTree(labs, orgID, maxLevel, "true", null);
        organization.setLabs(treeLabs);
        return organization;
    }

    public static void setLabChecked(List<Lab> labs, String labID) {
        for (Lab lab : labs) {
            if (labID.equals(lab.getLabID())) {
                lab.setChecked("true");
                setLabChecked(labs, lab.getLab_parent());
            }
        }
    }

    public static Organization getOrganization(String orgID, String key, String isParentLab, String isLabCast, String lab_id, JsonArray labIds) {
        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs = null;
        Integer maxLevel = null;
//        if(key==null){
//            labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
//        }else {
//            labs = AllDao.getInstance().getOrgDao().getLabsBylabId(key,orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevelBylabId(key,orgID);
//        }
        labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        if (labIds != null) {
            int size = labIds.size() == 0 ? 0 : labIds.size();
            for (int i = 0; i < size; i++) {
                String tmp = labIds.get(i).getAsString();
                for (Lab lab : labs) {
                    if (tmp.equals(lab.getLabID())) {
                        lab.setChecked("true");
                    }
                }
            }
        }
        maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
        Set<Lab> resultlabs = new HashSet<>();
        if (!StringUtils.isEmpty(key)) {
            if ("true".equals(isLabCast)) {//是否高亮
                spellLabCast(labs, key, resultlabs, key, orgID);
            } else {
                spellLab(labs, key, resultlabs, key, orgID, isParentLab);
            }
            labs = new LinkedList<>(resultlabs);

        }
        if (maxLevel == null) {
            return organization;
        }
        List<Lab> treeLabs = generateLabTree(labs, orgID, maxLevel, isParentLab, lab_id);
//        if(!StringUtils.isEmpty(key) && "true".equals(isParentLab)){
//            resultlabs.clear();
//            spellLabCast(treeLabs,key,resultlabs,key,orgID);
//            treeLabs =new LinkedList<>(resultlabs);
//        }

        if (treeLabs.size() == 0) {//若一个没搜到 默认清空
            organization.setEmpty(true);
        }
        organization.setLabs(treeLabs);
        return organization;
    }

    public static void spellLab(List<Lab> labs, String key, Set<Lab> resultlabs, String key1, String orgID, String isParentLab) {

        for (Lab lab : labs) {
            String lab_name = lab.getLab_name();
            if (lab_name.contains(key) || lab.getLabID().equals(key)) {
                if ("true".equals(isParentLab) && "一线临床类".equals(lab.getDepart_name())) continue;
                if (!resultlabs.contains(lab)) {
                    lab.setLab_name(lab_name.replaceAll(key1, "<span style='color:red'>" + key1 + "</span>"));
                    resultlabs.add(lab);
                    if (lab.getLab_parent() != null && !(orgID.equals(lab.getLab_parent()))) {
                        spellLab(labs, lab.getLab_parent(), resultlabs, key1, orgID, isParentLab);
                    }
                }
            }
        }
    }

    public static void spellLabCast(List<Lab> labs, String key, Set<Lab> resultlabs, String key1, String orgID) {

        for (Lab lab : labs) {
            String lab_name = lab.getLab_name();
            if (lab_name.contains(key) || lab.getLabID().contains(key)) {
                if (!resultlabs.contains(lab)) {
                    resultlabs.add(lab);
                    if (lab.getLab_parent() != null && !(orgID.equals(lab.getLab_parent()))) {
                        spellLabCast(labs, lab.getLab_parent(), resultlabs, key1, orgID);
                    }
                }
            }
        }
    }

    /**
     * 删除科室信息
     *
     * @param paramObj
     * @return
     */
    public String deleteOrg(JsonObject paramObj, User user) {
        List<String> labIDsList = null;
        try {
            JsonArray labIDsArray = paramObj.get("labIDs").getAsJsonArray();
            labIDsList = gson.fromJson(labIDsArray, LinkedList.class);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        Set<String> tempLabIdList = new HashSet<>();
        getAllDeleteLabids(tempLabIdList, labIDsList);
        String[] labIDs = tempLabIdList.toArray(new String[tempLabIdList.size()]);
        String orgID = user.getOrgID();
        String orgName = user.getOrg_name();
        Organization organization = null;
        OrgMapper orgdao = AllDao.getInstance().getOrgDao();
        SyUserMapper userdao = AllDao.getInstance().getSyUserDao();
        Set<String> alllab = new TreeSet<>();
        for (String labID : labIDs) {
            if (alllab.contains(labID)) continue;
            alllab.addAll(getLabs(orgdao, labID, orgID, orgName, userdao));
        }
        int counter = AllDao.getInstance().getOrgDao().deleteLabs(labIDs);
        List<String> uids = AllDao.getInstance().getSyUserDao().getRelateUserByLabId(labIDs, orgID);
        TreeSet<String> allUids = new TreeSet<>();
        if (uids != null && uids.size() > 0) {
            allUids.addAll(uids);
            SyUserMapper.addSelectRelateUid(allUids);
        }
        alllab = new TreeSet<>(AllDao.getInstance().getSyResourceDao().selectDeleteLabsReource(labIDs));
        labIDs = alllab.toArray(new String[alllab.size()]);
        if (labIDs.length != 0) {
            // 同步删除资源
            AllDao.getInstance().getSyResourceDao().deleteLabsReource(labIDs);
            AllDao.getInstance().getSyResourceDao().deleteRoleItem(labIDs);
        }
        RedisUtil.updateUserOnLine(allUids);
        int fail = alllab.size() - counter;
        // 更新用户信息
        logger.info("成功删除" + counter + "个科室信息,失败" + fail + "个");
        organization = getOrganization(orgID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);

        return gson.toJson(resultBean);
    }

    private void getAllDeleteLabids(Set<String> tempLabIdList, List<String> labIDsList) {
        if (labIDsList.size() == 0) {
            return;
        }
        for (String labId : labIDsList) {
            tempLabIdList.add(labId);
            List<String> labIds = AllDao.getInstance().getOrgDao().getLabIdByParentId(labId);
            getAllDeleteLabids(tempLabIdList, labIds);
        }
    }


    public Set<String> getLabs(OrgMapper orgdao, String labID, String orgID, String orgName, SyUserMapper userdao) {
        Lab parent = orgdao.getLabPInfo(labID, orgID);
        if (parent == null) {
            parent = new Lab();
            parent.setLabID(orgID);
            parent.setLab_name(orgName);
        }
        Set<String> allsubs = getAllSubLab(orgdao, labID, orgID);
        allsubs.add(labID);
        String[] labs = allsubs.toArray(new String[allsubs.size()]);
        List<String> uids = userdao.getUserIDsByLabID(labs, orgID);
        RedisUtil.updateUserOnLine(uids);
        if (allsubs != null && allsubs.size() > 0)
            userdao.updateUsersWhenDelLab(parent.getLabID(), parent.getLab_name(), labs, orgID);

        return allsubs;
    }

    public Set<String> getAllSubLab(OrgMapper orgdao, String labID, String orgID) {
        Set<String> allsubs = new TreeSet<>();
        List<String> subLabs = orgdao.getSubLabs(labID, orgID);
        LinkedList<String> todo = new LinkedList<>();
        todo.addAll(subLabs);
        String first = null;
        if (todo != null && todo.size() > 0) first = todo.removeFirst();
        else first = null;
        while (first != null) {
            if (!allsubs.contains(first)) {
                subLabs = orgdao.getSubLabs(first, orgID);
                allsubs.add(first);
                if (subLabs != null && subLabs.size() > 0) {
                    todo.addAll(subLabs);
                }
            }
            if (todo.size() > 0) first = todo.removeFirst();
            else first = null;
        }
        return allsubs;
    }

    /**
     * 更新科室信息
     *
     * @param paramObj
     * @return
     */
    public String updateOrg(JsonObject paramObj, User user) {
        String labID = null;
        String lab_name = null;
        String lab_leader = "";
        String lab_leaderName = "";
        String lab_parent = null;
        String depart_name = "";
        String parent_depart_name = "";
        boolean isResourceopen = false; //判断是否增加 lab_resources
        try {
            labID = paramObj.get("labID").getAsString();
            lab_name = paramObj.get("lab_name").getAsString();
            if (paramObj.has("depart_name")) {
                depart_name = paramObj.get("depart_name").getAsString();
            }
            if (paramObj.has("lab_leader")) {
                lab_leader = paramObj.get("lab_leader").getAsString();
            }
            if (paramObj.has("lab_leaderName")) {
                lab_leaderName = paramObj.get("lab_leaderName").getAsString();
            }
            lab_parent = paramObj.get("lab_parent").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        String orgID = user.getOrgID();
        Map<String, Object> map = new HashMap<>();
        Lab labNow = new Lab();//当前科室 labNow
        Lab lab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);//要修改的科室
        if (lab_parent.equals(user.getOrgID())) {
            parent_depart_name = "行政管理类";
        } else {
            Lab parentLab = AllDao.getInstance().getOrgDao().getLabBylabID(lab_parent);
            parent_depart_name = parentLab.getDepart_name();
        }
        if (lab == null) {
            return ParamUtils.errorParam(labID + "无此科室");
        }
        if (!lab.getLab_name().equals(lab_name)) { // 如果科室的名称修改啦,需要检查新的名称是否已经存在
            List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
            if (labNames.contains(lab_name)) {
                return ParamUtils.errorParam(lab_name + "已经存在");
            }
            // 当前科室下面的用户科室名称 ，若修改科室名 则更改 用户相关的科室
            AllDao.getInstance().getSyUserDao().updateUserLabNameByLabName(lab_name, lab.getLab_name(), orgID);

            List<String> userIds = AllDao.getInstance().getSyUserDao().getUserIDByLabID(labID, orgID);
            // 更新缓存
            RedisUtil.updateUserOnLine(userIds);
        }

         /*修改科室类别考虑以下情况：
            1.修改前 是行政  修改后是业务，增加 lab_resourece 修改后的上级只能为 行政 或业务
            2.修改前 是行政  修改后是临床   不增加 lab_resource 修改后上级只能为 业务
            3.修改前 是业务  修改后是行政  删除 lab_resource  修改后的上级只能为行政
            4.修改前 是业务  修改后是临床  删除 lab_resource 修改后上级只能为业务
            5.修改前 是临床  修改后是行政  不修改lab_resource 修改后上级只能为行政
            6.修改前 是临床  修改后是业务  增加 lab_resource 修改后上级可以为 行政 或者业务*/

        //如果现在不是一线临床类  原来不是一线临床类 则随着更新
        if ("业务管理类".equals(lab.getDepart_name()) && "业务管理类".equals(depart_name)) {
            if ("一线临床类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            } else {
                LabResource labResource = new LabResource();
                labResource.setSorgID(lab.getOrgID());
                labResource.setSdesc(lab_name + "病例数据资源");
                labResource.setSid(labID);
                labResource.setSlab_parent(lab_parent);
                labResource.setSlab_type(lab.getLab_level() + "");
                labResource.setStype("病例数据");
                labResource.setSname(lab_name + "资源");
                labResource.setSlab_name(lab_name);
                // logger.info("update "+gson.toJson(labResource));
                AllDao.getInstance().getSyResourceDao().updateResource(labResource);
            }
        } else if ("业务管理类".equals(lab.getDepart_name()) && "行政管理类".equals(depart_name)) {
            if (!"行政管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            } else {
                AllDao.getInstance().getSyResourceDao().deleteLabsReource(new String[]{lab.getLabID()});
            }
        } else if ("业务管理类".equals(lab.getDepart_name()) && "一线临床类".equals(depart_name)) {
            if (!"业务管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            } else {
                AllDao.getInstance().getSyResourceDao().deleteLabsReource(new String[]{lab.getLabID()});
            }
        } else if ("行政管理类".equals(lab.getDepart_name()) && "行政管理类".equals(depart_name)) {
            if (!"行政管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
        } else if ("行政管理类".equals(lab.getDepart_name()) && "业务管理类".equals(depart_name)) {
            if ("一线临床类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            } else {
                isResourceopen = true;
            }
        } else if ("行政管理类".equals(lab.getDepart_name()) && "一线临床类".equals(depart_name)) {
            if (!"业务管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
        } else if ("一线临床类".equals(lab.getDepart_name()) && "行政管理类".equals(depart_name)) {
            if (!"行政管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
        } else if ("一线临床类".equals(lab.getDepart_name()) && "业务管理类".equals(depart_name)) {
            if ("一线临床类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            } else {
                isResourceopen = true;
            }
        } else if ("一线临床类".equals(lab.getDepart_name()) && "一线临床类".equals(depart_name)) {
            if (!"业务管理类".equals(parent_depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
        }
        if (!lab.getLab_parent().equals(lab_parent)
                && !lab_parent.equals(orgID)) { // 改变了上级部门,并且改变的上级部门不是当前医院,需要检查上级部门是否存在
            Lab parentLab = AllDao.getInstance().getOrgDao().getLabBylabID(lab_parent);
            if (parentLab == null) {
                return ParamUtils.errorParam("上级部门不存在");
            }
        }

        //根据下级 约束当前科室
        List<Lab> juniorLabs = AllDao.getInstance().getOrgDao().getLabsByparentID(orgID, labID);
        for (Lab labTmp : juniorLabs) {
            if ("行政管理类".equals(labTmp.getDepart_name()) && !"行政管理类".equals(depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
            if ("业务管理类".equals(labTmp.getDepart_name()) && "一线临床类".equals(depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
            if ("一线临床类".equals(labTmp.getDepart_name()) && !"业务管理类".equals(depart_name)) {
                return ParamUtils.errorParam("上下级科室类别不符-更新失败");
            }
        }
        //同事修改等级
        Integer lab_level = 1;
        if (!lab_parent.equals(orgID)) {
            map.put("orgID", orgID);
            map.put("labID", lab_parent);
            lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            map.put("lab_level", lab_level + 1);
            labNow.setLab_level(lab_level);
        } else {
            map.put("lab_level", lab_level);
            labNow.setLab_level(lab_level);
        }
        map.put("labID", labID);
        map.put("lab_name", lab_name);
        map.put("lab_leader", lab_leader);
        map.put("lab_leaderName", lab_leaderName);
        map.put("lab_parent", lab_parent);
        map.put("depart_name", depart_name);//-增加 depart_name
        if (isResourceopen) {//同期增加 lab_resource
            labNow.setDepart_name(depart_name);
            labNow.setLab_leader(lab_leader);
            labNow.setLab_name(lab_name);
            labNow.setAdd_time(LogUtils.getStringTime());
            labNow.setLabID(labID);
            labNow.setLab_parent(lab_parent);
            labNow.setOrgID(orgID);
            addResource(labNow);
        }
        int counter = AllDao.getInstance().getOrgDao().updateLabInfo(map);
        if (counter == 0) {
            return ParamUtils.errorParam("更新失败");
        }
        Organization organization = getOrganization(orgID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public String getStaffInfo(JsonObject paramObj, User user) {
        String labID = null;
        String key = null;
        String limitStr = null;
        Integer offset = null;
        Integer limit = null;
        String projectId = null;
        try {
            labID = paramObj.get("labID").getAsString();
            key = paramObj.get("key").getAsString();
            if(paramObj.has("projectId")){
                projectId = paramObj.get("projectId").getAsString();
            }
            if (paramObj.has("limit")) {
                limitStr = paramObj.get("limit").getAsString();
                int[] ls = ParamUtils.parseLimit(limitStr);
                offset = (ls[0] - 1) * ls[1];
                limit = ls[1];
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }

        /*获取 rws 的用户列表*/
        List<String> uids = null;
        if(!StringUtils.isEmpty(projectId)){
            uids = new ArrayList<>();
            JSONObject param = new JSONObject();
            param.put("page",1);
            param.put("pageSize",Integer.MAX_VALUE-1);
            param.put("projectId",projectId);
            String url = ConfigurationService.getUrlBean().getProjectMemberList();
//            String url = "http://10.0.5.94:9001/rws-service/rws/projectMember/getProjectMemberList";
            String result = HttpRequestUtils.httpPost(url, gson.toJson(param));
            JsonObject object = jsonParser.parse(result).getAsJsonObject();
            JsonObject data = object.getAsJsonObject("data");
            JsonArray  value = data.getAsJsonArray("value");
            for (JsonElement element : value){
                uids.add(element.getAsJsonObject().get("uid").getAsString());
            }
        }
        List<User> users = null;
        ResultBean re = new ResultBean();
        re.setCode(1);
        if ("".equals(labID)) {
            if (offset != null && limit != null) {
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgID(key, offset, limit, user.getOrgID(),uids);
            } else {
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimit(key, user.getOrgID());
            }
            Long counter = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDCounter(key, user.getOrgID(),uids);
            Map<String, Object> info = new HashMap<>();
            info.put("count", counter);
            re.setInfo(info);
        } else {
            List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(user.getOrgID());
            //labID 集合
            List<String> list = new LinkedList<>();
            list.add(labID);
            int counter = 1;
            do {
                counter = list.size();
                for (Lab lab : labs) {
                    if (list.contains(lab.getLab_parent())) {
                        if (!list.contains(lab.getLabID())) {
                            list.add(lab.getLabID());
                        }
                    }
                }
            } while (counter != list.size());
            String[] labIDs = list.toArray(new String[list.size()]);
            if (offset != null && limit != null) {
                users = AllDao.getInstance().getSyUserDao().searchUsersByLabIDs(key, offset, limit, labIDs);
            } else {
                users = AllDao.getInstance().getSyUserDao().searchUsersByLabIDsNoLimit(key, labIDs);
            }

            Long count = AllDao.getInstance().getSyUserDao().searchUsersByLabIDsCounter(key, labIDs);
            Map<String, Object> info = new HashMap<>();
            info.put("count", count);
            re.setInfo(info);
        }
        for (User usr : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("orgID", usr.getOrgID());
            map.put("uid", usr.getUid());
            String effective = usr.getEffective_time();
            String failure = usr.getFailure_time();
            if ("禁用".equals(usr.getStatus())) {
                usr.setStatus_now("当前不可用");
            }
            if ("定期有效".equals(usr.getStatus())) {
                if (StringUtils.isEmpty(effective) || StringUtils.isEmpty(failure)) {
                    usr.setStatus_now("当前不可用");
                } else {
                    if (LogUtils.decideDate(effective, failure)) {
                        usr.setStatus_now("当前可用");
                    } else {
                        usr.setStatus_now("当前不可用");
                    }
                }
            }
            if ("长期有效".equals(usr.getStatus())) {
                usr.setStatus_now("当前可用");
            }
            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(map);
            if (rolesList != null) {
                for (Role role : rolesList) {
                    StringBuffer stringBuffer = new StringBuffer();
                    List<Resource> reList = AllDao.getInstance().getSyResourceDao().getResourceByRoleID(user.getOrgID(), role.getRoleid(), 0, 3000);
                    if (reList != null) {
                        for (Resource resource : reList) {
                            stringBuffer.append(resource.getSdesc()).append(",");
                        }
                    }
                    if (stringBuffer.toString().length() > 1) {
                        role.setResourceDesc(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
                    }
                }
            }
            usr.setRoles(rolesList);
        }
        re.setData(users);
        return gson.toJson(re);
    }

    public String addStaff(JsonObject paramObj, User user) {
        User adduser = gson.fromJson(gson.toJson(paramObj), User.class);
        adduser.setUid(user.getUid());
        adduser.setOrgID(user.getOrgID());
        adduser.setCtime(LogUtils.getStringTime());
        adduser.setUptime(LogUtils.getStringTime());
        adduser.setPwd(GStringUtils.getDefaultPasswd());
        adduser.setOrg_name(user.getOrg_name());
        JsonObject re = insertUser(adduser);
        // MemCachedUtil.setUserWithTime(adduser.getUid(),adduser, UserController.sessionTimeOut);

        part3Processor.userinfo(0, adduser);
        return gson.toJson(re);
    }


    public JsonObject insertUser(User adduser) {
        JsonObject resultBean = new JsonObject();
        String email = adduser.getUemail();
        String name = adduser.getUname();
        String unumber = adduser.getUnumber();
        String lab_name = adduser.getLab_name();
        String efftime = adduser.getEffective_time();
        String failtime = adduser.getFailure_time();
        String status = adduser.getStatus();
        if (StringUtils.isEmpty(status)) {
            resultBean.addProperty("code", 0);
            resultBean.addProperty("info", "账号有效期不能为空");
            return resultBean;
        }

        if ("定期有效".equals(status)) {
            if (StringUtils.isEmpty(efftime) || StringUtils.isEmpty(failtime)) {
                resultBean.addProperty("code", 0);
                resultBean.addProperty("info", "时间不能为空");
                return resultBean;
            }
            if (!StringUtils.isEmpty(efftime) && !StringUtils.isEmpty(failtime)) {
                try {
                    Date etime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(efftime);
                    Date ftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(failtime);
                    if (etime.after(ftime)) {
                        resultBean.addProperty("code", 0);
                        resultBean.addProperty("info", "日期格式不对，失效时间在生效时间前");
                        return resultBean;
                    }
                } catch (ParseException e) {
                    resultBean.addProperty("code", 0);
                    resultBean.addProperty("info", "日期格式不对");
                    return resultBean;
                }
            }
        }
        if ("长期有效".equals(status) || "禁用".equals(status)) {
            adduser.setEffective_time(null);
            adduser.setFailure_time(null);
        }

        if (name == null || "".equals(name)) {
            resultBean.addProperty("code", 0);
            resultBean.addProperty("info", "姓名不合法");
            return resultBean;
        } else if (lab_name == null || "".equals(lab_name)) {
            resultBean.addProperty("code", 0);
            resultBean.addProperty("info", "科室名称" + lab_name + "不合法");
            return resultBean;
        } else if (unumber == null || "".equals(unumber)) {
            resultBean.addProperty("code", 0);
            resultBean.addProperty("info", "工号" + unumber + "不合法");
            return resultBean;
        } else if (!StringUtils.isEmpty(email) && !GStringUtils.checkEmail(email)) {
            resultBean.addProperty("code", 0);
            resultBean.addProperty("info", "email" + email + "不合法");
            return resultBean;
        } else {
            if (!StringUtils.isEmpty(email)) {
                Integer exEamil = AllDao.getInstance().getSyUserDao().existEmail(email);
                if (exEamil >= 1) {
                    resultBean.addProperty("code", 0);
                    resultBean.addProperty("info", "email" + email + "已经存在了");
                    return resultBean;
                }
            }
            User exUnumber = AllDao.getInstance().getSyUserDao().getUserByUnumber(unumber, adduser.getOrgID());
            if (exUnumber != null) {
                resultBean.addProperty("code", 0);
                resultBean.addProperty("info", "工号" + unumber + "已经存在了");
                return resultBean;
            } else {
                if (lab_name.equals(adduser.getOrg_name())) {
                    adduser.setLabID(adduser.getOrgID());
                } else {
                    Lab exLab = AllDao.getInstance().getOrgDao().getLabBylabName(lab_name, adduser.getOrgID());
                    if (exLab == null) {
                        resultBean.addProperty("code", 0);
                        resultBean.addProperty("info", "科室" + lab_name + "不存在");
                        return resultBean;
                    } else {
                        adduser.setLabID(exLab.getLabID());
                        adduser.setLab_name(exLab.getLab_name());

                    }
                }
                Role role = AllDao.getInstance().getSyRoleDao().getLabMember(adduser.getOrgID());
                UUID uuid = UUID.randomUUID();
                adduser.setUid(uuid.toString());
                try {
                    Integer counter = AllDao.getInstance().getSyUserDao().insertOneUser(adduser);
                    if (counter == null || counter <= 0) {
                        resultBean.addProperty("code", 0);
                        resultBean.addProperty("info", "插入失败");
                        return resultBean;
                    } else {
                        if (role != null) {
                            counter = AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(role.getRoleid(), adduser.getUid());
                        }
                        resultBean.addProperty("code", 1);
                        resultBean.addProperty("info", "插入成功");
                        return resultBean;
                    }
                } catch (DataIntegrityViolationException e) {
                    resultBean.addProperty("code", 0);
                    resultBean.addProperty("info", "插入失败,填入的字符数超过20");
                    return resultBean;
                } catch (Exception e) {
                    logger.error("error ", e);
                    resultBean.addProperty("code", 0);
                    resultBean.addProperty("info", "插入失败");
                    return resultBean;
                }
            }
        }
    }

    public String deleteStaff(JsonArray paramObj, User user) {
        List<String> uidsList = null;
        try {
            uidsList = gson.fromJson(paramObj, LinkedList.class);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        List<String> admins = AllDao.getInstance().getSyUserDao().getAdminsByOrgID(user.getOrgID());
        int count = 0;
        for (String admin : admins) {
            if (uidsList.contains(admin)) {
                uidsList.remove(admin);
                count++;
            }
        }
        ResultBean re = new ResultBean();
        if (uidsList.size() == 0) {
            re.setCode(0);
            re.setInfo("无法删除管理员");
            return gson.toJson(re);
        } else {
            String[] uids = uidsList.toArray(new String[uidsList.size()]);
            AllDao.getInstance().getSyRoleDao().deleteByUids(uids);
            AllDao.getInstance().getOrgDao().updatalableader(uids);
            int counter = AllDao.getInstance().getSyUserDao().deleteUserByUids(uids);
            RedisUtil.updateUserOnLine(uidsList);
            if (counter > 0) {
                re.setCode(1);
                Map<String, Object> map = new HashMap<>();
                map.put("succeed", counter);
                map.put("fail", uids.length - counter + count);
                re.setData(map);

                for (String s : uids) {
                    User u = new User();
                    u.setUid(s);
                    part3Processor.userinfo(2, u);
                }
                return gson.toJson(re);
            } else {
                re.setCode(0);
                re.setInfo("全部失败");
                return gson.toJson(re);
            }
        }
    }

    public String getProfessionList(String orgID) {
        try {
            List<String> professionList = AllDao.getInstance().getOrgDao().getProfessionList(orgID);
            ResultBean re = new ResultBean();
            re.setCode(1);
            re.setData(professionList);
            return gson.toJson(re);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("发生异常");
        }
    }


    public String getGetRoleInfo(JsonObject paramObj, User user) {
        String key = null;
        int offset = 0;
        int limit = 0;
        try {
            key = paramObj.get("key").getAsString();
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0] - 1) * ls[1];
            limit = ls[1];
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("参数错误");
        }
        String orgID = user.getOrgID();
        Map<String, Object> map = new HashMap<>();
        map.put("orgID", user.getOrgID());
        List<Role> list = AllDao.getInstance().getSyRoleDao().searchRoles(key, offset, limit, orgID);
        for (Role role : list) {
            if ("1".equals(role.getRole_type())) {
                role.setResourceDesc("本科室资源");
            } else {
                map.put("roleid", role.getRoleid());
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResources(map);
                StringBuffer sb = new StringBuffer("");
                for (Resource r : resourcesList) {
                    if ("".equals(sb.toString())) {
                        sb.append(r.getSname());
                    } else {
                        sb.append(",").append(r.getSname());
                    }
                }
                role.setResourceDesc(sb.toString());
            }
        }
        int counter = AllDao.getInstance().getSyRoleDao().searchRolesCounter(key, orgID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String, Object> info = new HashMap<>();
        info.put("count", counter);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    public String getStaffTree(JsonObject paramObj, User user) {
        String key = null;
        try {
            key = paramObj.get("key").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数异常");
        }
        List<User> users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimit(key, user.getOrgID());
        Organization organization = getOrganization(user.getOrgID());
        // 将搜索到的用户挂在organization对象中
        organization = injectUsers(organization, users);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    /**
     * @param organization
     * @param users
     * @return
     */
    private Organization injectUsers(Organization organization, List<User> users) {
        String orgID = organization.getOrgID();
        // 搜索用户
        List<User> exList = searchUserByLabID(users, orgID);
        organization.setStaff(exList);
        List<Object> list = gson.fromJson(gson.toJson(organization.getLabs()), LinkedList.class);
        List<Lab> labList = new LinkedList<>();
        for (Object obj : list) {
            Lab lab = gson.fromJson(gson.toJson(obj), Lab.class);
            Lab exLab = injectUsersIntoLab(lab, users);
            labList.add(exLab);
        }
        organization.setLabs(labList);
        return organization;
    }

    private Lab injectUsersIntoLab(Lab lab, List<User> users) {
        String labID = lab.getLabID();
        List<User> exList = searchUserByLabID(users, labID);
        lab.setStaff(exList);
        if (lab.getSubLabs() != null) {
            List<Object> list = gson.fromJson(gson.toJson(lab.getSubLabs()), LinkedList.class);
            List<Lab> labList = new LinkedList<>();
            for (Object obj : list) {
                Lab subLab = gson.fromJson(gson.toJson(obj), Lab.class);
                Lab exLab = injectUsersIntoLab(subLab, users);
                labList.add(exLab);
            }
            lab.setSubLabs(labList);
        }
        return lab;
    }

    private List<User> searchUserByLabID(List<User> users, String labID) {
        List<User> exList = new LinkedList<>();
        for (User user : users) {
            if (labID.equals(user.getLabID())) {
                exList.add(user);
            }
        }
        return exList;
    }

    /**
     * @param paramObj
     * @param user
     * @param producerService
     * @return
     */
    public String deleteRoles(JsonArray paramObj, User user, ProducerService producerService) {
        Integer[] paraRoleids = null;
        try {
            paraRoleids = new Integer[paramObj.size()];
            for (int index = 0; index < paramObj.size(); index++) {
                paraRoleids[index] = paramObj.get(index).getAsInt();
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("参数错误");
        }
        List<Integer> checkedRoleids = new LinkedList<>();
        for (Integer roleid : paraRoleids) {
            List<String> userIds = AllDao.getInstance().getSyRoleDao().getUserIdByRole(roleid);
            for (String uid : userIds){
                producerService.checkOutPower(uid);
            }
            Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
            if (("0".equals(role.getRole_type()) || StringUtils.isEmpty(role.getRole_type())) && !checkedRoleids.contains(roleid)) {
                checkedRoleids.add(roleid);
            }
        }
        Map<String, Integer> map = new HashMap<>();
        if (checkedRoleids.size() > 0) {
            Integer[] roleids = checkedRoleids.toArray(new Integer[checkedRoleids.size()]);
            List<String> userIds = AllDao.getInstance().getSyUserDao().getAllUserIDByRoleID(roleids);
            AllDao.getInstance().getSyRoleDao().deleteRelationsByRoleids(roleids);
            AllDao.getInstance().getSyRoleDao().deleteRelationsWithReourcesByRoleids(roleids);
            int counter = AllDao.getInstance().getSyRoleDao().deleteRolesByRoleids(roleids);
            SyUserMapper.addSelectRelateUid(userIds);
            RedisUtil.updateUserOnLine(userIds);
            map.put("succeed", counter);
            map.put("fail", paraRoleids.length - counter);
        } else {
            map.put("succeed", 0);
            map.put("fail", paraRoleids.length);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(map);
        return gson.toJson(resultBean);
    }

    public String addRole(JsonObject paramObj, User user) {
        Role role = null;
        try {
            role = gson.fromJson(paramObj, Role.class);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        role.setOrgID(user.getOrgID());
        role.setCtime(LogUtils.getStringTime());
        role.setCreator(user.getUname());
        role.setCreatorID(user.getUid());
        if (role.getDesctext() == null) {
            role.setDesctext("");
        }
        if (role.getRole_type() == null) {
            role.setRole_type("0");
        }
        //从数据尝试获取角色
        Role exRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(), role.getRole());
        if (exRole != null) {
            return ParamUtils.errorParam("角色名称已经存在");
        } else {
            int counter = AllDao.getInstance().getSyRoleDao().insertUserRole(role);
            if (counter == 0) {
                return ParamUtils.errorParam("插入失败");
            } else {
                exRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(), role.getRole());
                List<String> uidList = (List<String>) role.getStaff();
                for (String uid : uidList) {
                    AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(exRole.getRoleid(), uid);
                }
                List<Object> resourceList = (List<Object>) role.getResources();
                for (Object resource : resourceList) {
                    Resource resourceObj = gson.fromJson(gson.toJson(resource), Resource.class);
                    resourceObj.setSorgID(user.getOrgID());
                    resourceObj.setRoleid(exRole.getRoleid());
                    AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resourceObj);
                }
                SyUserMapper.addSelectRelateUid(uidList);
                RedisUtil.updateUserOnLine(uidList);
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(1);
                return gson.toJson(resultBean);
            }
        }
    }

    private List<String> getNewAddRoleUserIdList(List<String> uidList, List<String> oldUidList) {
        List<String> resultList = new ArrayList<>();
        if (oldUidList == null || uidList == null){
            return resultList;
        }
        for (String str : uidList){
            if(!oldUidList.contains(str)){
                resultList.add(str);
            }
        }
        for (String str : oldUidList){
            if(!uidList.contains(str)){
                resultList.add(str);
            }
        }
        return resultList;
    }

    public String getRoleStaff(JsonObject paramObj, User user) {
        int offset = 0;
        int limit = 12;
        Integer roleid = null;
        try {
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0] - 1) * ls[1];
            limit = ls[1];
            roleid = paramObj.get("roleid").getAsInt();
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数错误");
        }
        Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
        if (role == null) {
            return ParamUtils.errorParam("该角色不存在");
        } else {
            List<User> list = AllDao.getInstance().getSyUserDao().getUserByRoleID(roleid, offset, limit);
            int counter = AllDao.getInstance().getSyUserDao().getUserByRoleIDCounter(roleid);
            Map<String, Object> info = new HashMap<>();
            info.put("count", counter);
            ResultBean result = new ResultBean();
            result.setCode(1);
            result.setData(list);
            result.setInfo(info);
            return gson.toJson(result);
        }
    }

    public String getRoleResource(JsonObject paramObj, User user) {
        int offset = 0;
        int limit = 12;
        Integer roleid = null;
        try {
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0] - 1) * ls[1];
            limit = ls[1];
            roleid = paramObj.get("roleid").getAsInt();
        } catch (Exception e) {
            return ParamUtils.errorParam("请求参数错误");
        }
        Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
        if (role == null) {
            return ParamUtils.errorParam("该角色不存在");
        } else {
            List<Resource> list = AllDao.getInstance().getSyResourceDao().getResourceByRoleID(user.getOrgID(), roleid, offset, limit);
            int counter = AllDao.getInstance().getSyResourceDao().getResourceByRoleIDCounter(user.getOrgID(), roleid);
            Map<String, Object> info = new HashMap<>();
            info.put("count", counter);
            ResultBean result = new ResultBean();
            result.setCode(1);
            result.setData(list);
            result.setInfo(info);
            return gson.toJson(result);
        }

    }

    public String editRole(JsonObject paramObj, User user, ProducerService producerService) {
        Role role = null;
        boolean proSennd = false;
        try {
            role = gson.fromJson(paramObj, Role.class);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        role.setOrgID(user.getOrgID());
        role.setCtime(LogUtils.getStringTime());
        if (role.getDesctext() == null) {
            role.setDesctext("");
        }
        if (role.getRole_type() == null) {
            role.setRole_type("");
        }
        if (role.getRoleid() == null) {
            return ParamUtils.errorParam("无角色id");
        }
        Role exRole = AllDao.getInstance().getSyRoleDao().getRoleByroleid(role.getRoleid());
        List<String> uids = (List<String>) role.getStaff();
        if (exRole == null) {
            return ParamUtils.errorParam("该角色id对应角色不存在");
        } else {
            List<String> updateUids = AllDao.getInstance().getSyRoleDao().getUserIdByRole(role.getRoleid());
            List<String> newAddUids = getNewAddRoleUserIdList(uids,updateUids);
            if (updateUids == null) updateUids = new LinkedList<>();
            updateUids.addAll(uids);
            String roleName = role.getRole();
            if ("1".equals(exRole.getRole_type()) || "1".equals(exRole.getRole_privilege())) {
                Role role1 = AllDao.getInstance().getSyRoleDao().getRoleByroleid(role.getRoleid());
                if (role1 != null) {//
                    Integer[] roleids = new Integer[]{role.getRoleid()};
                    AllDao.getInstance().getSyRoleDao().deleteRelationsByRoleids(roleids);//删除原有的关联关系
                    for (String uid : uids) {
                        AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(exRole.getRoleid(), uid);//插入新的
                    }
                    ResultBean resultBean = new ResultBean();
                    resultBean.setCode(1);
                    resultBean.setInfo("系统角色 更新完成");
                    SyUserMapper.addSelectRelateUid(updateUids);
                    RedisUtil.updateUserOnLine(new TreeSet<String>(updateUids));
                    producerService.checkOutPowerByUids(newAddUids);
                    return gson.toJson(resultBean);
                } else {
                    return ParamUtils.errorParam("该角色id对应角色不存在");
                }
            }
            //如果更新角色了名称
            if (!roleName.equals(exRole.getRole())) {
                //如果更新后的名字已经存在
                Role exRenameRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(), roleName);
                if (exRenameRole != null) {
                    return ParamUtils.errorParam("角色已经存在");
                }
                //todo  因为名字发生改变 适用所有人 考虑 可以更新所有人 但是意义不大 暂不更新
            }
            int counter = AllDao.getInstance().getSyRoleDao().updateUserRole(role);//更新用户信息
            List<Resource> oldResourceList = AllDao.getInstance().getSyResourceDao().getResourcesByRoleId(role.getRoleid());
            if (counter == 0) {
                return ParamUtils.errorParam("更新失败");
            } else {
                List<String> uidList = (List<String>) role.getStaff();
                Integer[] roleids = new Integer[]{role.getRoleid()};
                AllDao.getInstance().getSyRoleDao().deleteRelationsByRoleids(roleids);//删除原有的关联关系
                for (String uid : uidList) {
                    AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(exRole.getRoleid(), uid);//插入新的
                }
                List<Object> resourceList = (List<Object>) role.getResources();
                List<Resource> resourcesList = new LinkedList<>();
                AllDao.getInstance().getSyRoleDao().deleteRelationsWithReourcesByRoleids(roleids);//删除原有关联关系
                for (Object resource : resourceList) {
                    Resource resourceObj = gson.fromJson(gson.toJson(resource), Resource.class);
                    resourceObj.setSorgID(user.getOrgID());
                    resourceObj.setRoleid(role.getRoleid());
                    resourcesList.add(resourceObj);
                    AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resourceObj);//插入新的
                }
                proSennd = getIsProSennd(oldResourceList,resourcesList);
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(1);
                SyUserMapper.addSelectRelateUid(updateUids);
                RedisUtil.updateUserOnLine(new TreeSet<String>(updateUids));
                if(proSennd){
                    //发生改变 通知所有人
                    producerService.checkOutPowerByUids(uids);
                }else {
                    //未发生改变 通知新加的人
                    producerService.checkOutPowerByUids(newAddUids);
                }
                return gson.toJson(resultBean);
            }
        }
    }

    private boolean getIsProSennd(List<Resource> oldResourceList, List<Resource> resourceList) {
        if(oldResourceList == null){
            return false;
        }
        if(oldResourceList.size() != resourceList.size()){
            return true;
        }
        for (Resource ro : oldResourceList){
            if(!resourceList.contains(ro)){
                return true;
            }
        }
        for (Resource resource : resourceList) {
            if(!oldResourceList.contains(resource)){
                return true;
            }
        }
        return false;
    }

    public String getResourceTree(JsonObject paramObj, User user) {
        String type = null;
        String roleid = null;
        String key = null;
        JsonArray labIds = null;
        if (paramObj.has("keywords")) {
            key = paramObj.get("keywords").getAsString();
        }
        if (paramObj.has("roleid")) {
            JsonElement jsonElement = paramObj.get("roleid");
            if (jsonElement.isJsonPrimitive()) {
                roleid = jsonElement.getAsString();
            }
        }
        if (paramObj.has("labId")) {
            labIds = paramObj.get("labId").getAsJsonArray();
        }
        try {
            if (paramObj.has("type")) {
                type = paramObj.get("type").getAsString();
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        List<LabResource> list = AllDao.getInstance().getSyResourceDao().getLabResourcesByOrgID(user.getOrgID(), type);


        if (list != null && list.size() > 0) {
            Organization organization = null;
            if (StringUtils.isEmpty(key)) {
                organization = getOrganization(user.getOrgID(), roleid);
            } else {
                organization = getOrganization(user.getOrgID(), key, "true", null, null, labIds);
            }
//            organization.setLabs(injectResource(organization,organization.getLabs(), list));
            //去掉本科室
//            for (LabResource labResource : list) {
//                if ("本科室资源".equals(labResource.getSname())) {
//                    Lab lab = new Lab();
//                    lab.setLab_name(labResource.getSlab_name());
//                    lab.setSid(labResource.getSid());
//                    List<Lab> spLab = new LinkedList<>();
//                    spLab.add(lab);
//                    organization.setSpResource(spLab);
//                }
//            }
            List<Object> objlist = gson.fromJson(gson.toJson(organization.getLabs()), LinkedList.class);
            if (objlist.size() == 0) {
                organization.setEmpty(true);
            }
            ResultBean re = new ResultBean();
            re.setCode(1);
            if (organization.isEmpty()) {
                re.setData(new Organization());
            } else {
                re.setData(organization);
            }
            return gson.toJson(re);
        } else {
            return ParamUtils.errorParam("该类型资源现在不支持");
        }
    }

    private Object injectResource(Organization organization, Object labs, List<LabResource> list) {
        List<Object> objlist = gson.fromJson(gson.toJson(labs), LinkedList.class);
        List<Lab> labList = new LinkedList<>();
        for (Object obj : objlist) {
            Lab lab = gson.fromJson(gson.toJson(obj), Lab.class);
            //判断科室对应的资源是否存在
            boolean ex = resourceStillExist(list, lab.getLabID());
            if (ex) {
                lab.setSid(lab.getLabID());
                labList.add(lab);
            }
            if (lab.getSubLabs() != null) {
                Object subLab = injectResource(organization, lab.getSubLabs(), list);
                lab.setSubLabs(subLab);
            }
        }
        //位空 则全空
        return labList;
    }

    private boolean resourceStillExist(List<LabResource> list, String labID) {
        for (LabResource labResource : list) {
            if (labID.equals(labResource.getSid())) {
                return true;
            }
        }
        return false;
    }

    public String groupList(JsonObject paramObj, User user) {
        String skey = null;
        String limit = null;
        try {
            skey = paramObj.get("key").getAsString();
            limit = paramObj.get("limit").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        int[] result = ParamUtils.parseLimit(limit);
        Map<String, Object> conf = new HashMap<String, Object>();
        int startIndex = result[0];
        int maxNum = result[1];
        conf.put("orgID", user.getOrgID());
        conf.put("startIndex", (startIndex - 1) * maxNum);
        conf.put("maxNum", maxNum);
        conf.put("skey", skey);
        List<Group> list = AllDao.getInstance().getGroupDao().getGroupsBySearchName(conf);
        Integer counter = AllDao.getInstance().getGroupDao().getGroupsBySearchNameCounter(conf);
        Map<String, Object> map = new HashMap<>();
        map.put("orgID", user.getOrgID());
        for (Group group : list) {
            map.put("gid", group.getGid());
            List<User> userList = AllDao.getInstance().getGroupDao().getUsersByGroupID(map);
            if (userList.size() > 5) {
                userList = userList.subList(0, 5);
            }
            User creator = AllDao.getInstance().getSyUserDao().getUserByUid(group.getGroupCreator());
            if (creator != null) {
                group.setGroupCreatName(creator.getUname());
            }
            group.setMembers(userList);
        }
        Map<String, Object> info = new HashMap<>();
        info.put("count", counter);
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setData(list);
        re.setInfo(info);
        return gson.toJson(re);
    }

    public String addGroup(String param, User user) {
        Group group = null;
        try {
            group = gson.fromJson(param, Group.class);
            if (StringUtils.isEmpty(group.getGroupName())) {
                return ParamUtils.errorParam("插入失败,小组名称不能为空");
            }
            if (group.getGroupName().length() > 20) {
                return ParamUtils.errorParam("插入失败,小组名称必须小于20个字符");
            }
            if (!StringUtils.isEmpty(group.getGroupDesc()) && group.getGroupDesc().length() > 20) {
                return ParamUtils.errorParam("插入失败,小组描述必须小于20个字符");
            }
        } catch (Exception e) {
            logger.error("参数异常 ", e);
            return ParamUtils.errorParam("参数异常");
        }
        if (checkGroupName(user, group)) return ParamUtils.errorParam("小组名称已存在");
        UUID uuid = UUID.randomUUID();
        group.setOrgID(user.getOrgID());
        group.setGid(uuid.toString() + Long.toHexString(System.currentTimeMillis()));
        group.setGroupCreator(user.getUid());
        group.setGroupCreatName(LogUtils.getStringTime());

        int counter = AllDao.getInstance().getGroupDao().insertOneGroup(group);
        if (counter != 1) {
            return ParamUtils.errorParam("插入失败");
        } else {
            List<String> list = (List<String>) group.getMembers();
            Map<String, Object> map = new HashMap<>();
            map.put("gid", group.getGid());
            map.put("orgID", group.getOrgID());
            if (list != null && list.size() > 0) {
                for (String uid : list) {
                    map.put("uid", uid);
                    AllDao.getInstance().getGroupDao().insertOneGroupRelationUid(map);
                }
                SyUserMapper.addSelectRelateUid(list);
                RedisUtil.updateUserOnLine(new TreeSet<String>(list));
            }
        }
        ResultBean re = new ResultBean();
        re.setCode(1);
        return gson.toJson(re);
    }

    public boolean checkGroupName(User user, Group group) {
        Map<String, Object> fmap = new HashMap<>();
        fmap.put("groupName", group.getGroupName());
        if (!StringUtils.isEmpty(group.getGid())) fmap.put("gid", group.getGid());
        fmap.put("orgID", user.getOrgID());
        if (AllDao.getInstance().getGroupDao().getGroupsByName(fmap).size() > 0) {
            return true;
        }
        return false;
    }

    public String editGroup(String param, User user, ProducerService producerService) {
        Group group = null;
        boolean proSenndAll = false;
        try {
            group = gson.fromJson(param, Group.class);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数异常");
        }
        if (checkGroupName(user, group)) return ParamUtils.errorParam("小组名称已存在");
        Group oldGroup = AllDao.getInstance().getGroupDao().getGroupByGroupId(group.getGid());
        proSenndAll = oldGroup.equals(group);
        List<String> list = (List<String>) group.getMembers();
        List<String> uids = AllDao.getInstance().getGroupDao().getGroupRelationUid(group.getGid());
        List<String> addGroupUids = getNewAddRoleUserIdList(list,uids);
        if (uids == null) uids = new LinkedList<>();
        int count = AllDao.getInstance().getGroupDao().updateOneGroup(group);
        ResultBean re = new ResultBean();
        if (count == 1) {
            AllDao.getInstance().getGroupDao().deleteGroupRelationUid(group.getGid());
            Map<String, Object> map = new HashMap<>();
            map.put("gid", group.getGid());
            map.put("orgID", user.getOrgID());
            for (String uid : list) {
                map.put("uid", uid);
                AllDao.getInstance().getGroupDao().insertOneGroupRelationUid(map);
            }
            uids.addAll(list);
            SyUserMapper.addSelectRelateUid(uids);
            RedisUtil.updateUserOnLine(new TreeSet<String>(uids));
            re.setCode(1);
        } else {
            re.setCode(0);
        }
        if(proSenndAll){
            producerService.checkOutPowerByUids(addGroupUids);
        }else {
            producerService.checkOutPowerByUids(list);
        }
        return gson.toJson(re);
    }

    public String groupMembers(String param, User user) {
        String skey = null;
        String limit = null;
        String groupID = null;
        try {
            JsonObject jsonObj = (JsonObject) jsonParser.parse(param);
            skey = jsonObj.get("key").getAsString();
            limit = jsonObj.get("limit").getAsString();
            groupID = jsonObj.get("groupID").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        int[] result = ParamUtils.parseLimit(limit);
        Map<String, Object> map = new HashMap<>();
        map.put("orgID", user.getOrgID());
        map.put("gid", groupID);
        int startIndex = result[0];
        int maxNum = result[1];
        map.put("startIndex", (startIndex - 1) * maxNum);
        map.put("maxNum", maxNum);
        map.put("skey", skey);
        List<User> userList = AllDao.getInstance().getGroupDao().getUsersBySearchNameGroupID(map);
        int counter = AllDao.getInstance().getGroupDao().getUsersBySearchNameGroupIDCounter(map);
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setData(userList);
        Map<String, Object> info = new HashMap<>();
        info.put("count", counter);
        re.setInfo(info);
        return gson.toJson(re);
    }

    public String resetPassword(String param, User user) {
        List<String> uids = null;
        try {
            JsonObject jsonObj = (JsonObject) jsonParser.parse(param);
            uids = gson.fromJson(jsonObj.get("uid").getAsJsonArray(), new TypeToken<LinkedList<String>>() {
            }.getType());
            if (uids == null || uids.size() == 0) return ParamUtils.errorParam("参数错误");
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        String pwd = GStringUtils.getDefaultPasswd();
        int count = AllDao.getInstance().getSyUserDao().updatePWDByUids(uids.toArray(new String[uids.size()]), pwd, user.getOrgID());
        if (count > 0) {
            RedisUtil.exit(uids);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            return gson.toJson(resultBean);
        } else {
            return ParamUtils.errorParam("操作失败");
        }
    }

    public String isExistGroupName(String param, User user) {
        String groupName = null;
        try {
            JsonObject jsonObj = (JsonObject) jsonParser.parse(param);
            groupName = jsonObj.get("groupName").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("groupName", groupName);
        map.put("orgID", user.getOrgID());
        List<Group> list = AllDao.getInstance().getGroupDao().getGroupsByName(map);
        Map<String, Object> info = new HashMap<>();
        if (list == null || list.size() == 0) {
            info.put("count", 0);
        } else {
            info.put("count", list.size());
        }
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setInfo(info);
        return gson.toJson(re);
    }

    public String deleteGroup(String param, User user, ProducerService producerService) {
        Set<String> set = new HashSet<>();
        try {
            JsonArray gArray = (JsonArray) jsonParser.parse(param);
            for (JsonElement item : gArray) {
                set.add(item.getAsString());
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("参数异常");
        }
        Map<String, Object> data = new HashMap<>();
        int succeed = 0;
        int fail = 0;
        TreeSet<String> uids = new TreeSet<>();
        for (String gid : set) {
            int count = AllDao.getInstance().getGroupDao().deleteGroupByGID(gid);
            List<String> userIds = AllDao.getInstance().getGroupDao().getGroupRelationUid(gid);
            for (String uid : userIds){
                producerService.checkOutPower(uid);
            }
            if (count == 1) {
                data.put(gid, true);
                List<String> uidList = AllDao.getInstance().getSyUserDao().getAllUserIDByGroupID(gid);
                AllDao.getInstance().getGroupDao().deleteGroupRelationUid(gid);
                uids.addAll(uidList);
                succeed++;
            } else {
                data.put(gid, false);
                fail++;
            }
        }
        if (uids != null && uids.size() > 0) {
            SyUserMapper.addSelectRelateUid(uids);
            RedisUtil.updateUserOnLine(uids);
        }
        Map<String, Object> info = new HashMap<>();
        info.put("succeed", succeed);
        info.put("fail", fail);
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setData(data);
        re.setInfo(info);
        return gson.toJson(re);
    }

    public String passwordCorrect(User user, String param) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            String uid = user.getUid();
            String pwd = paramObj.get("pwd").getAsString();
            pwd = GStringUtils.str2Password(pwd);
            // TODO 需要考虑密码转化为MD5值 时的处理
            String pwdFromMysql = AllDao.getInstance().getSyUserDao().getPwdByUid(uid);
            if (pwdFromMysql.equals(pwd)) {
                String time = simpleDateFormat.format(new Date());
                String md5 = GStringUtils.getMD5(time);
                // TODO md5 插入到数据库
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("md5", md5);
                int counter = AllDao.getInstance().getSyUserDao().insertMd5ByUid(map);
                if (counter == 0) {
                    logger.error("更新md5失败");
                    re.setCode(0);
                    re.setData("更新md5失败");
                } else {
                    re.setCode(1);
                    re.setData(time);
                }
            } else {
                re.setCode(1);
                re.setData("wrong");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("验证当前密码操作失败");
        }
        return gson.toJson(re);
    }

    public String updatePwdByUid(User user, String param) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            int ret = 0;
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            String pwd = GStringUtils.str2Password(paramObj.get("pwd").getAsString());
            String md5 = GStringUtils.getMD5(paramObj.get("token").getAsString());
            String uid = user.getUid();
            String mysqlMd5 = AllDao.getInstance().getSyUserDao().getMd5ByUid(uid);
            if (md5.equals(mysqlMd5)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("pwd", pwd);
                ret = AllDao.getInstance().getSyUserDao().updatePwdByUid(map);
            }
            if (ret == 1) {
                re.setCode(1);
                re.setInfo("success");

                user.setPwd(pwd);
                part3Processor.userinfo(1, user);
            } else {
                re.setCode(0);
                re.setInfo("error");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("设置新密码操作失败");
        }
        return gson.toJson(re);
    }

    public String adminResetPassword(String param) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            JsonObject paramObj = jsonParser.parse(param).getAsJsonObject();
            String uid = paramObj.get("uid").getAsString();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", uid);
            map.put("pwd", GStringUtils.getDefaultPasswd());
            int ret = AllDao.getInstance().getSyUserDao().adminResetPassword(map);
            if (ret == 1) {
                // 如果用户已经登入则 将该用户 的Redis的数据清空
                RedisUtil.userLogoutByUid(uid);
                re.setCode(1);
                re.setInfo("success");


                User userByUid = AllDao.getInstance().getSyUserDao().getUserByUid(uid);
                userByUid.setPwd(GStringUtils.getDefaultPasswd());
                part3Processor.userinfo(1, userByUid);
            } else {
                re.setCode(0);
                re.setInfo("wrong");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("管理员重置密码操作失败");
        }
        return gson.toJson(re);
    }

    public String isDefaultPassword(User user) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            re.setCode(1);
            String uid = user.getUid();
            String pwd = AllDao.getInstance().getSyUserDao().getPasswordByUid(uid);
            if ("ls123456".equals(pwd) || "d8b8f198d882909be0a58778875953e8".equals(pwd)) {
                re.setInfo(true);
            } else {
                re.setInfo(false);
            }
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String failTime = AllDao.getInstance().getSyUserDao().getFailureTimeByUid(uid);
            String effecTime = AllDao.getInstance().getSyUserDao().getEffectiveTimeByUid(uid);
            Date date = new Date();
            if (!("长期有效".equals(user.getStatus()))) {
                if ("禁用".equals(user.getStatus())) {
                    return "false";
                }
                if (date.after(time.parse(failTime)) || date.before(time.parse(effecTime))) {
                    return "false";
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("判断当前密码是否为默认密码操作失败");
        }
        return gson.toJson(re);
    }

    //判断lab名字是否存在
    public String isExistLabName(JsonObject paramObj) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            String labName = paramObj.get("lab_name").getAsString();
            if (StringUtils.isEmpty(labName)) {
                return ParamUtils.errorParam(labName + "科室名字不能为空");
            }
            Lab lab = AllDao.getInstance().getOrgDao().getLabByOnelabName(labName);
            if (lab != null) {
                re.setCode(0);
                re.setInfo("科室名称不可用");
            } else {
                re.setCode(1);
                re.setInfo("名称可用");
            }

        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("判断当前科室名称是否存在操作失败");
        }
        return gson.toJson(re);
    }

    public void addAllRole() {
        Role roleAll = AllDao.getInstance().getSyRoleDao().getRoleByRole("全院科室成员");
        if (roleAll == null) {

            roleAll = new Role();
            Role roleLab = AllDao.getInstance().getSyRoleDao().getRoleByroleid(1);
            roleAll.setRole("全院科室成员");
            roleAll.setOrgID(roleLab.getOrgID());
            roleAll.setRole_type("0");
            roleAll.setDesctext("全院科室资源");
            roleAll.setRole_privilege("1");
            Integer i = AllDao.getInstance().getSyRoleDao().insertUserRole(roleAll);
            roleAll = AllDao.getInstance().getSyRoleDao().getRoleByRole("全院科室成员");
            logger.info("全院科室成员 角色创建成功");
            //增加 功能 resource
            Resource resource = new Resource();
            resource.setRoleid(roleAll.getRoleid());
            resource.setSid(roleAll.getOrgID() + "-all");
            resource.setSorgID(roleAll.getOrgID());
            resource.setHas_search("有");
            resource.setHas_searchExport("有");
            AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resource);
            //增加 科室resource
//            LabResource labResource=new LabResource();
//            labResource.setSid(roleAll.getOrgID());
//            labResource.setSname("全数据资源");
//            labResource.setSdesc("全部数据资源");
//            labResource.setStype("");
//            labResource.setSlab_name("全数据");
//            labResource.setSorgID(roleAll.getOrgID());
//            labResource.setSlab_parent(roleAll.getOrgID());
//            labResource.setStype_role("2");
//            AllDao.getInstance().getSyResourceDao().insertOneResource(labResource);


        }
    }

    public String isExistRoleName(JsonObject paramObj) {
        ResultBean re = null;
        try {
            re = new ResultBean();
            String roleName = paramObj.get("role_name").getAsString();
            if (StringUtils.isEmpty(roleName)) {
                return ParamUtils.errorParam(roleName + "角色名字不能为空");
            }
            Role role = AllDao.getInstance().getSyRoleDao().getRoleByRole(roleName);
            if (role != null) {
                re.setCode(0);
                re.setInfo("角色名称不可用");
            } else {
                re.setCode(1);
                re.setInfo("名称可用");
            }

        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("判断当前科室名称是否存在操作失败");
        }
        return gson.toJson(re);
    }

    public String getOrgInfo(JsonObject paramObj, User user) {
        String labID = null;
        String key = null;
        String limitStr = null;
        Integer offset = null;
        Integer limit = null;
        try {
            labID = paramObj.get("labID").getAsString();
            key = paramObj.get("key").getAsString();
            if (paramObj.has("limit")) {
                limitStr = paramObj.get("limit").getAsString();
                int[] ls = ParamUtils.parseLimit(limitStr);
                offset = (ls[0] - 1) * ls[1];
                limit = ls[1];
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        List<Lab> labs = null;
        ResultBean re = new ResultBean();
        re.setCode(1);
        if ("".equals(labID)) {
            if (offset != null && limit != null) {
//                labs = AllDao.getInstance().getOrgDao().getLabsByOrgID(key, offset, limit, user.getOrgID());
                labs = AllDao.getInstance().getOrgDao().searchLabByOrgIDNoLimit(key, user.getOrgID());

            } else {
                labs = AllDao.getInstance().getOrgDao().searchLabByOrgIDNoLimit(key, user.getOrgID());
            }
        } else {
            List<Lab> labAll=AllDao.getInstance().getOrgDao().getLabs(user.getOrgID());
            //labID 集合
            List<String> list = new LinkedList<>();
            list.add(labID);
            getLabIDs(labAll, list, labID);
            String labids[] = list.toArray(new String[list.size()]);
            if (offset != null && limit != null) {
//                labs = AllDao.getInstance().getOrgDao().getLabsBypartId(labids,key,user.getOrgID(),offset,limit);
                labs = AllDao.getInstance().getOrgDao().getLabsBypartIdNoLimit(labids, key, user.getOrgID());
            } else {
                labs = AllDao.getInstance().getOrgDao().getLabsBypartIdNoLimit(labids, key, user.getOrgID());
            }
        }

        for (Lab lab : labs) {
            String pname = AllDao.getInstance().getOrgDao().getlabnameBylabID(lab.getLab_parent());
            lab.setParentLabName(pname);
        }

        re.setData(labs);
        return gson.toJson(re);
    }

    public void getLabIDs(List<Lab> labs, List<String> list, String labID) {
        for (Lab lab : labs) {
            if (labID.equals(lab.getLab_parent())) {
                list.add(lab.getLabID());
                getLabIDs(labs, list, lab.getLabID());
            }
        }
    }

    public String isAccountLose(User user) {
        ResultBean re = null;
        boolean isAccountLose = true;
        try {
            re = new ResultBean();
            String uid = user.getUid();

            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String failTime = AllDao.getInstance().getSyUserDao().getFailureTimeByUid(uid);
            String effecTime = AllDao.getInstance().getSyUserDao().getEffectiveTimeByUid(uid);
            Date date = new Date();
            if (!("长期有效".equals(user.getStatus()))) {
                if ("禁用".equals(user.getStatus())) {
                    isAccountLose = false;
                }
                if (date.after(time.parse(failTime)) || date.before(time.parse(effecTime))) {
                    isAccountLose = false;
                }
            }

            if (isAccountLose) {
                re.setCode(1);
                re.setInfo("false");
            } else {
                re.setCode(0);
                re.setInfo("账户失效，请联系管理员");
            }

        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("判断当前密码是否为默认密码操作失败");
        }
        return gson.toJson(re);


    }

    public String deleteRedisKey(JsonObject paramObj, User user) {

        ResultBean result = null;
        String key = null;
        try {
            key = paramObj.get("key").getAsString();
            result = new ResultBean();
            RedisUtil.deleteKey(key);
            result.setCode(1);
            result.setInfo("删除成功");
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("判断当前科室名称是否存在操作失败");
        }
        return gson.toJson(result);
    }

    public String getStaffInfoForRws(JsonObject paramObj, User user) {
        String labID = null;
        String key = null;
        String limitStr = null;
        Integer offset = null;
        Integer limit = null;
        String projectId = null;
        try {
            projectId = paramObj.get("projectId").getAsString();
            labID = paramObj.get("labID").getAsString();
            key = paramObj.get("key").getAsString();
            if (paramObj.has("limit")) {
                limitStr = paramObj.get("limit").getAsString();
                int[] ls = ParamUtils.parseLimit(limitStr);
                offset = (ls[0] - 1) * ls[1];
                limit = ls[1];
            }
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        String url = "";
        String projectUser = HttpRequestUtils.httpPost(url,projectId);
        JsonArray proArray = (JsonArray) jsonParser.parse(projectUser);

        List<String> uidList = new ArrayList<>();
        for(JsonElement element : proArray){
            if(element.isJsonObject()){
                JsonObject obj = element.getAsJsonObject();
                String uid = obj.get("uid").getAsString();
                uidList.add(uid);
            }
        }

        List<User> users = null;
        ResultBean re = new ResultBean();
        re.setCode(1);

        if (offset != null && limit != null) {
            users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDRws(key, offset, limit, user.getOrgID(),uidList);

        } else {
            users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimitRws(key, user.getOrgID(),uidList);
        }
        Long counter = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDCounter(key, user.getOrgID(),null);
        Map<String, Object> info = new HashMap<>();
        info.put("count", counter);
        re.setInfo(info);

        re.setData(users);
        return gson.toJson(re);

//        for (User usr : users) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("orgID", usr.getOrgID());
//            map.put("uid", usr.getUid());
//            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(map);
//            if (rolesList != null) {
//                for (Role role : rolesList) {
//                    StringBuffer stringBuffer = new StringBuffer();
//                    List<Resource> reList = AllDao.getInstance().getSyResourceDao().getResourceByRoleID(user.getOrgID(), role.getRoleid(), 0, 3000);
//                    if (reList != null) {
//                        for (Resource resource : reList) {
//                            stringBuffer.append(resource.getSdesc()).append(",");
//                        }
//                    }
//                    if (stringBuffer.toString().length() > 1) {
//                        role.setResourceDesc(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
//                    }
//                }
//            }
//            usr.setRoles(rolesList);
//        }

    }

    public String sendSystemMessage(JsonObject paramObj, ProducerService producerService) {
        ResultBean result = null;
        String detail = null;
        String content = null;
        try {
            detail = paramObj.get("detail").getAsString();
            content = paramObj.get("content").getAsString();
            producerService.sendSystemMessage(content,detail);
            result = new ResultBean();
            result.setCode(1);
            result.setInfo("发送成功");
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("发送失败");
        }
        return gson.toJson(result);
    }
}
