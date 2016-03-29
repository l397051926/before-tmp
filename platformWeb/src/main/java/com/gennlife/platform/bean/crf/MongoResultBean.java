package com.gennlife.platform.bean.crf;

import java.util.List;

/**
 * Created by chen-song on 16/3/19.
 */
public class MongoResultBean {
    private Object _id;
    private int code;
    private String status;
    private String crf_id;
    private List<Object> children;
    public String getCrf_id() {
        return crf_id;
    }

    public void setCrf_id(String crf_id) {
        this.crf_id = crf_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

}
