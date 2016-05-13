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
