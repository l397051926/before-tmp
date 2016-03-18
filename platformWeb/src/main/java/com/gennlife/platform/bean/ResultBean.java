package com.gennlife.platform.bean;


/**
 * Created by chensong on 2015/12/5.
 */
public class ResultBean {
    private int code;
    private Object info;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
}
