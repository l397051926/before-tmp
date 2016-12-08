package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.*;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created by chensong on 2015/12/4.
 */
public class UserProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static JedisCluster jedisCluster = null;
    public UserProcessor(){
        if (jedisCluster == null){
            jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
        }
    }
    public User login(String email,String pwd) throws IOException {
        try{
            LogUtils.BussnissLog("用户：" + email + " >>> 进行登陆");
            Map<String,Object> confMap = new HashMap<String,Object>();
            confMap.put("email", email);
            confMap.put("pwd", pwd);
            User user = null;
            try{
                user = AllDao.getInstance().getSyUserDao().getUser(confMap);
            }catch (Exception e){
                logger.error("", e);
            }
            if(user == null){
                return null;
            }else{
                user = getUserByUid(user.getUid());
                return user;
            }

        }catch (Exception e){
            logger.error("",e);
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
            if(!user.getUid().equals(uidEx)){//更新的email不合法,已经存在
                return ParamUtils.errorParamResultBean("更新的email不合法,已经存在");
            }
            user.setCtime(null);//创建时间不可更新
            user.setUptime(LogUtils.getStringTime());//更新时间
            user.setOrg_name(null);//医院名称不能修改
            user.setOrgID(null);
            user.setRoles(null);//角色不可修改
            user.setPwd(null);//密码不可修改
            try{
                int counter = AllDao.getInstance().getSyUserDao().updateByUid(user);
                if(counter == 0){
                    flag = false;
                }
            }catch (Exception e){
                logger.error("更新失败",e);
                return ParamUtils.errorParamResultBean("更新失败");
            }
            user = getUserByUid(user.getUid());
            if(!flag){
                userBean.setCode(0);
                userBean.setData("更新失败");
            } if(user == null){
                userBean.setCode(0);
                userBean.setData("获取更新后数据失败");
            }else{
                map.put("pwd",user.getPwd());
                User user1 = login(user.getUemail(),user.getPwd());
                if(user1 == null){
                    userBean.setCode(0);
                    userBean.setData("更新失败");
                }else {
                    userBean.setCode(1);
                    userBean.setData(user1);
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

    public static User getUserByUid(String uid){
        if (jedisCluster == null){
            jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
        }
        User user = null;
        try {
            if(jedisCluster.exists(uid+"_info")){//如果存在，直接返回
                String userStr = jedisCluster.get(uid + "_info");
                JsonReader jsonReader = new JsonReader(new StringReader(userStr));
                jsonReader.setLenient(true);
                user = gson.fromJson(jsonReader, User.class);
                return user;
            }else {//缓存中不存在
                user = AllDao.getInstance().getSyUserDao().getUserByUid(uid);
                Map<String,Object> confMap = new HashMap<>();
                confMap.put("orgID",user.getOrgID());
                confMap.put("uid",user.getUid());
                List<Admin> adminList = AllDao.getInstance().getSyUserDao().getAdmins(confMap);
                user.setAdministrators(adminList);
                List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
                //转化本科室信息
                Power power = transformRole(user,rolesList);
                List<Group> list = AllDao.getInstance().getGroupDao().getGroupsByUid(confMap);
                Map<String,Object> map = new HashMap<>();
                map.put("orgID",user.getOrgID());
                for(Group group:list){
                    String gid = group.getGid();
                    map.put("gid",gid);
                    List<User> userList = AllDao.getInstance().getGroupDao().getUsersByGroupID(map);
                    List<User> newUserList = new LinkedList<>();
                    for(User member:userList){//补充成员的角色信息
                        User newMember = getUserByUser(member);
                        List<Role> roleList = newMember.getRoles();
                        if(roleList != null){
                            for(Role role:roleList){
                                if(role.getResources() != null){
                                    List<Resource> resourceList = (List<Resource>) role.getResources();
                                    for(Resource r:resourceList){
                                        power = addResourceInGroupToPower(power,r,group);
                                    }
                                }

                            }
                        }
                    }
                    group.setMembers(newUserList);
                }
                user.setGroups(list);
                user.setPower(power);
            }

        }catch (Exception e){
            logger.error("",e);
        }
        return user;
    }



    public static User getUserByUser(User user){
        if (jedisCluster == null){
            jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
        }
        if(user == null){
            return null;
        }else {
            if(jedisCluster.exists(user.getUid()+"_info")){
                String userStr = jedisCluster.get(user.getUid() + "_info");
                JsonReader jsonReader = new JsonReader(new StringReader(userStr));
                jsonReader.setLenient(true);
                user = gson.fromJson(jsonReader, User.class);
                return user;
            }else {
                user = AllDao.getInstance().getSyUserDao().getUserByUid(user.getUid());
                Map<String,Object> confMap = new HashMap<>();
                confMap.put("orgID",user.getOrgID());
                confMap.put("uid",user.getUid());
                List<Admin> adminList = AllDao.getInstance().getSyUserDao().getAdmins(confMap);
                user.setAdministrators(adminList);
                List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
                //转化本科室信息
                Power power = transformRole(user,rolesList);
                user.setPower(power);
                return user;
            }
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
        user.setRoles(rolesList);
        Map<String,Object> confMap = new HashMap<>();
        confMap.put("orgID",user.getOrgID());
        confMap.put("uid",user.getUid());
        for(Role role:rolesList){
            if("1".equals(role.getRole_type())){
                List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResourcesBySid(user.getOrgID(),user.getLabID(),role.getRoleid());
                List<Resource> reList = new LinkedList<>();
                for(Resource resource:resourcesList){
                    reList.add(resource);
                    power = addResourceToPower(power,resource);
                    break;//保留一个就行了
                }
                role.setResources(reList);
            }else{
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
                        power = addResourceToPower(power,resource);
                    }
                    reList.add(resource);
                }
                role.setResources(reList);
            }
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

    public String CRFList(String param) {
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        String lab_name = paramObj.get("lab_name").getAsString();
        String labID = paramObj.get("labID").getAsString();
        String orgID = paramObj.get("orgID").getAsString();
        JsonArray roles = paramObj.get("roles").getAsJsonArray();
        List<String> labIDSet = new LinkedList<>();
        Map<String,String> map = new HashMap<>();
        for(JsonElement roleItem:roles){
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
        }
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
        CRFLab crfLab = AllDao.getInstance().getSyResourceDao().getCrfIDByLab(labID,user.getOrgID());
        if(crfLab == null){
            return ParamUtils.errorParam("没有找到对应的参数");
        }else {
            ResultBean re = new ResultBean();
            re.setCode(1);
            re.setData(crfLab);
            return gson.toJson(re);
        }
    }

    public String offLineUser(String param) {
        JsonObject obj = (JsonObject) jsonParser.parse(param);
        String uid = obj.get("uid").getAsString();
        if(jedisCluster.exists(uid+"_info")){
            jedisCluster.del("*_info");
        }
        return "ok";
    }
}
