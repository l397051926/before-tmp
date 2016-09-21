package com.gennlife.platform.model;

import java.util.LinkedList;

/**
 * Created by chen-song on 16/9/12.
 */
public class Organization {
    private String orgID;
    private String org_name;
    private String leader;
    private String address;
    private Object staff = null;
    private Object Labs = new LinkedList<>();

    public Object getStaff() {
        return staff;
    }

    public void setStaff(Object staff) {
        this.staff = staff;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getLabs() {
        return Labs;
    }

    public void setLabs(Object labs) {
        Labs = labs;
    }
}
