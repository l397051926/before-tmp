package com.gennlife.platform;

import com.gennlife.platform.ReadConfig.LoadCrfCondition;
import com.gennlife.platform.service.CrfConditionService;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Map;

/**
 * @author liumingxin
 * @create 2018 17 20:26
 * @desc
 **/
public class ConditionTest {
    @Test
    public void getCrfConditionServiceTest(){
        System.out.println("test");
        Map<String,String> map = CrfConditionService.crfConditionMap;

        System.out.println("sucefull");
    }

    @Test
    public void loadCrfConditionTest(){
        System.out.println("load Crf Condition Test");
        JsonObject jsonObject = LoadCrfCondition.getCrfSearch("CVD");
        System.out.println("demo");
    }

}
