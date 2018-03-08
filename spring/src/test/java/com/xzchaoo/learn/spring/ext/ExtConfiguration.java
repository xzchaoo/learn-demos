package com.xzchaoo.learn.spring.ext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xzchaoo
 * @date 2017/12/29
 */
@Configuration
@ComponentScan
@EnableFoo("sadf")
public class ExtConfiguration {
	@Bean
	public FooService fooService() {
		return new FooServiceImpl();
	}
}
