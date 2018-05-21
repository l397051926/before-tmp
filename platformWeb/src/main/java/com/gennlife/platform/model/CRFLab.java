package com.gennlife.platform.model;

/**
 * Created by chen-song on 2016/10/10.
 */
public class CRFLab {
    private String labID;
    private String crf_id;
    private String crf_name;
    private String index_name;

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public String getLabID() {
        return labID;
    }

    public void setLabID(String labID) {
        this.labID = labID;
    }

    public String getCrf_id() {
        return crf_id;
    }

    public void setCrf_id(String crf_id) {
        this.crf_id = crf_id;
    }

    public String getCrf_name() {
        return crf_name;
    }

    public void setCrf_name(String crf_name) {
        this.crf_name = crf_name;
    }
}
