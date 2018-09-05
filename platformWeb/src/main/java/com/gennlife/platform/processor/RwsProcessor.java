package com.gennlife.platform.processor;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.bean.ResultBean;
import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.service.RwsService;
import com.gennlife.platform.service.RwsServiceImpl;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoxupan on 25/10/2017.
 */

@Component
public class RwsProcessor {
    private static Logger logger = LoggerFactory.getLogger(RwsProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    @Autowired
    private RwsService rwsService;

    //搜索结果导出到RWS项目空间
    public String PreLiminary(JsonObject paramObj) {
        String result = rwsService.PreLiminary(paramObj);
        return result;
    }

    public String PreAggregation(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPreAggregationUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("RWS请求图形接口发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String PreFindForProjectData(JsonObject paramObj) {
        String crfId = null;
        try {
            if (paramObj.has("crfId")) {
                crfId = paramObj.get("crfId").getAsString();
                ReadConditionByRedis.getCrfMapping(crfId);
                logger.info("crf 映射 rws 映射成功！！！" + crfId);
            }
            String url = ConfigurationService.getUrlBean().getPreFindForProjectData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("RWS图形下面列表接口", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String FindByProjectId(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getFindByProjectId();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目下所有已定义的事件/指标列表", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getAllActiveOrIndex(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getGetAllActiveOrIndex();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取 事件/指标 下拉选项", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getSavedActivityData(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getGetSavedActivityData();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取后台保存的 事件/指标", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveOrSearchActive(JsonObject paramObj) {

        try {
            JsonArray tmpJsonArray = new JsonArray();
            if (paramObj.has("crfId")) {
                String crfId = paramObj.get("crfId").getAsString();
                if ("EMR".equals(crfId) || StringUtils.isEmpty(crfId)) {
                    tmpJsonArray = ReadConditionByRedis.getEmrRws();
                } else {
                    tmpJsonArray = ReadConditionByRedis.getCrfRws(crfId);
                }
            }
            JsonObject tmpObject = (JsonObject) tmpJsonArray.get(0);
            JsonObject orderObject = tmpObject.get("resultOrderKey").getAsJsonObject();
            paramObj.add("resultOrderKey", orderObject);
            //返回排序json
            String url = ConfigurationService.getUrlBean().getSaveOrSearchActive();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("搜索或保存接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String searchClacResultSearch(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getClacResultSearch();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("搜索事件定义页下的详情接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String searchClacIndexResultSearch(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getClacIndexResultSearch();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("指标定义下定义页下的详情接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getCalcTotalByActiveId(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getGetCalcTotalByActiveId();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取定义活动患者列表表头数据接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String findTotalForImport(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getFindTotalForImport();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("列表上的总数接口 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deleteByActiveId(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDeleteByActiveId();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("根据活动id删除活动的全部信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String checkActiveIsOnlyOne(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCheckActiveIsOnlyOne();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("验证事件名称唯一 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String activeIsChange(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getCheckActiveDataIsChange();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("验证事件数据是否改变 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String dependenceChange(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDependenceChange();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("验证事件数据是否改变&有被依赖 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getRwsEventConfig(JsonObject paramObj) {
        ResultBean resultBean = new ResultBean();
        String crfId = null;
        JsonArray resultArray = null;
        try {
            if (paramObj.has("crfId")) {
                crfId = paramObj.get("crfId").getAsString();
            }
            if (StringUtils.isEmpty(crfId)) {
                resultArray = ReadConditionByRedis.getEmrRws();
            } else {
                resultArray = ReadConditionByRedis.getCrfRws(crfId);
            }
            JsonObject jsonObject = (JsonObject) resultArray.get(0);
            jsonObject.remove("resultOrderKey");
            JsonArray array = new JsonArray();
            array.add(jsonObject);
            resultBean.setCode(1);
            resultBean.setData(resultArray);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("事件配置文件获取", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getLoadSearchDefinedEventListConfig(JsonObject paramObj) {
        ResultBean resultBean = new ResultBean();
        String crfId = null;
        try {
            if (paramObj.has("crfId")) {
                crfId = paramObj.get("crfId").getAsString();
            }
            String data = ReadConditionByRedis.getLoadSearchDefinedEventListConfig(crfId);
            JsonObject target = (JsonObject) jsonParser.parse(data);
            resultBean.setCode(1);
            resultBean.setData(target);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("事件配置文件获取", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getContResult(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getContResultUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取图形列表 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getContResultForPatient(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getContResultForPatientUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取计算结果列表 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientGroupCondition(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientGroupCondition();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取对比分析页面搜索条件 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getResearchVariable(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getResearchVariableUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取研究变量参数 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveGroupCondition(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSaveGroupCondition();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("存储分组信息 对比分析页面 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deletePatientSet(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDeletePatientSet();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("删除患者集信息 对比分析页面 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getContrasAnalyList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getContrasAnalyList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取研究变量创建情况 对比分析页面 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取研究变量创建情况 对比分析页面 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    public String getPatientSet(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientSet();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取患者集信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientSetList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientSetList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("或者患者集列表 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getSearchCondition(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSearchCondition();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取患者集搜索条件 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String savePatientSet(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSavePatientSet();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("创建患者集信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String updatePatientSet(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getUpdatePatientSet();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("修改患者集信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getProjectByCrfId(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getProjectByCrfId();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("查看用户是否有项目 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deleteProject(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDeleteProject();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("删除项目信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getProject(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getProject();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getProjectList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getProjectList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目列表信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveProject(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSaveProject();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("创建项目 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String updateProject(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getUpdateProject();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("修改项目信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deletePatientGroup(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDeletePatientGroup();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("删除项目分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String exportGroupDataPatient(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getExportGroupDataPatient();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("进一步筛选患者分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getActiveIndexList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getActiveIndexList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取筛选条件 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientGroup(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientGroup();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取患者分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientGroupList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientGroupList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取患者分组列表信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getPatientListForGroup(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getPatientListForGroup();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取患者列表 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String insertGroupDataPatient(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getInsertGroupDataPatient();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("进一步筛选患者分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveActiveIndex(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSaveActiveIndex();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("保存分组详情筛选条件 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveGroupAndPatient(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSaveGroupAndPatient();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("拖拽 添加保存患者分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String savePatientGroup(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getSavePatientGroup();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("新增患者分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String updatePatientGroup(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getUpdatePatientGroup();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("编辑项目分组信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getOperLogsList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getOperLogsList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目日志信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String deleteProjectMember(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getDeleteProjectMember();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("删除项目成员信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getProjectMember(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getProjectMember();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目成员信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getProjectMemberList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getProjectMemberList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取项目成员列表信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String saveProjectMember(JsonObject paramObj) {
         try {
            String url = ConfigurationService.getUrlBean().getSaveProjectMember();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("创建项目成员信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String updateProjectMember(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getUpdateProjectMember();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("修改项目成员信息 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getScientific() {
        try {
            String url = ConfigurationService.getUrlBean().getScientific();
            String result = HttpRequestUtils.httpGet(url);
            return result;
        } catch (Exception e) {
            logger.error("获取科研类型 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String getGroupTypeList(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getGroupTypeList();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("获取分组类型列表 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }

    public String groupAggregation(JsonObject paramObj) {
        try {
            String url = ConfigurationService.getUrlBean().getGroupAggregation();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));
            return result;
        } catch (Exception e) {
            logger.error("分组圆形统计图展示 ", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }
}
