package com.gennlife.platform.bean.projectBean;

import java.io.Serializable;

/**
 * Created by chensong on 2015/12/11.
 */
public class ProUser implements Serializable {
    private String uid;
    private String projectID;
    private Integer role;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
