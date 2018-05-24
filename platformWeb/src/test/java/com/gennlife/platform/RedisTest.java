package com.gennlife.platform;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.util.RedisUtil;
import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liumingxin
 * @create 2018 18 14:12
 * @desc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedisTest {
    @Test
    public void isExists(){
        System.out.println("is exists ??");
        boolean b= RedisUtil.isExists("aaa");
        System.out.println(b);
    }

    @Test
    public void getReadConditionByRedisTest(){
        System.out.println("get crf_id condition");
        JsonObject jsonObject = ReadConditionByRedis.getCrfSearch("CVD");

        System.out.println("js" +jsonObject);
    }

    @Test
    public void deleteRedisKey(){
        String key = "SEARCH_CVD";
        RedisUtil.deleteKey(key);
    }

    @Test
    public void loadCrfMapping(){
        ReadConditionByRedis.loadCrfMapping();
    }

    @Test
    public void loadCrfSort(){
        ReadConditionByRedis.loadCrfHitSort();
    }


}
