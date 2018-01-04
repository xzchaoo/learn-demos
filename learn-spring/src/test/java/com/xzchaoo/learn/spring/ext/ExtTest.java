package com.xzchaoo.learn.spring.ext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xzchaoo
 * @date 2017/12/29
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ExtConfiguration.class})
public class ExtTest {
	@Autowired
	private FooService fooService;

	@Test
	public void test() {
		System.out.println(fooService.getInfo());
	}
}
