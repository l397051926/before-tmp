package com.gennlife.platform.controller;

import com.gennlife.platform.processor.FileProcessor;
import com.gennlife.platform.processor.MessageCenterProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chensong on 2015/12/9.
 */
@Controller
@RequestMapping("/msg")
public class MessageCenterController extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(MessageCenterController.class);
    private static MessageCenterProcessor processor = new MessageCenterProcessor();
    private static JsonParser jsonParser = new JsonParser();


    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String msgUpdate(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("标识消息为已读或删除此条消息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.msgUpdate(paramObj);
        } catch (Exception e) {
            logger.error("标识消息为已读或删除此条消息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("标识消息为已读或删除此条消息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/updateAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String msgUpdateAll(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("指定用户消息全部标记已读或删除 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.msgUpdateAll(paramObj);
        } catch (Exception e) {
            logger.error("指定用户消息全部标记已读或删除 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("指定用户消息全部标记已读或删除 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String msgQuery(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("查询指定条件的消息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.msgQuery(paramObj);
        } catch (Exception e) {
            logger.error("查询指定条件的消息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查询指定条件的消息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String updateBatch(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("批量消息标记为已读或删除 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updateBatch(paramObj);
        } catch (Exception e) {
            logger.error("批量消息标记为已读或删除 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("批量消息标记为已读或删除 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
