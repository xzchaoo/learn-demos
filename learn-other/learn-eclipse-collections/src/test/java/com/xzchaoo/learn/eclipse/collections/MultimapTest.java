package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.Test;

/**
 * value是多个值的map
 *
 * @author xzchaoo
 * @date 2018/1/16
 */
public class MultimapTest {
	@Test
	public void test() {
		//list可以通过 groupBy groupByEach 得到一个 MutableListMultimap

		MutableListMultimap<String, String> m = Multimaps.mutable.list.empty();
		m.put("a", "b");
		m.put("a", "c");
		MutableList<String> result = m.get("a");
		System.out.println(result);

		MutableSetMultimap<String, String> m2 = Multimaps.mutable.set.empty();
		m2.put("a", "b");
		m2.put("a", "c");
		//基于set的实现才有这个函数
		System.out.println(m2.containsKeyAndValue("a", "b"));
	}
}
