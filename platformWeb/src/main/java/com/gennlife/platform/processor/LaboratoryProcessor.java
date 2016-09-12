package com.gennlife.platform.processor;

import com.gennlife.platform.model.User;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        User user = null;
        return "";
    }
}
