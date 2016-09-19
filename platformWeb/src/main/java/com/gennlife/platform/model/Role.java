package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/9.
 */
public class Role {
    private int roleid;
    private String role;
    private String orgID;
    private String desctext;
    private String role_type;
    private String resourceDesc;
    private String ctime;
    private Object resources;
    private Object users;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public Object getUsers() {
        return users;
    }

    public void setUsers(Object users) {
        this.users = users;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getDesctext() {
        return desctext;
    }

    public void setDesctext(String desctext) {
        this.desctext = desctext;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }

    public Object getResources() {
        return resources;
    }

    public void setResources(Object resources) {
        this.resources = resources;
    }
}
