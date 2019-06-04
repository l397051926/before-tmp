package com.gennlife.platform.processor;

import com.alibaba.druid.util.StringUtils;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.User;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.RSAEncrypt;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by wangyiyan on 2019/3/16
 */
@Service
public class Part3Processor {
    private Logger logger = LoggerFactory.getLogger(Part3Processor.class);

    /**
     * 调取第三方服务获取到需要跳转到的链接getUrl
     * @param user 当前用户信息
     */
    public String getPart3Url(User user) {
        //获取第三方跳转链接
        long begin1 = System.currentTimeMillis();
        String uid = user.getUid();
        String pwd = AllDao.getInstance().getSyUserDao().getPwdByUid(uid);
        String resultStr = "";
        try {
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(ConfigurationService.getUrlBean().getSkipUrl()+"?uid="+uid+"&pwd="+pwd);
            resultStr = GsonUtil.toJsonStr(resultBean);
        } catch (Exception e) {
            return ParamUtils.errorParam("第三方信息异常");
        }
        long end1 = System.currentTimeMillis();
        logger.info("第三方系统返回跳转链接用时为：" + (end1 - begin1));
        return resultStr;
    }

    /**
     * 将用户信息传给第三方
     */
    public void userinfo(int action, User user){
        JsonObject userInfoObj = new JsonObject();
        userInfoObj.addProperty("uid",user.getUid() == null?"":user.getUid());
        userInfoObj.addProperty("unumber",user.getUnumber() == null?"":user.getUnumber());
        userInfoObj.addProperty("uemail",user.getUemail() == null?"":user.getUemail());
        userInfoObj.addProperty("pwd",user.getPwd() == null?"":user.getPwd());
        userInfoObj.addProperty("uname",user.getUname() == null?"":user.getUname());
        userInfoObj.addProperty("labID",user.getLabID() == null?"":user.getLabID());
        userInfoObj.addProperty("lab_name",user.getLab_name() == null?"":user.getLab_name());
        userInfoObj.addProperty("role", "科室人员");

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("action", action);
        resultObj.add("userInfo", userInfoObj);
        String param = GsonUtil.toJsonStr(resultObj);
        logger.info("传给第三方的用户信息为： " + param);
        try {
            String s = HttpRequestUtils.httpPost(ConfigurationService.getUrlBean().getUserInfoUrl(), param);
            logger.info("第三方用户信息操作返回结果: " + s);
        } catch (Exception e) {
            logger.info("第三方系统用户信息写入失败");
            e.printStackTrace();
        }
    }

    public String encryptPWD(String param) {
        String resultStr = "";
        try {
            JsonObject jsonObject = new JsonParser().parse(param).getAsJsonObject();
            String rsapwd = jsonObject.get("RSAPWD").getAsString();
            String orgPWD = RSAEncrypt.decrypt(rsapwd);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(orgPWD);
            resultStr = GsonUtil.toJsonStr(resultBean);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public String addUser() {
        List<User> allUsers = AllDao.getInstance().getSyUserDao().getAllUsers();
        for (User user : allUsers){
            this.userinfo(0, user);
        }
        return "success";
    }
}
