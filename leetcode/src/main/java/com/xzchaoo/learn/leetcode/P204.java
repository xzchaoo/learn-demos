package com.xzchaoo.learn.leetcode;

import org.junit.Test;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P204 {
	public int countPrimes(int n) {
		if (n <= 2) {
			return 0;
		}
		boolean[] f = new boolean[n];
		int mi = (int) Math.sqrt(n);
		for (int i = 3; i <= mi; i += 2) {
			if (!f[i]) {
				for (int j = i; i * j < n; j += 2) {
					f[i * j] = true;
				}
			}
		}
		int count = 1;
		for (int i = 3; i < n; i += 2) {
			if (!f[i]) {
				++count;
			}
		}
		return count;
	}

	@Test
	public void test() {
		System.out.println(countPrimes(10));
	}
}
