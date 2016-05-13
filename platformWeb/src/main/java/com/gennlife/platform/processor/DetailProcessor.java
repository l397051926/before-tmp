package com.gennlife.platform.processor;

import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.GsonUtil;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.gennlife.platform.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chen-song on 16/5/13.
 */
public class DetailProcessor {
    private Logger logger = LoggerFactory.getLogger(DetailProcessor.class);
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();
    private static View viewer= new View();
    /**
     * 详情页患者基础信息接口
     * 透传,无逻辑
     * @param req
     * @param resp
     */
    public void patientBasicInfo(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("PatientBasicInfo param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseDetailPatientBasicInfoURL();
        logger.info("PatientBasicInfo url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("PatientBasicInfo result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 详情页基本统计图形&筛选条件
     * 透传,无逻辑
     * @param req
     * @param resp
     */
    public void patientBasicFigure(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("PatientBasicFigure param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url= ConfigurationService.getUrlBean().getCaseDetailPatientBasicFigureURL();
        logger.info("PatientBasicFigure url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("PatientBasicFigure result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 详情页时间轴接口
     * 透传,无逻辑
     * @param req
     * @param resp
     */
    public void patientBasicTimeAxis(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("PatientBasicTimeAxis param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }

        String url = ConfigurationService.getUrlBean().getCasePatientBasicTimeAxisURL();
        logger.info("PatientBasicTimeAxis url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("PatientBasicTimeAxis result="+url);
        viewer.viewString(result,resp,req);
    }
}
