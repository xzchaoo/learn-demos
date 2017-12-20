package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

/**
 * created by xzchaoo at 2017/11/25
 *
 * @author xzchaoo
 */
public class ListTest {
	@Test
	public void test() {
		MutableList<Integer> list = Lists.mutable.of(1, 2, 3);
		//select满足条件的元素为一个新的list
		MutableList<Integer> result = list.select(x -> x % 2 == 0);
		//select不满足条件的元素为一个新的list
		list.reject(x -> x % 2 == 0);
		//select和reject的集合版
		list.partition(x -> x % 2 == 0);

		MutableListMultimap<Integer, Integer> m2 = list.groupBy(x -> x.toString().length());

		//相当于是map
		//MutableList<String> s = list.collect(Object::toString);
		//Collectors2 c;
		//查看是否有元素满足条件
		//list.anySatisfy()
		//list.allSatisfy()
		//list.noneSatisfy()

		System.out.println(result);
	}
}
