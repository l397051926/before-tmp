package com.gennlife.platform.enums;

/**
 * Created by chen-song on 16/3/19.
 */
public enum MongoCollectionNames {

    MetaName("meta", 0),
    SummaryName("summary", 1),
    CrfDataName("crfdata", 2);

    private String name;
    private int index;


    MongoCollectionNames(String name, int index) {
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
        for (MongoCollectionNames c : MongoCollectionNames.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
