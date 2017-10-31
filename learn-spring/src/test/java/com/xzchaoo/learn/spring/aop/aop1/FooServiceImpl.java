package com.xzchaoo.learn.spring.aop.aop1;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * created by xzchaoo at 2017/10/31
 *
 * @author xzchaoo
 */
@Service
public class FooServiceImpl implements FooService {
	@Async
	@Override
	public void f2() {
		System.out.println(Thread.currentThread() + " f2");
	}

	@Async
	@Override
	public void f1() {
		System.out.println(Thread.currentThread() +""+ getClass());
		//((FooService)AopContext.currentProxy()).f2();
		//自己调用自己是没有异步的
		f2();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("f1");
	}
}
