package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.Group;
import com.gennlife.platform.model.Power;
import com.gennlife.platform.model.Resource;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.RedisUtil;
import com.gennlife.platform.view.View;
import com.gennlife.yy.sso.CallService;
import com.google.gson.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yonyou.idm.core.ws.services.util.EncoderUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chensong on 2015/12/5.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserProcessor processor = new UserProcessor();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View view = new View();
    private static final String yySuccessCode = "6005880001";
    //用户登陆
    @RequestMapping(value = "/Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void postLogin(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            LogUtils.BussnissLog("Login param=" + param);
            String email = null;
            String pwd = null;
            try {
                JsonObject user = (JsonObject) jsonParser.parse(param);
                if (user.has("email")) {
                    email = user.get("email").getAsString();
                } else {
                    email = user.get("user").getAsString();
                }
                pwd = user.get("pwd").getAsString();
            } catch (Exception e) {
                view.viewString(ParamUtils.errorParam("参数错误"), response);
                return;
            }
            //获取用户名密码hospital_1
            User user = processor.login(email, pwd);
            ResultBean resultBean = new ResultBean();
            if (user != null) {
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String status=user.getStatus();
                String failTime=user.getFailure_time();
                String effectiveTtime=user.getEffective_time();
                Date date=new Date();
                if("禁用".equals(status)){
                    view.viewString(ParamUtils.errorParam("没有权限登陆"), response);
                    return;
                }
                if("定期有效".equals(status)){
                    if(StringUtils.isEmpty(failTime) || StringUtils.isEmpty(effectiveTtime)){
                        view.viewString(ParamUtils.errorParam("没有权限登陆"), response);
                        return;
                    }
                    if(date.after(time.parse(failTime)) || date.before(time.parse(effectiveTtime))){
                        view.viewString(ParamUtils.errorParam("时间失效，没有权限登陆"), response);
                        return;
                    }
                }
                HttpSession session = paramRe.getSession(true);
                String sessionID = session.getId();
                String loginSession = RedisUtil.getValue(user.getUid());
                if (!StringUtils.isEmpty(loginSession) && !loginSession.equals(sessionID)) {
                    LogUtils.BussnissLog("用户 " + email + " 已经登陆在其他session,进行重新登陆 " + loginSession);
                }
                String uid = null;
                try {
                    uid = RedisUtil.getValue(sessionID);
                } catch (Exception e) {
                    LogUtils.BussnissLogError("login error", e);
                }
                if (!user.getUid().equals(uid)) {
                    RedisUtil.userLogout(sessionID);
                    // logger.info("user.getUid() != uid");
                    if (!RedisUtil.setUserOnLine(user, sessionID)) {
                        view.viewString(ParamUtils.errorParam("登陆失败"), response);
                        return;
                    }
                }
                resultBean.setCode(1);
                resultBean.setData(user);
            } else {
                view.viewString(ParamUtils.errorParam("登陆失败"), response);
            }
            resultStr = gson.toJson(resultBean);
            //logger.info("user is: " + resultStr);
        } catch (Exception e) {
            logger.error("登陆异常", e);
            resultStr = ParamUtils.errorParam("登陆异常");
        }
        LogUtils.BussnissLog("登录get 耗时" + (System.currentTimeMillis() - start) + "ms");
        view.viewString(resultStr, response);
    }
    @RequestMapping(value = "/ssoLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public void ssoLogin(HttpServletRequest paramRe, HttpServletResponse response) {
        String yyssoUrl = ConfigurationService.getUrlBean().getYyssoUrl();
        Integer isMock = ConfigurationService.getUrlBean().getIsMock();
        String ssoSuccessUrl = ConfigurationService.getUrlBean().getSsoSuccessUrl();
        String ssoFailUrl = ConfigurationService.getUrlBean().getSsoFailUrl();
        String ssoSysmark = ConfigurationService.getUrlBean().getSsoSysmark();
        String ssoSuccessUrlTemp = ConfigurationService.getUrlBean().getSsoSuccessUrlTemp();
        String ssoErrorRedirectUrl = ConfigurationService.getUrlBean().getSsoErrorRedirectUrl();
        LogUtils.BussnissLog("yyssoUrl="+yyssoUrl+",isMock="+isMock+",ssoSuccessUrl="+ssoSuccessUrl+",ssoFailUrl="+ssoFailUrl+",ssoSysmark="+ssoSysmark);
        try {
            if(isMock == null){
                throw new Exception("isMock 参数未配置");
            }
            if(ssoSysmark == null){
                throw new Exception("系统标识未配置");
            }
            if(org.apache.commons.lang.StringUtils.isEmpty(yyssoUrl)){
                throw new Exception("yyssoUrl 参数未配置");
            }
            if(org.apache.commons.lang.StringUtils.isEmpty(ssoSuccessUrl)){
                throw new Exception("ssoSuccessUrl 参数未配置");
            }
            if(org.apache.commons.lang.StringUtils.isEmpty(ssoFailUrl)){
                throw new Exception("ssoFailUrl 参数未配置");
            }
            String token = paramRe.getParameter("token");
            String vitarkFlag = paramRe.getParameter("vitarkFlag");
            vitarkFlag = vitarkFlag == null? "1":"0";
            LogUtils.BussnissLog("门户系统请求的参数：token="+token);
            CallService service = new CallService(yyssoUrl,token,ssoSysmark);
            String xml = null;
            if(isMock == null || isMock == 1){
                xml = service.mockCall();
            }else{
                xml = service.call();
            }
            LogUtils.BussnissLog("校验token后，用友系统返回值,xml="+xml);
            Document document = DocumentHelper.parseText(xml);
            String errorCode = service.getRootTextByKey(document,"errcode");
            if(!org.apache.commons.lang.StringUtils.equals(errorCode,yySuccessCode)){
                String errmsg = service.getRootTextByKey(document, "errmsg");
                throw new Exception("门户系统的错误提示："+errmsg+",详细问题请联系系统管理员");
            }
            String passWord = service.getPassWord(document);
            String userName = service.getElementTextBykey(document,"username");
            LogUtils.BussnissLog("获取到的信息：errorCode="+errorCode+",passWord="+passWord+",userName="+userName);
            if(org.apache.commons.lang.StringUtils.isEmpty(userName)){
                throw new Exception("检验token后，门户系统返回的用户名未空");
            }
            String number = EncoderUtils.decode(userName);
            User user = processor.ssoLogin(number);
            String resultStr = null;
            ResultBean resultBean = new ResultBean();
            if (user != null) {
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String status = user.getStatus();
                String failTime = user.getFailure_time();
                String effectiveTtime = user.getEffective_time();
                Date date = new Date();
                boolean isError = false;
                if ("禁用".equals(status)) {
                    throw new Exception("您好"+number+"，您的账户没有权限访问本系统(被禁用)，详细问题请联系系统管理员******");
                }
                if ("定期有效".equals(status)) {
                    if (StringUtils.isEmpty(failTime) || StringUtils.isEmpty(effectiveTtime)) {
                        throw new Exception("您好"+number+"，您的账户没有权限访问本系统(账户失效)，详细问题请联系系统管理员******");
                    }
                    if (date.after(time.parse(failTime)) || date.before(time.parse(effectiveTtime))) {
                        throw new Exception("您好"+number+"，您的账户没有权限访问本系统(账户失效)，详细问题请联系系统管理员******");
                    }
                }
                HttpSession session = paramRe.getSession(true);
                String sessionID = session.getId();
                String uid = null;
                try {
                    uid = RedisUtil.getValue(sessionID);
                } catch (Exception e) {
                    LogUtils.BussnissLogError("login error", e);
                }
                if (!user.getUid().equals(uid)) {
                    RedisUtil.userLogout(sessionID);
                    if (!RedisUtil.setUserOnLine(user, sessionID)) {
                        throw new Exception("您好"+number+"，您的账户没有权限访问本系统，详细问题请联系系统管理员******");
                    }
                }
                resultBean.setCode(1);
                resultBean.setData(user);
                response.setStatus(302);
                if(org.apache.commons.lang.StringUtils.equals(vitarkFlag,"1")){
                    response.setHeader("location", ssoSuccessUrlTemp+"?token="+token);
                }else{
                    response.setHeader("location", ssoSuccessUrl);
                }
            } else {
                throw new Exception("您好"+number+"，您的账户没有权限访问本系统，详细问题请联系系统管理员******");
            }

        } catch (Exception e) {
            buildError(e.getMessage(),response, ssoFailUrl, ssoErrorRedirectUrl);
        }
    }

    private void buildError(String message,HttpServletResponse response, String ssoFailUrl, String ssoErrorRedirectUrl) {
        String error = org.apache.commons.lang.StringUtils.isEmpty(ssoErrorRedirectUrl)? ParamUtils.errorParam(message):ParamUtils.errorParamWithRedirect(message,ssoErrorRedirectUrl);
        response.setStatus(302);
        Base64.Encoder encoder = Base64.getEncoder();
        response.setHeader("location", ssoFailUrl+"error="+ encoder.encodeToString(error.getBytes(Charset.forName("UTF-8"))));
    }

    //获取用户信息
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void getUserInfo(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            user.setPower(null);
            user.setGroups(new ArrayList<Group>(0));
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setData(user);
            resultStr = gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("getUserInfo 出错：", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("getUserInfo 耗时: " + (System.currentTimeMillis() - start) + "ms");
        view.viewString(resultStr, response);
    }

    //修改
    @RequestMapping(value = "/UpdateInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postUpdate(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            if (user == null) {
                logger.error("paramRe里面无currentUser");
                return ParamUtils.errorSessionLosParam();
            }
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            String uid = null;
            try {
                uid = paramObj.get("uid").getAsString();
                if (!user.getUid().equals(uid) && !AuthorityUtil.isAdmin(user)) { // 不是自己修改,不是管理员修改
                    return ParamUtils.errorParam("无权限更新");
                }
            } catch (Exception e) {
                return ParamUtils.errorParam("缺少uid");
            }
            ResultBean resultBean = processor.update(param);
            UserProcessor.currentUpdate(user.getUid(), paramRe.getSession(false).getId());
            resultStr = gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;

    }


    @RequestMapping(value = "/UpdatePWD", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String UpdatePWD(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("UpdatePWD param=" + param);
            resultStr = processor.updatePWD(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新密码 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        logger.error("返回结果=" + resultStr);
        return resultStr;
    }


    @RequestMapping(value = "/SendEmailForChangePWD", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postChangePWD(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            logger.info("SendEmailForChangePWD param=" + param);
            resultStr = processor.changePwdSender(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("发送修改密码邮件 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ExistEmail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postExistEmail(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.existEmail(paramObj);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看账户是否存在 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/CRFList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String CRFList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            return processor.CRFList(user);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回=" + resultStr);
        logger.info("用户可访问crf列表 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VitaBoardConfigSave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VitaBoardConfigSave(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            User user = (User) paramRe.getAttribute("currentUser");
            return processor.vitaBoardConfigSave(user.getUid(), param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回=" + resultStr);
        logger.info("用户可访问crf列表 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VitaBoardConfig", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VitaBoardConfig(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            return processor.VitaBoardConfig(user.getUid());
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回=" + resultStr);
        logger.info("用户可访问crf列表 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/LabTransformCrfId", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String LabTransformCrfId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            String param = ParamUtils.getParam(paramRe);
            return processor.labTransformCrfId(user, param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("科室转化成病种id 返回=" + resultStr);
        logger.info("科室转化成病种id get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SetRedis", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String OffLineUser(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            return processor.setRedis(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("清空缓存中用户信息 返回=" + resultStr);
        logger.info(" get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String logout(HttpServletRequest paramRe) {
        try {
            String sessionId = paramRe.getSession(false).getId();
            RedisUtil.userLogout(sessionId);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("出现异常");
        }
    }

    /**
     * 科室权限校验
     */
    @RequestMapping(value = "/checkUserRole", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String checkUserRole(HttpServletRequest paramRe) {
        try {
            User user = (User) paramRe.getAttribute("currentUser");
            String dept = user.getLab_name();
            Set<String> unumbers = null;
            Set<String> depts = setDeot(user, dept);
            if (depts == null || depts.size() == 0) {
                unumbers = setUnumbers(user);
                depts = getDepts(dept, depts, user.getOrgID());
            }
            if ((unumbers == null || unumbers.size() == 0) && (depts == null || depts.size() == 0)) {
                return ParamUtils.errorParam("没有权限");
            }
            ResultBean resultBean = new ResultBean();
            JsonObject json = new JsonObject();
            json.addProperty("check", true);
            json.add("depts", gson.toJsonTree(depts));
            json.add("unumbers", gson.toJsonTree(unumbers));
            resultBean.setData(json);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("", e);
            return ParamUtils.errorParam("异常error");
        }
    }

    private Set<String> setUnumbers(User user) {
        Set<String> unumbers = new TreeSet<>();
        String unumber = user.getUnumber();
        if (StringUtils.isEmpty(unumber)) unumbers.add(unumber);
        List<Group> list = user.getGroups();
        if (list != null) list.forEach(group ->
        {
            try {
                JsonArray members = gson.toJsonTree(group.getMembers()).getAsJsonArray();
                for (JsonElement element : members) {
                    try {
                        String tmp = element.getAsJsonObject().get("unumber").getAsString();
                        if (!StringUtils.isEmpty(tmp)) unumbers.add(tmp);
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {

            }
        });
        return unumbers;
    }

    public Set<String> setDeot(User user, String dept) {
        if (StringUtils.isEmpty(dept)) {
            logger.warn("空科室");
            return null;
        }
        Power power = user.getPower();
        Set<Resource> list = power.getHas_search();
        if (list == null || list.size() == 0) {
            logger.warn("无权限,科室列表空");
            return null;
        }
        Set<String> depts = new TreeSet<>();
        boolean find = false;
        String orgID = user.getOrgID();
        for (Resource resource : list) {
            if (dept.equals(resource.getSlab_name()) && "有".equals(resource.getHas_search())) {
                find = true;
                break;
            }
        }
        if (find == false) {
            logger.warn("无权限 unfind dept " + dept);
            return null;
        }
        return getDepts(dept, depts, orgID);
    }

    public Set<String> getDepts(String dept, Set<String> depts, String orgID) {
        if (depts == null) depts = new TreeSet<>();
        depts.add(dept);
        try {
//            List<String> mapping = AllDao.getInstance().getSyRoleDao().getSlabNameMappingByLabName(dept, orgID);
            List<String> mapping = AllDao.getInstance().getSyRoleDao().getlabMappingByLabName(dept, orgID);
            if (mapping != null && mapping.size() > 0) {
                depts.addAll(mapping);
            }
        } catch (Exception e) {

        }
        return depts;
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getUser(HttpServletRequest paramRe) {
        HttpSession session = paramRe.getSession(false);
        String sessionID = session.getId();
        String uid = RedisUtil.getValue(sessionID);
        logger.info("getUser sessionID = " + sessionID + " uid = " + uid);
        User user = UserProcessor.getUserByUidFromRedis(uid);
        ResultBean bean = new ResultBean();
        bean.setData(user);
        return gson.toJson(bean);
    }

    @RequestMapping(value="/isExistUserInfo",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    @ResponseBody
    public String isExistUserName(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User) paramRe.getAttribute("currentUser");
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.isExistUserInfo(paramObj, user);
        }catch(Exception e){
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("判断当前用户名是否唯一 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

}
