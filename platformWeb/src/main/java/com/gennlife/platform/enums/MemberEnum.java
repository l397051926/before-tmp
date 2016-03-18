package com.gennlife.platform.enums;

/**
 * Created by chensong on 2015/12/12.
 */
public enum MemberEnum {
    Manager("负责人",0),
    Creater("创建者",1),
    Member("成员",2);

    private String name;
    private int index;
    MemberEnum(String name, int index){
        this.name = name;
        this.index = index;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static String getName(int index) {
        for (MemberEnum c : MemberEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
