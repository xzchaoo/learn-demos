package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P9 {
	public boolean isPalindrome(int x) {
		if (x == 0) {
			return true;
		}
		if (x % 10 == 0) {
			return false;
		}
		int r = 0;
		int o = x;
		while (x > 0) {
			int n = r * 10 + x % 10;
			if (n / 10 != r) {
				return false;
			}
			r = n;
			x /= 10;
		}
		return r == o;
	}
}
