package com.xzchaoo.learn.leetcode;

import org.junit.Test;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P191 {
	//方法2
	public int hammingWeight(int n) {
		int count = 0;
		while (n != 0) {
			++count;
			n = n & (n - 1);
		}
		return count;
	}

	//方法1
	public int hammingWeight2(int n) {
		int count = 0;
		while (n != 0) {
			count += (n & 1);
			n = n >>> 1;
		}
		return count;
	}

	@Test
	public void test() {
		System.out.println(hammingWeight(7));
	}
}
