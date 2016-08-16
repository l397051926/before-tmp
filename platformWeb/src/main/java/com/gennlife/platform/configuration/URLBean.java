package com.gennlife.platform.configuration;

/**
 * Created by chen-song on 16/5/13.
 */
public class URLBean {

    public String getCaseAdmission_records() {
        return caseAdmission_records;
    }

    public void setCaseAdmission_records(String caseAdmission_records) {
        this.caseAdmission_records = caseAdmission_records;
    }

    /**
     * 工作区工具列表
     */
    private String toolsURL = null;

    private String AtoolURL = null;
    /**
     * 搜索结果导出到项目
     */
    private String sampleImportIURL = null;
    /**
     * 样本详情接口
     */
    private String sampleDetailURL = null;

    private String sampleDaleteURL = null;

    /**
     * 病程 诊断报告:主诉
     */
    private String caseAdmission_records=null;

    /**
     * 新的搜索后端接口
     */
    private String caseSearchURL=null;
    /**
     * 搜索关键词提示词
     */
    private String caseSuggestURL = null;
    /**
     * 搜索:V1.0
     * 详情页患者基础信息接口
     */
    private String caseDetailPatientBasicInfoURL=null;
    /**
     *  搜索:V1.0
     *  基本统计图形&筛选条件
     */
    private String caseDetailPatientBasicFigureURL=null;
    /**
     * 搜索:V1.0
     * 详情页时间轴信息接口
     */
    private String casePatientBasicTimeAxisURL=null;

    /**
     * 查看指标变化: 具体指标
     */
    private String casePhysical_examination = null;
    /**
     * 查看指标变化: 可选列表
     */
    private String casePhysical_examination_list = null;

    /**
     * 知识库搜索
     */
    private String knowledgeURL = null;
    /**
     * 详情页总接口：唐乾斌提供
     */
    private String caseVisit_detail = null;
    /**
     * 详情页体检接口:唐乾斌提供
     */
    private String caseLab_result_item = null;
    /**
     * 搜索首页,疾病搜索基因
     */
    private String knowledgeDiseaseSearchGenesURL = null;
    /**
     * 检验项列表
     */
    private String caseLab_result_item_list = null;
    /**
     * 基因信息接口:搜索首页
     */
    private String knowledgeGeneInfoURL = null;
    /**
     * 变异信息接口:搜索首页
     */
    private String knowledgeVariationInfoURL = null;
    /**
     *
     */
    private String knowledgeDetailVariationSearchDiseaseURL = null;
    /**
     *
     */
    private String knowledgeDetailVariationSearchDrugURL = null;

    /**
     *单次就诊专用:诊断报告
     */
    private String caseDiagnose = null;


    /**
     * 遗传性疾病
     */
    private String caseGenetic_disease = null;
    /**
     * 药物反应
     */
    private String caseDrug_reaction = null;
    /**
     * 分类信息接口、及目录
     */
    private String caseCategory_catalog = null;
    /**
     * 分子检测
     */
    private String caseMolecular_detection = null;
    /**
     * 生物标本
     */
    private String caseBiological_specimen = null;
    /**
     * 检查
     */
    private String caseExam_result = null;
    /**
     * 病理检测
     */
    private String casePathological_examination = null;
    /**
     * 详情页,通过变异号和基因类型查询 药和用药指南
     */
    private String knowledgePharmGKBSearchDrugURL = null;
    /**
     * 基因数组校验接口
     */
    private String caseGeneErrorURL = null;

    /**
     * crf service:用户相关项目的crf模版列表：
     */
    private String CRFProjectCRFListURL = null;

    /**
     * crf service:将搜索到的病例导入crf接口
     */
    private String CRFAutoMapURL = null;

    /**
     * crf service:获取新的属性id
     */
    private String CRFGetAttrID = null;

    /**
     * crf service:获取新的组id
     */
    private String CRFGetGroupID = null;


