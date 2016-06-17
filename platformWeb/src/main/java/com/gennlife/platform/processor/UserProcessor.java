package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.bean.SyResource;
import com.gennlife.platform.bean.SyRole;
import com.gennlife.platform.bean.SyUser;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.Mailer;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chensong on 2015/12/4.
 */
public class UserProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserProcessor.class);
    private static SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer= new View();
    public String login(JsonObject jsonObject) throws IOException {
        try{
            String loginName = jsonObject.get("loginname").getAsString();
            String pwd = jsonObject.get("pwd").getAsString();
            LogUtils.BussnissLog("用户：" + loginName + " >>> 进行登陆");
            Map<String,Object> confMap = new HashMap<String,Object>();
            confMap.put("loginname", loginName);
            confMap.put("pwd", pwd);
            SyUser user = null;
            try{
                user = AllDao.getInstance().getSyUserDao().login(confMap);
            }catch (Exception e){
                logger.error("", e);
            }
            if(user == null){
                user = new SyUser();
                user.setId(-1);
            }else{
                Date uptime = user.getUptime();
                logger.info("uptime=" + uptime.toString());
                user.setLastModifyTime(time.format(uptime));
                Set<SyResource> resources = new HashSet<SyResource>();
                if (user.getId() == 1) {
                    List<SyResource> roleResourses = AllDao.getInstance().getSyResourceDao().getAllSyResource();
                    resources.addAll(roleResourses);
                }else{
                    List<SyRole> roles = AllDao.getInstance().getSyRoleDao().getAllSyRoleByUser(user.getId());
                    for (SyRole r : roles) {
                        List<SyResource> roleResourses =  AllDao.getInstance().getSyResourceDao().getAllSyResourceByRole(r.getId());
                        resources.addAll(roleResourses);
                    }
                }
                user.setResources(resources);
            }
            if(user.getId() == -1){
                LogUtils.BussnissLog("用户：" + loginName + " >>> 登陆失败");
            }else{
                LogUtils.BussnissLog("用户：" + loginName + " >>> 登录成功");
            }
            ResultBean userBean = new ResultBean();
            if(user.getId() == -1){
                userBean.setCode(0);
            }else{
                userBean.setCode(1);
            }
            userBean.setData(user);
            return gson.toJson(userBean);
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("出现异常");
        }

    }

    public String update(String param) throws IOException {
        boolean flag = true;
        SyUser user1 = null;
        try{
            logger.info("update param ="+param);
            SyUser user = gson.fromJson(param,SyUser.class);
            user.setUptime(new Date());
            try{
                int counter = AllDao.getInstance().getSyUserDao().updateByUid(user);
                if(counter == 0){
                    flag = false;
                }
            }catch (Exception e){
                logger.error("更新失败",e);
                return ParamUtils.errorParam("更新失败");
            }
            user1 = getUser(user.getUid());

        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("更新失败");
        }
        ResultBean userBean = new ResultBean();
        if(flag){
            userBean.setCode(1);
        }else{
            userBean.setCode(0);
        }
        userBean.setData(user1);
        return gson.toJson(userBean);
    }
    public String changePwdSender(JsonObject jsonObject) throws IOException {
        boolean flag = true;
        try{
            String uname = jsonObject.get("uid").getAsString();
            String email = jsonObject.get("email").getAsString();
            String url = jsonObject.get("url").getAsString();
            Mailer.sendHTMLMail(uname, email, url);
        }catch (Exception e){
            flag = false;
        }
        ResultBean resultBean = new ResultBean();
        if(flag){
            resultBean.setCode(1);
        }else{
            resultBean.setCode(0);
        }
        return gson.toJson(resultBean);

    }


    public static SyUser getUser(String uid){
        Map<String,Object> confMap = new HashMap<String,Object>();
        confMap.put("loginname", uid);
        SyUser user1 = null;
        try{
            user1 = AllDao.getInstance().getSyUserDao().getOneUser(confMap);
        }catch (Exception e){
            logger.error("",e);
        }
        if(user1 == null){
            user1 = new SyUser();
            user1.setId(-1);
        }
        Date uptime = user1.getUptime();
        user1.setLastModifyTime(time.format(uptime));
        logger.info(gson.toJson(user1));
        return user1;
    }

}
