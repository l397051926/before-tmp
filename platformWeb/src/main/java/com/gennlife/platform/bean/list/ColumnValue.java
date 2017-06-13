package com.gennlife.platform.bean.list;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chensong on 2015/12/24.
 */
public class ColumnValue {
    private String columnStatus = "1";
    private ColumnPropetity columnPropetity;


    public String getColumnStatus() {
        return columnStatus;
    }

    public void setColumnStatus(String columnStatus) {
        this.columnStatus = columnStatus;
    }

    public ColumnPropetity getColumnPropetity() {
        return columnPropetity;
    }

    public void setColumnPropetity(ColumnPropetity columnPropetity) {
        this.columnPropetity = columnPropetity;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ColumnValue columnValue = (ColumnValue) obj;
            if (this.columnPropetity != null &&
                    this.columnPropetity.getUIFieldName() != null &&
                    columnValue.columnPropetity != null &&
                    columnValue.columnPropetity.getUIFieldName() != null &&
                    this.columnPropetity.getUIFieldName().equals(columnValue.getColumnPropetity().getUIFieldName())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        ColumnValue columnValue = new ColumnValue();
        ColumnPropetity columnPropetity = new ColumnPropetity();
        columnValue.setColumnPropetity(columnPropetity);
        columnPropetity.setUIFieldName("1");
        List<ColumnValue> list = new LinkedList<ColumnValue>();
        list.add(columnValue);

        ColumnValue columnVa = new ColumnValue();
        ColumnPropetity columnP = new ColumnPropetity();
        columnVa.setColumnPropetity(columnP);
        columnP.setUIFieldName("1");
    }
}
