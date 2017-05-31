package com.gennlife.platform.bean.conf;

/**
 * Created by Chenjinfeng on 2017/3/7.
 */
public class SystemDefault {

    private String searchItemSetDefault;
    private String needGroup;
    private String passwdOperator;
    private String needToCreateIndex = "false";
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

    public void setPasswdOperator(String passwdOperator) {
        this.passwdOperator = passwdOperator;
    }

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
