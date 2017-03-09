package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
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
import java.util.*;

/**
 * Created by chensong on 2015/12/4.
 */
public class UserProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    public User login(String email,String pwd) throws IOException {
        try {
            LogUtils.BussnissLog("用户：" + email + " >>> 进行登陆");
            Map<String,Object> confMap = new HashMap<String,Object>();
            confMap.put("email", email);
            confMap.put("pwd", pwd);
            User user = null;
            try {
                user = AllDao.getInstance().getSyUserDao().getUser(confMap);
            //  System.out.println(user);
            } catch (Exception e) {
                logger.error("", e);
            }
            if (user == null) {
                return null;
            } else {
                user = getUserByUids(user.getUid());
                return user;
            }

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }



    public ResultBean update(String param) throws IOException {
        boolean flag = true;
        User user = null;
        ResultBean userBean = new ResultBean();
        try{
            user = gson.fromJson(param,User.class);
            Map<String,Object> map = new HashMap<>();
            map.put("email",user.getUemail());
            String uidEx =  AllDao.getInstance().getSyUserDao().getUidByEmail(map);
            if(!StringUtils.isEmpty(uidEx)&&!user.getUid().equals(uidEx)){//更新的email不合法,已经存在
                return ParamUtils.errorParamResultBean("更新的email不合法,已经存在");
            }
            user.setCtime(null);//创建时间不可更新
            user.setUptime(LogUtils.getStringTime());//更新时间
            user.setOrg_name(null);//医院名称不能修改
            user.setOrgID(null);
            user.setRoles(null);//角色不可修改
            user.setPwd(null);//密码不可修改
            try{
                int count=AllDao.getInstance().getSyUserDao().checkUnumber(user.getUnumber(),user.getUid());
                if(count>0) return ParamUtils.errorParamResultBean("更新的工号已经存在");
                int counter = AllDao.getInstance().getSyUserDao().updateByUid(user);
                if(counter == 0){
                    flag = false;
                }
                else
                    RedisUtil.updateUserOnLine(user.getUid());
            }catch (DataIntegrityViolationException e){
                return ParamUtils.errorParamResultBean("填入内容的长度超过20,更新失败");
            }catch (Exception e){
                logger.error("更新失败",e);
                return ParamUtils.errorParamResultBean("更新失败");
            }
            if(!flag){
                userBean.setCode(0);
                userBean.setData("更新失败");
            } else{
                user = getUserByUids(user.getUid());
                if(user == null){
                    userBean.setCode(0);
                    userBean.setData("更新失败");
                }else {
                    userBean.setCode(1);
                    userBean.setData(user);
                }
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParamResultBean("更新失败");
        }
        return userBean;
    }
    public String changePwdSender(JsonObject jsonObject) throws IOException {
        boolean flag = true;
        try{
            String email = jsonObject.get("email").getAsString();
            String token = jsonObject.get("token").getAsString();
            String md5 = jsonObject.get("md5").getAsString();
            Map<String,Object> map = new HashMap<>();
            map.put("email",email);
            map.put("md5",md5);
            int counter = AllDao.getInstance().getSyUserDao().updateMd5(map);
            if(counter == 0){
                logger.error("更新md5失败");
                flag = false;
            }else{
                User user = AllDao.getInstance().getSyUserDao().getUserByEmail(email);
                if(user == null){
                    return ParamUtils.errorParam("email 用户不存在");
                }else{
                    String url = ConfigurationService.getUrlBean().getEmailSendURL()+token;
                    Mailer.sendHTMLMail(email, url,user);
                }
            }

        }catch (Exception e){
            flag = false;
            logger.error("",e);
        }

        ResultBean resultBean = new ResultBean();
        if(flag){
            resultBean.setCode(1);
        }else{
            resultBean.setCode(0);
        }
        return gson.toJson(resultBean);

    }

    public static User getUserByUids(String uid){
        User user = null;
        try {
            Long start = System.currentTimeMillis();
            user = AllDao.getInstance().getSyUserDao().getUserByUid(uid);
            if (user==null) return null;
            Long start1 = System.currentTimeMillis();
            logger.info("查user="+(start1-start)+"ms");
            Map<String,Object> confMap = new HashMap<>();
            confMap.put("orgID", user.getOrgID());
            confMap.put("uid", user.getUid());
            List<Admin> adminList = AllDao.getInstance().getSyUserDao().getAdmins(confMap);
            user.setAdministrators(adminList);
            List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
            //转化本科室信息
            Power power = transformRole(user,rolesList);
            //
            user.setPower(power);
            List<Group> list = AllDao.getInstance().getGroupDao().getGroupsByUid(confMap);
            Long start5 = System.currentTimeMillis();
            Map<String,Object> map = new HashMap<>();
            map.put("orgID",user.getOrgID());
            for(Group group:list){
                String gid = group.getGid();
                map.put("gid",gid);
                List<User> userList = AllDao.getInstance().getGroupDao().getUsersByGroupID(map);
                List<User> newUserList = new LinkedList<>();
                for(User member:userList){//补充成员的角色信息
                    User newMember = getUserByUser(member);
                    newUserList.add(newMember);
                    List<Role> roleList = newMember.getRoles();
                    if(roleList != null){
                        for(Role role:roleList){
                            if(role.getResources() != null){
                                List<Object> resourceList = (List<Object>) role.getResources();
                                for(Object r:resourceList){
                                    Resource resource = gson.fromJson(gson.toJson(r),Resource.class);
                                    addResourceInGroupToPower(power,resource,group);
                                }
                            }

                        }
                    }
                    newMember.setFrontEndPower(null);
                    newMember.setRoles(null);
                    if(!newMember.getUid().equals(uid))
                        newMember.setAdministrators(null);
                }
                group.setMembers(newUserList);
            }
            Long start6 = System.currentTimeMillis();
            //System.out.println("设置组成员="+(start6-start5)+"ms");
            //
            user.setGroups(list);
            user.setRoles(new ArrayList<Role>(0));
        }catch (Exception e){
            logger.error("",e);
        }
        return user;
    }

    public static User getUserByUidFromRedis(String uid){
        return RedisUtil.getUser(uid);
    }

    public static User getUserByUser(User user){
        if(user == null){
            return null;
        }else {
            User exuser = RedisUtil.getUser(user.getUid());
            if(exuser != null){
                return exuser;
            }else {
                user = AllDao.getInstance().getSyUserDao().getUserByUid(user.getUid());
                Map<String,Object> confMap = new HashMap<>();
                confMap.put("orgID",user.getOrgID());
                confMap.put("uid",user.getUid());
                List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
                //转化本科室信息
                Power power = transformRole(user,rolesList);
                user.setPower(power);
                return user;
            }
        }

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
            if (departName != null && departName.size() > 0) {
                for (String department : departName) {
                    JsonObject jsonCopy = deepCopy(jsonobj);
                    jsonCopy.addProperty("slab_name", department);
                    insert.add(jsonCopy);
                }
            } else {
                insert.add(json);
            }
        }
        if (insert.size() > 0) {
            role.setResources(insert);
        }
    }
    public static JsonObject deepCopy(JsonObject json) {
        JsonObject copy = new JsonObject();
        for (Map.Entry<String, JsonElement> item : json.entrySet()) {
            copy.add(item.getKey(), item.getValue());
        }
        return copy;
    }



