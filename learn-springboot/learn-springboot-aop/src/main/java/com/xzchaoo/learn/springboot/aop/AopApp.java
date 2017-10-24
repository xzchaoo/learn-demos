package com.xzchaoo.learn.springboot.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这种方式 通常是在spring里自己实现AOP的最好的方式, 如果要提供一个spring的整合库的话 那么可能需要用旧版的AOP?
 *
 * @author zcxu
 */
@SpringBootApplication
public class AopApp implements ApplicationRunner {
	public static void main(String[] args) {
		SpringApplication.run(AopApp.class, args);
	}

	@Autowired
	private XxxService xxxService;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		System.out.println(xxxService.save(1));
	}
}