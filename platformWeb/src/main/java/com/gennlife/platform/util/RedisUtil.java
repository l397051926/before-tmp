package com.gennlife.platform.util;

import com.gennlife.platform.configuration.JedisClusterFactory;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CrfProcessor;
import com.gennlife.platform.processor.UserProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by chen-song on 2016/12/8.
 */
@Component
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static JedisCluster jedisCluster = null;
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();
    private static String suffix = "_info";
    private static boolean flag = true;
    private static String imageSaveToRedisId = "imageSaveToRedisId";

    @Autowired
    public void init(JedisClusterFactory redis) {
        logger.info("Redis init");
        jedisCluster = redis.getJedisCluster();
    }

    public static boolean setImageId(String sessionID, List<String> imgUrl) {
        try {
            sessionID += imageSaveToRedisId;
            String oldImageId = getValue(sessionID);
            JsonObject oldImageIdObj = null;
            if (oldImageId != null) {
                oldImageIdObj = jsonParser.parse(oldImageId).getAsJsonObject();
            } else {
                oldImageIdObj = new JsonObject();
            }
            for (String url : imgUrl) {
                oldImageIdObj.addProperty(url, url);
            }
            String result = jedisCluster.set(sessionID, gson.toJson(oldImageIdObj));
            if (!result.equalsIgnoreCase("ok")) {
                logger.error("redis 写入setImageId失败 " + sessionID + " return result " + result);
                return false;
            }
            logger.info("redis 写入setImageId成功 " + result);
            return true;
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
            return false;
        }
    }

    public static void delImageIdFromFs(String sessionID) {
        try {
            sessionID += imageSaveToRedisId;
            String url = getValue(sessionID);
            if (url != null) {
                CrfProcessor processor = new CrfProcessor();
                JsonObject urlObj = jsonParser.parse(url).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : urlObj.entrySet()) {
                    String imageId = entry.getValue().getAsString();
                    processor.deleteImg(imageId);
                }
            }
            logger.info("redis 删除ImageId: " + url);
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
        }
    }

    public static void delImageId(String sessionID) {
        try {
            sessionID += imageSaveToRedisId;
            if (jedisCluster.exists(sessionID)) {
                logger.info("保存 del ImageId: " + sessionID);
                jedisCluster.del(sessionID);
            }
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
        }
    }

    public static boolean setValue(String key, String value) {
        try {
            String result = jedisCluster.set(key, value);
            if (!result.equalsIgnoreCase("ok")) {
                logger.error("redis 写入失败 " + key + " return result " + result);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
            return false;
        }
    }


    public static String getValue(String key) {
        try {
            if (jedisCluster.exists(key)) {
                return jedisCluster.get(key);
            }
            return null;
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
            return null;
        }
    }

    public static void deleteKey(String key) {
        try {
            if (StringUtils.isEmpty(key)) return;
            if (key != null && jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
        }
    }

    public static User getUser(String uid) {
        try {
            String key = uid + suffix;
            if (jedisCluster.exists(key) && flag) {
                String userStr = jedisCluster.get(key);
                JsonReader jsonReader = new JsonReader(new StringReader(userStr));
                jsonReader.setLenient(true);
                User user = gson.fromJson(jsonReader, User.class);
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
            return null;
        }
    }

    public static void deleteUser(String uid) {
        try {
            String key = uid + suffix;
            if (jedisCluster.exists(key) && flag) {
                jedisCluster.del(key);
            }
        } catch (Exception e) {
            logger.error("redis 出错" + e.getMessage());
        }
    }

    public static boolean setUser(User user) {
        String key = user.getUid() + suffix;
        String value = gson.toJson(user);
        if (flag) {
            return setValue(key, value);
        }
        return false;
    }

    public static boolean setUserOnLine(User user, String sessionID) {
        String exSessionID = getValue(user.getUid());
        exit(user.getUid(), exSessionID);
        if (setValue(user.getUid(), sessionID) &&
                setValue(sessionID, user.getUid()) && setUser(UserProcessor.getUserByUids(user.getUid()))) {
            LogUtils.BussnissLog("登录设置:" + sessionID + "=" + user.getUid() + "成功");
        } else {
            exit(user.getUid(), sessionID);
            LogUtils.BussnissLogError("redis 写入失败");
        }
        return true;
    }

    public static void userLogout(String sessionID) {
        if (StringUtils.isEmpty(sessionID)) return;
        String uid = getValue(sessionID);
        if (!StringUtils.isEmpty(uid)) {
            exit(uid, sessionID);
        }

    }

    public static void userLogoutByUid(String uid) {
        if (StringUtils.isEmpty(uid)) return;
        String sessionID = getValue(uid);
        if (!StringUtils.isEmpty(sessionID)) {
            exit(uid, sessionID);
        }

    }

    public static void exit(String uid, String sessionID) {
        deleteKey(sessionID);
        deleteKey(uid);
        deleteUser(uid);
    }

    public static void updateUserOnLine(String uid) {
        String sessionID = getValue(uid);
        if (StringUtils.isEmpty(sessionID)) {
            return;
        }
        User user = UserProcessor.getUserByUids(uid);
        if (user == null) return;
        RedisUtil.setUserOnLine(user, sessionID);
        logger.info("更新设置:" + sessionID + "=" + user.getUid() + "成功");
    }

    public static void setFlag(boolean v) {
        flag = v;
        if (!v) {//false,关闭redis，清空redis 用户信息
            String key = "*" + suffix;
            jedisCluster.del(key);
        }
    }

    public static void updateUserOnLine(Collection<String> uidList) {
        if (uidList == null || uidList.size() == 0) return;
        logger.info("update users " + gson.toJsonTree(uidList));
        uidList.parallelStream().forEach(uid -> updateUserOnLine(uid));
    }

    public static void clearAll() {
        try {
            Map<String, JedisPool> map = jedisCluster.getClusterNodes();
            for (Map.Entry<String, JedisPool> item : map.entrySet()) {
                Jedis jedis = null;
                try {
                    jedis = item.getValue().getResource();
                    jedis.connect();
                    Set<String> keys = jedis.keys("*");
                    for (String key : keys) {
                        jedisCluster.del(key);
                    }
                } catch (Exception e) {
                    logger.error("jedis error ", e);

                } finally {
                    if (jedis != null) {
                        try {
                            jedis.close();
                        } catch (Exception e1) {

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("clear all error ", e);
        }
    }

    public static void exit(List<String> uids) {
        if (uids != null) uids.forEach(uid -> exit(uid, null));
    }
}
