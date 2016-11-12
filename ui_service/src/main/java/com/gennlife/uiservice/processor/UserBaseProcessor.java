package com.gennlife.uiservice.processor;

import com.gennlife.uiservice.init.ConfigurationService;
import com.gennlife.uiservice.model.CRFLab;
import com.gennlife.uiservice.model.User;
import com.gennlife.uiservice.service.ResourceService;
import com.gennlife.uiservice.service.UserService;
import com.gennlife.uiservice.util.GsonUtil;
import com.gennlife.uiservice.util.Mailer;
import com.gennlife.uiservice.util.SpringContextUtil;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen-song on 2016/11/5.
 */
public class UserBaseProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserBaseProcessor.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    private UserService userService;
    private ResourceService resourceService;
    private JedisCluster jedisCluster;
    public UserBaseProcessor(){
        userService = (UserService) SpringContextUtil.getBean("userService");
        jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
        resourceService = (ResourceService)SpringContextUtil.getBean("resourceService");
    }
    public User login(String email, String pwd, String sessionID){
        User user = userService.login(email,pwd);
        if(user != null){
            if(jedisCluster.exists(user.getUid())){//用户已经登陆
                //将缓存中这个用户数据晴空
                String exSessionID = jedisCluster.get(user.getUid());
                jedisCluster.del(exSessionID);
                jedisCluster.del(user.getUid());
                jedisCluster.del(user.getUid()+"_info");
            }
            jedisCluster.append(user.getUid()+"_info",gson.toJson(user));
            jedisCluster.append(sessionID,user.getUid());
            jedisCluster.append(user.getUid(),sessionID);
        }
        return user;


    }


    public User updateUserByUid(User user1) {
        int count = userService.updateUserByUid(user1);
        if (count == 1){//更新成功后，更新缓存数据
            User user = userService.getUserByUid(user1.getUid());
            jedisCluster.append(user1.getUid()+"_info",gson.toJson(user));
            return user;
        }else {
            return null;
        }
    }

    public User updatePWD(String email, String pwd, String md5) {
        int count =  userService.updatePWD(email,pwd,md5);
        if(count == 1){//更新成功
            User user = userService.getUserByEmail(email);
            return user;
        }else {
            return null;
        }
    }

    public User changePwdSender(String email, String token, String md5) {
        User user = userService.getUserByEmail(email);
        if (user == null){//不存在这个邮箱
            return null;
        }else {
            int count = userService.updateMd5(email,md5);
            if(count == 1){
                String url = ConfigurationService.getUrlBean().getEmailSendURL()+token;
                Mailer.sendHTMLMail(email, url,user);
                return user;
            }else {
                return null;
            }
        }

    }

    public int existEmail(String email) {
        User user = userService.getUserByEmail(email);
        if (user != null){
            return 1;
        }else {
            return 0;
        }
    }

    public String CRFList(User user) {
        JsonObject paramObj = (JsonObject) jsonParser.parse(gson.toJson(user));
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
        List<CRFLab> defaultList = resourceService.getCrfIDListByLab(labIDs,orgID);
        if(defaultList != null){
            JsonObject defaultObj = new JsonObject();
            defaultObj.addProperty("lab_name",lab_name);
            defaultObj.addProperty("labID",labID);
            JsonArray crfList = new JsonArray();
            defaultObj.add("crfList",crfList);
            for(CRFLab crfLab:defaultList){
                JsonObject item = new JsonObject();
                item.addProperty(crfLab.getCrf_id(),crfLab.getCrf_name());
                crfList.add(item);
            }
            data.add("default",defaultObj);
        }else{
            data.add("default",new JsonObject());
        }
        labIDs = labIDSet.toArray(new String[labIDSet.size()]);
        JsonArray listArray = new JsonArray();
        data.add("list",listArray);
        if(labIDSet.size() > 0){
            List<CRFLab> crfLablist =  resourceService.getCrfIDListByLab(labIDs,orgID);
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
}
