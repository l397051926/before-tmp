package com.gennlife.platform.model;

/**
 * Created by Chenjinfeng on 2018/1/5.
 */
public class GennListModel extends GennDataModel {

    private String sampleSn = "";

    private String disease = "";

    private String detectionResult = "";// 检测结果

    public GennListModel() {
    }

    public GennListModel(GennDataModel p) {
        setUniqueId(p.getUniqueId());
        setSampleType(p.getSampleType());
        setSampleGetDate(p.getSampleGetDate());
        setReportDate(p.getReportDate());
        setInspectionDept(p.getInspectionDept());
        setInspectionDoctor(p.getInspectionDoctor());
        setPdfId(p.getPdfId());
        setPatientSn(p.getPatientSn());
        setVisitSn(p.getVisitSn());
    }

    public String getSampleSn() {
        return sampleSn;
    }

    public void setSampleSn(String sampleSn) {
        if (sampleSn == null) sampleSn = "";
        this.sampleSn = sampleSn;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        if (disease == null) disease = "";
        this.disease = disease;
    }

    public String getDetectionResult() {
        return detectionResult;
    }

    public void setDetectionResult(String detectionResult) {
        this.detectionResult = detectionResult;
    }
}