    /**
     * crf service:通过projectID获取crf模板
     */
    private String CRFModelByProjectID = null;

    /**
     * crf service:通过CRFID获取crf模板
     */
    private String CRFModelByCRFID = null;


    /**
     * crf service:编辑模型
     */
    private String CRFEditModel = null;

    /**
     * crf service:保存模型
     */
    private String CRFSaveModel = null;
    /**
     * crf service:数据录入时,请求某个case数据
     */
    private String CRFGetData = null;
    /**
     * crf service:上传crf数据
     */
    private String CRFUpLoadData = null;
    /**
     * crf service:录入完成接口
     */
    private String CRFSaveData = null;

    /**
     * crf service:病历列表数据
     */
    private String CRFSampleCaseList = null;
    /**
     * crf service:删除某个case数据
     */
    private String CRFDeleteSample = null;
    /**
     * crf service:搜索病历列表
     */
    private String CRFSearchSampleList = null;

    /*
    *  病人基本信息查询
     */
    private String CRFGetPatientInfo=null;

    private String RRun = null;

    private String RStop = null;

    private String RSave = null;
 
    private String RLoad = null;

    private String RList = null;

    /**
     *.病案首页
     */
    private String caseMedicalRecord = null;
    /**
     * 手术信息
     */
    private String caseOperationRecords = null;
    /**
     * 用药医嘱
     */
    private String casePharmacy = null;
    /**
     * 出院记录
     */
    private String caseDischargeRecords = null;
    /**
     * 病程记录
     */
    private String caseCourseRecords = null;
    /**
     * 病例文书
     */
    private String caseMedicalCourse = null;


    private String CRFModelForTraceByCRFID = null;

    public String getCRFModelForTraceByCRFID() {
        return CRFModelForTraceByCRFID;
    }

    public void setCRFModelForTraceByCRFID(String CRFModelForTraceByCRFID) {
        this.CRFModelForTraceByCRFID = CRFModelForTraceByCRFID;
    }

    public String getCRFPatientVisitDetail() {
        return CRFPatientVisitDetail;
    }

    public void setCRFPatientVisitDetail(String CRFPatientVisitDetail) {
        this.CRFPatientVisitDetail = CRFPatientVisitDetail;
    }

    /*
        *溯源页病人详细信息
         */
    private String CRFPatientVisitDetail=null;

    public String getCRFPatientAllVisitsDetail() {
        return CRFPatientAllVisitsDetail;
    }

    public void setCRFPatientAllVisitsDetail(String CRFPatientAllVisitsDetail) {
        this.CRFPatientAllVisitsDetail = CRFPatientAllVisitsDetail;
    }

    /*
        *溯源查看全部病人详细信息
         */
    private String CRFPatientAllVisitsDetail=null;

    public String getSampleImportIURL() {
        return sampleImportIURL;
    }

    public void setSampleImportIURL(String sampleImportIURL) {
        this.sampleImportIURL = sampleImportIURL;
    }

    public String getCRFModelByProjectID() {
        return CRFModelByProjectID;
    }

    public void setCRFModelByProjectID(String CRFModelByProjectID) {
        this.CRFModelByProjectID = CRFModelByProjectID;
    }

    public String getCRFModelByCRFID() {
        return CRFModelByCRFID;
    }

    public void setCRFModelByCRFID(String CRFModelByCRFID) {
        this.CRFModelByCRFID = CRFModelByCRFID;
    }

    public String getCRFEditModel() {
        return CRFEditModel;
    }

    public void setCRFEditModel(String CRFEditModel) {
        this.CRFEditModel = CRFEditModel;
    }

    public String getCRFSaveModel() {
        return CRFSaveModel;
    }

    public void setCRFSaveModel(String CRFSaveModel) {
        this.CRFSaveModel = CRFSaveModel;
    }

    public String getCRFGetData() {
        return CRFGetData;
    }

