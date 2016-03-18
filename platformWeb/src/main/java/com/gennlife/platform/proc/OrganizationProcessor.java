package com.gennlife.platform.proc;

import com.gennlife.platform.bean.OrgListBean;
import com.gennlife.platform.bean.OrgMemberBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chensong on 2015/12/9.
 */
public class OrganizationProcessor {
    private static Logger logger = LoggerFactory.getLogger(OrganizationProcessor.class);
    private static View viewer= new View();
    private static JsonParser jsonParser = new JsonParser();
    /**
     * 返回组织机构的数据
     * @param request
     * @param resps
     */
    public void orgList(HttpServletRequest request, HttpServletResponse resps){
        List<OrgListBean> list = AllDao.getInstance().getOrgDao().getOrgList();
        viewer.viewList(list, null, true, resps, request);
    }

    /** {"data":["beijing_city_1"]}
     * 返回某组织的成员列表
     * @param request
     * @param resps
     */
    public void orgMembers(HttpServletRequest request, HttpServletResponse resps){
        String param = ParamUtils.getParam(request);
        JsonObject jsonObject =jsonParser.parse(param).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
        List<OrgMemberBean> list = new LinkedList<OrgMemberBean>();
        for(JsonElement jsonElement:jsonArray){
            String orgID = jsonElement.getAsString();
            List<OrgMemberBean> tmp = AllDao.getInstance().getOrgDao().getOneOrgList(orgID);
            list.addAll(tmp);
        }

        viewer.viewList(list, null, true, resps, request);
    }
}
