package com.xzchaoo.learn.spring.aop.aop1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by xzchaoo at 2017/10/31
 *
 * @author xzchaoo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Aop1Configuration.class})
public class AopTest1 {

	@Autowired
	private FooService fooService;

	@Test
	public void test1() {
		fooService.f1();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("f1 end");
	}
}
