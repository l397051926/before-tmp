package com.gennlife.platform.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author liumingxin
 * @create 2018 14 12:09
 * @desc 职称限制
 **/
public class Uprofession {
    private List<String> uprofession;

    public Uprofession(){
        uprofession=new LinkedList<String>();
        uprofession.add("主任医师");
        uprofession.add("副主任医师");
        uprofession.add("主治医师");
        uprofession.add("住院总医师");
        uprofession.add("住院医师");
        uprofession.add("实习医师");
        uprofession.add("其他");
    }

    public List<String> getUprofession() {
        return uprofession;
    }

    public void setUprofession(List<String> uprofession) {
        this.uprofession = uprofession;
    }
}
