package com.gennlife.platform.enums;

/**
 * @author lmx
 * @create 2019 26 11:28
 * @desc
 **/
public enum  EtlStatusTypeEnum {
    ONE(1,8),
    TWO(2,15);

    private Integer type;
    private Integer val;

    EtlStatusTypeEnum(Integer type,Integer val){
        this.type = type;
        this.val = val;
    }

    public static EtlStatusTypeEnum getEtlStatusType(Integer type){
        switch (type){
            case 1 :
                return ONE;
            case 2 :
                return TWO;
            default :
                return null;
        }
    }

    public Integer getVal() {
        return val;
    }
}
