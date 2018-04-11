package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.OrgMapper;
import com.gennlife.platform.dao.SyUserMapper;
import com.gennlife.platform.model.*;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen-song on 16/9/12.
 */
public class LaboratoryProcessor {
    private static Logger logger = LoggerFactory.getLogger(LaboratoryProcessor.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取科室组织信息
     *
     * @param user
     * @return
     */
    public String orgMapData(User user,String key) {
        String orgID = user.getOrgID();
        //通过科室数据，初始化的数据结构
        Organization organization = getOrganization(orgID,key);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public static List<Lab> generateLabTree(List<Lab> labs, String key, int maxLevel) {
        List<Lab> result = new LinkedList<>();
        if (labs != null) {
            for (Lab lab : labs) {
                if (lab.getLab_parent().equals(key)) {
                    lab.setOrgID(null);
                    result.add(lab);
                    if (maxLevel >= lab.getLab_level()) {
                        List<Lab> subLabs = generateLabTree(labs, lab.getLabID(), maxLevel);
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
            depart_name = paramObj.get("depart_name").getAsString();
            if (paramObj.has("lab_leader")) {
                lab_leader = paramObj.get("lab_leader").getAsString();
            }
            if(!("行政管理类".equals(depart_name)||"业务管理类".equals(depart_name)||"一线临床类".equals(depart_name))){
                return ParamUtils.errorParam("请求参数有错误");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        User user = UserProcessor.getUserByUids(uid);
        String orgID = user.getOrgID();
        Organization organization = null;
        Map<String, Object> map = new HashMap<>();
        List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
        if (labNames.contains(lab_name)) {
            return ParamUtils.errorParam(lab_name + "已经存在");
        }
        // 生成科室id
        String labID = orgID + "-" + ChineseToEnglish.getPingYin(lab_name);
        //
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
        String key="鼻咽";
        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs =null;
        Integer maxLevel =null;
//        if(key==null){
//            labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
//        }else {
//            labs = AllDao.getInstance().getOrgDao().getLabsBylabId(key,orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevelBylabId(key,orgID);
//        }
        labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
        if(!StringUtils.isEmpty(key)){
            Set<Lab> resultlabs=new HashSet<>();
            spellLab(labs,key,resultlabs);
            labs =new LinkedList<>(resultlabs);
        }
        if (maxLevel == null) {
            return organization;
        }
        List<Lab> treeLabs = generateLabTree(labs, orgID, maxLevel);
        organization.setLabs(treeLabs);
        return organization;
    }
    public static Organization getOrganization(String orgID,String key) {
        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs =null;
        Integer maxLevel =null;
//        if(key==null){
//            labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
//        }else {
//            labs = AllDao.getInstance().getOrgDao().getLabsBylabId(key,orgID);
//            maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevelBylabId(key,orgID);
//        }
        labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
        if(!StringUtils.isEmpty(key)){
            Set<Lab> resultlabs=new HashSet<>();
            spellLab(labs,key,resultlabs);
            labs =new LinkedList<>(resultlabs);
        }
        if (maxLevel == null) {
            return organization;
        }
        List<Lab> treeLabs = generateLabTree(labs, orgID, maxLevel);
        organization.setLabs(treeLabs);
        return organization;
    }

    public static void spellLab(List<Lab> labs,String key,Set<Lab> resultlabs){

        for(Lab lab :labs){
            String lab_name=lab.getLab_name();
            if(lab_name.contains(key) || lab.getLabID().contains(key)){
                if(!resultlabs.contains(lab)){
                    lab.setLab_name(lab_name.replaceAll(key,"<span style='color:red'>" + lab_name + "</span>"));
                    resultlabs.add(lab);
                    if(lab.getLab_parent()!=null && !("hospital_1".equals(lab.getLab_parent()) )){
                        spellLab(labs,lab.getLab_parent(),resultlabs);
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
        String[] labIDs = labIDsList.toArray(new String[labIDsList.size()]);
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
        alllab = new TreeSet<>(AllDao.getInstance().getSyResourceDao().selectDeleteLabsReource(labIDs));
        labIDs = alllab.toArray(new String[alllab.size()]);
        int counter = AllDao.getInstance().getOrgDao().deleteLabs(labIDs);
        List<String> uids = AllDao.getInstance().getSyUserDao().getRelateUserByLabId(labIDs, orgID);
        TreeSet<String> allUids = new TreeSet<>();
        if (uids != null && uids.size() > 0) {
            allUids.addAll(uids);
            SyUserMapper.addSelectRelateUid(allUids);
        }
        // 同步删除资源
        AllDao.getInstance().getSyResourceDao().deleteLabsReource(labIDs);
        AllDao.getInstance().getSyResourceDao().deleteRoleItem(labIDs);
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
        String depart_name="";
        try {
            labID = paramObj.get("labID").getAsString();
            lab_name = paramObj.get("lab_name").getAsString();
            depart_name=paramObj.get("depart_name").getAsString();
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
        Lab lab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
        if (lab == null) {
            return ParamUtils.errorParam(labID + "无此科室");
        }
        if (!lab.getLab_name().equals(lab_name)) { // 如果科室的名称修改啦,需要检查新的名称是否已经存在
            List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
            if (labNames.contains(lab_name)) {
                return ParamUtils.errorParam(lab_name + "已经存在");
            }
            // 当前科室下面的用户科室名称

            AllDao.getInstance().getSyUserDao().updateUserLabNameByLabName(lab_name, lab.getLab_name(), orgID);
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

            List<String> userIds = AllDao.getInstance().getSyUserDao().getUserIDByLabID(labID, orgID);
            // 更新缓存
            RedisUtil.updateUserOnLine(userIds);
        }
        if (!lab.getLab_parent().equals(lab_parent)
                && !lab_parent.equals(orgID)) { // 改变了上级部门,并且改变的上级部门不是当前医院,需要检查上级部门是否存在
            Lab parentLab = AllDao.getInstance().getOrgDao().getLabBylabID(lab_parent);
            if (parentLab == null) {
                return ParamUtils.errorParam("上级部门不存在");
            }
        }
        Integer lab_level = 1;
        if (!lab_parent.equals(orgID)) {
            map.put("orgID", orgID);
            map.put("labID", lab_parent);
            lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            map.put("lab_level", lab_level + 1);
        } else {
            map.put("lab_level", lab_level);
        }
        map.put("labID", labID);
        map.put("lab_name", lab_name);
        map.put("lab_leader", lab_leader);
        map.put("lab_leaderName", lab_leaderName);
        map.put("lab_parent", lab_parent);
        map.put("depart_name",depart_name);//-增加 depart_name
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
        List<User> users = null;
        ResultBean re = new ResultBean();
        re.setCode(1);
        if ("".equals(labID)) {
            if (offset != null && limit != null) {
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgID(key, offset, limit, user.getOrgID());

            } else {
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimit(key, user.getOrgID());
            }
            Long counter = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDCounter(key, user.getOrgID());
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
            String effective=usr.getEffective_time();
            String failure=usr.getFailure_time();
            if("禁用".equals(usr.getStatus())){
                usr.setStatus_now("当前不可用");
            }
            if("定期有效".equals(usr.getStatus())){
                if(LogUtils.decideDate(effective,failure)){
                    usr.setStatus_now("当前可用");
                }else{
                    usr.setStatus_now("当前不可用");
                }
            }
            if("长期有效".equals(usr.getStatus())){
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
        adduser.setOrgID(user.getOrgID());
        adduser.setCtime(LogUtils.getStringTime());
        adduser.setUptime(LogUtils.getStringTime());
        adduser.setPwd(GStringUtils.getDefaultPasswd());
        adduser.setOrg_name(user.getOrg_name());
        JsonObject re = insertUser(adduser);
        // MemCachedUtil.setUserWithTime(adduser.getUid(),adduser, UserController.sessionTimeOut);
        return gson.toJson(re);
    }


    public JsonObject insertUser(User adduser) {
        JsonObject resultBean = new JsonObject();
        String email = adduser.getUemail();
        String name = adduser.getUname();
        String unumber = adduser.getUnumber();
        String lab_name = adduser.getLab_name();
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
            int counter = AllDao.getInstance().getSyUserDao().deleteUserByUids(uids);
            RedisUtil.updateUserOnLine(uidsList);
            if (counter > 0) {
                re.setCode(1);
                Map<String, Object> map = new HashMap<>();
                map.put("succeed", counter);
                map.put("fail", uids.length - counter + count);
                re.setData(map);
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
     * @return
     */
    public String deleteRoles(JsonArray paramObj, User user) {
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

    public String editRole(JsonObject paramObj, User user) {
        Role role = null;
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
            if (updateUids == null) updateUids = new LinkedList<>();
            updateUids.addAll(uids);
            String roleName = role.getRole();
            if ("1".equals(exRole.getRole_type())) {
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
            }
            int counter = AllDao.getInstance().getSyRoleDao().updateUserRole(role);//更新用户信息

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
                AllDao.getInstance().getSyRoleDao().deleteRelationsWithReourcesByRoleids(roleids);//删除原有关联关系
                for (Object resource : resourceList) {
                    Resource resourceObj = gson.fromJson(gson.toJson(resource), Resource.class);
                    resourceObj.setSorgID(user.getOrgID());
                    resourceObj.setRoleid(role.getRoleid());
                    AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resourceObj);//插入新的
                }
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(1);
                SyUserMapper.addSelectRelateUid(updateUids);
                RedisUtil.updateUserOnLine(new TreeSet<String>(updateUids));
                return gson.toJson(resultBean);
            }
        }
    }

    public String getResourceTree(JsonObject paramObj, User user) {
        String type = null;
        String key = "aa";
        try {
            type = paramObj.get("type").getAsString();

        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        List<LabResource> list = AllDao.getInstance().getSyResourceDao().getLabResourcesByOrgID(user.getOrgID(), type);
        if (list != null && list.size() > 0) {
            Organization organization = getOrganization(user.getOrgID());
            organization.setLabs(injectResource(organization.getLabs(), list));
            for (LabResource labResource : list) {
                if ("本科室资源".equals(labResource.getSname())) {
                    Lab lab = new Lab();
                    lab.setLab_name(labResource.getSlab_name());
                    lab.setSid(labResource.getSid());
                    List<Lab> spLab = new LinkedList<>();
                    spLab.add(lab);
                    organization.setSpResource(spLab);
                }
            }
            ResultBean re = new ResultBean();
            re.setCode(1);
            re.setData(organization);
            return gson.toJson(re);
        } else {
            return ParamUtils.errorParam("该类型资源现在不支持");
        }
    }

    private Object injectResource(Object labs, List<LabResource> list) {
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
                Object subLab = injectResource(lab.getSubLabs(), list);
                lab.setSubLabs(subLab);
            }
        }
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

    public String editGroup(String param, User user) {
        Group group = null;
        try {
            group = gson.fromJson(param, Group.class);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数异常");
        }
        if (checkGroupName(user, group)) return ParamUtils.errorParam("小组名称已存在");
        List<String> list = (List<String>) group.getMembers();
        List<String> uids = AllDao.getInstance().getGroupDao().getGroupRelationUid(group.getGid());
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

    public String deleteGroup(String param, User user) {
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
            SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String failTime = AllDao.getInstance().getSyUserDao().getFailureTimeByUid(uid);
            String effecTime = AllDao.getInstance().getSyUserDao().getEffectiveTimeByUid(uid);
            Date date=new Date();
            if(!("长期有效".equals(user.getStatus()))){
                if("禁用".equals(user.getStatus())){
                    return "false";
                }
                if(date.after(time.parse(failTime)) ||date.before(time.parse(effecTime))){
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
        try{
            re =new ResultBean();
            String labName=paramObj.get("lab_name").getAsString();
            if(StringUtils.isEmpty(labName)){
                return ParamUtils.errorParam(labName + "科室名字不能为空");
            }
            Lab lab=AllDao.getInstance().getOrgDao().getLabByOnelabName(labName);
            if(lab!=null){
                re.setCode(0);
                re.setInfo("名称重复");
                return ParamUtils.errorParam(labName + "科室名字已经存在");
            }else{
                re.setCode(1);
                re.setInfo("名称可用");
            }

        }catch(Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("判断当前科室名称是否存在操作失败");
        }
        return gson.toJson(re);
    }

    public void addAllRole() {
        Role roleAll=AllDao.getInstance().getSyRoleDao().getRoleByRole("全院科室成员");
        if(roleAll==null) {

            roleAll = new Role();
            Role roleLab = AllDao.getInstance().getSyRoleDao().getRoleByroleid(1);
            roleAll.setRole("全院科室成员");
            roleAll.setOrgID(roleLab.getOrgID());
            roleAll.setRole_type("0");
            roleAll.setDesctext("全院科室资源");
            Integer  i=AllDao.getInstance().getSyRoleDao().insertUserRole(roleAll);
            logger.info("全院科室成员 角色创建成功");
            //增加 功能
        }
    }
}
