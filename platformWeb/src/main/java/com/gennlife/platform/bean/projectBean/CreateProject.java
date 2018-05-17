package com.gennlife.platform.bean.projectBean;

import java.util.Date;

/**
 * Created by chensong on 2015/12/11.
 */
public class CreateProject {
    private String projectID;//项目id
    private String creator;//创建者id
    private String projectName;//项目名称
    private String projectEngName;//项目英文名
    private String projectDesp;//项目描述

    private Date startTime;//课题开始时间
    private Date endTime;//预计结束时间
    private String unit;//单位
    private String manager;//负责人
    private String center;//中心
    private int members;//成员
    private int planNum;//方案数目
    private Date createTime;//创建时间
    private String type;//科研类型
    private int setCount;//样本集数量
    private String disease;
    private String registerNumber;
    private int isdelete = 0;

    //病种的名字和数据源
    private String crfName;
    private String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getCrfName() {
        return crfName;
    }

    public void setCrfName(String crfName) {
        this.crfName = crfName;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getProjectDesp() {
        return projectDesp;
    }

    public void setProjectDesp(String projectDesp) {
        this.projectDesp = projectDesp;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSetCount() {
        return setCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }
}
