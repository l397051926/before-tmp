package com.gennlife.platform.bean.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("OrgBean")
@Scope("singleton")
@ConfigurationProperties(prefix = "ui.orgBean")
public class OrgBean {
    private String orgID;
    private String orgName;

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}