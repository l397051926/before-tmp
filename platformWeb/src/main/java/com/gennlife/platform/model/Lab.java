package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/12.
 */
public class Lab {
    private String orgID;
    private String labID;
    private String lab_name;
    private String lab_leader;
    private Object lab_parent;
    private Object lab_level;

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

    public Object getLab_parent() {
        return lab_parent;
    }

    public void setLab_parent(Object lab_parent) {
        this.lab_parent = lab_parent;
    }

    public Object getLab_level() {
        return lab_level;
    }

    public void setLab_level(Object lab_level) {
        this.lab_level = lab_level;
    }
}
