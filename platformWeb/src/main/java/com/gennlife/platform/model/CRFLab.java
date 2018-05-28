package com.gennlife.platform.model;

/**
 * Created by chen-song on 2016/10/10.
 */
public class CRFLab {
    private String labID;
    private String crf_id;
    private String crfName;
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

    public String getCrfName() {
        return crfName;
    }

    public void setCrfName(String crfName) {
        this.crfName = crfName;
    }
}
