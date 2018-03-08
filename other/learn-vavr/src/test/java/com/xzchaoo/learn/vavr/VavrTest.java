package com.xzchaoo.learn.vavr;

import org.junit.Test;

import io.vavr.collection.List;

/**
 * @author xzchaoo
 * @date 2017/12/31
 */
public class VavrTest {
	@Test
	public void test() {
		List<Integer> of = List.of(1, 2, 3);
		of.append(3);
	}
}
