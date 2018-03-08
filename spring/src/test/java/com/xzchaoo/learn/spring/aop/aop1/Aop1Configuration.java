package com.xzchaoo.learn.spring.aop.aop1;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * created by xzchaoo at 2017/10/31
 *
 * @author xzchaoo
 */
@Configuration
@ComponentScan
@EnableAsync(proxyTargetClass = true,mode = AdviceMode.ASPECTJ)
public class Aop1Configuration {
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(4);
		te.setMaxPoolSize(4);
		return te;
	}
}
