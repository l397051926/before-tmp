package com.gennlife.platform.authority;

import com.gennlife.platform.controller.UserController;
import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.MemCachedUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


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
        HttpSession session = paramRe.getSession();
        if(session == null){
            return ParamUtils.errorSessionLosParam();
        }
        String sessionID = session.getId();
        logger.debug("sessionID = "+sessionID);
        String uid = MemCachedUtil.get(sessionID);
        logger.debug("uid = "+uid);
        if(uid == null){
            return ParamUtils.errorSessionLosParam();
        }
        User userS = MemCachedUtil.getUser(uid);
        if(userS == null){
            userS = UserProcessor.getUserByUid(uid);
            //MemCachedUtil.setUserWithTime(uid,userS, UserController.sessionTimeOut);
        }
        JsonObject user = (JsonObject) jsonParser.parse(gson.toJson(userS));
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

    public static String addAuthorityForString(String param,HttpSession session){
        JsonElement paramElement = jsonParser.parse(param);
        if(session == null){
            return ParamUtils.errorSessionLosParam();
        }
        String sessionID = session.getId();
        logger.debug("sessionID = "+sessionID);
        String uid = MemCachedUtil.get(sessionID);
        logger.debug("uid = "+uid);
        if(uid == null){
            return ParamUtils.errorSessionLosParam();
        }
        User userS = UserProcessor.getUserByUid(uid);
        JsonObject user = (JsonObject) jsonParser.parse(gson.toJson(userS));
        JsonArray roles = user.getAsJsonArray("roles");
        if(paramElement.isJsonObject()){
            JsonObject paramObj = paramElement.getAsJsonObject();
            paramObj.add("roles",roles);
            //paramObj.addProperty("indexName",indexName);
            return gson.toJson(paramObj);
        }else if(paramElement.isJsonArray()){
            return gson.toJson(paramElement);
        }else{
            return null;
        }
    }

}
