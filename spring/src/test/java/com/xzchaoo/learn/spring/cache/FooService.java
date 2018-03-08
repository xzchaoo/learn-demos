package com.xzchaoo.learn.spring.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * created by xzchaoo at 2017/12/13
 *
 * @author xzchaoo
 */
@Component
public class FooService {
	@Cacheable("a")
	public String func1(String key) {
		System.out.println("fun1 is called");
		return "result " + key;
	}
}
