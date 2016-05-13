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
