package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.UserProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.util.RedisUtil;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Created by chensong on 2015/12/5.
 */
@Controller
@RequestMapping("/user")
public class UserController{
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserProcessor processor = new UserProcessor();
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View view = new View();

    @RequestMapping(value="/Login",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public void postLogin(HttpServletRequest paramRe, HttpServletResponse response){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            HttpSession session = paramRe.getSession(true);
            String sessionID = session.getId();
            String param = ParamUtils.getParam(paramRe);
            logger.info("Login sessionID="+sessionID);
            logger.info("Login param="+param);
            String email = null;
            String pwd = null;
            try{
                JsonObject user = (JsonObject)jsonParser.parse(param);
                email = user.get("email").getAsString();
                pwd = user.get("pwd").getAsString();
            }catch (Exception e) {
                view.viewString(ParamUtils.errorParam("参数错误"), response);
                return;
            }
            User user = processor.login(email,pwd);
            ResultBean resultBean = new ResultBean();
            if(user != null){
                boolean hasLogin=UserProcessor.getUserByUidFromRedis(user.getUid())!=null;
                if(hasLogin)
                {
                    logger.warn("用户 "+email+" 已经登陆在其他session,进行重新登陆");
                }
                RedisUtil.setUserOnLine(user,sessionID);
                resultBean.setCode(1);
                resultBean.setData(user);
                Cookie cookie = new Cookie("JSESSIONID",sessionID);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }else {
                view.viewString(ParamUtils.errorParam("登陆失败"), response);
            }
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("登录get 耗时"+(System.currentTimeMillis()-start) +"ms");
        view.viewString(resultStr,response);
    }


    @RequestMapping(value="/UpdateInfo",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpdate(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            if(user == null ){
                logger.error("paramRe里面无currentUser");
                return ParamUtils.errorSessionLosParam();
            }
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            String uid = null;
            try{
                uid = paramObj.get("uid").getAsString();
                if(!user.getUid().equals(uid) && !AuthorityUtil.isAdmin(user)){//不是自己修改,不是管理员修改
                    return ParamUtils.errorParam("无权限更新");
                }
            }catch (Exception e){
                return ParamUtils.errorParam("缺少uid");
            }
            ResultBean resultBean =  processor.update(param);
            if(resultBean.getCode() == 1){
                try{
                    User realUser = (User) resultBean.getData();
                    if(realUser != null ){
                        RedisUtil.deleteUser(realUser.getUid());
                    }
                }catch (Exception e){
                    logger.error("",e);
                }

            }
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/UpdatePWD",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String UpdatePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            logger.info("UpdatePWD param="+param);
            resultStr =  processor.updatePWD(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新密码 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        logger.error("返回结果="+resultStr);
        return resultStr;
    }


    @RequestMapping(value="/SendEmailForChangePWD",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postChangePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            logger.info("SendEmailForChangePWD param="+param);
            resultStr =  processor.changePwdSender(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("发送修改密码邮件 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    @RequestMapping(value="/ExistEmail",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postExistEmail(@RequestParam("param")String param){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr =  processor.existEmail(paramObj);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看账户是否存在 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/CRFList",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String CRFList(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String userStr = gson.toJson(user);
            return processor.CRFList(userStr);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回="+resultStr);
        logger.info("用户可访问crf列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/VitaBoardConfigSave",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String VitaBoardConfigSave(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            User user = (User)paramRe.getAttribute("currentUser");
            return processor.vitaBoardConfigSave(user.getUid(),param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回="+resultStr);
        logger.info("用户可访问crf列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/VitaBoardConfig",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String VitaBoardConfig(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            return processor.VitaBoardConfig(user.getUid());
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回="+resultStr);
        logger.info("用户可访问crf列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/LabTransformCrfId",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String LabTransformCrfId(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User)paramRe.getAttribute("currentUser");
            String param = ParamUtils.getParam(paramRe);
            return processor.labTransformCrfId(user,param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("科室转化成病种id 返回="+resultStr);
        logger.info("科室转化成病种id get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }

    @RequestMapping(value="/SetRedis",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String OffLineUser(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            return processor.setRedis(param);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("清空缓存中用户信息 返回="+resultStr);
        logger.info(" get 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
