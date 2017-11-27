package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P136 {
	public int singleNumber(int[] nums) {
		int x = 0;
		for (int n : nums) {
			x ^= n;
		}
		return x;
	}
}
