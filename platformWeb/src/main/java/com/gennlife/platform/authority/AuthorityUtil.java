package com.gennlife.platform.authority;

import com.gennlife.platform.bean.HitsConfigBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.*;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
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
        } else {
            User user = (User) paramRe.getAttribute("currentUser");
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
                return paramElement.isJsonArray() ? gson.toJson(paramElement) : null;
            }
        }
    }

    public static boolean isUrlArrayHasStr(String[] urlArray, String str) {
        for (String s : urlArray) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static String addTreatedAuthority(HttpServletRequest paramRe) {
        //从request请求中获取传送的实体内容
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        if(param.contains("crfId")){
            return param;
        }
        Object hist = paramRe.getAttribute(HitsConfigBean.HIES_SESSION_ID);
        logger.info(" hist: "+ hist);
        if(hist !=null && !StringUtils.isEmpty(hist.toString()) && HitsConfigBean.HIES_SESSION_ID.equals(hist.toString()) ){
            return param;
        }
        //从request域中获取当前用户
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        } else {
            if (paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                return addRolesToParam(paramRe, paramObj);
            } else {
                return paramElement.isJsonArray() ? gson.toJson(paramElement) : null;
            }
        }
    }

    public static String addTreatedAuthority(HttpServletRequest paramRe,JsonElement paramElement) {
        //从request请求中获取传送的实体内容
        String param = ParamUtils.getParam(paramRe);
        if(param.contains("crfId")){
            return param;
        }
        //从request域中获取当前用户
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        } else {
            if (paramElement.isJsonObject()) {
                JsonObject paramObj = paramElement.getAsJsonObject();
                return addRolesToParam(paramRe, paramObj);
            } else {
                return paramElement.isJsonArray() ? gson.toJson(paramElement) : null;
            }
        }
    }

    /**
     * 给详情页加上权限
     * @param paramRe  request请求
     * @param paramObj 请求中的实体内容封装成jsonObject
     * @return
     */
    public static String addRolesToParam(HttpServletRequest paramRe, JsonObject paramObj) {
        //从域中获取当前用户对象
        User user = (User) paramRe.getAttribute("currentUser");
        //List<Role> roles = user.getRoles();
        List<Group> groups = user.getGroups();
        //获取当前对象的权限
        Power power = user.getPower();
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

        //分割url
        String[] urlArray = paramRe.getRequestURI().split("/");
        if (isUrlArrayHasStr(urlArray, "detail")) {  //？
            power.setHas_searchExport(null);
        }
        power.setHas_traceCRF(null);
        power.setHas_addCRF(null);
        power.setHas_editCRF(null);
        power.setHas_deleteCRF(null);
        power.setHas_browseDetail(null);
        power.setHas_addBatchCRF(null);
        power.setHas_searchCRF(null);
        power.setHas_importCRF(null);

        //添加小组与权限两个字段
        paramObj.add("groups", gson.toJsonTree(groups));
        paramObj.add("power", gson.toJsonTree(power));
        return CaseProcessor.transformSid(paramObj, user);
    }

    public static String addSearchCaseAuthority(HttpServletRequest paramRe) {
        String param = ParamUtils.getParam(paramRe);
        JsonElement paramElement = jsonParser.parse(param);
        Object object = paramRe.getAttribute("currentUser");
        if (object == null) {
            logger.error("paramRe里面无currentUser");
            return ParamUtils.errorSessionLosParam();
        } else {
            User user = (User) paramRe.getAttribute("currentUser");
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
                power.setHas_searchCRF(null);
                power.setHas_importCRF(null);
                paramObj.add("groups", gson.toJsonTree(groups));
                paramObj.add("power", gson.toJsonTree(power));
                return CaseProcessor.transformSid(paramObj, user);
            } else {
                return paramElement.isJsonArray() ? gson.toJson(paramElement) : null;
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

    public static String getCurrentDeptQuery(User user) {
//        List<String> depts = AllDao.getInstance().getSyRoleDao().getSlabNameMappingByLabName(user.getLab_name(), user.getOrgID());
        List<String> depts = AllDao.getInstance().getSyRoleDao().getlabMappingByLabName(user.getLab_name(), user.getOrgID());
        if (depts == null) depts = new LinkedList<>();
        if(!StringUtils.isEmpty(user.getLab_name()))depts.add(user.getLab_name());
        if(depts.size()==0)return null;
        return "[患者基本信息.历次就诊科室] 包含 " + StringUtils.collectionToDelimitedString(depts, ",");
    }
}
