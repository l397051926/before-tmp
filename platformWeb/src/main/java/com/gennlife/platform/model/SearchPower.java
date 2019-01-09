package com.gennlife.platform.model;

/**
 * @author liumingxin
 * @create 2019 08 17:35
 * @desc
 **/
public class SearchPower {
    private String sid;
    private String slab_name;
    private String has_search;

    private static final String ALL_SID = "hospital_all";
    private static final String ALL_LAB_NAME = "_all";
    private static final String POWER_HAVE = "有";

    public SearchPower() {
    }

    public SearchPower(String sid, String slab_name) {
        this.sid = sid;
        this.slab_name = slab_name;
        this.has_search = POWER_HAVE;
    }

    public SearchPower(String sid, String slab_name, String has_search) {
        this.sid = sid;
        this.slab_name = slab_name;
        this.has_search = has_search;
    }

    public static SearchPower getAllSearchPower() {
        SearchPower searchPower = new SearchPower(ALL_SID,ALL_LAB_NAME,POWER_HAVE);
        return searchPower;
    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSlab_name() {
        return slab_name;
    }

    public void setSlab_name(String slab_name) {
        this.slab_name = slab_name;
    }

    public String getHas_search() {
        return has_search;
    }

    public void setHas_search(String has_search) {
        this.has_search = has_search;
    }
}
