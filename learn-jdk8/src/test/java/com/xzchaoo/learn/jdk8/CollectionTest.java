package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author zcxu
 * @date 2018/1/4
 */
public class CollectionTest {

	//通过keySet是可以删除key的!
	@Test
	public void test_map_keySet_remove() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "b");
		map.keySet().remove("a");
		assertEquals(0, map.size());
	}
}
