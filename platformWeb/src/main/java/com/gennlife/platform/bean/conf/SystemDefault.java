package com.gennlife.platform.bean.conf;

/**
 * Created by Chenjinfeng on 2017/3/7.
 */
public class SystemDefault {

    private String searchItemSetDefault;
    private String needGroup;

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
}
