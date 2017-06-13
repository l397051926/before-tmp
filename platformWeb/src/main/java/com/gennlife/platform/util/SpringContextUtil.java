package com.gennlife.platform.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by chensong on 2015/12/4.
 */
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }
}
