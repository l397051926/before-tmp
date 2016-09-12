package com.gennlife.platform.processor;


import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by chen-song on 16/9/12.
 */
public class LaboratoryProcessor {
    private Logger logger = LoggerFactory.getLogger(LaboratoryProcessor.class);
    /**
     * 获取科室组织信息
     * @param paramObj
     * @return
     */
    public String orgMapData(JsonObject paramObj) {
        String uid = null;
        try{
            uid = paramObj.get("uid").getAsString();
        }catch (Exception e){
            logger.error("",e);
        }
        User user = UserProcessor.getUserByUid(uid);
        String orgID = user.getOrgID();
        Map<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("orgID",orgID);
        List<Admin> admins = AllDao.getInstance().getSyUserDao().getAdmins(map);
        boolean isAdmin = false;
        for(Admin admin:admins){
            if (admin.getPrivilegeType().equals("admin")
                    && admin.getPrivilegeValue().equals("admin")){
                isAdmin = true;
            }
        }
        JsonObject data = new JsonObject();
        if(isAdmin){

        }
        return "";
    }
}
