package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xzchaoo
 * @date 2018/1/16
 */
public class MapTest {

	/**
	 * 是一个 int->int 的map
	 */
	@Test
	public void test_IntIntMap() {
		//相当于是new一个
		MutableIntIntMap map = IntIntMaps.mutable.empty();
		map.put(1, 2);
		assertEquals(2, map.get(1));

		//不存在的会返回0
		assertEquals(0, map.get(2));

		try {
			map.getOrThrow(2);
			assertTrue(false);
		} catch (IllegalStateException e) {
		}
	}
}
