package com.xzchaoo.learn.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * created by xzchaoo at 2017/12/7
 *
 * @author xzchaoo
 */
public class CglibTest {
	@Test
	public void test4() throws Exception {
	}

	@Test
	public void test3() throws Exception {
		Enhancer e = new Enhancer();
		e.setSuperclass(FooClass1.class);
		//这个虽然通用 但是效率没其他的高
		//比如它需要创建比较多的运行时对象
		e.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
					return "haha";
				} else {
					return methodProxy.invokeSuper(o, objects);
				}
			}
		});
		FooClass1 fc = (FooClass1) e.create();
		System.out.println(fc.foo());
		System.out.println(fc.hashCode());
	}

	@Test
	public void test2() throws Exception {
		Enhancer e = new Enhancer();
		e.setSuperclass(FooClass1.class);
		e.setCallback(new InvocationHandler() {
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
					return "haha";
				} else {
					throw new RuntimeException("Do not know how to do");
				}
			}
		});
		FooClass1 fc = (FooClass1) e.create();
		System.out.println(fc.foo());
		System.out.println(fc.hashCode());
	}

	@Test
	public void test1() throws Exception {
		//https://dzone.com/articles/cglib-missing-manual
		//Enhancer用于创建子类

		//基于代理的 无法拦截 final 和 私有
		Enhancer e = new Enhancer();
		e.setSuperclass(FooClass1.class);
		e.setCallback(new FixedValue() {
			@Override
			public Object loadObject() throws Exception {
				//无论什么方法(除了一些如getClass的)都返回 haha
				//连hashCode都返回 "haha" 因此会产生一个异常
				return "haha";
			}
		});
		FooClass1 proxy = (FooClass1) e.create();
		assertEquals("haha", proxy.foo());
		System.out.println(proxy.toString());
	}
}
