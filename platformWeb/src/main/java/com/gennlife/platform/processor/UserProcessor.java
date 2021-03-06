/**/
package com.gennlife.platform.processor;

import com.gennlife.darren.controlflow.break_.Break;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.conf.SystemDefault;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.*;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * Created by chensong on 2015/12/4.
 */
public class UserProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    public User login(String email, String pwd, String isMD5) throws IOException {
        try {
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pwd))
                return null;
            Long start = System.currentTimeMillis();
            User user = null;
            try {
                if (isMD5.trim().equals("false")) {
                    pwd = GStringUtils.str2Password(pwd);//通过md5加密处理
                }
                if (email != null && GStringUtils.checkEmail(email)) {
                    Map<String, Object> confMap = new HashMap<String, Object>();
                    confMap.put("email", email);
                    confMap.put("pwd", pwd);
                    user = AllDao.getInstance().getSyUserDao().getUser(confMap);//从数据库获取用户信息
                } else {
                    user = AllDao.getInstance().getSyUserDao().loginByUnumber(email, pwd);
                }
                logger.info("登录时数据库查询User耗时: " + (System.currentTimeMillis() - start) + "ms");
                //  System.out.println(user);
            } catch (Exception e) {
                logger.error("", e);
            }
            if (user == null) {
                return null;
            } else {
                long s = System.currentTimeMillis();
                user = getUserByUids(user.getUid());//~~~~~~~~~~~~~~
                long e = System.currentTimeMillis();
                logger.info("登录时加载权限信息耗时 " + (e - s) + " ms");
                return user;
            }

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 用于郑大一附单点登陆
     * @param unumber
     * @return
     * @throws IOException
     */
    public User ssoLogin(String unumber) {

        User user = null;
        long start = System.currentTimeMillis();
        try {

            user = AllDao.getInstance().getSyUserDao().selectUserByUnumber(unumber);//从数据库获取用户信息
            logger.info("登录时数据库查询User耗时: " + (System.currentTimeMillis() - start) + "ms");
            //  System.out.println(user);
            if (user == null) {
                return null;
            } else {
                long s = System.currentTimeMillis();
                user = getUserByUids(user.getUid());
                long e = System.currentTimeMillis();
                logger.info("登录时加载权限信息耗时 " + (e - s) + " ms");
                return user;
            }

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }
    public ResultBean update(String param) throws IOException {
        synchronized (FileUploadUtil.Lock) {
            boolean flag = true;
            User user = null;
            ResultBean userBean = new ResultBean();
            try {
                user = gson.fromJson(param, User.class);
                String uemail = user.getUemail();
                if (!StringUtils.isEmpty(uemail)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("email", uemail);
                    String uidEx = AllDao.getInstance().getSyUserDao().getUidByEmail(map);
                    if (!StringUtils.isEmpty(uidEx) && !user.getUid().equals(uidEx)) {//更新的email不合法,已经存在
                        return ParamUtils.errorParamResultBean("更新的email不合法,已经存在");
                    }
                }
                //时间限制
                String status = user.getStatus();
                if(StringUtils.isEmpty(status)){
                    return ParamUtils.errorParamResultBean("更新失败，状态不能为空");
                }
                String efftime=user.getEffective_time();
                String failtime=user.getFailure_time();

                if("定期有效".equals(status)){
                    if(StringUtils.isEmpty(efftime) || StringUtils.isEmpty(failtime)){
                        return ParamUtils.errorParamResultBean("更新失败， 生效时间 失效时间不能位空");
                    }
                    if(!StringUtils.isEmpty(efftime) && !StringUtils.isEmpty(failtime)){
                        try {
                            Date etime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(efftime);
                            Date ftime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(failtime);
                            if(etime.after(ftime)){
                                return ParamUtils.errorParamResultBean("更新失败，日期格式不对，失效时间在生效时间前");
                            }
                        } catch (ParseException e) {
                            logger.error("", e);
                            return ParamUtils.errorParamResultBean("日期格式不对");
                        }
                    }
                }
                if("长期有效".equals(status) || "禁用".equals(status)){
                    user.setEffective_time(null);
                    user.setFailure_time(null);
                }

                user.setCtime(null);//创建时间不可更新
                user.setUptime(LogUtils.getStringTime());//更新时间
                user.setOrg_name(null);//医院名称不能修改
                user.setOrgID(null);
                user.setRoles(null);//角色不可修改
                user.setPwd(null);//密码不可修改
                try {
                    int count = AllDao.getInstance().getSyUserDao().checkUnumber(user.getUnumber(), user.getUid());
                    if (count > 0) return ParamUtils.errorParamResultBean("更新的工号已经存在");
                    int counter = AllDao.getInstance().getSyUserDao().updateByUid(user);
                    if (counter == 0) {
                        flag = false;
                    } else
                        RedisUtil.updateUserOnLine(user.getUid());
                } catch (DataIntegrityViolationException e) {
                    return ParamUtils.errorParamResultBean("填入内容的长度超过20,更新失败");
                } catch (Exception e) {
                    logger.error("更新失败", e);
                    return ParamUtils.errorParamResultBean("更新失败");
                }
                if (!flag) {
                    userBean.setCode(0);
                    userBean.setData("更新失败");
                } else {
                    user = getUserByUids(user.getUid());
                    if (user == null) {
                        userBean.setCode(0);
                        userBean.setData("更新失败");
                    } else {
                        userBean.setCode(1);
                        userBean.setData(user);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
                return ParamUtils.errorParamResultBean("更新失败");
            }

            new Part3Processor().userinfo(1, user);
            return userBean;
        }
    }

    public String changePwdSender(JsonObject jsonObject) throws IOException {
        boolean flag = true;
        try {
            String email = jsonObject.get("email").getAsString();
            String token = jsonObject.get("token").getAsString();
            String md5 = jsonObject.get("md5").getAsString();
            Map<String, Object> map = new HashMap<>();
            map.put("email", email);
            map.put("md5", md5);
            int counter = AllDao.getInstance().getSyUserDao().updateMd5(map);
            if (counter == 0) {
                logger.error("更新md5失败");
                flag = false;
            } else {
                User user = AllDao.getInstance().getSyUserDao().getUserByEmail(email);
                if (user == null) {
                    return ParamUtils.errorParam("email 用户不存在");
                } else {
                    String url = ConfigurationService.getUrlBean().getEmailSendURL() + token;
                    Mailer.sendHTMLMail(email, url, user);
                }
            }

        } catch (Exception e) {
            flag = false;
            logger.error("", e);
        }

        ResultBean resultBean = new ResultBean();
        if (flag) {
            resultBean.setCode(1);
        } else {
            resultBean.setCode(0);
        }
        return gson.toJson(resultBean);

    }

    public static User getUserByUids(String uid) {
        User user = null;
        try {
            Long start = System.currentTimeMillis();
            user = AllDao.getInstance().getSyUserDao().getUserByUid(uid);
            String platformEnvrionment = ((SystemDefault) SpringContextUtil.getBean("systemDefault")).getNeedGroup();
            if (user == null) return null;
            Long start1 = System.currentTimeMillis();
            logger.info("查user=" + (start1 - start) + "ms");
            Map<String, Object> confMap = new HashMap<>();
            confMap.put("orgID", user.getOrgID());
            confMap.put("uid", user.getUid());
            List<Admin> adminList = AllDao.getInstance().getSyUserDao().getAdmins(confMap);
            user.setAdministrators(adminList);
            //获取角色名字
            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
            //转换全院 方式
//            rolesList=transforAllRole(rolesList);
            //转化本科室信息
            Power power = transformRole(user, rolesList);
            //
            user.setPower(power);
            List<Group> list = AllDao.getInstance().getGroupDao().getGroupsByUid(confMap);
            Long start5 = System.currentTimeMillis();
            Map<String, Object> map = new HashMap<>();
            map.put("orgID", user.getOrgID());
            Set<String> hasAddUser = new TreeSet<>();
            Group resultGroup = new Group();
            List<JsonObject> newUserList = new LinkedList<>();
            hasAddUser.add(user.getUid());
            newUserList.add(user.CopyMemberInfo());
            for (Group group : list) {
                String gid = group.getGid();
                map.put("gid", gid);
                List<User> userList = AllDao.getInstance().getGroupDao().getUsersByGroupID(map);
                for (User member : userList) {//补充成员的角色信息
                    if (hasAddUser.contains(member.getUid())) continue;
                    hasAddUser.add(member.getUid());
                    User newMember = getUserByUser(member);
                    newUserList.add(newMember.CopyMemberInfo());
                    List<Role> roleList = newMember.getRoles();
                    if (roleList != null) {
                        for (Role role : roleList) {
                            if("全院科室成员".equals(role.getRole())){
                                user.setIfRoleAll("是");
                            }
                            if (role.getResources() != null) {
                                List<Object> resourceList = (List<Object>) role.getResources();
                                for (Object r : resourceList) {
                                    Resource resource = gson.fromJson(gson.toJson(r), Resource.class);
                                    addResourceInGroupToPower(power, resource, group);
                                }
                            }
                        }
                    }
                    newMember.setFrontEndPower(null);
                    newMember.setRoles(null);
                    newMember.setPower(null);
                    newMember.setGroups(null);
                    if (!newMember.getUid().equals(uid))
                        newMember.setAdministrators(null);
                }

            }
            resultGroup.setMembers(newUserList);
            Long start6 = System.currentTimeMillis();
            //System.out.println("设置组成员="+(start6-start5)+"ms");
            //
            Power frontEndPower = power.deepCopy();
            user.setFrontEndPower(frontEndPower);//前端可视化
            try {
//                Map<String, List<String>> mapDep = getDepartmentFromMysql(AllDao.getInstance().getSyRoleDao().getSlabNames());
                Map<String, List<Lab>> mapDep = getDepartmentFromMysql(AllDao.getInstance().getSyRoleDao().getLabMAP());
                power.setHas_search(addDepartmentPower(power.getHas_search(), mapDep));//更改方案，我查询其父级下面所有子科室
                power.setHas_searchExport(addDepartmentPower(power.getHas_searchExport(), mapDep));
            } catch (Exception e) {
                logger.error("科室映射失败：" + e.getMessage());
            }
            list.clear();
            list.add(resultGroup);
            if (platformEnvrionment != null && platformEnvrionment.equals("false")) { // Group清空
                user.setGroups(null);
            } else {
                user.setGroups(list);
            }
            user.setRoles(new ArrayList<Role>(0));
        } catch (Exception e) {
            logger.error("", e);
        }
        return user;
    }

    private static List<Role> transforAllRole(List<Role> rolesList) {
        for (Role role :rolesList){
            if(role.getRole().equals("全院科室成员")){
                rolesList=AllDao.getInstance().getSyRoleDao().getAllRoles();
            }
        }
        return rolesList;
    }

    public static User getUserByUidFromRedis(String uid) {
        return RedisUtil.getUser(uid);
    }

    public static User getUserByUser(User user) {
        if (user == null) {
            return null;
        } else {
            user = AllDao.getInstance().getSyUserDao().getUserByUid(user.getUid());
            Map<String, Object> confMap = new HashMap<>();
            confMap.put("orgID", user.getOrgID());
            confMap.put("uid", user.getUid());
            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
            //转化本科室信息
            transformRole(user, rolesList);
            user.setRoles(rolesList);
            return user;
        }

    }

    private static List<Role> powerToRoles(Power power, List<Role> roles) {
        if (power == null) return roles;
        LinkedList<Role> result = new LinkedList<>();
        if (roles != null) result.addAll(roles);
        Role role = new Role();
        LinkedList<Resource> list = new LinkedList<>();
        list.addAll(power.getHas_search());
        list.addAll(power.getHas_searchExport());
        ;
        list.addAll(power.getHas_addBatchCRF());
        list.addAll(power.getHas_addCRF());
        list.addAll(power.getHas_browseDetail());
        list.addAll(power.getHas_traceCRF());
        list.addAll(power.getHas_deleteCRF());
        list.addAll(power.getHas_editCRF());
        list.addAll(power.getHas_searchCRF());
        list.addAll(power.getHas_importCRF());
        role.setResources(list);
        result.add(role);
        return result;
    }

    @Deprecated
    public static void addDepartmentRole(Role role, Map<String, List<String>> departNames) {

        // 处理role下的 resources 调用 addDepartmentPower
        JsonArray resource = gson.toJsonTree(role.getResources()).getAsJsonArray();
        // resource.get("key").getAsJsonObject()

        JsonArray insert = new JsonArray();

        for (JsonElement json : resource) {
            JsonObject jsonobj = json.getAsJsonObject();
            String sid = jsonobj.get("sid").getAsString();
            List<String> departName = departNames.get(sid);
            insert.add(json);
            if (departName != null && departName.size() > 0) {
                for (String department : departName) {
                    JsonObject jsonCopy = powerSearchCopy(jsonobj);
                    jsonCopy.addProperty("slab_name", department);
                    insert.add(jsonCopy);
                }
            }
        }
        if (insert.size() > 0) {
            role.setResources(insert);
        }
    }

    public static JsonObject powerSearchCopy(JsonObject json) {
        JsonObject copy = new JsonObject();
        copy.add("slab_name", json.get("slab_name"));
        copy.add("has_search", json.get("has_search"));
        copy.add("sid", json.get("sid"));
        copy.add("has_searchExport", json.get("has_searchExport"));
        return copy;
    }

    //加一组gennlife_resource 若其下有映射，在加一组拼接特定单词的映射
    public static Set<Resource> addDepartmentPower(Collection<Resource> list, Map<String, List<Lab>> departNames) {
        JsonArray resource = gson.toJsonTree(list).getAsJsonArray();
        JsonArray insert = new JsonArray();
        for (JsonElement json : resource) {
            JsonObject jsonobj = json.getAsJsonObject();
            String sid = jsonobj.get("sid").getAsString();
            List<Lab> departName = departNames.get(sid);
            insert.add(json);
            if (departName != null && departName.size() > 0) {
                for (Lab department : departName) {
                    JsonObject jsonCopy = powerSearchCopy(jsonobj);//把特定的词提出来 存入jsonobject
                    jsonCopy.addProperty("sid",department.getLabID());
                    jsonCopy.addProperty("slab_name", department.getLab_name());
                    insert.add(jsonCopy);
                }
            }
        }

        return gson.fromJson(insert, new TypeToken<TreeSet<Resource>>() {
        }.getType());
    }
//    将相同的labid departname放入list中，每个labid 当为key
//    public static Map<String, List<String>> getDepartmentFromMysql(List<DepartmentMap> departName) {
//        Map<String, List<String>> mapDep = new HashMap<String, List<String>>();
//        for (DepartmentMap dep : departName) {
//
//            List<String> array = new LinkedList<String>();
//
//            List<String> arrayList = mapDep.get(dep.getLab_id());
//            if (arrayList != null && arrayList.size() != 0) {
//                arrayList.add(dep.getDepart_name());
//                mapDep.put(dep.getLab_id(), arrayList);
//            } else {
//                array.add(dep.getDepart_name());
//                mapDep.put(dep.getLab_id(), array);
//            }
//        }
//        return mapDep;
//    }

    //将相同的labid departname放入list中，每个labid 当为key
    public static Map<String, List<Lab>> getDepartmentFromMysql(List<Lab> departName) {
        Map<String, List<Lab>> mapDep = new HashMap<>();
        for (Lab dep : departName) {

            List<Lab> array = new LinkedList<>();

            List<Lab> arrayList = mapDep.get(dep.getLab_parent());
            if (arrayList != null && arrayList.size() != 0) {
                arrayList.add(dep);
                mapDep.put(dep.getLab_parent(), arrayList);
            } else {
                array.add(dep);
                mapDep.put(dep.getLab_parent(), array);
            }
        }
        return mapDep;
    }

    @Deprecated
    public static void departmentMapping(User user, Map<String, List<String>> mapDep) {

        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            addDepartmentRole(role, mapDep);
        }
    }

    /**
     * 转换本科室
     * @param user
     * @param rolesList
     * @return
     */
    public static Power transformRole(User user, List<Role> rolesList) {
        if (rolesList == null) {
            return null;
        }
        if (user == null || user.getLabID() == null) {
            return null;
        }
        Power power = new Power();
        //
        user.setRoles(rolesList);

        Map<String, Object> confMap = new HashMap<>();
        confMap.put("orgID", user.getOrgID());
        confMap.put("uid", user.getUid());
        Optional<Role> tmpRole = rolesList.stream().filter( o -> Objects.equals("全院科室成员",o.getRole())).findFirst();
        if(tmpRole.isPresent()) {
            List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getAllResources(user.getOrgID(), tmpRole.get().getRoleid());
            List<Resource> reList = new LinkedList<>();
            for (Resource resource : resourcesList) {
                reList.add(resource);
                power = addResourceToPowerForAll(power, resource);
            }
            //添加 判断user 权限是否全量
            user.setIfRoleAll("是");
            tmpRole.get().setResources(reList);
        }
        for (Role role : rolesList) {
            if(Objects.equals("是",user.getIfRoleAll())){
                break;
            }
            if("全院科室成员".equals(role.getRole())){
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getAllResources(user.getOrgID(), role.getRoleid());
                List<Resource> reList = new LinkedList<>();
                for (Resource resource : resourcesList) {
                    reList.add(resource);
                    power = addResourceToPowerForAll(power, resource);
                }
                //添加 判断user 权限是否全量
                user.setIfRoleAll("是");
                role.setResources(reList);
            }else  if ("1".equals(role.getRole_type())) {
                if(Objects.equals(user.getOrgID(),user.getLabID())){
                    List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getAllResources(user.getOrgID(), role.getRoleid());
                    List<Resource> reList = new LinkedList<>();
                    for (Resource resource : resourcesList) {
                        reList.add(resource);
                        power = addResourceToPowerForAll(power, resource);
                    }
                    //添加 判断user 权限是否全量
                    user.setIfRoleAll("是");
                    role.setResources(reList);
                }else {
                    List<String> labIDs = getAllLabId(user.getLabID());
                    List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResourcesBySids(user.getOrgID(), labIDs, role.getRoleid());
                    List<Resource> reList = new LinkedList<>();
                    for (Resource resource : resourcesList) {
                        reList.add(resource);
                        power = addResourceToPower(power, resource);
                    }
                    role.setResources(reList);
                }
            } else {
                confMap.put("roleid", role.getRoleid());
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResources(confMap);
                List<Resource> reList = new LinkedList<>();
                for (Resource resource : resourcesList) {
                    if ("1".equals(resource.getStype_role())) {//本科室资源
                        if (user.getLabID().equals(user.getOrgID())) {
                            continue;
                        }
                        resource.setSid(user.getLabID());
                        resource.setSlab_name(user.getLab_name());

                    }
                    reList.add(resource);
                    power = addResourceToPower(power, resource);
                }
                role.setResources(reList);
            }
        }
        return power;
    }
    private static List<String> getAllLabId(String labId){
        List<Lab> labs = AllDao.getInstance().getOrgDao().getLabIdAndParentId();
        Map<String, Set<Lab>> labsByParentId = labs.stream().collect(groupingBy(Lab::getLab_parent, toSet()));
        Set<String> resultIds = new HashSet<>();
        resultIds.add(labId);
        getAllLabIdsForLabs(labsByParentId,labId,resultIds);
        return new ArrayList<>(resultIds);
    }

    private static void getAllLabIdsForLabs(Map<String, Set<Lab>> labsByParentId, String labId, Set<String> resultIds) {
        if (labsByParentId.containsKey(labId)) {
            Set<Lab> labs = labsByParentId.get(labId);
            labs.forEach(lab -> {
                resultIds.add(lab.getLabID());
                getAllLabIdsForLabs(labsByParentId, lab.getLabID(), resultIds);
            });
        }
    }

    private static List<String> getAllLabIds(String labID) {
        List<String> labIds =  AllDao.getInstance().getOrgDao().getLabIdByParentIds(labID);
        labIds.add(labID);
        List<String> labs = new ArrayList<>();
        getAllLabIdByIds(labIds,labs);
        return labs;
    }

    private static void getAllLabIdByIds(List<String> labIds, List<String> labs) {
        for (String lab : labIds){
            labs.add(lab);
            List<String> tmpLabs = AllDao.getInstance().getOrgDao().getLabIdByParentIds(lab);
            getAllLabIdByIds(tmpLabs,labs);
        }
    }

    private static Power addResourceToPowerForAll(Power power, Resource resource) {
        if ("有".equals(resource.getHas_search())) {
            if (!isExistResource(power.getHas_search(), resource)) {
                power.addInHasSearch(resource);
            }
        }
        if ("有".equals(resource.getHas_searchExport())) {
            if (!isExistResource(power.getHas_searchExport(), resource)) {
                power.addInHasSearchExport(resource);
            }
        }
        return power;
    }

    public static Power addResourceToPower(Power power, Resource resource) {
        if ("有".equals(resource.getHas_search())) {
            if (!isExistResource(power.getHas_search(), resource)) {
                power.addInHasSearch(resource);
            }
        }
        if ("有".equals(resource.getHas_searchExport())) {
            if (!isExistResource(power.getHas_searchExport(), resource)) {
                power.addInHasSearchExport(resource);
            }
        }
        if ("有".equals(resource.getHas_traceCRF())) {
            if (!isExistResource(power.getHas_traceCRF(), resource)) {
                power.addInHasTraceCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_addCRF())) {
            if (!isExistResource(power.getHas_addCRF(), resource)) {
                power.addInHasAddCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_addBatchCRF())) {
            if (!isExistResource(power.getHas_addBatchCRF(), resource)) {
                power.addInHasAddBatchCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_editCRF())) {
            if (!isExistResource(power.getHas_editCRF(), resource)) {
                power.addInHasEditCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_deleteCRF())) {
            if (!isExistResource(power.getHas_deleteCRF(), resource)) {
                power.addInHasDeleteCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_browseDetail())) {
            if (!isExistResource(power.getHas_browseDetail(), resource)) {
                power.addInHasBrowseDetail(resource);
            }
        }
        if ("有".equals(resource.getHas_searchCRF())) {
            if (!isExistResource(power.getHas_searchCRF(), resource)) {
                power.addInHassearchCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_importCRF())) {
            if (!isExistResource(power.getHas_importCRF(), resource)) {
                power.addInHasimportCRF(resource);
            }
        }
        return power;
    }

    public static Power addResourceInGroupToPower(Power power, Resource resource, Group group) {
        if ("有".equals(resource.getHas_search()) && "有".equals(group.getHas_search())) {
            if (!isExistResource(power.getHas_search(), resource)) {
                power.addInHasSearch(resource);
            }

        }
        if ("有".equals(resource.getHas_searchExport()) && "有".equals(group.getHas_searchExport())) {
            if (!isExistResource(power.getHas_searchExport(), resource)) {
                power.addInHasSearchExport(resource);
            }

        }
        if ("有".equals(resource.getHas_traceCRF()) && "有".equals(group.getHas_traceCRF())) {
            if (!isExistResource(power.getHas_traceCRF(), resource)) {
                power.addInHasTraceCRF(resource);
            }

        }
        if ("有".equals(resource.getHas_addCRF()) && "有".equals(group.getHas_addCRF())) {
            if (!isExistResource(power.getHas_addCRF(), resource)) {
                power.addInHasAddCRF(resource);
            }
        }
        if ("有".equals(resource.getHas_addBatchCRF()) && "有".equals(group.getHas_addBatchCRF())) {
            if (!isExistResource(power.getHas_addBatchCRF(), resource)) {
                power.addInHasAddBatchCRF(resource);
            }

        }
        if ("有".equals(resource.getHas_editCRF()) && "有".equals(group.getHas_editCRF())) {
            if (!isExistResource(power.getHas_editCRF(), resource)) {
                power.addInHasEditCRF(resource);
            }

        }
        if ("有".equals(resource.getHas_deleteCRF()) && "有".equals(group.getHas_deleteCRF())) {
            if (!isExistResource(power.getHas_deleteCRF(), resource)) {
                power.addInHasDeleteCRF(resource);
            }

        }
        if ("有".equals(resource.getHas_browseDetail()) && "有".equals(group.getHas_browseDetail())) {
            if (!isExistResource(power.getHas_browseDetail(), resource)) {
                power.addInHasBrowseDetail(resource);
            }

        }
        if ("有".equals(resource.getHas_searchCRF()) && "有".equals(group.getHas_searchCRF())) {
            if (!isExistResource(power.getHas_searchCRF(), resource)) {
                power.addInHassearchCRF(resource);
            }

        }
        if ("有".equals(resource.getHas_importCRF()) && "有".equals(group.getHas_importCRF())) {
            if (!isExistResource(power.getHas_importCRF(), resource)) {
                power.addInHasimportCRF(resource);
            }

        }
        return power;
    }

    public static boolean isExistResource(Set<Resource> list, Resource resource) {
        if (resource == null || resource.getSid() == null) {
            return false;
        }
        boolean flag = false;
        for (Resource r : list) {
            if (resource.getSid().equals(r.getSid())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 更新密码
     *
     * @param param
     * @return
     */
    public String updatePWD(String param) {
        String email = null;
        String pwd = null;
        String md5 = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            email = paramObj.get("email").getAsString();
            pwd = paramObj.get("pwd").getAsString();
            if (paramObj.has("md5")) md5 = paramObj.get("md5").getAsString();
            if (StringUtils.isEmpty(md5)) md5 = "";
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("参数错误");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("pwd", GStringUtils.str2Password(pwd));
        map.put("md5", md5);
        int counter = AllDao.getInstance().getSyUserDao().updatePWD(map);
        if (counter == 0) {
            return ParamUtils.errorParam("更新失败,更新链接失效");
        } else {
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("更新成功");
            return gson.toJson(resultBean);
        }
    }

    /**
     * @param paramObj
     * @return
     */
    public static String existEmail(JsonObject paramObj) {
        String email = paramObj.get("email").getAsString();
        int counter = AllDao.getInstance().getSyUserDao().existEmail(email);
        ResultBean resultBean = new ResultBean();
        if (counter == 0) {
            resultBean.setCode(0);
        } else {
            resultBean.setCode(1);
        }
        return gson.toJson(resultBean);
    }


    public static User transformRole(User user) {
        List<Role> roles = user.getRoles();
        String labID = user.getLabID();
        if (labID.equals(user.getOrgID())) {//用户属于医院,不需要检查本科室成员角色
            return user;
        }
        for (Role role : roles) {
            List<Resource> resources = (List<Resource>) role.getResources();
            for (Resource resource : resources) {
                if ("1".equals(resource.getStype_role())) {
                    resource.setSlab_name(user.getLab_name());
                    resource.setSid(user.getLabID());
                }
            }

        }
        return user;
    }

    public String CRFList(User user) {
        //JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        String lab_name = user.getLab_name();
        String labID = user.getLabID();
        String orgID = user.getOrgID();
        Power power = user.getPower();
        List<String> labIDSet = new LinkedList<>();
        Map<String, String> map = new HashMap<>();
        Set<Resource> addlist = power.getHas_addCRF();
        for (Resource resource : addlist) {
            String sid = resource.getSid();
            String has_addCRF = resource.getHas_addCRF();
            if (!labIDSet.contains(sid) && "有".equals(has_addCRF)) {
                labIDSet.add(sid);
                map.put(sid, resource.getSlab_name());
            }
        }
/*        for(JsonElement roleItem:roles){
            JsonObject roleObj = roleItem.getAsJsonObject();
            JsonArray resources = roleObj.getAsJsonArray("resources");
            for(JsonElement resourceItem:resources){
                JsonObject resourceObj = resourceItem.getAsJsonObject();
                String tmplabID = resourceObj.get("sid").getAsString();
                if(resourceObj.has("has_addCRF")){
                    String has_addCRF = resourceObj.get("has_addCRF").getAsString();
                    if(!labIDSet.contains(tmplabID) && "有".equals(has_addCRF)){
                        labIDSet.add(tmplabID);
                        map.put(tmplabID,resourceObj.get("slab_name").getAsString());
                    }
                }
            }
        }*/
        String[] labIDs = new String[]{labID};
        JsonObject result = new JsonObject();
        JsonObject data = new JsonObject();
        result.add("data", data);
        List<CRFLab> defaultList = AllDao.getInstance().getSyResourceDao().getCrfIDListByLab(labIDs, orgID);
        if (defaultList != null) {
            JsonObject defaultObj = new JsonObject();
            defaultObj.addProperty("lab_name", lab_name);
            defaultObj.addProperty("labID", labID);
            JsonArray crfList = new JsonArray();
            defaultObj.add("crfList", crfList);
            for (CRFLab crfLab : defaultList) {
                JsonObject item = new JsonObject();
                item.addProperty(crfLab.getCrf_id(), crfLab.getCrfName());
            }
            if (crfList.size() == 0) {
                data.add("default", new JsonObject());
            } else {
                data.add("default", defaultObj);
            }

        } else {
            data.add("default", new JsonObject());
        }
        labIDs = labIDSet.toArray(new String[labIDSet.size()]);
        JsonArray listArray = new JsonArray();
        data.add("list", listArray);
        if (labIDSet.size() > 0) {
            List<CRFLab> crfLablist = AllDao.getInstance().getSyResourceDao().getCrfIDListByLab(labIDs, orgID);
            for (String tmpID : map.keySet()) {
                JsonObject item = new JsonObject();
                String name = map.get(tmpID);
                item.addProperty("labName", name);
                item.addProperty("labID", tmpID);
                JsonObject crfList = new JsonObject();
                item.add("crfList", crfList);
                for (CRFLab crfLab : crfLablist) {
                    if (crfLab.getLabID().equals(tmpID)) {
                        crfList.addProperty(crfLab.getCrf_id(), crfLab.getCrfName());
                    }
                }
                listArray.add(item);
            }
        }

        data.add("list", listArray);
        result.addProperty("code", 1);
        return gson.toJson(result);
    }

    /**
     * @param uid
     * @param param
     * @return
     */
    public String vitaBoardConfigSave(String uid, String param) {
        JsonObject paramObj = null;
        String dataStr = null;
        try {
            paramObj = (JsonObject) jsonParser.parse(param);
            JsonArray dataArray = paramObj.getAsJsonArray("data");
            dataStr = gson.toJson(dataArray);
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        AllDao.getInstance().getSyUserDao().deleteVitaCong(uid);
        int count = AllDao.getInstance().getSyUserDao().insertVitaCong(uid, dataStr);
        ResultBean re = new ResultBean();
        if (count == 1) {
            re.setCode(1);
        } else {
            re.setCode(0);
        }
        return gson.toJson(re);
    }

    public String VitaBoardConfig(String uid) {
        String data = AllDao.getInstance().getSyUserDao().getVitaCong(uid);
        JsonArray dataArray = (JsonArray) jsonParser.parse(data);
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setData(dataArray);
        return gson.toJson(re);
    }

    public String labTransformCrfId(User user, String param) {
        String labID = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            labID = paramObj.get("labID").getAsString();
        } catch (Exception e) {
            return ParamUtils.errorParam("参数错误");
        }
        List<CRFLab> crfLablist = AllDao.getInstance().getSyResourceDao().getCrfIDByLab(labID, user.getOrgID());
        if (crfLablist == null || crfLablist.size() == 0) {
            return ParamUtils.errorParam("没有找到对应的参数");
        } else {
            CRFLab crfLab = crfLablist.get(0);
            ResultBean re = new ResultBean();
            re.setCode(1);
            re.setData(crfLab);
            return gson.toJson(re);
        }
    }


    public String setRedis(String param) {
        boolean flag = true;
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        flag = paramObj.get("flag").getAsBoolean();
        RedisUtil.setFlag(flag);
        ResultBean re = new ResultBean();
        re.setCode(1);
        re.setInfo("ok");
        return gson.toJson(re);
    }

    //更新当前用户
    public static void currentUpdate(String uid, String sessionID) {
        User user = UserProcessor.getUserByUids(uid);
        if (user != null) RedisUtil.setUserOnLine(user, sessionID);
    }


    public String isExistUserInfo(JsonObject paramObj, User user) {
        ResultBean re = null;
        try{
            re =new ResultBean();
            if(paramObj.has("uname")){
                String uname=paramObj.get("uname").getAsString();
                if(!StringUtils.isEmpty(uname)){
                    Integer exuser=AllDao.getInstance().getSyUserDao().existUserName(uname);
                    if(exuser!=0){
                        re.setCode(0);
                        re.setInfo("名称不可用");
//                        return ParamUtils.errorParam(uname + "用户名字已经存在");
                    }else{
                        re.setCode(1);
                        re.setInfo("名称可用");
                    }
                }
            }
            if(paramObj.has("unumber")){
                String unumber=paramObj.get("unumber").getAsString();
                if(!StringUtils.isEmpty(unumber)){
                    Integer exuser=AllDao.getInstance().getSyUserDao().existUserNumber(unumber);
                    if(exuser!=0){
                        re.setCode(0);
                        re.setInfo("名称不可用");
//                        return ParamUtils.errorParam(unumber + "用户工号已经存在");
                    }else{
                        re.setCode(1);
                        re.setInfo("名称可用");
                    }
                }
            }
            if(paramObj.has("uemail")){
                String uemail=paramObj.get("uemail").getAsString();
                if(!StringUtils.isEmpty(uemail)){
                    Integer exuser=AllDao.getInstance().getSyUserDao().existEmail(uemail);
                    if(exuser!=0){
                        re.setCode(0);
                        re.setInfo("邮箱不可用");
//                        return ParamUtils.errorParam(uemail + "用户邮箱已经存在");
                    }else{
                        re.setCode(1);
                        re.setInfo("名称可用");
                    }
                }
            }

        }catch(Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("判断当前用户信息是否存在操作失败");
        }
        return gson.toJson(re);
    }

}
