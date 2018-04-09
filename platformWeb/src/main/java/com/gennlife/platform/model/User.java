package com.gennlife.platform.model;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by chen-song on 16/9/8.
 */
public class User {
    private String uid;
    private transient String pwd;
    private String uname;
    private String uemail;
    private String uposition;
    private String uprofession;
    private String orgID;
    private String org_name;
    private String labID;
    private String lab_name;
    private String telphone;
    private int age;
    private int sex;
    private String ctime;
    private String uptime;
    private String unumber;
    private List<Role> roles; // 角色列表
    private List<Admin> administrators; // 管理员角色
    private List<Group> groups;
    private Power power = new Power(); // 权限
    private Power frontEndPower = new Power();

    private String status;  //1:长期有效、2:定期有效、3:禁用
    private String effective_time;   //有效开始时间
    private String failure_time;    //失效时间

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEffective_time() {
        return effective_time;
    }

    public void setEffective_time(String effective_time) {
        this.effective_time = effective_time;
    }

    public String getFailure_time() {
        return failure_time;
    }

    public void setFailure_time(String failure_time) {
        this.failure_time = failure_time;
    }

    public Power getFrontEndPower() {
        return frontEndPower;
    }

    public void setFrontEndPower(Power power) {
        this.frontEndPower = power;
    }

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Admin> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<Admin> administrators) {
        this.administrators = administrators;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }


    public JsonObject CopyMemberInfo() {
        JsonObject json = new JsonObject();
        json.addProperty("uid", uid);
        json.addProperty("uname", uname);
        json.addProperty("unumber", unumber);
        return json;
    }
}
