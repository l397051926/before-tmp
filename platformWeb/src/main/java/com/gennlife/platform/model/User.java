package com.gennlife.platform.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chen-song on 16/9/8.
 */
public class User {
    /**
     *`uid` varchar(20) DEFAULT NULL COMMENT '用户id',
     `pwd` varchar(40) DEFAULT NULL COMMENT '用户密码加密后的',
     `uname` varchar(50) DEFAULT NULL COMMENT '用户名称',
     `uemail` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
     `uposition` varchar(20) DEFAULT NULL COMMENT '职位',
     `uprofession` varchar(20) DEFAULT NULL COMMENT '职业',
     `orgID` varchar(100) DEFAULT NULL COMMENT '所在单位ID',
     `labID` varchar(50) DEFAULT NULL COMMENT '科室,XXX科',
     `telphone` varchar(20) DEFAULT NULL COMMENT '电话',
     `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
     `sex` tinyint(4) DEFAULT NULL COMMENT '性别，1为男，0为女',
     `ctime` varchar(30) DEFAULT NULL COMMENT 'yyyy-MM-dd HH:mm:ss格式时间',
     `uptime` varchar(30) DEFAULT NULL COMMENT '最后一次修改数据时间',
     `unumber` varchar(50) DEFAULT NULL COMMENT '工号',
     `md5` varchar(40) DEFAULT NULL COMMENT '修改密码的md5校验',
     */
    private String uid = "";
    private String pwd = "ls123456";
    private String uname = "";
    private String uemail = "";
    private String uposition = "";
    private String uprofession = "";
    private String orgID = "";
    private String org_name = "";
    private String labID ="";
    private String lab_name = "";
    private String telphone = "";
    private int age = 0;
    private int sex = 0;
    private String ctime;
    private String uptime;
    private String unumber = "";
    private Object roles = new LinkedList<>();//角色列表
    private Object administrators = new LinkedList<>();//管理员角色

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUposition() {
        return uposition;
    }

    public void setUposition(String uposition) {
        this.uposition = uposition;
    }

    public String getUprofession() {
        return uprofession;
    }

    public void setUprofession(String uprofession) {
        this.uprofession = uprofession;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getLabID() {
        return labID;
    }

    public void setLabID(String labID) {
        this.labID = labID;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getUnumber() {
        return unumber;
    }

    public void setUnumber(String unumber) {
        this.unumber = unumber;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }

    public Object getAdministrators() {
        return administrators;
    }

    public void setAdministrators(Object administrators) {
        this.administrators = administrators;
    }
}
