package com.gennlife.platform.enums;

/**
 * @author lmx
 * @create 2019 21 11:40
 * @desc
 **/
public enum  VistTypeEnum {
    outpatient(0,"门诊"),
    hospital(1,"住院"),
    physical(2,"急诊"),
    check_ups(3,"体检");

    private Integer type;
    private String name;

    VistTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static VistTypeEnum getVistTypeEnum(Integer type){
        switch (type){
            case 0:{
                return outpatient;
            }
            case 1:{
                return hospital;
            }
            case 2:{
                return physical;
            }
            case 3:{
                return check_ups;
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
