package com.xzchaoo.learn.jdk8.reference;

import org.junit.Test;

import java.lang.ref.WeakReference;

import static org.junit.Assert.*;

/**
 * @author xzchaoo
 * @date 2017/12/30
 */
public class ReferenceTest {
	@Test
	public void test() {
		//默认对象应用是强引用关系
		//一旦一个对象存在强引用关系, 那么他就不会被回收, 当然, GC应该是可以检测环形依赖的
		WeakReference<Object> w = new WeakReference<>(new Object());
//		assertNotNull(w.get());
//		System.gc();
//		assertNotNull(w.get());
	}
}
