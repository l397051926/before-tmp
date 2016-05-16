package com.gennlife.platform.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chensong on 2015/12/4.
 */
public class SyResource implements Serializable {
    private Integer id;

    private String name;

    private String description;

    private String url;

    private Integer syresourcetype;

    private Date createtime;

    private Date updatetime;

    private Integer syresourceid;

    private Integer seq;

    private Integer sign;

    private boolean checked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSyresourcetype() {
        return syresourcetype;
    }

    public void setSyresourcetype(Integer syresourcetype) {
        this.syresourcetype = syresourcetype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getSyresourceid() {
        return syresourceid;
    }

    public void setSyresourceid(Integer syresourceid) {
        this.syresourceid = syresourceid;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
