package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P172 {
	public int trailingZeroes(int n) {
		int count = 0;
		while (n > 0) {
			n /= 5;
			count += n;
		}
		return count;
	}
}
