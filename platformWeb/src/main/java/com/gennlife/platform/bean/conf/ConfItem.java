package com.gennlife.platform.bean.conf;

import java.util.List;

/**
 * Created by chensong on 2016/1/4.
 */
public class ConfItem {
    private String dataType;
    private String IndexFieldName;
    private String UIFieldName;
    private List<ConfGroupInfo> groupInfoList;
    private String columnStatus;

    public String getColumnStatus() {
        return columnStatus;
    }

    public void setColumnStatus(String columnStatus) {
        this.columnStatus = columnStatus;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<ConfGroupInfo> getGroupInfoList() {
        return groupInfoList;
    }

    public void setGroupInfoList(List<ConfGroupInfo> groupInfoList) {
        this.groupInfoList = groupInfoList;
    }

    public String getUIFieldName() {
        return UIFieldName;
    }

    public void setUIFieldName(String UIFieldName) {
        this.UIFieldName = UIFieldName;
    }

    public String getIndexFieldName() {
        return IndexFieldName;
    }

    public void setIndexFieldName(String indexFieldName) {
        IndexFieldName = indexFieldName;
    }
}
