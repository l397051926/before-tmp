package com.gennlife.platform.bean.etl;

import java.util.Date;

/**
 * @author lmx
 * @create 2019 21 17:20
 * @desc
 **/
public class EtlDatacount {
    private Integer id;
    private String code;
    private Integer values;
    private Date updateTime;
    private String displayName;
    private String statisticsType;
    private Integer value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getValues() {
        return values;
    }

    public void setValues(Integer values) {
        this.values = values;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
