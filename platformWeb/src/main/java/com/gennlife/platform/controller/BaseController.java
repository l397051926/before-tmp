package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.processor.BaseProcessor;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chen-song on 16/6/3.
 */
@Controller
@RequestMapping("/base")
public class BaseController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    private BaseProcessor processor = new BaseProcessor();

    private static JsonParser jsonParser = new JsonParser();

    @RequestMapping(value = "/Chain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postChain(@RequestBody String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.chainItem(paramObj);
            logger.info("二级关联属性post 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("二级关联属性post 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/Chain", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getChain(@RequestParam("param") String param) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.chainItem(paramObj);
            logger.info("二级关联属性get 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("二级关联属性get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ProjectDisease", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postProjectDisease() {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.projectDisease();
            logger.info("项目病种get 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("项目病种get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    /*测试？？？*/
    @RequestMapping(value = "/Test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String Test() {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.test();
            logger.info("test 耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("test 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VitaGramServerVitaGramData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VitaGramServerVitaGramData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            resultStr = processor.vitaGramServerVitaGramData(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取vitaGram图数据接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VitaGramServerNccnRecommendPath", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VitaGramServerNccnRecommendPath(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            resultStr = processor.vitaGramServerNccnRecommendPath(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取NCCN推荐路径 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/VitaGramServerMedicalHistoryRecommendPath", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String VitaGramServerMedicalHistoryRecommendPath(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addAuthority(paramRe);
            resultStr = processor.VitaGramServerMedicalHistoryRecommendPath(param);
        } catch (Exception e) {
            logger.error("", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取实例推荐路径 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
