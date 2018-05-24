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
    public String PreLiminary(JsonObject paramObj){
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
        try {
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
            if(paramObj.has("crfId")){
                String crfId = paramObj.get("crfId").getAsString();
                if(StringUtils.isEmpty(crfId)){
                    tmpJsonArray = ReadConditionByRedis.getEmrRws();
                }else {
                    tmpJsonArray = ReadConditionByRedis.getCrfRws(crfId);
                }
            }
            JsonObject tmpObject = (JsonObject) tmpJsonArray.get(0);
            JsonObject orderObject = tmpObject.get("resultOrderKey").getAsJsonObject();
            paramObj.add("resultOrderKey",orderObject);
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
        String crfId= null;
        JsonArray resultArray = null;
        try {
            if(paramObj.has("crfId")){
                crfId = paramObj.get("crfId").getAsString();
            }
            if(StringUtils.isEmpty(crfId)){
                 resultArray = ReadConditionByRedis.getEmrRws();
            }else {
                resultArray = ReadConditionByRedis.getCrfRws(crfId);
            }
            JsonObject jsonObject= (JsonObject) resultArray.get(00);
            jsonObject.remove("resultOrderKey");
            JsonArray  array = new JsonArray();
            array.add(jsonObject);
            resultBean.setCode(1);
            resultBean.setData(resultArray);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("事件配置文件获取", e);
            return ParamUtils.errorParam("请求发生异常");
        }
    }


    /*        int counter = 0;
        try {
            String projectId = paramObj.get("projectId").getAsString();
            if (paramObj.has("crfId")){
                String crfId = paramObj.get("crfId").getAsString();
                String dataSource = "单病种-"+ArkService.getDiseaseName(crfId);
                counter = insertProCrfId(projectId,dataSource,crfId);
            } else {
                counter = insertProCrfId(projectId,"EMR","");
            }

            String url = ConfigurationService.getUrlBean().getPreLiminaryUrl();
            String result = HttpRequestUtils.httpPost(url, gson.toJson(paramObj));

            Map<String, Object> info = new HashMap<String, Object>();
            info.put("counter", counter);
            info.put("result",result);
            ResultBean resultBean = new ResultBean();
            resultBean.setCode(1);
            resultBean.setInfo(info);
            return gson.toJson(resultBean);
        } catch (Exception e) {
            logger.error("请求发生异常", e);
            return ParamUtils.errorParam("请求发生异常");
        }*/

    //如果是crf项目，需要写入p_project的crfId字段中，然后存入对应名字到datasource
/*    public int insertProCrfId(String projectID,String dataSource,String crfId){
        Map<String,String> map = new HashMap<>();
        map.put("projectID",projectID);
        map.put("dataSource",dataSource);
        map.put("crfId",crfId);
        long start = System.currentTimeMillis();
        int count = AllDao.getInstance().getProjectDao().insertProCrfId(map);

        long end = System.currentTimeMillis();
        logger.debug("insertProCrfId(map) mysql耗时"+(end-start)+"ms");
        return count;
    }*/
}
