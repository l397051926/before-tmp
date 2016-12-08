package com.gennlife.platform.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chen-song on 2016/12/8.
 */
public class Power {
    private List<Resource> has_search = new LinkedList<>();
    private List<Resource> has_searchExport = new LinkedList<>();
    private List<Resource> has_traceCRF = new LinkedList<>();
    private List<Resource> has_addCRF = new LinkedList<>();
    private List<Resource> has_editCRF = new LinkedList<>();
    private List<Resource> has_deleteCRF = new LinkedList<>();
    private List<Resource> has_browseDetail = new LinkedList<>();
    private List<Resource> has_addBatchCRF = new LinkedList<>();

    public List<Resource> getHas_search() {
        return has_search;
    }

    public void setHas_search(List<Resource> has_search) {
        this.has_search = has_search;
    }

    public List<Resource> getHas_searchExport() {
        return has_searchExport;
    }

    public void setHas_searchExport(List<Resource> has_searchExport) {
        this.has_searchExport = has_searchExport;
    }

    public List<Resource> getHas_traceCRF() {
        return has_traceCRF;
    }

    public void setHas_traceCRF(List<Resource> has_traceCRF) {
        this.has_traceCRF = has_traceCRF;
    }

    public List<Resource> getHas_addCRF() {
        return has_addCRF;
    }

    public void setHas_addCRF(List<Resource> has_addCRF) {
        this.has_addCRF = has_addCRF;
    }

    public List<Resource> getHas_editCRF() {
        return has_editCRF;
    }

    public void setHas_editCRF(List<Resource> has_editCRF) {
        this.has_editCRF = has_editCRF;
    }

    public List<Resource> getHas_deleteCRF() {
        return has_deleteCRF;
    }

    public void setHas_deleteCRF(List<Resource> has_deleteCRF) {
        this.has_deleteCRF = has_deleteCRF;
    }

    public List<Resource> getHas_browseDetail() {
        return has_browseDetail;
    }

    public void setHas_browseDetail(List<Resource> has_browseDetail) {
        this.has_browseDetail = has_browseDetail;
    }

    public List<Resource> getHas_addBatchCRF() {
        return has_addBatchCRF;
    }

    public void setHas_addBatchCRF(List<Resource> has_addBatchCRF) {
        this.has_addBatchCRF = has_addBatchCRF;
    }
}
