package com.gennlife.platform.configuration;

/**
 * Created by chen-song on 16/5/13.
 */
public class URLBean {
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
     * 搜索:V1.0
     * 关键词同义接口
     */
    private String synQueryURL = null;
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

    public String getSynQueryURL() {
        return synQueryURL;
    }

    public void setSynQueryURL(String synQueryURL) {
        this.synQueryURL = synQueryURL;
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
}
