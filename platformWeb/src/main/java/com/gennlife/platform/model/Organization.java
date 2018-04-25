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
    private Object Resource = new LinkedList<>();
    private Object spResource = null;

    private boolean isEmpty;

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public Object getSpResource() {
        return spResource;
    }

    public void setSpResource(Object spResource) {
        this.spResource = spResource;
    }

    public Object getResource() {
        return Resource;
    }

    public void setResource(Object resource) {
        Resource = resource;
    }

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
