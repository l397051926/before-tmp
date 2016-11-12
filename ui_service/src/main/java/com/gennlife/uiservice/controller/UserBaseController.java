package com.gennlife.uiservice.controller;

import com.gennlife.uiservice.authority.AuthorityUtil;
import com.gennlife.uiservice.bean.ResultBean;
import com.gennlife.uiservice.model.User;
import com.gennlife.uiservice.processor.UserBaseProcessor;
import com.gennlife.uiservice.util.GsonUtil;
import com.gennlife.uiservice.util.LogUtils;
import com.gennlife.uiservice.util.ParamUtils;
import com.gennlife.uiservice.view.View;
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
 * Created by chen-song on 2016/11/5.
 */
@Controller
@RequestMapping("/user")
public class UserBaseController {
    private Logger logger = LoggerFactory.getLogger(UserBaseController.class);
    private static JsonParser jsonParser = new JsonParser();
    private UserBaseProcessor processor = new UserBaseProcessor();
    private static Gson gson = GsonUtil.getGson();
    private static View view = new View();
    //checked
    @RequestMapping(value="/Login",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public void  postLogin(HttpServletRequest paramRe,HttpServletResponse response){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            HttpSession session = paramRe.getSession(true);
            String sessionID = session.getId();
            String param = ParamUtils.getParam(paramRe);
            String email = null;
            String pwd = null;
            try{
                JsonObject paramObj = (JsonObject) jsonParser.parse(param);
                email = paramObj.get("email").getAsString();
                pwd = paramObj.get("pwd").getAsString();
            }catch (Exception e){
                view.viewString(ParamUtils.errorParam("参数错误"),response);
            }
            User user =  processor.login(email,pwd,sessionID);
            ResultBean resultBean = new ResultBean();
            if(user == null){
                //LogUtils.BussnissLog(LogUtils.getStringTime()+"####"+email+"####Login####fail");
                resultBean.setCode(0);
                resultBean.setInfo("用户不存在");
            }else {
                resultBean.setCode(1);
                LogUtils.BussnissLog(LogUtils.getStringTime()+"####"+email+"####Login####succeed");
                resultBean.setData(user);
            }
            Cookie cookie = new Cookie("JSESSIONID",sessionID);
            response.addCookie(cookie);
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("登录get 耗时"+(System.currentTimeMillis()-start) +"ms");
        view.viewString(resultStr,response);
    }
    //checked
    @RequestMapping(value="/UpdateInfo",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postUpdate(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            User user = (User) paramRe.getAttribute("currentUser");
            if(user == null ){
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
            User user1 = gson.fromJson(param,User.class);
            User newUser =  processor.updateUserByUid(user1);
            ResultBean resultBean = new ResultBean();
            if(newUser != null ){//更新成功
                resultBean.setCode(1);
                resultBean.setData(newUser);
                LogUtils.BussnissLog(LogUtils.getStringTime()+"####"+newUser.getUid()+"####UpdateInfo####"+gson.toJson(newUser));
            }else{
                resultBean.setCode(0);
                resultBean.setInfo("更新失败");
            }
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("用户更新个人信息 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    //checked
    @RequestMapping(value="/UpdatePWD",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String UpdatePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String email = null;
            String pwd = null;
            String md5 = null;
            String param = ParamUtils.getParam(paramRe);
            logger.info("UpdatePWD param="+param);
            try{
                JsonObject paramObj = (JsonObject) jsonParser.parse(param);
                email = paramObj.get("email").getAsString();
                pwd = paramObj.get("pwd").getAsString();
                md5 = paramObj.get("md5").getAsString();
            }catch (Exception e){
                return ParamUtils.errorParam("参数错误");
            }
            ResultBean resultBean = new ResultBean();
            User user =  processor.updatePWD(email,pwd,md5);
            if(user == null){
                resultBean.setCode(0);
                resultBean.setInfo("更新失败");
            }else {
                resultBean.setCode(1);
                resultBean.setData(user);
                LogUtils.BussnissLog(LogUtils.getStringTime()+"####"+email+"####UpdatePWD####succeed");
            }
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("更新密码 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
    //checked
    @RequestMapping(value="/SendEmailForChangePWD",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postChangePWD(HttpServletRequest paramRe){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String param = ParamUtils.getParam(paramRe);
            String email = null;
            String token = null;
            String md5 = null;
            try{
                JsonObject paramObj = (JsonObject) jsonParser.parse(param);
                email = paramObj.get("email").getAsString();
                token = paramObj.get("token").getAsString();
                md5 = paramObj.get("md5").getAsString();
            }catch (Exception e){
                return ParamUtils.errorParam("参数错误");
            }
            User user =  processor.changePwdSender(email,token,md5);
            ResultBean resultBean = new ResultBean();
            if(user != null){
                resultBean.setCode(1);
                resultBean.setInfo("已经发送");
                LogUtils.BussnissLog(LogUtils.getStringTime()+"####"+email+"####SendEmailForChangePWD####succeed");
            }else {
                resultBean.setCode(0);
                resultBean.setInfo("发生异常");
            }
            resultStr = gson.toJson(resultBean);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("发送修改密码邮件 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }


    @RequestMapping(value="/ExistEmail",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody String postExistEmail(@RequestParam("param")String param, HttpSession session){
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try{
            String email = null;
            try{
                JsonObject paramObj = (JsonObject) jsonParser.parse(param);
                email = paramObj.get("email").getAsString();
            }catch (Exception e){
                return ParamUtils.errorParam("参数异常");
            }
            int count =  processor.existEmail(email);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(count);
            resultStr = gson.toJson(resultBean);
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
            User user = null;
            try{
                user = (User) paramRe.getAttribute("currentUser");
            }catch (Exception e){
                return ParamUtils.errorSessionLosParam();
            }
            return processor.CRFList(user);
        }catch (Exception e){
            logger.error("",e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("CRFList 返回="+resultStr);
        logger.info("用户可访问crf列表 post 耗时"+(System.currentTimeMillis()-start) +"ms");
        return resultStr;
    }
}