    public void setCRFGetData(String CRFGetData) {
        this.CRFGetData = CRFGetData;
    }

    public String getCRFUpLoadData() {
        return CRFUpLoadData;
    }

    public void setCRFUpLoadData(String CRFUpLoadData) {
        this.CRFUpLoadData = CRFUpLoadData;
    }

    public String getCRFSaveData() {
        return CRFSaveData;
    }

    public void setCRFSaveData(String CRFSaveData) {
        this.CRFSaveData = CRFSaveData;
    }

    public String getCRFSampleCaseList() {
        return CRFSampleCaseList;
    }

    public void setCRFSampleCaseList(String CRFSampleCaseList) {
        this.CRFSampleCaseList = CRFSampleCaseList;
    }

    public String getCRFDeleteSample() {
        return CRFDeleteSample;
    }

    public void setCRFDeleteSample(String CRFDeleteSample) {
        this.CRFDeleteSample = CRFDeleteSample;
    }

    public String getCRFSearchSampleList() {
        return CRFSearchSampleList;
    }

    public void setCRFSearchSampleList(String CRFSearchSampleList) {
        this.CRFSearchSampleList = CRFSearchSampleList;
    }

    public String getCRFGetAttrID() {
        return CRFGetAttrID;
    }

    public void setCRFGetAttrID(String CRFGetAttrID) {
        this.CRFGetAttrID = CRFGetAttrID;
    }

    public String getCRFGetGroupID() {
        return CRFGetGroupID;
    }

    public void setCRFGetGroupID(String CRFGetGroupID) {
        this.CRFGetGroupID = CRFGetGroupID;
    }

    public String getCRFAutoMapURL() {
        return CRFAutoMapURL;
    }

    public void setCRFAutoMapURL(String CRFAutoMapURL) {
        this.CRFAutoMapURL = CRFAutoMapURL;
    }

    public String getCRFProjectCRFListURL() {
        return CRFProjectCRFListURL;
    }

    public void setCRFProjectCRFListURL(String CRFProjectCRFListURL) {
        this.CRFProjectCRFListURL = CRFProjectCRFListURL;
    }

    public String getCaseGeneErrorURL() {
        return caseGeneErrorURL;
    }

    public void setCaseGeneErrorURL(String caseGeneErrorURL) {
        this.caseGeneErrorURL = caseGeneErrorURL;
    }

    public String getKnowledgePharmGKBSearchDrugURL() {
        return knowledgePharmGKBSearchDrugURL;
    }

    public void setKnowledgePharmGKBSearchDrugURL(String knowledgePharmGKBSearchDrugURL) {
        this.knowledgePharmGKBSearchDrugURL = knowledgePharmGKBSearchDrugURL;
    }

    public String getCaseExam_result() {
        return caseExam_result;
    }

    public void setCaseExam_result(String caseExam_result) {
        this.caseExam_result = caseExam_result;
    }

    public String getCasePathological_examination() {
        return casePathological_examination;
    }

    public void setCasePathological_examination(String casePathological_examination) {
        this.casePathological_examination = casePathological_examination;
    }

    public String getCaseGenetic_disease() {
        return caseGenetic_disease;
    }

    public void setCaseGenetic_disease(String caseGenetic_disease) {
        this.caseGenetic_disease = caseGenetic_disease;
    }

    public String getCaseDrug_reaction() {
        return caseDrug_reaction;
    }

    public void setCaseDrug_reaction(String caseDrug_reaction) {
        this.caseDrug_reaction = caseDrug_reaction;
    }

    public String getCaseCategory_catalog() {
        return caseCategory_catalog;
    }

    public void setCaseCategory_catalog(String caseCategory_catalog) {
        this.caseCategory_catalog = caseCategory_catalog;
    }

    public String getCaseMolecular_detection() {
        return caseMolecular_detection;
    }

