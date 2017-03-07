package com.gennlife.platform.bean.projectBean;

import java.util.Date;

/**
 * Created by chensong on 2015/12/14.
 */
public class ProSample {
    private String projectID;
    private String planName;
    private int bstatus;
    private String sampleURI;
    private String sampleName;
    private String operator;
    private Date opTime;
    private int batchID;
    private String operatTime;
    private int total;
    private String items;
    private String sampleDesc="";
    private int sampledelete=0;

    public String getSampleDesc() {
        return sampleDesc;
    }

    public void setSampleDesc(String sampleDesc) {
        this.sampleDesc = sampleDesc;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getOperatTime() {
        return operatTime;
    }

    public void setOperatTime(String operatTime) {
        this.operatTime = operatTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSampleURI() {
        return sampleURI;
    }

    public void setSampleURI(String sampleURI) {
        this.sampleURI = sampleURI;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public int getSampledelete() {
        return sampledelete;
    }

    public void setSampledelete(int sampledelete) {
        this.sampledelete = sampledelete;
    }
}
