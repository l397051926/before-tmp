package com.gennlife.platform.processor;

import com.gennlife.platform.service.ConfigurationService;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chen-song on 16/5/13.
 */
public class DetailProcessor {
    private Logger logger = LoggerFactory.getLogger(DetailProcessor.class);
    /**
     * 详情页患者基础信息接口
     * 透传,无逻辑
     * @param param
     */
    public String patientBasicInfo(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDetailPatientBasicInfoURL();
            logger.info("PatientBasicInfo url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("PatientBasicInfo result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }

    }

    /**
     * 遗传性疾病
     * 透传,无逻辑
     * @param param
     */
    public String geneticDisease(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseGenetic_disease();
            logger.info("GeneticDisease url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("GeneticDisease result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }

    }



    /**
     * 药物反应
     * 透传,无逻辑
     * @param param
     */
    public String drugReaction(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDrug_reaction();
            logger.info("DrugReaction url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("DrugReaction result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }

    }

    /**
     * 详情页基本统计图形&筛选条件
     * 透传,无逻辑
     * @param param
     */
    public String patientBasicFigure(String param ) {
        try{
            String url= ConfigurationService.getUrlBean().getCaseDetailPatientBasicFigureURL();
            logger.info("PatientBasicFigure url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("PatientBasicFigure result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页时间轴接口
     * 透传,无逻辑
     * @param param
     */
    public String patientBasicTimeAxis(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePatientBasicTimeAxisURL();
            logger.info("PatientBasicTimeAxis url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("PatientBasicTimeAxis result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 查看指标变化：可选列表
     * @param param
     */
    public String choicesList(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePhysical_examination_list();
            logger.info("ChoicesList url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("ChoicesList result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 查看指标变化：具体指标
     * @param param
     */
    public String specificChoice(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePhysical_examination();
            logger.info("SpecificChoice url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("SpecificChoice result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页总接口：唐乾斌提供
     * @param param
     */
    public String visitDetail(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseVisit_detail();
            logger.info("VisitDetail url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("VisitDetail result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 详情页体检接口:唐乾斌提供
     * @param param
     */
    public String labResultItem(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseLab_result_item();
            logger.info("LabResultItem url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("LabResultItem result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 检验项列表
     * @param param
     */
    public String labResultItemList(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseLab_result_item_list();
            logger.info("LabResultItemList url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("LabResultItemList result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }



    public String categoryCatalog(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseCategory_catalog();
            logger.info("CategoryCatalog url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("CategoryCatalog result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String molecularDetection(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseMolecular_detection();
            logger.info("MolecularDetection url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("MolecularDetection result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String biologicalSpecimen( String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseBiological_specimen();
            logger.info("BiologicalSpecimen url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("BiologicalSpecimen result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String examResult(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseExam_result();
            logger.info("ExamResult url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("ExamResult result="+url);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String pathologicalExamination(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePathological_examination();
            logger.info("PathologicalExamination url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("PathologicalExamination result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }
}
