package com.gennlife.platform.bean.list;

/**
 * Created by chensong on 2015/12/24.
 */
public class ColumnPropetity {
    private String IndexFieldName;
    private String dataType;
    private String UIFieldName;
    public ColumnPropetity(){
    }
    public ColumnPropetity(ColumnPropetity obj){
        this.dataType = obj.getDataType();
        this.IndexFieldName = obj.getIndexFieldName();
        this.UIFieldName = obj.getUIFieldName();
    }
    public String getIndexFieldName() {
        return IndexFieldName;
    }

    public void setIndexFieldName(String indexFieldName) {
        IndexFieldName = indexFieldName;
    }

    public String getUIFieldName() {
        return UIFieldName;
    }

    public void setUIFieldName(String UIFieldName) {
        this.UIFieldName = UIFieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
