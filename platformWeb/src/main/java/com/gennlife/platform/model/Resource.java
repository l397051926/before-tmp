package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/9.
 */
public class Resource implements Comparable<Resource>{
    /**
     * `sid` varchar(50) DEFAULT NULL COMMENT '资源id',
     `sname` varchar(50) DEFAULT NULL COMMENT '资源名称',
     `sdesc` varchar(100) DEFAULT NULL COMMENT '资源描述',
     `stype` varchar(30) DEFAULT NULL COMMENT '资源类型',
     `slab_type` varchar(30) DEFAULT NULL COMMENT '科室类型',
     `slab_name` varchar(50) DEFAULT NULL COMMENT '科室名称',
     `sorgID` varchar(30) DEFAULT NULL COMMENT '组织id'
     */
    private Integer roleid;
    private String sid;
    private String sname;
    private String sdesc;
    private String stype;
    private String slab_type;
    private String slab_name;
    private String sorgID;
    private String has_search;
    private String has_searchExport;
    private String has_traceCRF;
    private String has_addCRF;
    private String has_editCRF;
    private String has_deleteCRF;
    private String has_browseDetail;
    private String has_addBatchCRF;
    private String stype_role;

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public String getHas_deleteCRF() {
        return has_deleteCRF;
    }

    public void setHas_deleteCRF(String has_deleteCRF) {
        this.has_deleteCRF = has_deleteCRF;
    }

    public String getHas_search() {
        return has_search;
    }

    public void setHas_search(String has_search) {
        this.has_search = has_search;
    }

    public String getHas_searchExport() {
        return has_searchExport;
    }

    public void setHas_searchExport(String has_searchExport) {
        this.has_searchExport = has_searchExport;
    }

    public String getHas_traceCRF() {
        return has_traceCRF;
    }

    public void setHas_traceCRF(String has_traceCRF) {
        this.has_traceCRF = has_traceCRF;
    }

    public String getHas_addCRF() {
        return has_addCRF;
    }

    public void setHas_addCRF(String has_addCRF) {
        this.has_addCRF = has_addCRF;
    }

    public String getHas_editCRF() {
        return has_editCRF;
    }

    public void setHas_editCRF(String has_editCRF) {
        this.has_editCRF = has_editCRF;
    }

    public String getHas_browseDetail() {
        return has_browseDetail;
    }

    public void setHas_browseDetail(String has_browseDetail) {
        this.has_browseDetail = has_browseDetail;
    }

    public String getHas_addBatchCRF() {
        return has_addBatchCRF;
    }

    public void setHas_addBatchCRF(String has_addBatchCRF) {
        this.has_addBatchCRF = has_addBatchCRF;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSdesc() {
        return sdesc;
    }

    public void setSdesc(String sdesc) {
        this.sdesc = sdesc;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getSlab_type() {
        return slab_type;
    }

    public void setSlab_type(String slab_type) {
        this.slab_type = slab_type;
    }

    public String getSlab_name() {
        return slab_name;
    }

    public void setSlab_name(String slab_name) {
        this.slab_name = slab_name;
    }

    public String getSorgID() {
        return sorgID;
    }

    public void setSorgID(String sorgID) {
        this.sorgID = sorgID;
    }

    public String getStype_role() {
        return stype_role;
    }

    public void setStype_role(String stype_role) {
        this.stype_role = stype_role;
    }

    @Override
    public int compareTo(Resource o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Resource{");
        sb.append("slab_name='").append(slab_name).append('\'');
        sb.append(",has_search='").append(has_search).append('\'');
        sb.append(", has_searchExport='").append(has_searchExport).append('\'');
        sb.append(", has_traceCRF='").append(has_traceCRF).append('\'');
        sb.append(", has_addCRF='").append(has_addCRF).append('\'');
        sb.append(", has_editCRF='").append(has_editCRF).append('\'');
        sb.append(", has_deleteCRF='").append(has_deleteCRF).append('\'');
        sb.append(", has_browseDetail='").append(has_browseDetail).append('\'');
        sb.append(", has_addBatchCRF='").append(has_addBatchCRF).append('\'');
        sb.append(", slab_name='").append(slab_name).append('\'');
        sb.append(", stype_role='").append(stype_role).append('\'');
        sb.append(", sorgID='").append(sorgID).append('\'');
        sb.append(", slab_type='").append(slab_type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
