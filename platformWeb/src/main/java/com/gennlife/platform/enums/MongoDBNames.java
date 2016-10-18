package com.gennlife.platform.enums;

/**
 * Created by chen-song on 16/3/19.
 */
public enum MongoDBNames {
    CRFName("CRF_Model", 0),
    Session("ssesionMem",1);
    private String name;
    private int index;


    MongoDBNames(String name, int index) {
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
        for (MongoDBNames c : MongoDBNames.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