    public void setCaseMolecular_detection(String caseMolecular_detection) {
        this.caseMolecular_detection = caseMolecular_detection;
    }

    public String getCaseBiological_specimen() {
        return caseBiological_specimen;
    }

    public void setCaseBiological_specimen(String caseBiological_specimen) {
        this.caseBiological_specimen = caseBiological_specimen;
    }

    public String getKnowledgeDetailVariationSearchDiseaseURL() {
        return knowledgeDetailVariationSearchDiseaseURL;
    }

    public void setKnowledgeDetailVariationSearchDiseaseURL(String knowledgeDetailVariationSearchDiseaseURL) {
        this.knowledgeDetailVariationSearchDiseaseURL = knowledgeDetailVariationSearchDiseaseURL;
    }

    public String getKnowledgeDetailVariationSearchDrugURL() {
        return knowledgeDetailVariationSearchDrugURL;
    }

    public void setKnowledgeDetailVariationSearchDrugURL(String knowledgeDetailVariationSearchDrugURL) {
        this.knowledgeDetailVariationSearchDrugURL = knowledgeDetailVariationSearchDrugURL;
    }

    public String getKnowledgeVariationInfoURL() {
        return knowledgeVariationInfoURL;
    }

    public void setKnowledgeVariationInfoURL(String knowledgeVariationInfoURL) {
        this.knowledgeVariationInfoURL = knowledgeVariationInfoURL;
    }

    public String getKnowledgeGeneInfoURL() {
        return knowledgeGeneInfoURL;
    }

    public void setKnowledgeGeneInfoURL(String knowledgeGeneInfoURL) {
        this.knowledgeGeneInfoURL = knowledgeGeneInfoURL;
    }

    public String getCaseLab_result_item_list() {
        return caseLab_result_item_list;
    }

    public void setCaseLab_result_item_list(String caseLab_result_item_list) {
        this.caseLab_result_item_list = caseLab_result_item_list;
    }

    public String getKnowledgeDiseaseSearchGenesURL() {
        return knowledgeDiseaseSearchGenesURL;
    }

    public void setKnowledgeDiseaseSearchGenesURL(String knowledgeDiseaseSearchGenesURL) {
        this.knowledgeDiseaseSearchGenesURL = knowledgeDiseaseSearchGenesURL;
    }

    public String getCaseLab_result_item() {
        return caseLab_result_item;
    }

    public void setCaseLab_result_item(String caseLab_result_item) {
        this.caseLab_result_item = caseLab_result_item;
    }

    public String getCaseVisit_detail() {
        return caseVisit_detail;
    }

    public void setCaseVisit_detail(String caseVisit_detail) {
        this.caseVisit_detail = caseVisit_detail;
    }

    public String getKnowledgeURL() {
        return knowledgeURL;
    }

    public void setKnowledgeURL(String knowledgeURL) {
        this.knowledgeURL = knowledgeURL;
    }

    public String getCasePhysical_examination_list() {
        return casePhysical_examination_list;
    }

    public void setCasePhysical_examination_list(String casePhysical_examination_list) {
        this.casePhysical_examination_list = casePhysical_examination_list;
    }

    public String getCasePhysical_examination() {
        return casePhysical_examination;
    }

    public void setCasePhysical_examination(String casePhysical_examination) {
        this.casePhysical_examination = casePhysical_examination;
    }

    public String getCaseSuggestURL() {
        return caseSuggestURL;
    }

    public void setCaseSuggestURL(String caseSuggestURL) {
        this.caseSuggestURL = caseSuggestURL;
    }


    public String getCasePatientBasicTimeAxisURL() {
        return casePatientBasicTimeAxisURL;
    }

    public void setCasePatientBasicTimeAxisURL(String casePatientBasicTimeAxisURL) {
        this.casePatientBasicTimeAxisURL = casePatientBasicTimeAxisURL;
    }

    public String getCaseSearchURL() {
        return caseSearchURL;
    }

