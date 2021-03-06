package com.gennlife.platform.bean.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Chenjinfeng on 2017/3/7.
 */
@Component("systemDefault")
@Scope("singleton")
@ConfigurationProperties(prefix = "ui.systemDefault")
public class SystemDefault {

    private String searchItemSetDefault;
    private String needGroup;
    @Deprecated
    private String passwdOperator;
    private String needToCreateIndex = null;
    private boolean DefaultCrfId;

    public boolean getDefaultCrfId() {
        return DefaultCrfId;
    }

    public void setDefaultCrfId(boolean defaultCrfId) {
        DefaultCrfId = defaultCrfId;
    }

    public String getNeedGroup() {
        return needGroup;
    }

    public void setNeedGroup(String needGroup) {
        this.needGroup = needGroup;
    }

    public void setSearchItemSetDefault(String searchItemSetDefault) {
        this.searchItemSetDefault = searchItemSetDefault;
    }

    public String getSearchItemSetDefault() {
        return searchItemSetDefault;
    }

    @Deprecated
    public void setPasswdOperator(String passwdOperator) {
        this.passwdOperator = passwdOperator;
    }

    @Deprecated
    public String getPasswdOperator() {
        return passwdOperator;
    }

    public String getNeedToCreateIndex() {
        return needToCreateIndex;
    }

    public void setNeedToCreateIndex(String needToCreateIndex) {
        this.needToCreateIndex = needToCreateIndex;
    }
}
