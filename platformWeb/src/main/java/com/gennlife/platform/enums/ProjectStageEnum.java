package com.gennlife.platform.enums;

/**
 * Created by chensong on 2015/12/12.
 */
public enum ProjectStageEnum {
    CreateStage("创建", 0),
    SampleNotImportStage("样本未录入", 1),
    SomeSampleReady("部分样本就绪", 2),
    SampleReadey("样本已就绪", 3),
    SampleAnalyze("样本分析中", 4),
    GameOver("已完结", 5);
    private String name;
    private int index;

    ProjectStageEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
