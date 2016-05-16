package com.gennlife.platform.bean.crf;

import java.util.List;

/**
 * Created by chen-song on 16/3/29.
 */
public class DataBean {
    private String crf_id = null;
    private String caseID = null;
    private String patientNo = "0";
    private String patientName = null;
    private List<Object> children = null;
    public String getCrf_id() {
        return crf_id;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public void setCrf_id(String crf_id) {

        this.crf_id = crf_id;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
