package com.gennlife.platform.bean;

import com.gennlife.platform.enums.GennMappingEnum;

/**
 * Created by Chenjinfeng on 2017/12/15.
 */
public class GennUpResultBean {
    private GennMappingEnum status;
    private String msg;

    public GennUpResultBean(GennMappingEnum status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public GennUpResultBean(GennMappingEnum status) {
        this.status = status;
    }

    public GennMappingEnum getStatus() {
        return status;
    }

    public void setStatus(GennMappingEnum status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
