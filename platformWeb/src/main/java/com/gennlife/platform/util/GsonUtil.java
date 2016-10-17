package com.gennlife.platform.util;

import com.gennlife.platform.model.Role;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by chensong on 2015/12/31.
 */
public class GsonUtil {
    private static Gson gson = null;
    static{
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

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args){
        Role role = new Role();
        role.setCreatorID("aaa");
        System.out.println(getGson().toJson(role));
    }
}
