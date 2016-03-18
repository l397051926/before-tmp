package com.gennlife.platform.bean.projectBean;

import java.util.Date;

/**
 * Created by chensong on 2015/12/12.
 */
public class ProLog {
    private String projectID;
    private String planName;
    private String uid;
    private String action;
    private Date logTime;
    private String logText;
    private String lTime;
    private String sampleName;
    private String sampleURI;

    public String getSampleURI() {
        return sampleURI;
    }

    public void setSampleURI(String sampleURI) {
        this.sampleURI = sampleURI;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getlTime() {
        return lTime;
    }

    public void setlTime(String lTime) {
        this.lTime = lTime;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
