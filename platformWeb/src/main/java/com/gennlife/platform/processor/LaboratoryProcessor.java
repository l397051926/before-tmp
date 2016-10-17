package com.gennlife.platform.processor;


import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.controller.UserController;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.*;
import com.gennlife.platform.util.*;
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
    private static Logger logger = LoggerFactory.getLogger(LaboratoryProcessor.class);
    private static Gson gson = GsonUtil.getGson();
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
        Organization organization = getOrganization(orgID);
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
        Organization organization = null;
        Map<String,Object> map = new HashMap<>();
        List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
        if(labNames.contains(lab_name)){
            return ParamUtils.errorParam(lab_name+"已经存在");
        }
        String labID = orgID+"-"+ChineseToEnglish.getPingYin(lab_name);
        //
        Lab exLab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
        if(exLab != null){
            int count = 1;
            while(true){
                labID = labID + "_" + count;
                exLab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
                if(exLab != null){
                    count ++;
                }else {
                    break;
                }
            }
        }
        Lab lab = new Lab();
        lab.setOrgID(orgID);
        if(lab_parent.equals(orgID)){//一级科室
            lab.setLab_level(1);
        }else{
            map.put("orgID",orgID);
            map.put("labID",lab_parent);
            Integer lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            if(lab_level == null){
                return ParamUtils.errorParam("无法获取"+lab_parent+"对应的科室信息");
            }else{
                lab.setLab_level(lab_level+1);
            }
        }
        lab.setLab_leader(lab_leader);
        lab.setLab_name(lab_name);
        lab.setAdd_time(LogUtils.getStringTime());
        lab.setAdd_user(uid);
        lab.setLabID(labID);
        lab.setLab_parent(lab_parent);
        int counter = AllDao.getInstance().getOrgDao().insertOneLab(lab);
        //同步增加资源
        addResource(lab);
        if(counter == 1){
            organization = getOrganization(orgID);
        }else {
            return ParamUtils.errorParam("插入失败");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    public static  void addResource(Lab lab) {
        LabResource labResource = new LabResource();
        labResource.setSorgID(lab.getOrgID());
        labResource.setSdesc(lab.getLab_name()+"病例数据资源");
        labResource.setSid(lab.getLabID());
        labResource.setSlab_parent(lab.getLab_parent());
        labResource.setSlab_type(lab.getLab_level()+"");
        labResource.setStype("病例数据");
        labResource.setSname(lab.getLab_name()+"资源");
        labResource.setSlab_name(lab.getLab_name());
        AllDao.getInstance().getSyResourceDao().insertOneResource(labResource);
    }




    public static Organization getOrganization(String orgID){
        Organization organization = AllDao.getInstance().getOrgDao().getOrganization(orgID);
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(orgID);
        Integer maxLevel = AllDao.getInstance().getOrgDao().getMaxlabLevel(orgID);
        List<Lab> treeLabs = generateLabTree(labs,orgID,maxLevel);
        organization.setLabs(treeLabs);
        return organization;
    }

    /**
     * 删除科室信息
     * @param paramObj
     * @return
     */
    public String deleteOrg(JsonObject paramObj,User user) {
        List<String> labIDsList = null;
        try{
            JsonArray labIDsArray= paramObj.get("labIDs").getAsJsonArray();
            labIDsList = gson.fromJson(labIDsArray,LinkedList.class);
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("请求参数有错误");
        }
        String[] labIDs = labIDsList.toArray(new String[labIDsList.size()]);
        String orgID = user.getOrgID();
        Organization organization = null;
        int counter = AllDao.getInstance().getOrgDao().deleteLabs(labIDs);
        //同步删除资源
        AllDao.getInstance().getSyResourceDao().deleteLabsReource(labIDs);
        int fail = labIDsList.size()-counter;
        logger.info("成功删除"+counter+"个科室信息,失败"+fail+"个");
        organization = getOrganization(orgID);
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
    public String updateOrg(JsonObject paramObj,User user) {
        String labID = null;
        String lab_name = null;
        String lab_leader = "";
        String lab_leaderName = "";
        String lab_parent = null;
        try {
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
        String orgID = user.getOrgID();
        Map<String,Object> map = new HashMap<>();
        Lab lab = AllDao.getInstance().getOrgDao().getLabBylabID(labID);
        if(lab == null){
            return ParamUtils.errorParam(labID+"无此科室");
        }
        if(!lab.getLab_name().equals(lab_name)){//如果科室的名称修改啦,需要检查新的名称是否已经存在
            List<String> labNames = AllDao.getInstance().getOrgDao().getLabsName(orgID);
            if(labNames.contains(lab_name)){
                return ParamUtils.errorParam(lab_name+"已经存在");
            }
            //当前科室下面的用户科室名称
            AllDao.getInstance().getSyUserDao().updateUserLabNameByLabName(lab_name,lab.getLab_name(),orgID);
            //获取当前科室的所有成员
            List<User> userList = AllDao.getInstance().getSyUserDao().getUserByLabID(labID,orgID);
            //更新缓存
            for(User user1:userList){
                MemCachedUtil.setUser(user1.getUid(),user1);
            }


        }
        if(!lab.getLab_parent().equals(lab_parent)
                && !lab_parent.equals(orgID)){//改变了上级部门,并且改变的上级部门不是当前医院,需要检查上级部门是否存在
            Lab parentLab = AllDao.getInstance().getOrgDao().getLabBylabID(lab_parent);
            if(parentLab == null){
                return ParamUtils.errorParam("上级部门不存在");
            }
        }
        Integer lab_level = 1;
        if(!lab_parent.equals(orgID)){
            map.put("orgID",orgID);
            map.put("labID",lab_parent);
            lab_level = AllDao.getInstance().getOrgDao().getLabLevel(map);
            map.put("lab_level",lab_level+1);
        }else {
            map.put("lab_level",lab_level);
        }
        map.put("labID",labID);

        map.put("lab_name",lab_name);
        map.put("lab_leader",lab_leader);
        map.put("lab_leaderName",lab_leaderName);
        map.put("lab_parent",lab_parent);
        int counter = AllDao.getInstance().getOrgDao().updateLabInfo(map);
        if(counter == 0){
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
        try{
            labID = paramObj.get("labID").getAsString();
            key = paramObj.get("key").getAsString();
            if(paramObj.has("limit")){
                limitStr = paramObj.get("limit").getAsString();
                int[] ls = ParamUtils.parseLimit(limitStr);
                offset = (ls[0]-1) * ls[1];
                limit = ls[1];
            }
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        List<User> users = null;
        ResultBean re = new ResultBean();
        re.setCode(1);
        if("".equals(labID)){
            if(offset != null && limit != null){
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgID(key,offset,limit,user.getOrgID());

            }else{
                users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimit(key,user.getOrgID());
            }
            Long counter = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDCounter(key,user.getOrgID());
            Map<String,Object> info = new HashMap<>();
            info.put("count",counter);
            re.setInfo(info);

        }else{
            List<Lab> labs = AllDao.getInstance().getOrgDao().getLabs(user.getOrgID());
            //labID 集合
            List<String> list = new LinkedList<>();
            list.add(labID);
            int counter = 1;
            do{
                counter = list.size();
                for(Lab lab:labs){
                    if(list.contains(lab.getLab_parent())){
                        if(!list.contains(lab.getLabID())){
                            list.add(lab.getLabID());
                        }
                    }
                }
            }while (counter !=list.size());
            String[] labIDs = list.toArray(new String[list.size()]);
            if(offset != null && limit != null){
                users = AllDao.getInstance().getSyUserDao().searchUsersByLabIDs(key,offset,limit,labIDs);
            }else{
                users = AllDao.getInstance().getSyUserDao().searchUsersByLabIDsNoLimit(key,labIDs);
            }

            Long count = AllDao.getInstance().getSyUserDao().searchUsersByLabIDsCounter(key,labIDs);
            Map<String,Object> info = new HashMap<>();
            info.put("count",count);
            re.setInfo(info);
        }
        for(User usr:users){
            Map<String,Object> map = new HashMap<>();
            map.put("orgID",usr.getOrgID());
            map.put("uid",usr.getUid());
            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(map);
            usr.setRoles(rolesList);
        }

        re.setData(users);
        return gson.toJson(re);
    }

    public String addStaff(JsonObject paramObj, User user) {
        User adduser = gson.fromJson(gson.toJson(paramObj),User.class);
        adduser.setOrgID(user.getOrgID());
        adduser.setCtime(LogUtils.getStringTime());
        adduser.setUptime(LogUtils.getStringTime());
        adduser.setPwd("ls123456");
        adduser.setOrg_name(user.getOrg_name());
        JsonObject re = insertUser(adduser);
        MemCachedUtil.setUserWithTime(adduser.getUid(),adduser, UserController.sessionTimeOut);
        return gson.toJson(re);
    }


    public JsonObject insertUser(User adduser){
        JsonObject resultBean = new JsonObject();
        String email = adduser.getUemail();
        String name = adduser.getUname();
        String unumber = adduser.getUnumber();
        String lab_name = adduser.getLab_name();
        if(name == null ||"".equals(name)){
            resultBean.addProperty("code",0);
            resultBean.addProperty("info","姓名不合法");
            return resultBean;
        }else if(lab_name == null ||"".equals(lab_name)){
            resultBean.addProperty("code",0);
            resultBean.addProperty("info","科室名称"+lab_name+"不合法");
            return resultBean;
        }else if(unumber == null ||"".equals(unumber)){
            resultBean.addProperty("code",0);
            resultBean.addProperty("info","工号"+unumber+"不合法");
            return resultBean;
        }else if(email == null ||"".equals(email)){
            resultBean.addProperty("code",0);
            resultBean.addProperty("info","email"+unumber+"不合法");
            return resultBean;
        }else{
            Integer exEamil = AllDao.getInstance().getSyUserDao().existEmail(email);
            if(exEamil >=1){
                resultBean.addProperty("code",0);
                resultBean.addProperty("info","email"+email+"已经存在了");
                return resultBean;
            }else{
                User exUnumber = AllDao.getInstance().getSyUserDao().getUserByUnumber(unumber,adduser.getOrgID());
                if(exUnumber != null){
                    resultBean.addProperty("code",0);
                    resultBean.addProperty("info","工号"+exUnumber+"已经存在了");
                    return resultBean;
                }else{
                    if(lab_name.equals(adduser.getOrg_name())){
                        adduser.setLabID(adduser.getOrgID());
                    }else{
                        Lab exLab = AllDao.getInstance().getOrgDao().getLabBylabName(lab_name,adduser.getOrgID());
                        if(exLab == null){
                            resultBean.addProperty("code",0);
                            resultBean.addProperty("info","科室"+lab_name+"不存在");
                            return resultBean;
                        }else{
                            adduser.setLabID(exLab.getLabID());
                            adduser.setLab_name(exLab.getLab_name());

                        }
                    }
                    Role role = AllDao.getInstance().getSyRoleDao().getLabMember(adduser.getOrgID());
                    UUID uuid = UUID.randomUUID();
                    adduser.setUid(uuid.toString());
                    Integer counter = AllDao.getInstance().getSyUserDao().insertOneUser(adduser);
                    if(counter == null || counter <=0){
                        resultBean.addProperty("code",0);
                        resultBean.addProperty("info","插入失败");
                        return resultBean;
                    }else{
                        counter = AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(role.getRoleid(),adduser.getUid());
                        resultBean.addProperty("code",1);
                        resultBean.addProperty("info","插入成功");
                        return resultBean;
                    }
                }
            }
        }
    }

    public String deleteStaff(JsonArray paramObj, User user) {
        List<String> uidsList = null;
        try{
            uidsList = gson.fromJson(paramObj,LinkedList.class);
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        List<String> admins = AllDao.getInstance().getSyUserDao().getAdminsByOrgID(user.getOrgID());
        int count = 0;
        for(String admin:admins){
            if(uidsList.contains(admin)){
                uidsList.remove(admin);
                count ++;
            }
        }
        ResultBean re = new ResultBean();
        if(uidsList.size() == 0){
            re.setCode(0);
            re.setInfo("无法删除管理员");
            return gson.toJson(re);
        }else {
            String[] uids = uidsList.toArray(new String[uidsList.size()]);
            AllDao.getInstance().getSyRoleDao().deleteByUids(uids);
            int counter = AllDao.getInstance().getSyUserDao().deleteUserByUids(uids);

            for(String uid:uids){
                MemCachedUtil.delete(uid);
                MemCachedUtil.daleteUser(uid);
            }
            if(counter > 0){
                re.setCode(1);
                Map<String,Object> map = new HashMap<>();
                map.put("succeed",counter);
                map.put("fail",uids.length - counter + count);
                re.setData(map);
                return gson.toJson(re);
            }else {
                re.setCode(0);
                re.setInfo("全部失败");
                return gson.toJson(re);
            }
        }


    }
    public String getProfessionList(String orgID) {
        try{
            List<String> professionList = AllDao.getInstance().getOrgDao().getProfessionList(orgID);
            ResultBean re = new ResultBean();
            re.setCode(1);
            re.setData(professionList);
            return gson.toJson(re);
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("发生异常");
        }


    }


    public String getGetRoleInfo(JsonObject paramObj, User user) {
        String key = null;
        int offset = 0;
        int limit = 0;
        try{
            key = paramObj.get("key").getAsString();
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0]-1) * ls[1];
            limit = ls[1];
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        String orgID = user.getOrgID();
        Map<String,Object> map = new HashMap<>();
        map.put("orgID",user.getOrgID());
        List<Role> list = AllDao.getInstance().getSyRoleDao().searchRoles(key,offset,limit,orgID);
        for(Role role:list){
            if("科室成员".equals(role.getRole())){
                role.setResourceDesc("本科室资源");
            }else{
                map.put("roleid",role.getRoleid());
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResources(map);
                StringBuffer sb = new StringBuffer("");
                for(Resource r:resourcesList){
                    if("".equals(sb.toString())){
                        sb.append(r.getSname());
                    }else{
                        sb.append(",").append(r.getSname());
                    }
                }
                role.setResourceDesc(sb.toString());
            }
        }
        int counter = AllDao.getInstance().getSyRoleDao().searchRolesCounter(key,orgID);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(list);
        Map<String,Object> info = new HashMap<>();
        info.put("count",counter);
        resultBean.setInfo(info);
        return gson.toJson(resultBean);
    }

    public String getStaffTree(JsonObject paramObj, User user) {
        String key = null;
        try{
            key = paramObj.get("key").getAsString();
        }catch (Exception e){
            return ParamUtils.errorParam("参数异常");
        }
        List<User> users = AllDao.getInstance().getSyUserDao().searchUsersByOrgIDNoLimit(key,user.getOrgID());
        Organization organization = getOrganization(user.getOrgID());
        //将搜索到的用户挂在organization对象中
        organization = injectUsers(organization,users);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(organization);
        return gson.toJson(resultBean);
    }

    /**
     *
     * @param organization
     * @param users
     * @return
     */
    private Organization injectUsers(Organization organization, List<User> users) {
        String orgID = organization.getOrgID();
        //搜索用户
        List<User> exList = searchUserByLabID(users,orgID);
        organization.setStaff(exList);
        List<Object> list = gson.fromJson(gson.toJson(organization.getLabs()),LinkedList.class);
        List<Lab> labList = new LinkedList<>();
        for(Object obj:list){
            Lab lab = gson.fromJson(gson.toJson(obj),Lab.class);
            Lab exLab = injectUsersIntoLab(lab,users);
            labList.add(exLab);
        }
        organization.setLabs(labList);
        return organization;
    }

    private Lab injectUsersIntoLab(Lab lab, List<User> users) {
        String labID = lab.getLabID();
        List<User> exList = searchUserByLabID(users,labID);
        lab.setStaff(exList);
        if(lab.getSubLabs() != null){
            List<Object> list = gson.fromJson(gson.toJson(lab.getSubLabs()),LinkedList.class);
            List<Lab> labList = new LinkedList<>();
            for(Object obj:list){
                Lab subLab = gson.fromJson(gson.toJson(obj),Lab.class);
                Lab exLab = injectUsersIntoLab(subLab,users);
                labList.add(exLab);
            }
            lab.setSubLabs(labList);
        }

        return lab;
    }

    private List<User> searchUserByLabID(List<User> users, String labID) {
        List<User> exList = new LinkedList<>();
        for(User user:users){
            if(labID.equals(user.getLabID())){
                exList.add(user);
            }
        }
        return exList;
    }

    /**
     *
     * @param paramObj
     * @param user
     * @return
     */
    public String deleteRoles(JsonArray paramObj, User user) {
        Integer[] paraRoleids = null;
        try{
            paraRoleids = new Integer[paramObj.size()];
            for(int index=0;index < paramObj.size();index ++){
                paraRoleids[index] = paramObj.get(index).getAsInt();
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        List<Integer> checkedRoleids = new LinkedList<>();
        for(Integer roleid:paraRoleids){
            Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
            if(!"科室成员".equals(role.getRole()) && !checkedRoleids.contains(roleid)){
                checkedRoleids.add(roleid);
            }
        }
        Integer[] roleids = checkedRoleids.toArray(new Integer[checkedRoleids.size()]);
        AllDao.getInstance().getSyRoleDao().deleteRelationsByRoleids(roleids);
        AllDao.getInstance().getSyRoleDao().deleteRelationsWithReourcesByRoleids(roleids);
        int counter = AllDao.getInstance().getSyRoleDao().deleteRolesByRoleids(roleids);
        for(Integer roleid:roleids){
            List<User> list = AllDao.getInstance().getSyUserDao().getUserByRoleID(roleid,0,10000);
            for(User user1:list){
                MemCachedUtil.daleteUser(user1.getUid());
            }
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("succeed",counter);
        map.put("fail",paraRoleids.length - counter);
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        resultBean.setData(map);
        return gson.toJson(resultBean);
    }

    public String addRole(JsonObject paramObj, User user) {
        Role role = null;
        try {
            role = gson.fromJson(paramObj, Role.class);
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        role.setOrgID(user.getOrgID());
        role.setCtime(LogUtils.getStringTime());
        role.setCreator(user.getUname());
        role.setCreatorID(user.getUid());
        if(role.getDesctext() ==null){
            role.setDesctext("");
        }
        if(role.getRole_type() == null){
            role.setRole_type("");
        }
        Role exRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(),role.getRole());
        if(exRole != null){
            return ParamUtils.errorParam("角色名称已经存在");
        }else{
            int counter = AllDao.getInstance().getSyRoleDao().insertUserRole(role);
            if(counter == 0){
                return ParamUtils.errorParam("插入失败");
            }else{
                exRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(),role.getRole());
                List<String> uidList = (List<String>) role.getStaff();
                for(String uid:uidList){
                    AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(exRole.getRoleid(),uid);
                }
                List<Object> resourceList = (List<Object>) role.getResources();
                for(Object resource:resourceList){
                    Resource resourceObj = gson.fromJson(gson.toJson(resource),Resource.class);
                    resourceObj.setSorgID(user.getOrgID());
                    resourceObj.setRoleid(role.getRoleid());
                    AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resourceObj);
                }

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
        try{
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0]-1) * ls[1];
            limit = ls[1];
            roleid = paramObj.get("roleid").getAsInt();
        }catch (Exception e){
            return ParamUtils.errorParam("请求参数错误");
        }
        Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
        if(role == null){
            return ParamUtils.errorParam("该角色不存在");
        }else{
            List<User> list = AllDao.getInstance().getSyUserDao().getUserByRoleID(roleid,offset,limit);
            int counter = AllDao.getInstance().getSyUserDao().getUserByRoleIDCounter(roleid);
            Map<String,Object> info = new HashMap<>();
            info.put("count",counter);
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
        try{
            String limitStr = paramObj.get("limit").getAsString();
            int[] ls = ParamUtils.parseLimit(limitStr);
            offset = (ls[0]-1) * ls[1];
            limit = ls[1];
            roleid = paramObj.get("roleid").getAsInt();
        }catch (Exception e){
            return ParamUtils.errorParam("请求参数错误");
        }
        Role role = AllDao.getInstance().getSyRoleDao().getRoleByroleid(roleid);
        if(role == null){
            return ParamUtils.errorParam("该角色不存在");
        }else{
            List<Resource> list = AllDao.getInstance().getSyResourceDao().getResourceByRoleID(user.getOrgID(),roleid,offset,limit);
            int counter = AllDao.getInstance().getSyResourceDao().getResourceByRoleIDCounter(user.getOrgID(),roleid);
            Map<String,Object> info = new HashMap<>();
            info.put("count",counter);
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
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        role.setOrgID(user.getOrgID());
        role.setCtime(LogUtils.getStringTime());
        if(role.getDesctext() ==null){
            role.setDesctext("");
        }
        if(role.getRole_type() == null){
            role.setRole_type("");
        }
        if(role.getRoleid() == null){
            return ParamUtils.errorParam("无角色id");
        }
        Role exRole = AllDao.getInstance().getSyRoleDao().getRoleByroleid(role.getRoleid());
        if(exRole == null){
            return ParamUtils.errorParam("该角色id对应角色不存在");
        }else{
            String roleName = role.getRole();
            //如果更新角色了名称
            if(!roleName.equals(exRole.getRole())){
                //如果更新后的名字已经存在
                Role exRenameRole = AllDao.getInstance().getSyRoleDao().getRoleByRoleName(user.getOrgID(),roleName);
                if(exRenameRole != null){
                    return ParamUtils.errorParam("角色已经存在");
                }
            }
            int counter = AllDao.getInstance().getSyRoleDao().updateUserRole(role);//更新用户信息
            List<User> users = AllDao.getInstance().getSyUserDao().getUserByRoleID(role.getRoleid(),0,10000);
            for(User user1:users){
                MemCachedUtil.daleteUser(user1.getUid());
            }
            if(counter == 0){
                return ParamUtils.errorParam("更新失败");
            }else{
                List<String> uidList = (List<String>) role.getStaff();
                Integer[] roleids = new Integer[]{role.getRoleid()};
                AllDao.getInstance().getSyRoleDao().deleteRelationsByRoleids(roleids);//删除原有的关联关系
                for(String uid:uidList){
                    AllDao.getInstance().getSyRoleDao().insertUserRoleRelation(exRole.getRoleid(),uid);//插入新的
                }
                List<Object> resourceList = (List<Object>) role.getResources();
                AllDao.getInstance().getSyRoleDao().deleteRelationsWithReourcesByRoleids(roleids);//删除原有关联关系
                for(Object resource:resourceList){
                    Resource resourceObj = gson.fromJson(gson.toJson(resource),Resource.class);
                    resourceObj.setSorgID(user.getOrgID());
                    resourceObj.setRoleid(role.getRoleid());
                    AllDao.getInstance().getSyResourceDao().insertRoleResourceRelation(resourceObj);//插入新的
                }
                ResultBean resultBean = new ResultBean();
                resultBean.setCode(1);
                return gson.toJson(resultBean);
            }
        }
    }

    public String getResourceTree(JsonObject paramObj, User user) {
        String type = null;
        try{
            type = paramObj.get("type").getAsString();
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        List<LabResource> list = AllDao.getInstance().getSyResourceDao().getLabResourcesByOrgID(user.getOrgID(),type);
        if(list != null && list.size() > 0){
            Organization organization = getOrganization(user.getOrgID());
            organization.setLabs(injectResource(organization.getLabs(),list));
            for(LabResource labResource:list){
                if("本科室资源".equals(labResource.getSname())){
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
        }else{
            return ParamUtils.errorParam("该类型资源现在不支持");
        }
    }

    private Object injectResource(Object labs, List<LabResource> list) {
        List<Object> objlist = gson.fromJson(gson.toJson(labs),LinkedList.class);
        List<Lab> labList = new LinkedList<>();
        for(Object obj:objlist){
            Lab lab = gson.fromJson(gson.toJson(obj),Lab.class);
            //判断科室对应的资源是否存在
            boolean ex = resourceStillExist(list,lab.getLabID());
            if(ex){
                lab.setSid(lab.getLabID());
                labList.add(lab);
            }
            if(lab.getSubLabs()!= null){
                Object subLab = injectResource(lab.getSubLabs(),list);
                lab.setSubLabs(subLab);
            }
        }
        return labList;
    }

    private boolean resourceStillExist(List<LabResource> list, String labID) {
        for(LabResource labResource:list){
            if(labID.equals(labResource.getSid())){
                return true;
            }
        }
        return false;
    }
}