    public static List<Resource> addDepartmentPower(List<Resource> list, Map<String, List<String>> departNames) {

        JsonArray resource = gson.toJsonTree(list).getAsJsonArray();
        JsonArray insert = new JsonArray();

        for (JsonElement json : resource) {

            JsonObject jsonobj = json.getAsJsonObject();
            if (!jsonobj.get("has_search").isJsonNull() && jsonobj.get("has_search").getAsString().equals("有")) {
                String sid = jsonobj.get("sid").getAsString();
                List<String> departName = departNames.get(sid);
                if (departName != null && departName.size() > 0) {
                    for (String department : departName) {
                        JsonObject jsonCopy = deepCopy(jsonobj);
                        jsonCopy.addProperty("slab_name", department);
                        insert.add(jsonCopy);
                    }
                } else {
                    insert.add(json);
                }
            } else {
                insert.add(json);
            }
        }

        return gson.fromJson(insert, new TypeToken<List<Resource>>(){}.getType());
    }

    public static Map<String, List<String>> getDepartmentFromMysql(List<DepartmentMap> departName) {
        Map<String, List<String>> mapDep = new HashMap<String, List<String>>();
        for (DepartmentMap dep: departName) {

            List<String> array = new LinkedList<String>();

            List<String> arrayList = mapDep.get(dep.getLab_id());
            if (arrayList != null && arrayList.size() != 0) {
                arrayList.add(dep.getDepart_name());
                mapDep.put(dep.getLab_id(), arrayList);
            } else {
                array.add(dep.getDepart_name());
                mapDep.put(dep.getLab_id(), array);
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

    public static Power transformRole(User user,List<Role> rolesList){
        if(rolesList == null){
            return null;
        }
        if(user == null || user.getLabID() == null){
            return null;
        }
        Power power = new Power();
        //
        user.setRoles(rolesList);

        Map<String,Object> confMap = new HashMap<>();
        confMap.put("orgID",user.getOrgID());
        confMap.put("uid",user.getUid());
        for (Role role:rolesList) {
            if ("1".equals(role.getRole_type())){
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResourcesBySid(user.getOrgID(),user.getLabID(),role.getRoleid());
                List<Resource> reList = new LinkedList<>();
                for (Resource resource:resourcesList) {
                    reList.add(resource);
                    power = addResourceToPower(power,resource);
                    break;//保留一个就行了
                }
                role.setResources(reList);
            } else {
                confMap.put("roleid",role.getRoleid());
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResources(confMap);
                List<Resource> reList = new LinkedList<>();
                for(Resource resource:resourcesList){
                    if("1".equals(resource.getStype_role())){//本科室资源
                        if(user.getLabID().equals(user.getOrgID())){
                            continue;
                        }
                        resource.setSid(user.getLabID());
                        resource.setSlab_name(user.getLab_name());

                    }
                    reList.add(resource);
                    power = addResourceToPower(power,resource);
                }
                role.setResources(reList);
            }
        }
        user.setFrontEndPower((Power)gson.fromJson(gson.toJsonTree(power).getAsJsonObject(), new TypeToken<Power>(){}.getType()));
        try {
            Map<String, List<String>> mapDep = getDepartmentFromMysql(AllDao.getInstance().getSyRoleDao().getSlabNames());
            power.setHas_search(addDepartmentPower(power.getHas_search(), mapDep));
            power.setHas_searchExport(addDepartmentPower(power.getHas_searchExport(), mapDep));
        } catch (Exception e) {
            logger.error("科室映射失败：", e);
        }
        return power;
    }

    public static Power addResourceToPower(Power power,Resource resource){
        if("有".equals(resource.getHas_search())){
            if(!isExistResource(power.getHas_search(),resource)){
                power.getHas_search().add(resource);
            }
        }
        if("有".equals(resource.getHas_searchExport())){
            if(!isExistResource(power.getHas_searchExport(),resource)){
                power.getHas_searchExport().add(resource);
            }
        }
        if("有".equals(resource.getHas_traceCRF())){
            if(!isExistResource(power.getHas_traceCRF(),resource)){
                power.getHas_traceCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_addCRF())){
            if(!isExistResource(power.getHas_addCRF(),resource)){
                power.getHas_addCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_addBatchCRF())){
            if(!isExistResource(power.getHas_addBatchCRF(),resource)){
                power.getHas_addBatchCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_editCRF())){
            if(!isExistResource(power.getHas_editCRF(),resource)){
                power.getHas_editCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_deleteCRF())){
            if(!isExistResource(power.getHas_deleteCRF(),resource)){
                power.getHas_deleteCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_browseDetail())){
            if(!isExistResource(power.getHas_browseDetail(),resource)){
                power.getHas_browseDetail().add(resource);
            }
        }
        return power;
    }
    public static Power addResourceInGroupToPower(Power power,Resource resource,Group group){
        if("有".equals(resource.getHas_search()) && "有".equals(group.getHas_search())){
            if(!isExistResource(power.getHas_search(),resource)){
                power.getHas_search().add(resource);
            }

        }
        if("有".equals(resource.getHas_searchExport()) && "有".equals(group.getHas_searchExport())){
            if(!isExistResource(power.getHas_searchExport(),resource)){
                power.getHas_searchExport().add(resource);
            }

        }
        if("有".equals(resource.getHas_traceCRF()) && "有".equals(group.getHas_traceCRF())){
            if(!isExistResource(power.getHas_traceCRF(),resource)){
                power.getHas_traceCRF().add(resource);
            }

        }
        if("有".equals(resource.getHas_addCRF()) && "有".equals(group.getHas_addCRF())){
            if(!isExistResource(power.getHas_addCRF(),resource)){
                power.getHas_addCRF().add(resource);
            }
        }
        if("有".equals(resource.getHas_addBatchCRF()) && "有".equals(group.getHas_addBatchCRF())){
            if(!isExistResource(power.getHas_addBatchCRF(),resource)){
                power.getHas_addBatchCRF().add(resource);
            }

        }
        if("有".equals(resource.getHas_editCRF()) && "有".equals(group.getHas_editCRF())){
            if(!isExistResource(power.getHas_editCRF(),resource)){
                power.getHas_editCRF().add(resource);
            }

        }
        if("有".equals(resource.getHas_deleteCRF()) && "有".equals(group.getHas_deleteCRF())){
            if(!isExistResource(power.getHas_deleteCRF(),resource)){
                power.getHas_deleteCRF().add(resource);
            }

        }
        if("有".equals(resource.getHas_browseDetail()) && "有".equals(group.getHas_browseDetail())){
            if(!isExistResource(power.getHas_browseDetail(),resource)){
                power.getHas_browseDetail().add(resource);
            }

        }
        return power;
    }

    public static boolean isExistResource(List<Resource> list,Resource resource){
        if(resource == null || resource.getSid() == null){
            return false;
        }
        boolean flag = false;
        for(Resource r:list){
            if(resource.getSid().equals(r.getSid())){
                flag = true;
                break;
            }
        }
        return flag;
    }
    /**
     * 更新密码
     * @param param
     * @return
     */
    public String updatePWD(String param) {
        String email = null;
        String pwd = null;
        String md5 = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            email = paramObj.get("email").getAsString();
            pwd = paramObj.get("pwd").getAsString();
            md5 = paramObj.get("md5").getAsString();
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("pwd",pwd);
        map.put("md5",md5);
        int counter = AllDao.getInstance().getSyUserDao().updatePWD(map);
        if(counter == 0){
            return ParamUtils.errorParam("更新失败,更新链接失效");
        }else{
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("更新成功");
            return gson.toJson(resultBean);
        }
    }

    /**
     *
     * @param paramObj
     * @return
     */
    public static String existEmail(JsonObject paramObj) {
        String email = paramObj.get("email").getAsString();
        int counter = AllDao.getInstance().getSyUserDao().existEmail(email);
        ResultBean resultBean = new ResultBean();
        if(counter == 0){
            resultBean.setCode(0);
        }else {
            resultBean.setCode(1);
        }
        return gson.toJson(resultBean);
    }



    public static User transformRole(User user){
        List<Role> roles = user.getRoles();
        String labID = user.getLabID();
        if(labID.equals(user.getOrgID())){//用户属于医院,不需要检查本科室成员角色
            return user;
        }
        for(Role role:roles){
            List<Resource> resources = (List<Resource>) role.getResources();
            for(Resource resource:resources){
                if("1".equals(resource.getStype_role())){
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
        Map<String,String> map = new HashMap<>();
        List<Resource> addlist = power.getHas_addCRF();
        for(Resource resource:addlist)
        {
            String sid=resource.getSid();
            String has_addCRF=resource.getHas_addCRF();
            if(!labIDSet.contains(sid) && "有".equals(has_addCRF)){
                labIDSet.add(sid);
                map.put(sid,resource.getSlab_name());
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
        result.add("data",data);
        List<CRFLab> defaultList = AllDao.getInstance().getSyResourceDao().getCrfIDListByLab(labIDs,orgID);
        if(defaultList != null){
            JsonObject defaultObj = new JsonObject();
            defaultObj.addProperty("lab_name",lab_name);
            defaultObj.addProperty("labID",labID);
            JsonArray crfList = new JsonArray();
            defaultObj.add("crfList",crfList);
            for(CRFLab crfLab:defaultList){
                JsonObject item = new JsonObject();
                item.addProperty(crfLab.getCrf_id(),crfLab.getCrf_name());
            }
            if(crfList.size() == 0){
                data.add("default",new JsonObject());
            }else{
                data.add("default",defaultObj);
            }

        }else{
            data.add("default",new JsonObject());
        }
        labIDs = labIDSet.toArray(new String[labIDSet.size()]);
        JsonArray listArray = new JsonArray();
        data.add("list",listArray);
        if(labIDSet.size() > 0){
            List<CRFLab> crfLablist =  AllDao.getInstance().getSyResourceDao().getCrfIDListByLab(labIDs,orgID);
            for(String tmpID:map.keySet()){
                JsonObject item = new JsonObject();
                String name = map.get(tmpID);
                item.addProperty("labName",name);
                item.addProperty("labID",tmpID);
                JsonObject crfList = new JsonObject();
                item.add("crfList",crfList);
                for(CRFLab crfLab:crfLablist){
                    if(crfLab.getLabID().equals(tmpID)){
                        crfList.addProperty(crfLab.getCrf_id(),crfLab.getCrf_name());
                    }
                }
                listArray.add(item);
            }
        }

        data.add("list",listArray);
        result.addProperty("code",1);
        return gson.toJson(result);
    }

    /**
     *
     * @param uid
     * @param param
     * @return
     */
    public String vitaBoardConfigSave(String uid, String param) {
        JsonObject paramObj = null;
        String dataStr = null;
        try{
            paramObj = (JsonObject) jsonParser.parse(param);
            JsonArray dataArray = paramObj.getAsJsonArray("data");
            dataStr = gson.toJson(dataArray);
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        AllDao.getInstance().getSyUserDao().deleteVitaCong(uid);
        int count = AllDao.getInstance().getSyUserDao().insertVitaCong(uid,dataStr);
        ResultBean re = new ResultBean();
        if(count == 1){
            re.setCode(1);
        }else {
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
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            labID = paramObj.get("labID").getAsString();
        }catch (Exception e){
            return ParamUtils.errorParam("参数错误");
        }
        List<CRFLab> crfLablist = AllDao.getInstance().getSyResourceDao().getCrfIDByLab(labID,user.getOrgID());
        if(crfLablist == null || crfLablist.size()==0){
            return ParamUtils.errorParam("没有找到对应的参数");
        }else {
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
        if(StringUtils.isEmpty(sessionID))return;
        String lastuid=RedisUtil.getValue(sessionID);
        if(StringUtils.isEmpty(lastuid))
        {
            User user=UserProcessor.getUserByUids(uid);
            RedisUtil.setUserOnLine(user,sessionID);
        }

    }
}
