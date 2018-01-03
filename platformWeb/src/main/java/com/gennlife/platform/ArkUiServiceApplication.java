package com.gennlife.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan
@ImportResource(locations={"classpath:urlconf.xml","classpath:mybaits.xml"})
public class ArkUiServiceApplication {

	public static void main(String[] args) {
		System.setProperty("org.apache.catalina.SESSION_COOKIE_NAME", "ark-uiservice");
		SpringApplication.run(ArkUiServiceApplication.class, args);
	}
}
