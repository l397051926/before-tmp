package com.gennlife.platform.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by chen-song on 2016/12/8.
 */
public class Power {
    private Set<Resource> has_search = new TreeSet<>();
    private Set<Resource> has_searchExport = new TreeSet<>();
    private Set<Resource> has_traceCRF = new TreeSet<>();
    private Set<Resource> has_addCRF = new TreeSet<>();
    private Set<Resource> has_editCRF = new TreeSet<>();
    private Set<Resource> has_deleteCRF = new TreeSet<>();
    private Set<Resource> has_browseDetail = new TreeSet<>();
    private Set<Resource> has_addBatchCRF = new TreeSet<>();

    public Set<Resource> getHas_search() {
        return has_search;
    }

    public void setHas_search(Set<Resource> has_search) {
        this.has_search.clear();
        if (has_search != null) {
            for (Resource resource : has_search) {
                if (resource.getHas_search().equals("有"))
                    this.has_search.add(resource);
            }
        }

    }

    public Set<Resource> getHas_searchExport() {
        return has_searchExport;
    }

    public void setHas_searchExport(Set<Resource> has_searchExport) {
        this.has_searchExport.clear();
        if (has_searchExport != null) {
            for (Resource resource : has_searchExport) {
                if (resource.getHas_searchExport().equals("有"))
                    this.has_searchExport.add(resource);
            }
        }
    }

    public Set<Resource> getHas_traceCRF() {
        return has_traceCRF;
    }

    public void setHas_traceCRF(Set<Resource> has_traceCRF) {
        this.has_traceCRF.clear();
        if (has_traceCRF != null) {
            for (Resource resource : has_traceCRF) {
                if (resource.getHas_traceCRF().equals("有"))
                    this.has_traceCRF.add(resource);
            }
        }
    }

    public Set<Resource> getHas_addCRF() {
        return has_addCRF;
    }

    public void setHas_addCRF(Set<Resource> has_addCRF) {
        this.has_addCRF.clear();
        if (has_addCRF != null) {
            for (Resource resource : has_addCRF) {
                if (resource.getHas_addCRF().equals("有"))
                    this.has_addCRF.add(resource);
            }
        }
    }

    public Set<Resource> getHas_editCRF() {
        return has_editCRF;
    }

    public void setHas_editCRF(Set<Resource> has_editCRF) {
        this.has_editCRF.clear();
        if (has_editCRF != null) {
            for (Resource resource : has_editCRF) {
                if (resource.getHas_editCRF().equals("有"))
                    this.has_editCRF.add(resource);
            }
        }
    }

    public Set<Resource> getHas_deleteCRF() {
        return has_deleteCRF;
    }

    public void setHas_deleteCRF(Set<Resource> has_deleteCRF) {
        this.has_deleteCRF.clear();
        if (has_deleteCRF != null) {
            for (Resource resource : has_deleteCRF) {
                if (resource.getHas_deleteCRF().equals("有"))
                    this.has_deleteCRF.add(resource);
            }
        }
    }

    public Set<Resource> getHas_browseDetail() {
        return has_browseDetail;
    }

    public void setHas_browseDetail(Set<Resource> has_browseDetail) {
        this.has_browseDetail.clear();
        if (has_browseDetail != null) {
            for (Resource resource : has_browseDetail) {
                if (resource.getHas_browseDetail().equals("有"))
                    this.has_browseDetail.add(resource);
            }
        }
    }

    public Set<Resource> getHas_addBatchCRF() {
        return has_addBatchCRF;
    }

    public void setHas_addBatchCRF(Set<Resource> has_addBatchCRF) {
        this.has_addBatchCRF.clear();
        if (has_addBatchCRF != null) {
            for (Resource resource : has_addBatchCRF) {
                if (resource.getHas_addBatchCRF().equals("有"))
                    this.has_addBatchCRF.add(resource);
            }
        }
    }
}
