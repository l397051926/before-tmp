package com.gennlife.platform.controller;

import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.Part3Processor;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by wangyiyan on 2019/3/16
 */
@Controller
@RequestMapping("/part3/")
public class Part3Controller {
    private Logger logger = LoggerFactory.getLogger(Part3Controller.class);

    @Autowired
    private Part3Processor processor;

    @RequestMapping(value = "/getPart3Url", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPart3Url(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        User user = (User) paramRe.getAttribute("currentUser");
        String resultStr = null;
        try {
            resultStr = processor.getPart3Url(user);
        } catch (Exception e) {
            logger.error("获取第三方跳转链接", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取第三方跳转链接 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String addUser(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.addUser();
        } catch (Exception e) {
            logger.error("插入数据到第三方系统", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("插入数据到第三方系统 post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
