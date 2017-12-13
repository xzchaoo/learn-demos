package com.xzchaoo.learn.spring.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by xzchaoo at 2017/12/13
 *
 * @author xzchaoo
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringCacheApp.class)
public class SpringCacheTest {
	@Autowired
	private FooService fooService;

	@Test
	public void test() {
		//默认key: 如果没有参数  那么会用一个固定值 如果只有1个参数且非数组 那么就用它本身 否则会用一个SimpleKey的实例
		//定制缓存key的方法:
		//1. SpEL
		//2. 实现接口 手动指定Key生成器
		fooService.func1("k1");
		fooService.func1("k1");
		fooService.func1("k1");
	}
}
