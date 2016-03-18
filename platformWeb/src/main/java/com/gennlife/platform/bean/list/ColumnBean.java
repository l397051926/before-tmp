package com.gennlife.platform.bean.list;

import java.util.List;

/**
 * Created by chensong on 2015/12/24.
 */
public class ColumnBean {
    private String parentTitle;
    private List<ColumnValue> columnValue;

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public List<ColumnValue> getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(List<ColumnValue> columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            ColumnBean columnBean = (ColumnBean) obj;
            if(this.parentTitle != null && this.parentTitle.equals(columnBean.getParentTitle())){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
