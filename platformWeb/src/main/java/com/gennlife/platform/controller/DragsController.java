package com.gennlife.platform.controller;

import com.gennlife.platform.service.DragsService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lmx
 * @create 2019 08 14:29
 * @desc  医保 控费 对外接口
 **/
@RestController
@RequestMapping("/drgs")
public class DragsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DragsController.class);
    private static Gson gson = GsonUtil.getGson();
    private static JsonParser jsonParser = new JsonParser();

    @Autowired
    private DragsService dragsService;

    @RequestMapping(value = "/index", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String index(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.drgsIndex(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 登陆进入首页时，返回首页数据 出现异常");
        }
        LOGGER.info("医保控费 登陆进入首页时，返回首页数据 出现异常 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/hint", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String hint(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.drgsHint(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 hint，返回首页数据 出现异常");
        }
        LOGGER.info("医保控费 hint，返回首页数据 出现异常 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/index/setting", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String setting(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.indexSetting(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户在首页进行高级设置后的请求接口 出现异常");
        }
        LOGGER.info("医保控费 用户在首页进行高级设置后的请求接口 出现异常 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/index/redraw", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String redraw(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.indexRedraw(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 redraw的请求接口 出现异常");
        }
        LOGGER.info("医保控费 redraw的请求接口 出现异常 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/index/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.indexList(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户点击或输入页码，或者点击排序，或者过滤组名后请求该接口 出现异常");
        }
        LOGGER.info("医保控费 用户点击或输入页码，或者点击排序，或者过滤组名后请求该接口 出现异常 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/factor/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String factorList(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.factorList(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户在首页列表点击某一个DRGs组后弹出下钻页面 出现异常");
        }
        LOGGER.info("医保控费 用户在首页列表点击某一个DRGs组后弹出下钻页面 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/mining/catalog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String catalog(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.miningCatalog(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户在费用分析列表点击一行时，或者用户再详细分析页面点击按费用展开tab时，调用此接口 出现异常");
        }
        LOGGER.info("医保控费 用户在费用分析列表点击一行时，或者用户再详细分析页面点击按费用展开tab时，调用此接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/mining/dept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String dept(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.miningDept(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户在点击按科室展开Tab页时访问该接口 出现异常");
        }
        LOGGER.info("医保控费 用户在点击按科室展开Tab页时访问该接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/mining/patient", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String patient(HttpServletRequest paramRe, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            resultStr = dragsService.miningParent(param);
        } catch (Exception e) {
            LOGGER.error("", e);
            resultStr = ParamUtils.errorParam("医保控费 用户在点击病人列表Tab页时访问该接口 出现异常");
        }
        LOGGER.info("医保控费 用户在点击病人列表Tab页时访问该接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }




}
