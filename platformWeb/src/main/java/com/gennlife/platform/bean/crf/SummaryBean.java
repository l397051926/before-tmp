package com.gennlife.platform.bean.crf;


import java.util.Date;
import java.util.Set;

/**
 * Created by chen-song on 16/3/19.
 */
public class SummaryBean {
    private String crf_id;//crf的主键
    private String projectID;//项目号
    private String crfName;
    private String createTime;//"yyyy-MM-dd HH:mm:ss"格式创建时间
    private String crfStatus;
    private String crfRemark;
    private String uid;//创建人
    private String lastTime;//"yyyy-MM-dd HH:mm:ss"最后修改时间
    private Set<String> users;//项目的成员
    private String caseID = null;//当前录入的caseid
    private int maxCaseNo = 0;//病人编号最大值
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getMaxCaseNo() {
        return maxCaseNo;
    }

    public void setMaxCaseNo(int maxCaseNo) {
        this.maxCaseNo = maxCaseNo;
    }

    public String getCrf_id() {
        return crf_id;
    }

    public void setCrf_id(String crf_id) {
        this.crf_id = crf_id;
    }


    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCrfName() {
        return crfName;
    }

    public void setCrfName(String crfName) {
        this.crfName = crfName;
    }


    public String getCrfStatus() {
        return crfStatus;
    }

    public void setCrfStatus(String crfStatus) {
        this.crfStatus = crfStatus;
    }

    public String getCrfRemark() {
        return crfRemark;
    }

    public void setCrfRemark(String crfRemark) {
        this.crfRemark = crfRemark;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
