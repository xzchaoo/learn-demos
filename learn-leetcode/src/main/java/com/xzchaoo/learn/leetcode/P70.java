package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P70 {
	public int climbStairs(int n) {
		if (n <= 2) {
			return n == 1 ? 1 : 2;
		}
		int f1 = 1;
		int f2 = 2;
		for (int i = 3; i <= n; ++i) {
			int f3 = f2 + f1;
			f1 = f2;
			f2 = f3;
		}
		return f2;
	}
}
