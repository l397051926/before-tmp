package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/12.
 */
public class Lab {
    private String orgID;
    private String labID;
    private String lab_name;
    private String lab_leader;
    private String lab_parent;
    private String add_user;
    private String add_time;
    private int lab_level;
    private Object subLabs;

    public String getAdd_user() {
        return add_user;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getLab_leader() {
        return lab_leader;
    }

    public void setLab_leader(String lab_leader) {
        this.lab_leader = lab_leader;
    }

    public String getLab_parent() {
        return lab_parent;
    }

    public void setLab_parent(String lab_parent) {
        this.lab_parent = lab_parent;
    }

    public int getLab_level() {
        return lab_level;
    }

    public void setLab_level(int lab_level) {
        this.lab_level = lab_level;
    }

    public Object getSubLabs() {
        return subLabs;
    }

    public void setSubLabs(Object subLabs) {
        this.subLabs = subLabs;
    }
}
