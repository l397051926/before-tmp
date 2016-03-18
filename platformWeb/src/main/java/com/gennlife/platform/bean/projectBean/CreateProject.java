package com.gennlife.platform.bean.projectBean;

import java.util.Date;

/**
 * Created by chensong on 2015/12/11.
 */
public class CreateProject {
    private String projectID;//项目id
    private String creater;//创建者id
    private String projectName;//项目名称
    private String issueType;//课题类型
    private String issueName;//课题名称
    private String issueEngName;//课题英文名称
    private Date startTime;//课题开始时间
    private Date endTime;//预计结束时间
    private String unit;//申办单位
    private String leaderUnit;//组长单位
    private String manager;//负责人
    private int caseNum;//病例数要求
    private String monitorUnit;//监查单位
    private String CRO;// CRO单位
    private String planVersion;//方案版本号
    private String abstractText;//课题摘要
    private String researchArea;//研究方向
    private String researchDesc;//研究方向具体具体描述
    private String center;//中心
    private String typein;//录入方式：单人录入/多方录入
    private String testType;//临床实验类型:II期临床试验/生物等效性试验
    private String testGroup;//单臂实验/双臂实验
    private int pstatus;//0,创建;1,导入样本;2,分析等等
    private String randomMethod;//随机方式
    private String blindSetting;//盲法设置
    private int members;
    private int planNum;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueEngName() {
        return issueEngName;
    }

    public void setIssueEngName(String issueEngName) {
        this.issueEngName = issueEngName;
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

    public String getLeaderUnit() {
        return leaderUnit;
    }

    public void setLeaderUnit(String leaderUnit) {
        this.leaderUnit = leaderUnit;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(int caseNum) {
        this.caseNum = caseNum;
    }

    public String getMonitorUnit() {
        return monitorUnit;
    }

    public void setMonitorUnit(String monitorUnit) {
        this.monitorUnit = monitorUnit;
    }

    public String getCRO() {
        return CRO;
    }

    public void setCRO(String CRO) {
        this.CRO = CRO;
    }

    public String getPlanVersion() {
        return planVersion;
    }

    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public String getResearchDesc() {
        return researchDesc;
    }

    public void setResearchDesc(String researchDesc) {
        this.researchDesc = researchDesc;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getTypein() {
        return typein;
    }

    public void setTypein(String typein) {
        this.typein = typein;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(String testGroup) {
        this.testGroup = testGroup;
    }

    public int getPstatus() {
        return pstatus;
    }

    public void setPstatus(int pstatus) {
        this.pstatus = pstatus;
    }

    public String getRandomMethod() {
        return randomMethod;
    }

    public void setRandomMethod(String randomMethod) {
        this.randomMethod = randomMethod;
    }

    public String getBlindSetting() {
        return blindSetting;
    }

    public void setBlindSetting(String blindSetting) {
        this.blindSetting = blindSetting;
    }
}
