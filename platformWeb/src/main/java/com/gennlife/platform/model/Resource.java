package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/9.
 */
public class Resource {
    /**
     * `sid` varchar(50) DEFAULT NULL COMMENT '资源id',
     `sname` varchar(50) DEFAULT NULL COMMENT '资源名称',
     `sdesc` varchar(100) DEFAULT NULL COMMENT '资源描述',
     `stype` varchar(30) DEFAULT NULL COMMENT '资源类型',
     `slab_type` varchar(30) DEFAULT NULL COMMENT '科室类型',
     `slab_name` varchar(50) DEFAULT NULL COMMENT '科室名称',
     `sorgID` varchar(30) DEFAULT NULL COMMENT '组织id'
     */

    private String sid;
    private String sname;
    private String sdesc;
    private String stype;
    private String slab_type;
    private String slab_name;
    private String sorgID;
    private String has_add;
    private String has_read;
    private String has_modify;
    private String has_delete;

    public String getHas_add() {
        return has_add;
    }

    public void setHas_add(String has_add) {
        this.has_add = has_add;
    }

    public String getHas_read() {
        return has_read;
    }

    public void setHas_read(String has_read) {
        this.has_read = has_read;
    }

    public String getHas_modify() {
        return has_modify;
    }

    public void setHas_modify(String has_modify) {
        this.has_modify = has_modify;
    }

    public String getHas_delete() {
        return has_delete;
    }

    public void setHas_delete(String has_delete) {
        this.has_delete = has_delete;
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
}
