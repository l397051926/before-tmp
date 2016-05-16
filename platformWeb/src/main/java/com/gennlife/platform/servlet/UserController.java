package com.gennlife.platform.servlet;

import com.gennlife.platform.proc.UserProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by chensong on 2015/12/5.
 */
public class UserController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserProcessor userProcessor = new UserProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        String uri = req.getRequestURI();
        if("/user/Login".equals(uri)){
            try{
                userProcessor.login(req,resp);
            }catch(Exception e){
                logger.error("用户登录异常",e);
                //可以加上监控
            }
            logger.info("登录耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/user/Update".equals(uri)){
            try{
                userProcessor.update(req, resp);
            }catch(Exception e){
                logger.error("用户更新个人信息异常",e);
                //可以加上监控
            }
            logger.info("用户更新个人信息耗时:" + (System.currentTimeMillis()-start) +"ms");
        }else if("/user/ChangePWD".equals(uri)){
            try{
                userProcessor.changePwdSender(req,resp);
            }catch(Exception e){
                logger.error("用户修改密码异常",e);
            }
            logger.info("用户修改密码，发送邮件耗时:" + (System.currentTimeMillis()-start) +"ms");
        }
    }
}
