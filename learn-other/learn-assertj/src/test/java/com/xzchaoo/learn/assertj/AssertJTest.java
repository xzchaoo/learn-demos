package com.xzchaoo.learn.assertj;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/1/1
 */
public class AssertJTest {
	@Test
	public void test() {
		String name = "xzc";
		assertThat(name)
			.hasSize(3)
			.contains("zc")
			.doesNotContain("F")
			.startsWith("x")
			.endsWith("c")
			.isEqualTo("xzc")
			.isEqualToIgnoringCase("XZC");
	}
}
