package com.gennlife.platform.authority;

import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.Role;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by chen-song on 16/9/23.
 */
public class AuthorityUtil {
    private static Logger logger = LoggerFactory.getLogger(AuthorityUtil.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    public static String addAuthority(HttpServletRequest paramRe){
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        Object object = paramRe.getAttribute("currentUser");
        if(object == null){
            return ParamUtils.errorSessionLosParam();
        }else {
            JsonObject user = (JsonObject)jsonParser.parse(gson.toJson(object));
            JsonArray roles = user.getAsJsonArray("roles");
            JsonArray groups = user.getAsJsonArray("groups");
            if(paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                paramObj.add("roles", roles);
                //从groups数组扩展权限
                paramObj.add("groups",groups);
                for(JsonElement group:groups){
                    JsonObject groupObj = group.getAsJsonObject();
                }
                return gson.toJson(paramObj);
            } else {
                return paramElement.isJsonArray()?gson.toJson(paramElement):null;
            }
        }
    }
    public static boolean isAdmin(User user){
        List<Admin> admins = user.getAdministrators();
        if(admins == null){
            return false;
        }
        boolean isAdmin = false;
        for (Admin admin : admins) {
            if (admin.getPrivilegeType().equals("admin")
                    && admin.getPrivilegeValue().equals("admin")) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }
    public static JsonArray mergeRoles(JsonObject groupObj,Role role, JsonArray roles){
        boolean flag = false;

        for(JsonElement roleElement:roles){
            JsonObject roleObj = roleElement.getAsJsonObject();
            int roleid = roleObj.get("roleid").getAsInt();
            if(roleid == role.getRoleid()){

                flag = true;
            }
        }
        if(!flag){
            JsonObject roleObj = (JsonObject) jsonParser.parse(gson.toJson(role));
            roles.add(roleObj);
        }
        return roles;
    }
}
