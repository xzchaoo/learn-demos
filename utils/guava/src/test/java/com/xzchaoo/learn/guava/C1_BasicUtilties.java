/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.guava;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class C1_BasicUtilties {
	@Test
	public void test1() {
		//避免使用null
		//1. Map.get() 分不清key是否存在 需要使用 containsKey()
		//2. 使用 Optional, 它会强制你的程序对值是否存在进行检查

		//of方法的参数不可为null
		Optional<Integer> o1 = Optional.of(1);
		//fromNullable的参数可以为null
		//Optional.fromNullable()

		assertTrue(o1.isPresent());
		assertEquals(1, o1.get().intValue());
	}

	@Test(expected = IllegalStateException.class)
	public void test2() {
		Optional<Integer> o1 = Optional.absent();
		assertFalse(o1.isPresent());
		o1.get();//IllegalStateException
	}

	@Test
	public void test3() {
		Optional<Integer> o1 = Optional.absent();
		assertFalse(o1.isPresent());
		assertTrue(null == o1.orNull());
		assertTrue(1 == o1.or(1));
	}

	@Test
	public void test4() {
		//假设a,b是方法的参数
		final int a = 1;
		final int b = 2;
		final Object c = new Object();
		final List<Integer> list = Lists.newArrayList(1, 2, 3);
		//做一些前置条件的检查
		Preconditions.checkArgument(a > 0 && b > 0, "Expected a>0 && b>0");
		Preconditions.checkArgument(a < b, "Expected a < b, but got a=%d b=%d", a, b);
		Preconditions.checkNotNull(c);
		Preconditions.checkState(1 == 1);
		Preconditions.checkElementIndex(a, list.size());//size是不包含的
		Preconditions.checkPositionIndex(a, list.size());//size是包含的
	}

	@Test
	public void equals() {
		assertTrue(Objects.equal(null, null));
		assertFalse(Objects.equal("a", null));
	}

	@Test
	public void hashcode() {
		//@Deprecated
		System.out.println(Objects.hashCode(1, 2, 3));

		//use this
		java.util.Objects.hash(1, 2, 3);
	}

	@Test
	public void comparisonChain() {
		int result = ComparisonChain.start()
			.compare(20, 20)
			.compare(50, 50)
			.compare("abc", "abd")
			.result();
		assertEquals(-1, result);
	}

	@Test
	public void test6() {
		try {
			//...
		} catch (Throwable t) {
			if (t instanceof Error) {
				throw t;
			}
			if (t instanceof RuntimeException) {
				throw t;
			}
			if (t instanceof IOException) {
				throw t;
			}
			throw new RuntimeException(t);
		}
	}

	@Test
	public void test5() {
		//Throwables.getRootCause()
		try {
			//...
		} catch (Throwable t) {
			Throwables.throwIfUnchecked(t);
		}
	}
}
