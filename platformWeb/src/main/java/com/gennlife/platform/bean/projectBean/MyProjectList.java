package com.gennlife.platform.bean.projectBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chensong on 2015/12/11.
 */
public class MyProjectList {
    /**
     * creator,
     * p.projectID,
     * projectName,
     * projectEngName,
     * createTime,
     * members,
     * planNum,
     * projectDesp,
     * setCount,
     * unit,
     * manager,
     * type,
     * disease,
     * registerNumber
     */
    private List<ProLog> logs;
    private String creator;
    private String projectID;
    private String projectName;
    private String projectEngName;
    private String cTime;
    private int members;
    private int planNum;
    private String unit;
    private String manager;
    private String disease;
    private String type;
    private String registerNumber;
    private String projectDesp;
    private int setCount;

    public List<ProLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ProLog> logs) {
        this.logs = logs;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectEngName() {
        return projectEngName;
    }

    public void setProjectEngName(String projectEngName) {
        this.projectEngName = projectEngName;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getPlanNum() {
        return planNum;
    }

    public void setPlanNum(int planNum) {
        this.planNum = planNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getProjectDesp() {
        return projectDesp;
    }

    public void setProjectDesp(String projectDesp) {
        this.projectDesp = projectDesp;
    }

    public int getSetCount() {
        return setCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }
}
