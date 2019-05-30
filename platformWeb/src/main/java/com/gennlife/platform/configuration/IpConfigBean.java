package com.gennlife.platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lmx
 * @create 2019 26 17:54
 * @desc
 **/
@Component
public class IpConfigBean {
    @Value("${msg.ipAndPort: msgIpAndPort}")
    private String mcIpConfig ;

    public String getMcIpConfig() {
        return mcIpConfig;
    }

    public void setMcIpConfig(String mcIpConfig) {
        this.mcIpConfig = mcIpConfig;
    }
}
