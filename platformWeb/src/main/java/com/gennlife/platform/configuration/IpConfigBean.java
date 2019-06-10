package com.gennlife.platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author lmx
 * @create 2019 26 17:54
 * @desc
 **/
@Component
public class IpConfigBean {
    @Value("${mcOut.ipAndPort:}")
    private String mcOutConfig;
    @Value("${msg.ipAndPort:}")
    private String mcIpConfig ;

    public String getMcOut(){
        if(StringUtils.isEmpty(mcOutConfig)){
            return mcIpConfig;
        }
        return mcOutConfig;
    }

    public String getMcIpConfig() {
        return mcIpConfig;
    }

    public void setMcIpConfig(String mcIpConfig) {
        this.mcIpConfig = mcIpConfig;
    }

    public String getMcOutConfig() {
        return mcOutConfig;
    }

    public void setMcOutConfig(String mcOutConfig) {
        this.mcOutConfig = mcOutConfig;
    }
}
