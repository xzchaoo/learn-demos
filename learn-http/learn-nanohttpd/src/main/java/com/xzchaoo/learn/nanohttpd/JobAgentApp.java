package com.xzchaoo.learn.nanohttpd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 依赖了sigar 注意dll和so文件
 * Created by Administrator on 2017/4/25.
 */
@SpringBootApplication
@EnableConfigurationProperties({JobAgentProperties.class})
public class JobAgentApp {
	public static void main(String[] args) {
		SpringApplication.run(JobAgentApp.class, args);
	}
}