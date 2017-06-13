package com.gennlife.platform.model;

/**
 * Created by chen-song on 16/9/21.
 */
public class LabResource {
    private String sid;
    private String sname;
    private String sdesc;
    private String stype;
    private String slab_type;
    private String slab_name;
    private String sorgID;
    private String slab_parent;
    private String stype_role;

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

    public String getSlab_parent() {
        return slab_parent;
    }

    public void setSlab_parent(String slab_parent) {
        this.slab_parent = slab_parent;
    }

    public String getStype_role() {
        return stype_role;
    }

    public void setStype_role(String stype_role) {
        this.stype_role = stype_role;
    }
}
