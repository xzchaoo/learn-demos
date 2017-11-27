package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P69 {
	public int sqrt1(int x) {
		if (x <= 1) {
			return x;
		}
		int i = 1;
		for (; i < x; ++i) {
			if (i * i > x) {
				break;
			}
		}
		return i - 1;
	}

	public int sqrt2(int x) {
		if (x <= 1) {
			return x;
		}
		int ans = 0;
		int left = 0;
		int right = x;
		while (left <= right) {
			int m = (left + right) >> 1;
			int y = m * m;
			if (y / m != m || y > x) {
				right = m - 1;
			} else {
				ans = m;
				left = m + 1;
			}
		}
		return ans;
	}

	public int mySqrt(int x) {
		if (x <= 1) {
			return x;
		}
		long xx = x;
		long ans = 0;
		long left = 0;
		long right = x;
		while (left <= right) {
			long m = (left + right) >> 1;
			long y = m * m;
			if (y <= xx) {
				ans = m;
				left = m + 1;
			} else {
				right = m - 1;
			}
		}
		return (int) ans;
	}
}
