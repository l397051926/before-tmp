package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/12.
 */
public class Admin {
    private String uid = null;
    private String privilegeType = null;
    private String privilegeValue = null;
    private String orgID = null;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(String privilegeType) {
        this.privilegeType = privilegeType;
    }

    public String getPrivilegeValue() {
        return privilegeValue;
    }

    public void setPrivilegeValue(String privilegeValue) {
        this.privilegeValue = privilegeValue;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }
}
