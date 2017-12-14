package com.gennlife.platform.model;

/**
 * Created by Chenjinfeng on 2017/12/14.
 */
public class GennDataModel {

    private String uniqueId;
    private String sampleType;
    private String sampleGetDate;
    private String reportDate;
    private String inspectionDept;
    private String inspectionDoctor;
    private String pdfId;
    private String patientSn;
    private String visitSn;
    private String synTime;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleGetDate() {
        return sampleGetDate;
    }

    public void setSampleGetDate(String sampleGetDate) {
        this.sampleGetDate = sampleGetDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getInspectionDept() {
        return inspectionDept;
    }

    public void setInspectionDept(String inspectionDept) {
        this.inspectionDept = inspectionDept;
    }

    public String getInspectionDoctor() {
        return inspectionDoctor;
    }

    public void setInspectionDoctor(String inspectionDoctor) {
        this.inspectionDoctor = inspectionDoctor;
    }

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getPatientSn() {
        return patientSn;
    }

    public void setPatientSn(String patientSn) {
        this.patientSn = patientSn;
    }

    public String getVisitSn() {
        return visitSn;
    }

    public void setVisitSn(String visitSn) {
        this.visitSn = visitSn;
    }

    public String getSynTime() {
        return synTime;
    }

    public void setSynTime(String synTime) {
        this.synTime = synTime;
    }
}
