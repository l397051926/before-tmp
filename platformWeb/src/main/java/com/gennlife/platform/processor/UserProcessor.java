package com.gennlife.platform.processor;

import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Admin;
import com.gennlife.platform.model.Resource;
import com.gennlife.platform.model.Role;
import com.gennlife.platform.model.User;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.Mailer;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chensong on 2015/12/4.
 */
public class UserProcessor {
    private static Logger logger = LoggerFactory.getLogger(UserProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    public ResultBean login(String param) throws IOException {
        try{
            JsonObject jsonObject = (JsonObject) jsonParser.parse(param);
            String email = jsonObject.get("email").getAsString();
            String pwd = jsonObject.get("pwd").getAsString();
            LogUtils.BussnissLog("用户：" + email + " >>> 进行登陆");
            Map<String,Object> confMap = new HashMap<String,Object>();
            confMap.put("email", email);
            confMap.put("pwd", pwd);
            User user = null;
            try{
                user = AllDao.getInstance().getSyUserDao().getUser(confMap);
                user.setPwd(null);//密码不返回
            }catch (Exception e){
                logger.error("", e);
            }
            if(user == null){
                return ParamUtils.errorParamResultBean("登陆失败");
            }else{
                confMap.put("orgID",user.getOrgID());
                confMap.put("uid",user.getUid());
                List<Admin> adminList = AllDao.getInstance().getSyUserDao().getAdmins(confMap);
                user.setAdministrators(adminList);
                List<Role> rolesList = AllDao.getInstance().getSyRoleDao().getRoles(confMap);
                if(rolesList != null){
                    user.setRoles(rolesList);
                    for(Role role:rolesList){
                        confMap.put("roleid",role.getRoleid());
                        List<Resource> resourcesList = AllDao.getInstance().getSyResourceDao().getResources(confMap);
                        role.setResources(resourcesList);
                    }
                }
            }
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(user);
            return resultBean;
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParamResultBean("出现异常");
        }

    }

    public ResultBean update(String param) throws IOException {
        boolean flag = true;
        User user = null;
        ResultBean userBean = new ResultBean();
        try{
            user = gson.fromJson(param,User.class);
            Map<String,Object> map = new HashMap<>();
            map.put("email",user.getUemail());
            String uidEx =  AllDao.getInstance().getSyUserDao().getUidByEmail(map);
            if(!user.getUid().equals(uidEx)){//更新的email不合法,已经存在
                return ParamUtils.errorParamResultBean("更新的email不合法,已经存在");
            }
            user.setCtime(null);//创建时间不可更新
            user.setUptime(LogUtils.getStringTime());//更新时间
            user.setOrg_name(null);//医院名称不能修改
            user.setOrgID(null);
            user.setRoles(null);//角色不可修改
            user.setPwd(null);//密码不可修改
            try{
                int counter = AllDao.getInstance().getSyUserDao().updateByUid(user);
                if(counter == 0){
                    flag = false;
                }
            }catch (Exception e){
                logger.error("更新失败",e);
                return ParamUtils.errorParamResultBean("更新失败");
            }
            user = getUserByUid(user.getUid());
            if(!flag){
                userBean.setCode(0);
                userBean.setData("更新失败");
            } if(user == null){
                userBean.setCode(0);
                userBean.setData("获取更新后数据失败");
            }else{
                map.put("pwd",user.getPwd());
                userBean = login(gson.toJson(map));
            }
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParamResultBean("更新失败");
        }
        return userBean;
    }
    public String changePwdSender(JsonObject jsonObject) throws IOException {
        boolean flag = true;
        try{
            String email = jsonObject.get("email").getAsString();
            String token = jsonObject.get("token").getAsString();
            String md5 = jsonObject.get("md5").getAsString();
            Map<String,Object> map = new HashMap<>();
            map.put("email",email);
            map.put("md5",md5);
            int counter = AllDao.getInstance().getSyUserDao().updateMd5(map);
            if(counter == 0){
                logger.error("更新md5失败");
                flag = false;
            }else{
                User user = AllDao.getInstance().getSyUserDao().getUserByEmail(email);
                if(user == null){
                    return ParamUtils.errorParam("email 用户不存在");
                }else{
                    String url = ConfigurationService.getUrlBean().getEmailSendURL()+token;
                    Mailer.sendHTMLMail(email, url,user);
                }

            }

        }catch (Exception e){
            flag = false;
            logger.error("",e);
        }

        ResultBean resultBean = new ResultBean();
        if(flag){
            resultBean.setCode(1);
        }else{
            resultBean.setCode(0);
        }
        return gson.toJson(resultBean);

    }

    public static User getUserByUid(String uid){
        User user = null;
        try {
            user = AllDao.getInstance().getSyUserDao().getUserByUid(uid);
        }catch (Exception e){
            logger.error("",e);
        }
        return user;
    }

    /**
     * 更新密码
     * @param param
     * @return
     */
    public String updatePWD(String param) {
        String email = null;
        String pwd = null;
        String md5 = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            email = paramObj.get("email").getAsString();
            pwd = paramObj.get("pwd").getAsString();
            md5 = paramObj.get("md5").getAsString();
        }catch (Exception e){
            logger.error("",e);
            return ParamUtils.errorParam("参数错误");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("pwd",pwd);
        map.put("md5",md5);
        int counter = AllDao.getInstance().getSyUserDao().updatePWD(map);
        if(counter == 0){
            return ParamUtils.errorParam("更新失败,更新链接失效");
        }else{
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo("更新成功");
            return gson.toJson(resultBean);
        }
    }

    /**
     *
     * @param paramObj
     * @return
     */
    public static String existEmail(JsonObject paramObj) {
        String email = paramObj.get("email").getAsString();
        int counter = AllDao.getInstance().getSyUserDao().existEmail(email);
        ResultBean resultBean = new ResultBean();
        if(counter == 0){
            resultBean.setCode(0);
        }else {
            resultBean.setCode(1);
        }
        return gson.toJson(resultBean);
    }



    public User transformRole(User user){
        List<Role> roles = user.getRoles();
        String labID = user.getLabID();
        if(labID.equals(user.getOrgID())){//用户属于医院,不需要检查本科室成员角色
            return user;
        }
        for(Role role:roles){
            if("科室成员".equals(role.getRole_type())){//如果存在本科室角色
                List<Resource> resources = (List<Resource>) role.getResources();
                for(Resource resource:resources){
                    if("本科室资源".equals(resource.getSname())){
                        resource.setSlab_name(user.getLab_name());
                    }
                }
            }
        }
        return user;
    }
}
