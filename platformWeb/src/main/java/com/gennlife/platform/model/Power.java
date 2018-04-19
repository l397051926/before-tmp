package com.gennlife.platform.model;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by chen-song on 2016/12/8.
 */
public class Power {
    private static Comparator<Resource> comparator = new Comparator<Resource>() {
        @Override
        public int compare(Resource o1, Resource o2) {
            return o1.getSlab_name().compareTo(o2.getSlab_name());
        }
    };
    private Set<Resource> has_search = new TreeSet<>(comparator);
    private Set<Resource> has_searchExport = new TreeSet<>(comparator);
    private Set<Resource> has_traceCRF = new TreeSet<>(comparator);
    private Set<Resource> has_addCRF = new TreeSet<>(comparator);
    private Set<Resource> has_editCRF = new TreeSet<>(comparator);
    private Set<Resource> has_deleteCRF = new TreeSet<>(comparator);
    private Set<Resource> has_browseDetail = new TreeSet<>(comparator);
    private Set<Resource> has_addBatchCRF = new TreeSet<>(comparator);

    public Set<Resource> getHas_search() {
        return has_search;
    }

    public void setHas_search(Collection<Resource> has_search) {
        this.has_search.clear();
        if (has_search != null) {
            for (Resource resource : has_search) {
                if ( resource.getHas_search().equals("有"))
                    addInHasSearch(resource);
            }
        }

    }

    public void addInHasSearch(Resource resource) {
        this.has_search.add(resource.ResourcePowerleftOne("has_search"));
    }

    public Set<Resource> getHas_searchExport() {
        return has_searchExport;
    }

    public void setHas_searchExport(Collection<Resource> has_searchExport) {
        this.has_searchExport.clear();
        if (has_searchExport != null) {
            for (Resource resource : has_searchExport) {
                if ( resource.getHas_searchExport().equals("有"))
                    addInHasSearchExport(resource);
            }
        }
    }

    public void addInHasSearchExport(Resource resource) {
        this.has_searchExport.add(resource.ResourcePowerleftOne("has_searchExport"));
    }

    public Set<Resource> getHas_traceCRF() {
        return has_traceCRF;
    }

    public void setHas_traceCRF(Collection<Resource> has_traceCRF) {
        this.has_traceCRF.clear();
        if (has_traceCRF != null) {
            for (Resource resource : has_traceCRF) {
                if (resource.getHas_traceCRF().equals("有"))
                    addInHasTraceCRF(resource);
            }
        }
    }

    public void addInHasTraceCRF(Resource resource) {
        this.has_traceCRF.add(resource.ResourcePowerleftOne("has_traceCRF"));
    }

    public Set<Resource> getHas_addCRF() {
        return has_addCRF;
    }

    public void setHas_addCRF(Collection<Resource> has_addCRF) {
        this.has_addCRF.clear();
        if (has_addCRF != null) {
            for (Resource resource : has_addCRF) {
                if (resource.getHas_addCRF().equals("有"))
                    addInHasAddCRF(resource);
            }
        }
    }

    public void addInHasAddCRF(Resource resource) {
        this.has_addCRF.add(resource.ResourcePowerleftOne("has_addCRF"));
    }

    public Set<Resource> getHas_editCRF() {
        return has_editCRF;
    }

    public void setHas_editCRF(Collection<Resource> has_editCRF) {
        this.has_editCRF.clear();
        if (has_editCRF != null) {
            for (Resource resource : has_editCRF) {
                if (resource.getHas_editCRF().equals("有"))
                    addInHasEditCRF(resource);
            }
        }
    }

    public void addInHasEditCRF(Resource resource) {
        this.has_editCRF.add(resource.ResourcePowerleftOne("has_editCRF"));
    }

    public Set<Resource> getHas_deleteCRF() {
        return has_deleteCRF;
    }

    public void setHas_deleteCRF(Collection<Resource> has_deleteCRF) {
        this.has_deleteCRF.clear();
        if (has_deleteCRF != null) {
            for (Resource resource : has_deleteCRF) {
                if (resource.getHas_deleteCRF().equals("有"))
                    addInHasDeleteCRF(resource);
            }
        }
    }

    public void addInHasDeleteCRF(Resource resource) {
        this.has_deleteCRF.add(resource.ResourcePowerleftOne("has_deleteCRF"));
    }

    public Set<Resource> getHas_browseDetail() {
        return has_browseDetail;
    }

    public void setHas_browseDetail(Collection<Resource> has_browseDetail) {
        this.has_browseDetail.clear();
        if (has_browseDetail != null) {
            for (Resource resource : has_browseDetail) {
                if (resource.getHas_browseDetail().equals("有"))
                    addInHasBrowseDetail(resource);
            }
        }
    }

    public Set<Resource> getHas_addBatchCRF() {
        return has_addBatchCRF;
    }

    public void setHas_addBatchCRF(Collection<Resource> has_addBatchCRF) {
        this.has_addBatchCRF.clear();
        if (has_addBatchCRF != null) {
            for (Resource resource : has_addBatchCRF) {
                if (resource.getHas_addBatchCRF().equals("有"))
                    addInHasAddBatchCRF(resource);
            }
        }
    }

    public void addInHasAddBatchCRF(Resource resource) {
        this.has_addBatchCRF.add(resource.ResourcePowerleftOne("has_addBatchCRF"));
    }

    public Power deepCopy() {
        Power power = new Power();
        power.setHas_addBatchCRF(has_addBatchCRF);
        power.setHas_browseDetail(has_browseDetail);
        power.setHas_addCRF(has_addCRF);
        power.setHas_editCRF(has_editCRF);
        power.setHas_search(has_search);
        power.setHas_searchExport(has_searchExport);
        power.setHas_deleteCRF(has_deleteCRF);
        power.setHas_traceCRF(has_traceCRF);
        return power;
    }

    public void addInHasBrowseDetail(Resource resource) {
        this.has_browseDetail.add(resource.ResourcePowerleftOne("has_browseDetail"));
    }
}
