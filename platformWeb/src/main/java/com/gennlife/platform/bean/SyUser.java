package com.gennlife.platform.bean;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chensong on 2015/12/2.
 */
public class SyUser {
    private Integer id;//自增id
    private String uid;//用户id
    private String uname;//用户名称
    private String pwd;//密码，加密后的
    private String uemail;//邮箱
    private String uposition;//职称
    private String hospitalID;//所在医院id
    private String hospitalName;//所在单位
    private String lab;//科室
    private String telphone;
    private int age;//年龄
    private int sex;//性别，1为男，0为女
    private Date ctime;//创建时间
    private Date uptime;//最后一次修改时间
    private String lastModifyTime;
    private Set<SyResource> resources = new HashSet<SyResource>();//权限
    private String role;//项目中的角色，manager，creater，member
    private String unumber;//工号

    public String getUnumber() {
        return unumber;
    }

    public void setUnumber(String unumber) {
        this.unumber = unumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUptime() {
        return uptime;
    }

    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    public Set<SyResource> getResources() {
        return resources;
    }

    public void setResources(Set<SyResource> resources) {
        this.resources = resources;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUposition() {
        return uposition;
    }

    public void setUposition(String uposition) {
        this.uposition = uposition;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
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




    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
