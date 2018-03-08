package com.xzchaoo.learn.springboot.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * target指的是被增强的对象 proxy指的是spring为target创建的一个代理对象, 它会代替target被注入到其他bean里
 * 也就是说我们平时拿到的对象其实都是proxy
 * 织入:指的是将切面和target对象链接到一起的过程, 可以发生在运行时(Spring, 通过代理), 编译时(AspectJ, 通过特殊的编译器), 加载时
 * 有好几种advice, 最强的是Around
 *
 * @author zcxu
 */
@Service
public class XxxServiceImpl implements XxxService {
	public XxxServiceImpl() {
		System.out.println("构造函数 " + this);
	}

	//为了调用自身的 protected 方法, 可以使用注入自己 或 AopContext.currentProxy() 获得当前的proxy
	//如果bean具有继承关系, 那么只能使用AopContext.currentProxy()了 否则不知道当前bean是谁
	@Autowired
	private XxxServiceImpl self;

	@Timed(scenario = "XxxService", action = "innerMethod")
	protected void innerMethod() {
		System.out.println("i am innerMethod");
	}

	@Timed(scenario = "flight-lowpricebot-mongodb-write", action = "mongodbWrite")
	@Override
	public int save(int in) {
		System.out.println(this);
		self.innerMethod();
		return in + 1;
	}
}
