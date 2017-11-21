package com.gennlife.platform.controller;

import com.gennlife.platform.authority.AuthorityUtil;
import com.gennlife.platform.model.User;
import com.gennlife.platform.processor.CaseProcessor;
import com.gennlife.platform.processor.RwsProcessor;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by luoxupan on 25/10/2017.
 */
@Controller
@RequestMapping("/rws")
public class RwsController {
    private Logger logger = LoggerFactory.getLogger(RwsController.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private RwsProcessor processor = new RwsProcessor();
    private CaseProcessor caseProcessor = new CaseProcessor();

    @RequestMapping(value = "/PreLiminary", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSampleImport(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
//            String paramNew = AuthorityUtil.addSearchCaseAuthority(paramRe);
            String sid = "";
            JsonObject paramsObj = (JsonObject) jsonParser.parse(ParamUtils.getParam(paramRe));
            if (paramsObj.has("sid")) {
                sid = paramsObj.get("sid").getAsString();
            }
            String paramNew = AuthorityUtil.addTreatedAuthority(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(paramNew);
            paramObj.addProperty("sid", sid);

            logger.info("搜索结果导出到RWS项目空间 get方式 参数=" + gson.toJson(paramObj));
            resultStr = processor.PreLiminary(paramObj);
        } catch (Exception e) {
            logger.error("搜索结果导出到RWS项目空间", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索结果导出到RWS项目空间 get 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/PreAggregation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postPreAggregation(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("RWS请求图形接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PreAggregation(paramObj);
        } catch (Exception e) {
            logger.error("RWS请求图形接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("RWS请求图形接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/PreFindForProjectData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postPreFindForProjectData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("RWS图形下面列表接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.PreFindForProjectData(paramObj);
        } catch (Exception e) {
            logger.error("RWS图形下面列表接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("RWS图形下面列表接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/FindByProjectId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postFindByProjectId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目下所有已定义的事件/指标列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.FindByProjectId(paramObj);
        } catch (Exception e) {
            logger.error("获取项目下所有已定义的事件/指标列表接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目下所有已定义的事件/指标列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/GetAllActiveOrIndex", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getAllActiveOrIndex(HttpServletRequest paramRe) {
        // 从后台获取 事件/指标 选项
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取 事件/指标 下拉选项 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getAllActiveOrIndex(paramObj);
        } catch (Exception e) {
            logger.error("获取 事件/指标 下拉选项", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取 事件/指标 下拉选项 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/GetSavedActivityData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSavedActivityData(HttpServletRequest paramRe) {
        // 获取后台保存的 事件/指标
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取后台保存的 事件/指标 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getSavedActivityData(paramObj);
        } catch (Exception e) {
            logger.error("获取后台保存的 事件/指标", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取后台保存的 事件/指标 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/SaveOrSearchActive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveOrSearchActive(HttpServletRequest paramRe) {
        // 搜索或保存接口
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("搜索或保存接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveOrSearchActive(paramObj);
        } catch (Exception e) {
            logger.error("搜索或保存接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索或保存 接口 请求结果" + resultStr);
        logger.info("搜索或保存 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ClacResultSearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String clacResultSearch(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("搜索事件定义页下的详情 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchClacResultSearch(paramObj);
        } catch (Exception e) {
            logger.error("搜索事件定义页下的详情接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("搜索事件定义页下的详情 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/ClacIndexResultSearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String clacIndexResultSearch(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("指标定义下定义页下的详情 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.searchClacIndexResultSearch(paramObj);
        } catch (Exception e) {
            logger.error("指标定义下定义页下的详情接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("指标定义下定义页下的详情 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/getCalcTotalByActiveId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getCalcTotalByActiveId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取定义活动患者列表表头数据(大量数据的那种) 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getCalcTotalByActiveId(paramObj);
        } catch (Exception e) {
            logger.error("获取定义活动患者列表表头数据 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取定义活动患者列表表头数据 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/FindTotalForImport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String FindTotalForImport(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("列表上的总数接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.findTotalForImport(paramObj);
        } catch (Exception e) {
            logger.error("列表上的总数接口 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("列表上的总数接口 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/DeleteByActiveId", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String DeleteByActiveId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("根据活动id删除活动的全部信息接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteByActiveId(paramObj);
        } catch (Exception e) {
            logger.error("根据活动id删除活动的全部信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("根据活动id删除活动的全部信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/checkActiveIsOnlyOne", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String checkActiveIsOnlyOne(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("验证事件名称唯一接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.checkActiveIsOnlyOne(paramObj);
        } catch (Exception e) {
            logger.error("验证事件名称唯一 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("验证事件名称唯一 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/activeIsChange", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String activeIsChange(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("验证事件数据是否改变接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.activeIsChange(paramObj);
        } catch (Exception e) {
            logger.error("验证事件数据是否改变 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("验证事件数据是否改变 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/dependenceChange", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String dependenceChange(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("验证事件数据是否改变&有被依赖接口 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.dependenceChange(paramObj);
        } catch (Exception e) {
            logger.error("验证事件数据是否改变&有被依赖 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("验证事件数据是否改变&有被依赖 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
}
