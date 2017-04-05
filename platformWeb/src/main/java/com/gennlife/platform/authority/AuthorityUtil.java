package com.gennlife.platform.authority;

import com.gennlife.platform.model.*;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.*;
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
    public static String addAuthority(HttpServletRequest paramRe) {
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        }else {
            User user = (User)paramRe.getAttribute("currentUser");
            List<Role> roles = user.getRoles();
            List<Group> groups = user.getGroups();
            Power power = user.getPower();
            if (paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                //paramObj.add("roles", gson.toJsonTree(roles));
                //从groups数组扩展权限
                paramObj.add("groups", gson.toJsonTree(groups));
                paramObj.add("power", gson.toJsonTree(power));
                return CaseProcessor.transformSid(paramObj, user);
            } else {
                return paramElement.isJsonArray()? gson.toJson(paramElement): null;
            }
        }
    }
    public static boolean isUrlArrayHasStr(String[] urlArray, String str) {
        for (String s: urlArray) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }
    public static String addTreatedAuthority(HttpServletRequest paramRe) {
        String[] urlArray = paramRe.getRequestURI().split("/");
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        } else {
            User user = (User)paramRe.getAttribute("currentUser");
            //List<Role> roles = user.getRoles();
            List<Group> groups = user.getGroups();
            Power power = user.getPower();
            if (paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                //paramObj.add("roles", gson.toJsonTree(roles));
                //从groups数组扩展权限
                /*for (Group group: groups) {
                    // List<User> members = group.getMembers();
                    JsonArray members = gson.toJsonTree(group.getMembers()).getAsJsonArray();
                    for (JsonElement member: members) {
                        JsonObject jsonUser = member.getAsJsonObject();
                        try {
                            JsonObject jsonPower = jsonUser.get("power").getAsJsonObject();
                            jsonUser.remove("frontEndPower");
                            jsonPower.remove("has_traceCRF");
                            jsonPower.remove("has_addCRF");
                            jsonPower.remove("has_editCRF");
                            jsonPower.remove("has_deleteCRF");
                            jsonPower.remove("has_browseDetail");
                            jsonPower.remove("has_addBatchCRF");
                            if (isUrlArrayHasStr(urlArray, "detail")) {
                                jsonPower.remove("has_searchExport");
                            }
                        } catch (Exception e) {
                            logger.error("错误：", e);
                        }
                    }
                    group.setMembers(members);
                }*/
                if (isUrlArrayHasStr(urlArray, "detail")) {
                    power.setHas_searchExport(null);
                }
                power.setHas_traceCRF(null);
                power.setHas_addCRF(null);
                power.setHas_editCRF(null);
                power.setHas_deleteCRF(null);
                power.setHas_browseDetail(null);
                power.setHas_addBatchCRF(null);
                paramObj.add("groups", gson.toJsonTree(groups));
                paramObj.add("power", gson.toJsonTree(power));
                return CaseProcessor.transformSid(paramObj,user);
            } else {
                return paramElement.isJsonArray()? gson.toJson(paramElement): null;
            }
        }
    }
    public static String addSearchCaseAuthority(HttpServletRequest paramRe) {
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        } else {
            User user = (User)paramRe.getAttribute("currentUser");
            List<Role> roles = user.getRoles();
            List<Group> groups = user.getGroups();
            Power power = user.getPower();
            if (paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                // paramObj.add("roles", gson.toJsonTree(roles));
                // 从groups数组扩展权限
                power.setHas_traceCRF(null);
                power.setHas_searchExport(null);
                power.setHas_addCRF(null);
                power.setHas_editCRF(null);
                power.setHas_deleteCRF(null);
                power.setHas_browseDetail(null);
                power.setHas_addBatchCRF(null);
                paramObj.add("groups",gson.toJsonTree(groups));
                paramObj.add("power",gson.toJsonTree(power));
                return CaseProcessor.transformSid(paramObj,user);
            } else {
                return paramElement.isJsonArray()?gson.toJson(paramElement):null;
            }
        }
    }
    public static boolean isAdmin(User user) {
        List<Admin> admins = user.getAdministrators();
        if (admins == null) {
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
