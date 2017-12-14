package com.gennlife.platform.authority;

import com.gennlife.platform.configuration.URLBean;
import com.gennlife.platform.util.ConfigUtils;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chenjinfeng on 2017/12/13.
 */
@Component
public class AccessUtils {
    @Autowired
    URLBean urlBean;

    public boolean checkAccessPatientSn(String patient_sn, HttpServletRequest paramRe) {
        return checkAccessPatientSn(patient_sn, paramRe, new JsonArray());
    }

    public boolean checkAccessPatientSn(String patient_sn, HttpServletRequest paramRe, JsonArray source) {
        JsonObject patient = getPatData(patient_sn, paramRe, source);
        String getPatientSn = GsonUtil.getStringValue("hits.hits._source.patient_info.PATIENT_SN", patient);
        if (StringUtils.isEmpty(getPatientSn)) return false;
        return true;
    }

    public JsonObject getPatData(String patient_sn, HttpServletRequest paramRe, JsonArray source) {
        JsonObject param = new JsonObject();
        if (paramRe != null) {
            AuthorityUtil.addRolesToParam(paramRe, param);
        }
        param.addProperty("size", 1);
        param.addProperty("page", 1);
        param.addProperty("hospitalID", "public");
        param.addProperty("indexName", ConfigUtils.getSearchIndexName());
        source.add("patient_info.PATIENT_SN");
        param.add("source", source);
        param.addProperty("query", "[患者基本信息.患者编号] 包含 " + patient_sn);
        return GsonUtil.toJsonObject(HttpRequestUtils.httpPost(urlBean.getCaseSearchURL(), GsonUtil.toJsonStr(param)));
    }


}
