package com.xzchaoo.learn.spring.targetsource;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zcxu
 * @date 2018/1/3
 */
public class TestTargetSource {
	@Test
	public void test() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TargetSourceConfig.class);

		FooService fs = ac.getBean(FooService.class);
		System.out.println(fs.foo());
	}
}
