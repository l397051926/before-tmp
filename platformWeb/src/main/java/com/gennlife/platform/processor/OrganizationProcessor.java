package com.gennlife.platform.processor;

import com.gennlife.platform.bean.OrgListBean;
import com.gennlife.platform.bean.OrgMemberBean;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.SpringContextUtil;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chensong on 2015/12/9.
 */
public class OrganizationProcessor {
    private static Logger logger = LoggerFactory.getLogger(OrganizationProcessor.class);
    private static View viewer = new View();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    /**
     * 返回组织机构的数据
     */
    public String orgList() {
        List<OrgListBean> list = AllDao.getInstance().getOrgDao().getOrgList();
        ResultBean userBean = new ResultBean();
        userBean.setCode(1);
        userBean.setData(list);
        String jsonString = gson.toJson(userBean);
        return jsonString;
    }

    /**
     * {"data":["beijing_city_1"]}
     * 返回某组织的成员列表
     */
    public String orgMembers(JsonObject jsonObject) {
        try {
            JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
            List<OrgMemberBean> list = new LinkedList<OrgMemberBean>();
            for (JsonElement jsonElement : jsonArray) {
                String orgID = jsonElement.getAsString();
                List<OrgMemberBean> tmp = AllDao.getInstance().getOrgDao().getOneOrgList(orgID);
                list.addAll(tmp);
            }
            ResultBean userBean = new ResultBean();
            userBean.setCode(1);
            userBean.setData(list);
            String jsonString = gson.toJson(userBean);
            return jsonString;
        } catch (Exception e) {
            logger.error("请求参数出错", e);
            return ParamUtils.errorParam("请求参数出错");
        }
    }


}
