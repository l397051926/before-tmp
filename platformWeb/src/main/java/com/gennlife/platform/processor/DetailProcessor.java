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

    /**
     * 查看指标变化：可选列表
     * @param req
     * @param resp
     */
    public void choicesList(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("ChoicesList param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }

        String url = ConfigurationService.getUrlBean().getCasePhysical_examination_list();
        logger.info("ChoicesList url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("ChoicesList result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 查看指标变化：具体指标
     * @param req
     * @param resp
     */
    public void specificChoice(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("SpecificChoice param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }

        String url = ConfigurationService.getUrlBean().getCasePhysical_examination();
        logger.info("SpecificChoice url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("SpecificChoice result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 详情页总接口：唐乾斌提供
     * @param req
     * @param resp
     */
    public void visitDetail(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("VisitDetail param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseVisit_detail();
        logger.info("VisitDetail url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("VisitDetail result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 详情页体检接口:唐乾斌提供
     * @param req
     * @param resp
     */
    public void labResultItem(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("LabResultItem param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseLab_result_item();
        logger.info("LabResultItem url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("LabResultItem result="+url);
        viewer.viewString(result,resp,req);
    }

    /**
     * 检验项列表
     * @param req
     * @param resp
     */
    public void labResultItemList(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("LabResultItemList param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseLab_result_item_list();
        logger.info("LabResultItemList url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("LabResultItemList result="+url);
        viewer.viewString(result,resp,req);
    }

    public void geneticDisease(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("GeneticDisease param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseGenetic_disease();
        logger.info("GeneticDisease url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("GeneticDisease result="+result);
        viewer.viewString(result,resp,req);
    }

    public void drugReaction(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("DrugReaction param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseDrug_reaction();
        logger.info("DrugReaction url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("DrugReaction result="+url);
        viewer.viewString(result,resp,req);
    }

    public void categoryCatalog(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("CategoryCatalog param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseCategory_catalog();
        logger.info("CategoryCatalog url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("CategoryCatalog result="+url);
        viewer.viewString(result,resp,req);
    }

    public void molecularDetection(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("MolecularDetection param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseMolecular_detection();
        logger.info("MolecularDetection url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("MolecularDetection result="+url);
        viewer.viewString(result,resp,req);
    }

    public void biologicalSpecimen(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("BiologicalSpecimen param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseBiological_specimen();
        logger.info("BiologicalSpecimen url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("BiologicalSpecimen result="+url);
        viewer.viewString(result,resp,req);
    }

    public void examResult(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("ExamResult param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCaseExam_result();
        logger.info("ExamResult url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("ExamResult result="+url);
        viewer.viewString(result,resp,req);
    }

    public void pathologicalExamination(HttpServletRequest req, HttpServletResponse resp) {
        String param = null;
        try{
            param = ParamUtils.getParam(req);
            logger.info("PathologicalExamination param="+param);
        }catch (Exception e){
            ParamUtils.errorParam(req,resp);
            return;
        }
        String url = ConfigurationService.getUrlBean().getCasePathological_examination();
        logger.info("PathologicalExamination url="+url);
        String result = HttpRequestUtils.httpPost(url,param);
        logger.info("PathologicalExamination result="+url);
        viewer.viewString(result,resp,req);
    }
}
