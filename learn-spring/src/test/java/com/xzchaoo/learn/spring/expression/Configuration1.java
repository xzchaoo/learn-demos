package com.xzchaoo.learn.spring.expression;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class Configuration1 {
	@Bean
	public UserService userService() {
		return new UserService();
	}
}
