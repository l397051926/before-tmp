package com.gennlife.platform.enums;

/**
 * @author lmx
 * @create 2019 21 11:40
 * @desc
 **/
public enum  VistTypeEnum {
    outpatient(1,"门诊"),
    hospital(2,"住院"),
    physical(3,"体检");

    private Integer type;
    private String name;

    VistTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static VistTypeEnum getVistTypeEnum(Integer type){
        switch (type){
            case 1:{
                return outpatient;
            }
            case 2:{
                return hospital;
            }
            case 3:{
                return physical;
            }
            default: return null;
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
