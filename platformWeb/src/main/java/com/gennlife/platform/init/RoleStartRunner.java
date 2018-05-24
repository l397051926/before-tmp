package com.gennlife.platform.init;

import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.processor.LaboratoryProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author liumingxin
 * @create 2018 10 20:46
 * @desc
 **/
@Component
@Order(value = 1)
public class RoleStartRunner implements CommandLineRunner {
    private static LaboratoryProcessor processor = new LaboratoryProcessor();

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("-------------------------------创建全员角色表------------------------------------------------");
        //读 crfMAPPING
        ReadConditionByRedis.loadCrfMapping();
        processor.addAllRole();
    }
}
