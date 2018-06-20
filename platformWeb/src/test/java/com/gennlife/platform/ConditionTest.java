package com.gennlife.platform;

import com.gennlife.platform.ReadConfig.LoadCrfCondition;
import com.gennlife.platform.service.CrfConditionService;
import com.gennlife.platform.util.FilesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author liumingxin---09
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
    @Test
    public void jsonObject(){
        JsonObject jsonObject = new JsonObject();
        System.out.println(jsonObject.entrySet().size()==0);
    }

    @Test
    public void read() throws IOException {
//        String data = FilesUtils.readCrfFile("src/main/resources/crf/lymphoma.json");
        System.out.println("Dd");
    }


}
