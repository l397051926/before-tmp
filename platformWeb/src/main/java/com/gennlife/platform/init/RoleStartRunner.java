package com.gennlife.platform.init;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.processor.LaboratoryProcessor;
import com.gennlife.platform.rocketmq.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import com.gennlife.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liumingxin
 * @create 2018 10 20:46
 * @desc
 **/
@Component
@Order(value = 1)
public class RoleStartRunner implements CommandLineRunner {
    private static LaboratoryProcessor processor = new LaboratoryProcessor();

    @Autowired
    private ProducerService producerService;


    @Override
    public void run(String... strings) throws Exception {
        System.out.println("-------------------------------创建全员角色表------------------------------------------------");
        //读 crfMAPPING
        ReadConditionByRedis.loadCrfMapping();
        List<String> keys = new ArrayList<>();
        keys.add("detail_schema");
        keys.add("etl_data_count");
        for (String key : keys){
            RedisUtil.deleteKey(key);
        }
        processor.addAllRole();
//        producerService.sendSystemStartMessage();
    }
}