    public void setCaseSearchURL(String caseSearchURL) {
        this.caseSearchURL = caseSearchURL;
    }

    public String getCaseDetailPatientBasicInfoURL() {
        return caseDetailPatientBasicInfoURL;
    }

    public void setCaseDetailPatientBasicInfoURL(String caseDetailPatientBasicInfoURL) {
        this.caseDetailPatientBasicInfoURL = caseDetailPatientBasicInfoURL;
    }

    public String getCaseDetailPatientBasicFigureURL() {
        return caseDetailPatientBasicFigureURL;
    }

    public void setCaseDetailPatientBasicFigureURL(String caseDetailPatientBasicFigureURL) {
        this.caseDetailPatientBasicFigureURL = caseDetailPatientBasicFigureURL;
    }

    public String getCRFGetPatientInfo() {
        return CRFGetPatientInfo;
    }

    public void setCRFGetPatientInfo(String CRFGetPatientInfo) {
        this.CRFGetPatientInfo = CRFGetPatientInfo;
    }

    public String getRRun() {
        return RRun;
    }

    public void setRRun(String RRun) {
        this.RRun = RRun;
    }

    public String getRStop() {
        return RStop;
    }

    public void setRStop(String RStop) {
        this.RStop = RStop;
    }

    public String getRSave() {
        return RSave;
    }

    public void setRSave(String RSave) {
        this.RSave = RSave;
    }

    public String getRLoad() {
        return RLoad;
    }

    public void setRLoad(String RLoad) {
        this.RLoad = RLoad;
    }

    public String getRList() {
        return RList;
    }

    public void setRList(String RList) {
        this.RList = RList;
    }

    public String getCaseDiagnose() {
        return caseDiagnose;
    }

    public void setCaseDiagnose(String caseDiagnose) {
        this.caseDiagnose = caseDiagnose;
    }

    public String getSampleDetailURL() {
        return sampleDetailURL;
    }

    public void setSampleDetailURL(String sampleDetailURL) {
        this.sampleDetailURL = sampleDetailURL;
    }

    public String getSampleDaleteURL() {
        return sampleDaleteURL;
    }

    public void setSampleDaleteURL(String sampleDaleteURL) {
        this.sampleDaleteURL = sampleDaleteURL;
    }

    public String getToolsURL() {
        return toolsURL;
    }

    public void setToolsURL(String toolsURL) {
        this.toolsURL = toolsURL;
    }

    public String getAtoolURL() {
        return AtoolURL;
    }

    public void setAtoolURL(String atoolURL) {
        AtoolURL = atoolURL;
    }

    public String getCaseMedicalRecord() {
        return caseMedicalRecord;
    }

    public void setCaseMedicalRecord(String caseMedicalRecord) {
        this.caseMedicalRecord = caseMedicalRecord;
    }

    public String getCaseOperationRecords() {
        return caseOperationRecords;
    }

    public void setCaseOperationRecords(String caseOperationRecords) {
        this.caseOperationRecords = caseOperationRecords;
    }

    public String getCasePharmacy() {
        return casePharmacy;
    }

    public void setCasePharmacy(String casePharmacy) {
        this.casePharmacy = casePharmacy;
    }

    public String getCaseDischargeRecords() {
        return caseDischargeRecords;
    }

    public void setCaseDischargeRecords(String caseDischargeRecords) {
        this.caseDischargeRecords = caseDischargeRecords;
    }

    public String getCaseCourseRecords() {
        return caseCourseRecords;
    }

    public void setCaseCourseRecords(String caseCourseRecords) {
        this.caseCourseRecords = caseCourseRecords;
    }

    public String getCaseMedicalCourse() {
        return caseMedicalCourse;
    }

    public void setCaseMedicalCourse(String caseMedicalCourse) {
        this.caseMedicalCourse = caseMedicalCourse;
    }
}
