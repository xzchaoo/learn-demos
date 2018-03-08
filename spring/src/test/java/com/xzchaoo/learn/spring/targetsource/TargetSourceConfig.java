package com.xzchaoo.learn.spring.targetsource;

import org.springframework.aop.TargetSource;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zcxu
 * @date 2018/1/3
 */
@Configuration
public class TargetSourceConfig {
	@Bean
	public TargetSource fooService() {
		return new HotSwappableTargetSource(new FooServiceImpl("default"));
	}
}
