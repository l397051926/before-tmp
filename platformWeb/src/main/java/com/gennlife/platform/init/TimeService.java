package com.gennlife.platform.init;

import com.gennlife.platform.dao.AllDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lmx
 * @create 2019 08 22:13
 * @desc
 **/
@Component
@Configuration
@EnableScheduling
public class TimeService {
    private Logger LOG = LoggerFactory.getLogger(TimeService.class);


//    /**
//     * 测试线程
//     */
//    @Scheduled(cron = "0/3 * * * * ?")
//    public void reSubmitTask(){
//        AllDao.getInstance().getOrgDao().seleteA();
//        LOG.info("------执行了 稳定查询数据库 每 5秒");
//    }
}
