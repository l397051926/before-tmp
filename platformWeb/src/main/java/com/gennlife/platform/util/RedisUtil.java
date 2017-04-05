package com.gennlife.platform.util;

import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.dao.SessionMapper;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * Created by chen-song on 2016/12/8.
 */
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static JedisCluster jedisCluster = null;
    private static Gson gson = GsonUtil.getGson();
    private static String suffix = "_info";
    private static boolean flag = true;

    public static void init(){
        jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
    }
    public static boolean setValue(String key,String value){
        String result=jedisCluster.set(key,value);
        if(!result.equalsIgnoreCase("ok"))
        {
            logger.error("redis 写入失败 "+key+" return result "+result);
            return false;
        }
        return true;
    }


    public static String getValue(String key){
        if(jedisCluster.exists(key)){
            return jedisCluster.get(key);
        }
        return null;
    }
    public static void deleteKey(String key){
        if(key!=null&&jedisCluster.exists(key)){
            jedisCluster.del(key);
        }
    }

    public static User getUser(String uid){
        String key = uid + suffix;
        if(jedisCluster.exists(key) && flag){
            String userStr = jedisCluster.get(key);
            JsonReader jsonReader = new JsonReader(new StringReader(userStr));
            jsonReader.setLenient(true);
            User user = gson.fromJson(jsonReader, User.class);
            return user;
        }else {
            return null;
        }
    }
    public static void deleteUser(String uid){
        String key = uid + suffix;
        if(jedisCluster.exists(key) && flag){
            jedisCluster.del(key);
        }
    }

    public static boolean setUser(User user){
        String key = user.getUid()+suffix;
        String value = gson.toJson(user);
        if(flag){
            return setValue(key,value);
        }
        return false;
    }

    public static boolean setUserOnLine(User user,String sessionID){
        String exSessionID = getValue(user.getUid());
        exit(user.getUid(),exSessionID);
        if(setValue(user.getUid(),sessionID)&&
        setValue(sessionID,user.getUid())&&setUser(user)) {
            LogUtils.BussnissLog("登录设置:" + sessionID + "=" + user.getUid() + "成功");
        }
        else {
            exit(user.getUid(),sessionID);
            LogUtils.BussnissLogError("redis 写入失败");
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            SessionMapper dao = AllDao.getInstance().getSessionDao();
            dao.deleteByUid(user.getUid());
            dao.insertData(user.getUid(), sessionID, simpleDateFormat.format(new Date()));
        }
        catch (Exception e)
        {
            LogUtils.BussnissLogError("session_uid error",e);
            return false;
        }
        return true;
    }
    public static void userLogout(String sessionID){
        if(StringUtils.isEmpty(sessionID))return;
        String uid = getValue(sessionID);
        if(!StringUtils.isEmpty(uid)){
            exit(uid, sessionID);
        }

    }
    public static void userLogoutByUid(String uid){
        if(StringUtils.isEmpty(uid))return;
        String sessionID = getValue(uid);
        if(!StringUtils.isEmpty(sessionID)){
            exit(uid, sessionID);
        }

    }

    public static void exit(String uid, String sessionID) {
        deleteKey(sessionID);
        deleteKey(uid);
        deleteUser(uid);
        try {
            AllDao.getInstance().getSessionDao().deleteByUid(uid);
        }
        catch (Exception e)
        {
            logger.error("delete sesion_uid  ",e);
        }
    }

    public static void updateUserOnLine(String uid){
        if(true)exit(uid,null);
        //除了当前用户，其余全部下线
        String sessionID=getValue(uid);
        if(StringUtils.isEmpty(sessionID)) return;
        User user=UserProcessor.getUserByUids(uid);
        if(user==null) return;
        logger.info("更新设置:"+sessionID+"="+user.getUid()+"成功");
        setUser(user);
    }
    public static void setFlag(boolean v){
        flag = v;
        if(!v){//false,关闭redis，清空redis 用户信息
            String key = "*"+ suffix;
            jedisCluster.del(key);
        }
    }

    public static void updateUserOnLine(Collection<String> uidList) {
        if(uidList==null || uidList.size()==0) return;
        for(String uid:uidList)
            updateUserOnLine(uid);
    }

    public static void clearAll() {
        try {
            Map<String, JedisPool> map = jedisCluster.getClusterNodes();
            for (Map.Entry<String, JedisPool> item : map.entrySet()) {
                Jedis jedis = null;
                try {
                    jedis = item.getValue().getResource();
                    jedis.connect();
                    Set<String> keys=jedis.keys("*");
                    for(String key:keys)
                        jedis.del(key);
                }
                catch (Exception e)
                {
                    logger.error("jedis error ",e);

                }
                finally {
                    if(jedis!=null)
                    {
                        try {
                            jedis.close();
                        }
                        catch (Exception e1)
                        {

                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("clear all error ",e);
        }
    }
}
