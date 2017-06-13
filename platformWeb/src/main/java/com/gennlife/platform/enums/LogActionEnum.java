package com.gennlife.platform.enums;

/**
 * Created by chensong on 2015/12/12.
 */
public enum LogActionEnum {
    CreateProject("创建项目", 0),
    CreatePlan("创建方案", 1),
    ImportSamples("导入样本集", 2),
    DeleteSamples("删除样本集", 3),
    StoreSat("存储数据集合", 4),
    DeletePlan("删除方案", 5),
    UpdateProject("更新项目信息", 6),
    ExitProject("退出项目", 7),
    DeleteProjectMember("移除项目成员", 8),
    AddProjectMember("添加项目成员", 9),
    UpdateSetInfo("更新样本集信息", 10);
    private String name;
    private int index;


    LogActionEnum(String name, int index) {
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
        for (LogActionEnum c : LogActionEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

}
