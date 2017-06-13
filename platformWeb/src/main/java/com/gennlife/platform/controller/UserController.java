package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.model.*;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.util.*;
import com.gennlife.platform.view.View;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                email = user.get("email").getAsString();
                pwd = user.get("pwd").getAsString();
            } catch (Exception e) {
                view.viewString(ParamUtils.errorParam("参数错误"), response);
                return;
            }
            User user = processor.login(email, pwd);
            ResultBean resultBean = new ResultBean();
            if (user != null) {
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
            logger.info("user is: " + resultStr);
        } catch (Exception e) {
            ;
            ;
            logger.error("登陆异常", e);
            resultStr = ParamUtils.errorParam("登陆异常");
        }
        LogUtils.BussnissLog("登录get 耗时" + (System.currentTimeMillis() - start) + "ms");
        view.viewString(resultStr, response);
    }

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

    @RequestMapping(value = "/IsInnerNet", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String IsInnerNet(HttpServletRequest paramRe) {
        Object xRealIpobj = null;
        try {
            xRealIpobj = SpringContextUtil.getBean("xrealip");
            if (xRealIpobj == null) {
                return ParamUtils.errorParam("无内外网限制");
            }
        } catch (Exception e) {
            logger.error("error 无内外网限制 ", e);
            return ParamUtils.errorParam("无内外网限制");
        }
        XRealIp xRealIp = (XRealIp) xRealIpobj;
        String value = paramRe.getHeader(xRealIp.getKey());
        if (StringUtils.isEmpty(value)) {
            return ParamUtils.errorParam("无对应key");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(1);
        JsonObject json = new JsonObject();
        logger.info("ip: " + value);
        if (value.equals(xRealIp.getValue())) {
            json.addProperty("inner", true);
            resultBean.setMsg("内网");
        } else {
            json.addProperty("inner", false);
            resultBean.setMsg("外网");
            resultBean.setData(json);
        }
        return gson.toJson(resultBean);
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
            List<String> mapping = AllDao.getInstance().getSyRoleDao().getSlabNameMappingByLabName(dept, orgID);
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
}
