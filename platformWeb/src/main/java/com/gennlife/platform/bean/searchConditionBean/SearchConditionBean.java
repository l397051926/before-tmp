package com.gennlife.platform.bean.searchConditionBean;

/**
 * Created by chen-song on 16/8/9.
 */
public class SearchConditionBean {
    private Integer conditionID = null;
    private String uid = null;
    private String conditionStr = null;
    private String logTime = null;
    private String conditionName = null;
    private String conditionList = null;
    private String crfId = null;

    public String getCrfId() {
        return crfId;
    }

    public void setCrfId(String crfId) {
        this.crfId = crfId;
    }

    public Integer getConditionID() {
        return conditionID;
    }

    public void setConditionID(Integer conditionID) {
        this.conditionID = conditionID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getConditionStr() {
        return conditionStr;
    }

    public void setConditionStr(String conditionStr) {
        this.conditionStr = conditionStr;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getConditionList() {
        return conditionList;
    }

    public void setConditionList(String conditionList) {
        this.conditionList = conditionList;
    }
}
