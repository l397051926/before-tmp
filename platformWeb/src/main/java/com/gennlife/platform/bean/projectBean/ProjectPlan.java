package com.gennlife.platform.bean.projectBean;

import java.util.Date;

/**
 * Created by chensong on 2015/12/12.
 */
public class ProjectPlan {
    private int id;
    private String projectID;
    private String planName;
    private String planDesc;
    private String creator;
    private int planStatus;
    private Date modifTime;
    private Date createTime;
    private String cTime;
    private String modifier;
    private String taskID;
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getProjectID() {
        return projectID;
    }

    public Date getModifTime() {
        return modifTime;
    }

    public void setModifTime(Date modifTime) {
        this.modifTime = modifTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPlanDesc() {
        return planDesc;
    }

    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
