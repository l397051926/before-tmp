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
            logger.info("PatientBasicInfo result="+result);
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
            logger.info("GeneticDisease result="+result);
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
            logger.info("DrugReaction result="+result);
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
            logger.info("PatientBasicFigure result="+result);
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
            logger.info("PatientBasicTimeAxis result="+result);
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
            logger.info("ChoicesList result="+result);
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
            logger.info("LabResultItem result="+result);
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
            logger.info("LabResultItemList result="+result);
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
            logger.info("CategoryCatalog result="+result);
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
            logger.info("MolecularDetection result="+result);
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
            logger.info("BiologicalSpecimen result="+result);
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
            logger.info("ExamResult result="+result);
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
            //logger.info("PathologicalExamination result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 单次就诊专用
     * @param param
     * @return
     */
    public String visitDiagnose(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDiagnose();
            logger.info("visitDiagnose url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("visitDiagnose result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病程 诊断报告:主诉
     * @param param
     * @return
     */
    public String admissionRecords(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseAdmission_records();
            logger.info("admissionRecords url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("admissionRecords result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病案首页
     * @param param
     * @return
     */
    public String medicalRecord(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseMedicalRecord();
            logger.info("medicalRecord url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("medicalRecord result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 手术信息
     * @param param
     * @return
     */
    public String operationRecords(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseOperationRecords();
            logger.info("operationRecords url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("operationRecords result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 用药医嘱
     * @param param
     * @return
     */
    public String pharmacy(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePharmacy();
            logger.info("pharmacy url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("pharmacy result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 出院记录
     * @param param
     * @return
     */
    public String dischargeRecords(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDischargeRecords();
            logger.info("dischargeRecords url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("dischargeRecords result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病程记录
     * @param param
     * @return
     */
    public String courseRecords(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseCourseRecords();
            logger.info("courseRecords url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("courseRecords result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病例文书
     * @param param
     * @return
     */
    public String medicalCourse(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseMedicalCourse();
            logger.info("medicalCourse url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("medicalCourse result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 病史信息接口
     * @param param
     * @return
     */
    public String visitClassifySection(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDetailVisitClassifySectionURL();
            logger.info("visitClassifySection url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            logger.info("visitClassifySection result="+result);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 图片获取接口
     * @param param
     * @return
     */
    public String visitClassifyImage(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseDetailVisitClassifyImageURL();
            logger.info("visitClassifyImage url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String VisitDcOrder(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseVisitDcOrder();
            logger.info("VisitDcOrder url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientDcOrder(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePatientDcOrder();
            logger.info("PatientDcOrder url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientRadiotherapy(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePatientRadiotherapy();
            logger.info("PatientRadiotherapy url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String VisitRadiotherapy(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseyVisitRadiotherapy();
            logger.info("VisitRadiotherapy url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String PatientChemotherapyInfo(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCasePatientChemotherapyInfo();
            logger.info("PatientChemotherapyInfo url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * vitaboard画图数据获取接口
     * @param param
     * @return
     */
    public String similarServiceGetSimilars(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceGetSimilars();
            logger.info("SimilarServiceGetSimilars url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     *
     * @param param
     * @return
     */
    public String patientDetailVitaboardParam(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceVitaboardparam();
            logger.info("SimilarServiceVitaboardParam url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String similarServiceSimilarParam(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarParam();
            logger.info("similarServiceSimilarParam url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 生成相似病人查询条件接口
     * @param param
     * @return
     */
    public String similarServiceSimilarQuery(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarQuery();
            logger.info("similarServiceSimilarQuery url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 获取相似病人情况的基本信息
     * @param param
     * @return
     */
    public String similarServiceSimilarBasicDetail(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarBasicDetail();
            logger.info("similarServiceSimilarBasicDetail url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    /**
     * 当前病人的信息
     * @param param
     * @return
     */
    public String similarServiceSimilarPatientInfo(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarPatientInfo();
            logger.info("similarServiceSimilarPatientInfo url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }

    public String similarServiceSimilarPatientExtraInfo(String param) {
        try{
            String url = ConfigurationService.getUrlBean().getCaseSimilarServiceSimilarPatientExtraInfo();
            logger.info("similarServiceSimilarPatientExtraInfo url="+url);
            String result = HttpRequestUtils.httpPost(url,param);
            return result;
        }catch (Exception e){
            return ParamUtils.errorParam("请求出错");
        }
    }
    public String PatientBatchDataConsultingRoom(String param) {
            try{
                String url = ConfigurationService.getUrlBean().getCasePatientBatchDataConsultingRoom();
                logger.info("PatientBatchDataConsultingRoom url="+url);
                String result = HttpRequestUtils.httpPost(url,param);
                return result;
            }catch (Exception e){
                return ParamUtils.errorParam("请求出错");
            }

    }
}
