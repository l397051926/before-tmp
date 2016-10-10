package com.gennlife.platform.authority;

import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by chen-song on 16/9/23.
 */
public class AuthorityUtil {
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static String addAuthority(HttpServletRequest paramRe){
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        HttpSession session = paramRe.getSession();
        if(session.getAttribute("user") == null){
            return ParamUtils.errorSessionLosParam();
        }
        String userStr = session.getAttribute("user").toString();
        JsonObject user = (JsonObject) jsonParser.parse(userStr);
        JsonArray roles = user.getAsJsonArray("roles");
        if(paramElement.isJsonObject()){
            JsonObject paramObj = paramElement.getAsJsonObject();
            paramObj.add("roles",roles);
            return gson.toJson(paramObj);
        }else if(paramElement.isJsonArray()){
            return gson.toJson(paramElement);
        }else{
            return null;
        }
    }
    public static boolean isAdmin(User user){
        List<Admin> admins = user.getAdministrators();
        boolean isAdmin = false;
        for (Admin admin : admins) {
            if (admin.getPrivilegeType().equals("admin")
                    && admin.getPrivilegeValue().equals("admin")) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }

}
