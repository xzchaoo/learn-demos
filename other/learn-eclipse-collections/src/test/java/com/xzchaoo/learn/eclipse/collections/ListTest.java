package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * created by xzchaoo at 2017/11/25
 *
 * @author xzchaoo
 */
public class ListTest {
	@Test
	public void test() {
		MutableList<Integer> list = Lists.mutable.of(1, 2, 3);


		//lazy类似于 stream, 之后可以做各种操作, 然后调用 toList 得到一个最终结果
		list.asLazy();

		MutableList<Integer> collectResult = list.collect(x -> x * x);
		assertEquals(1, collectResult.get(0).intValue());
		assertEquals(4, collectResult.get(1).intValue());
		assertEquals(9, collectResult.get(2).intValue());

		//list.collectInt() 可以转成一个int版本的实现

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
