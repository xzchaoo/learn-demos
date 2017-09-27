/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.guava;

import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nullable;

import static org.junit.Assert.*;

public class C2_Collections {
	@Test
	public void copyOnWiriteArrayList() {
		//这是JDK里的
		CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
	}

	@Test
	public void multiset() {
		//类似set 但是一个元素可以出现多次 可以用于计数(但效率不高)
		HashMultiset<String> hm = HashMultiset.create();
		hm.add("a");
		hm.add("a");
		hm.add("a");
		System.out.println(hm.size());
		System.out.println(hm.count("a"));
	}

	@Test
	public void bimap() {
		//具备双向map的能力
		HashBiMap<Integer, String> hbm = HashBiMap.create();
		hbm.put(1, "a");
		hbm.put(2, "b");
		BiMap<String, Integer> hbmi = hbm.inverse();
		assertEquals("a", hbm.get(1));
		assertEquals(1, hbmi.get("a").intValue());
	}

	@Test
	public void maps() {
		//建立 str.length() -> str 的映射 这里需要保证 str.length() 是唯一的
		ImmutableMap<Integer, String> map = Maps.uniqueIndex(Arrays.asList("a", "bb", "ccc"), new Function<String, Integer>() {
			@Nullable
			@Override
			public Integer apply(@Nullable String input) {
				return input.length();
			}
		});
		//map可以做交集 差集
	}

	@Test
	public void sets() {
		//IdentityHashMap
		Sets.newIdentityHashSet();

		//集合相减
		//ets.difference()

		//集合交集
		//Sets.intersection()

	}

	@Test
	public void lists() {
		//一些方法在JDK7之后就可以废弃了
		Lists.newArrayList();

		//方便构造
		Lists.newArrayList(1, 2, 3);

		Lists.newArrayListWithCapacity(2);

		Lists.newCopyOnWriteArrayList();

		List<List<Integer>> lists = Lists.partition(Lists.newArrayList(1, 2, 3, 4, 5), 2);
		//0 = 1,2
		//1 = 3,4
		//2 = 5
	}

	@Test
	public void immutableSet() {
		//通常来说 我们不可以假设一个函数返回的集合是可以修改的 除非我们非常明确地了解这个函数
		//因此通常我们会假设 一个函数的返回的集合类型是不可变的 是只读的 因此是线程安全的

		//构造不可变的set
		ImmutableSet<Integer> set = ImmutableSet.of(1, 2, 3, 4);
		//转成list
		set.asList();

		//copyOf 会尽力减少复制次数
		ImmutableSet<Integer> set1 = ImmutableSet.copyOf(set);
		assertTrue(set == set1);

		//使用这些显式的不可变集合类 意图更加明显 内部实现也比JDK的不可变集合性能更好
		//guava的集合类显式地拒绝null值

		//builder模式
		//ImmutableSet.builder().add(1).build();
	}
}
