package com.gennlife.platform.util;

import com.gennlife.platform.model.User;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import redis.clients.jedis.JedisCluster;

import java.io.StringReader;

/**
 * Created by chen-song on 2016/12/8.
 */
public class RedisUtil {
    private static JedisCluster jedisCluster = null;
    private static Gson gson = GsonUtil.getGson();
    private static String suffix = "_info";
    private static boolean flag = true;

    public static void init(){
        jedisCluster = (JedisCluster) SpringContextUtil.getBean("jedisClusterFactory");
    }
    public static void setValue(String key,String value){
        jedisCluster.set(key,value);
    }


    public static String getValue(String key){
        if(jedisCluster.exists(key)){
            return jedisCluster.get(key);
        }
        return null;
    }
    public static void deleteKey(String key){
        if(jedisCluster.exists(key)){
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

    public static void setUser(User user){
        String key = user.getUid()+suffix;
        String value = gson.toJson(user);
        if(flag){
            jedisCluster.set(key,value);
        }

    }

    public static void setUserOnLine(User user,String sessionID){
        String exSessionID = getValue(user.getUid());
        if(exSessionID != null){
            deleteKey(exSessionID);
            deleteKey(user.getUid());
            deleteUser(user.getUid());
        }
        setValue(user.getUid(),sessionID);
        setValue(sessionID,user.getUid());
        setUser(user);
    }
    public static void setFlag(boolean v){
        flag = v;
        if(!v){//false,关闭redis，清空redis 用户信息
            String key = "*"+ suffix;
            jedisCluster.del(key);
        }
    }

}
