package com.gennlife.platform.util;

import com.gennlife.platform.model.Role;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chensong on 2015/12/31.
 */
public class GsonUtil {
    private static Gson gson = null;
    private static JsonParser jsonParser = new JsonParser();
    private static final Logger logger = LoggerFactory.getLogger(GsonUtil.class);

    static {
        ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().startsWith("CGLIB");
            }

            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        gson = new GsonBuilder()
                .setExclusionStrategies(myExclusionStrategy)
                .disableHtmlEscaping()
                .create();
    }

    public static String getStringValue(String key, JsonObject tmp) {
        String[] keys = key.split("\\.");
        Object obj = getObjValue(keys, tmp);
        if (obj instanceof String) return (String) obj;
        if (obj == null) return "";
        return toJsonStr(obj);
    }

    public static JsonObject toJsonObject(String str) {
        if (StringUtils.isEmpty(str)) return null;
        try {
            return jsonParser.parse(str).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            logger.error("", e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        } catch (NullPointerException e) {
            logger.error("", e);
        }
        return null;

    }

    public static Object getObjValue(String[] keys, JsonObject tmp) {
        if (tmp == null) return null;
        for (int i = 0; i < (keys.length - 1); i++) {
            if (tmp.has(keys[i])) {
                JsonElement tmpelem = tmp.get(keys[i]);
                if (tmpelem.isJsonArray()) {
                    if (tmpelem.getAsJsonArray().size() == 0) return null;
                    tmp = tmpelem.getAsJsonArray().get(0).getAsJsonObject();
                } else if (tmpelem.isJsonObject())
                    tmp = tmpelem.getAsJsonObject();
                else if (tmpelem.isJsonNull())
                    return null;
            } else {
                return null;
            }

        }
        if (tmp.has(keys[keys.length - 1])) {
            JsonElement result = tmp.get(keys[keys.length - 1]);
            if (result.isJsonPrimitive()) return result.getAsString();
            else if (result.isJsonArray()) return result.getAsJsonArray();
            else if (result.isJsonNull()) return null;
            else if (result.isJsonObject()) return result.getAsJsonObject();
            else return result.toString();
        }
        return null;
    }

    public static String toJsonStr(Object obj) {
        if (obj instanceof String) return (String) obj;
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) {
        Role role = new Role();
        role.setCreatorID("aaa");
        System.out.println(getGson().toJson(role));
    }

    public static <T> JsonElement toJsonTree(List<T> list) {
        return gson.toJsonTree(list);
    }
    public static LinkedList<JsonElement> getJsonArrayAllValue(String key, JsonObject data) {
        LinkedList<JsonElement> tmplist = new LinkedList<>();
        int find = -1;
        LinkedList<JsonElement> resultlist = new LinkedList<>();
        resultlist.add(data);
        LinkedList<JsonElement> swap = null;
        String head = null;
        while (!StringUtils.isEmpty(key)) {
            find = key.indexOf('.');
            if (find > 0) {
                head = key.substring(0, find);
                key = key.substring(find + 1);
            } else {
                head = key;
                key = null;
            }
            for (JsonElement element : resultlist) {
                if (element.isJsonObject()) {
                    JsonObject tmp = element.getAsJsonObject();
                    if (tmp.has(head)) {
                        JsonElement tmpelement = tmp.get(head);
                        if (tmpelement.isJsonArray())
                            arrayToCollection(tmplist, tmpelement.getAsJsonArray());
                        else
                            tmplist.add(tmpelement);
                    }
                }
            }
            swap = tmplist;
            tmplist = resultlist;
            resultlist = swap;
            tmplist.clear();
        }

        return resultlist;

    }
    public static void arrayToCollection(Collection collection, JsonArray array) {
        for (JsonElement element : array) {
            collection.add(element);
        }
    }
}
