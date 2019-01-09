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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RwsProcessor processor;
    private CaseProcessor caseProcessor = new CaseProcessor();

    @RequestMapping(value = "/PreLiminary", method = {RequestMethod.GET,RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String postSampleImport(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = AuthorityUtil.addTreatedAuthority(paramRe);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            User user = (User) paramRe.getAttribute("currentUser");
            logger.info("搜索结果导出到RWS项目空间 get方式 参数=" + gson.toJson(paramObj));
            resultStr = processor.PreLiminary(paramObj,user);
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

    @RequestMapping(value = "/getRwsEventConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getRwsEventConfig(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("事件配置文件获取 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getRwsEventConfig(paramObj);
        } catch (Exception e) {
            logger.error("事件配置文件获取 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("事件配置文件获取 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/getLoadSearchDefinedEventListConfig", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getLoadSearchDefinedEventListConfig(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取配置文件LoadSearch 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getLoadSearchDefinedEventListConfig(paramObj);
        } catch (Exception e) {
            logger.error("事件配置文件获取获取配置文件LoadSearch 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取配置文件LoadSearch  接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/cort/getContResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getContResult(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取图形列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getContResult(paramObj);
        } catch (Exception e) {
            logger.error("获取图形列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取图形列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/cort/getContResultForPatient", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getContResultForPatient(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取计算结果列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getContResultForPatient(paramObj);
        } catch (Exception e) {
            logger.error("获取计算结果列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取计算结果列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/cort/getPatientGroupCondition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientGroupCondition(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取对比分析页面搜索条件 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientGroupCondition(paramObj);
        } catch (Exception e) {
            logger.error("获取对比分析页面搜索条件 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取对比分析页面搜索条件 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/cort/getResearchVariable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getResearchVariable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取研究变量参数 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getResearchVariable(paramObj);
        } catch (Exception e) {
            logger.error("获取研究变量参数 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取研究变量参数 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/cort/getAllResearchVariable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getAllResearchVariable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取全部研究变量参数 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getAllResearchVariable(paramObj);
        } catch (Exception e) {
            logger.error("获取全部研究变量参数 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取全部研究变量参数 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/cort/saveGroupCondition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveGroupCondition(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("存储分组条件 对比分析页面 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveGroupCondition(paramObj);
        } catch (Exception e) {
            logger.error("存储分组条件 对比分析页面 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("村粗分组条件 对比分析页面 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/patientSet/deletePatientSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deletePatientSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除患者集信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deletePatientSet(paramObj);
        } catch (Exception e) {
            logger.error("删除患者集信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除患者集信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/getContrasAnalyList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getContrasAnalyList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取研究变量创建情况 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getContrasAnalyList(paramObj);
        } catch (Exception e) {
            logger.error("获取研究变量创建情况 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取研究变量创建情况 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/getPatientList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取研究变量创建情况 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientList(paramObj);
        } catch (Exception e) {
            logger.error("获取研究变量创建情况 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取研究变量创建情况 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/getPatientSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取患者集信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientSet(paramObj);
        } catch (Exception e) {
            logger.error("获取患者集信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者集信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/getPatientSetList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientSetList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("或者患者集列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientSetList(paramObj);
        } catch (Exception e) {
            logger.error("或者患者集列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("或者患者集列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/getSearchCondition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getSearchCondition(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取患者集搜索条件 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getSearchCondition(paramObj);
        } catch (Exception e) {
            logger.error("获取患者集搜索条件 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者集搜索条件 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/savePatientSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String savePatientSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("创建患者集信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.savePatientSet(paramObj);
        } catch (Exception e) {
            logger.error("创建患者集信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建患者集信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientSet/updatePatientSet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String updatePatientSet(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("修改患者集信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updatePatientSet(paramObj);
        } catch (Exception e) {
            logger.error("修改患者集信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("修改患者集信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/preLiminary/getProjectByCrfId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getProjectByCrfId(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("查看用户是否有项目 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getProjectByCrfId(paramObj);
        } catch (Exception e) {
            logger.error("查看用户是否有项目 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("查看用户是否有项目 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/project/deleteProject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deleteProject(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除项目信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteProject(paramObj);
        } catch (Exception e) {
            logger.error("删除项目信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除项目信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/getScientific", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getScientific(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            resultStr = processor.getScientific();
        } catch (Exception e) {
            logger.error("获取科研类型 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取科研类型 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/getProject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getProject(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getProject(paramObj);
        } catch (Exception e) {
            logger.error("获取项目信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/getProjectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getProjectListgetProject(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目列表信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getProjectList(paramObj);
        } catch (Exception e) {
            logger.error("获取项目列表信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目列表信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/saveProject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveProject(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("创建项目 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveProject(paramObj);
        } catch (Exception e) {
            logger.error("创建项目 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建项目 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/updateProject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String updateProject(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("修改项目信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updateProject(paramObj);
        } catch (Exception e) {
            logger.error("修改项目信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("修改项目信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/deletePatientGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deletePatientGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除项目分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deletePatientGroup(paramObj);
        } catch (Exception e) {
            logger.error("删除项目分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除项目分组信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/exportGroupDataPatient", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String exportGroupDataPatient(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("进一步筛选患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.exportGroupDataPatient(paramObj);
        } catch (Exception e) {
            logger.error("进一步筛选患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("进一步筛选患者分组信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getActiveIndexList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getActiveIndexList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取筛选条件 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getActiveIndexList(paramObj);
        } catch (Exception e) {
            logger.error("获取筛选条件 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取筛选条件 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getPatientGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientGroup(paramObj);
        } catch (Exception e) {
            logger.error("获取患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者分组信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getPatientGroupList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientGroupList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取患者分组列表信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientGroupList(paramObj);
        } catch (Exception e) {
            logger.error("获取患者分组列表信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者分组列表信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getPatientList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getPatientListForGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取患者列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getPatientListForGroup(paramObj);
        } catch (Exception e) {
            logger.error("获取患者列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取患者列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/insertGroupDataPatient", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String insertGroupDataPatient(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("进一步筛选患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.insertGroupDataPatient(paramObj);
        } catch (Exception e) {
            logger.error("进一步筛选患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("进一步筛选患者分组信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/saveActiveIndex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveActiveIndex(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("保存分组详情筛选条件 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveActiveIndex(paramObj);
        } catch (Exception e) {
            logger.error("保存分组详情筛选条件 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("保存分组详情筛选条件 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/patientGroup/saveGroupAndPatient", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveGroupAndPatient(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("拖拽 添加保存患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveGroupAndPatient(paramObj);
        } catch (Exception e) {
            logger.error("拖拽 添加保存患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("拖拽 添加保存患者分组信息 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/patientGroup/savePatientGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String savePatientGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("新增患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.savePatientGroup(paramObj);
        } catch (Exception e) {
            logger.error("新增患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("新增患者分组信息接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/patientGroup/updatePatientGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String updatePatientGroup(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("编辑患者分组信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updatePatientGroup(paramObj);
        } catch (Exception e) {
            logger.error("编辑患者分组信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("编辑患者分组信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/getOperLogsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getOperLogsList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目日志信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getOperLogsList(paramObj);
        } catch (Exception e) {
            logger.error("获取项目日志信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目日志信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/projectMember/deleteProjectMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deleteProjectMember(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除项目成员信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteProjectMember(paramObj);
        } catch (Exception e) {
            logger.error("删除项目成员信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除项目成员信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/projectMember/getProjectMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getProjectMember(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目成员信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getProjectMember(paramObj);
        } catch (Exception e) {
            logger.error("获取项目成员信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目成员信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


    @RequestMapping(value = "/projectMember/getProjectMemberList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getProjectMemberList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目成员列表信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getProjectMemberList(paramObj);
        } catch (Exception e) {
            logger.error("获取项目成员列表信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目成员列表信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/projectMember/saveProjectMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveProjectMember(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("创建项目成员信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveProjectMember(paramObj);
        } catch (Exception e) {
            logger.error("创建项目成员信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("创建项目成员信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/projectMember/updateProjectMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String updateProjectMember(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("修改项目成员信息 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.updateProjectMember(paramObj);
        } catch (Exception e) {
            logger.error("修改项目成员信息 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("修改项目成员信息 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getGroupTypeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGroupTypeList(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取分组类型列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getGroupTypeList(paramObj);
        } catch (Exception e) {
            logger.error("获取分组类型列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取分组类型列表 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/groupAggregation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String groupAggregation(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("分组圆形统计图展示 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.groupAggregation(paramObj);
        } catch (Exception e) {
            logger.error("分组圆形统计图展示 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("分组圆形统计图展示 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/checkName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String checkName(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("验证名字是否重复 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.checkName(paramObj);
        } catch (Exception e) {
            logger.error("验证名字是否重复 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("验证名字是否重复 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/editActiveName", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String editActiveName(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("修改指标事件名称 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.editActiveName(paramObj);
        } catch (Exception e) {
            logger.error("修改指标事件名称 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("修改指标事件名称 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/patientGroup/getGroupParentData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String getGroupParentData(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取分组要筛选的数据 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.getGroupParentData(paramObj);
        } catch (Exception e) {
            logger.error("获取分组要筛选的数据 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取分组要筛选的数据 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }
    @RequestMapping(value = "/cort/saveResearchVariable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String saveResearchVariable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取分组要筛选的数据 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.saveResearchVariable(paramObj);
        } catch (Exception e) {
            logger.error("保存研究变量用户条件 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("保存研究变量用户条件 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/cort/deleteResearchVariable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String deleteResearchVariable(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("删除研究变量 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.deleteResearchVariable(paramObj);
        } catch (Exception e) {
            logger.error("删除研究变量 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("删除研究变量 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/projectAggregation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String projectAggregation(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目统计列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.projectAggregation(paramObj);
        } catch (Exception e) {
            logger.error("获取项目统计列表 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("获取项目统计列表 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }

    @RequestMapping(value = "/project/projectPowerExamine", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    String projectPowerExamine(HttpServletRequest paramRe) {
        Long start = System.currentTimeMillis();
        String resultStr = null;
        try {
            String param = ParamUtils.getParam(paramRe);
            logger.info("获取项目统计列表 参数 = " + param);
            JsonObject paramObj = (JsonObject) jsonParser.parse(param);
            resultStr = processor.projectPowerExamine(paramObj);
        } catch (Exception e) {
            logger.error("权限校验 接口", e);
            resultStr = ParamUtils.errorParam("出现异常");
        }
        logger.info("权限校验 接口 耗时" + (System.currentTimeMillis() - start) + "ms");
        return resultStr;
    }


}
