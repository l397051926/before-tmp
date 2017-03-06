package com.gennlife.platform.authority;

import com.gennlife.platform.model.*;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
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
        Object object = paramRe.getAttribute("currentUser");
        if(object == null){
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        }else {
            User user = (User)paramRe.getAttribute("currentUser");
            List<Role> roles = user.getRoles();
            List<Group> groups = user.getGroups();
            Power power = user.getPower();
            if(paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                //paramObj.add("roles", gson.toJsonTree(roles));
                //从groups数组扩展权限
                paramObj.add("groups",gson.toJsonTree(groups));
                paramObj.add("power",gson.toJsonTree(power));
                return CaseProcessor.transformSid(paramObj,user);
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
}
