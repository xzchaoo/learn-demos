package com.xzchaoo.learn.leetcode;

import org.junit.Test;

/**
 * created by zcxu at 2017/11/28
 *
 * @author zcxu
 */
public class P374 {
	private int guess(int num) {
		return Integer.compare(6, num);
	}

	public int guessNumber(int n) {
		long left = 1;
		long right = n;
		while (left <= right) {
			int mid = (int) ((left + right) >> 1);
			int g = guess(mid);
			if (g == 0) {
				return mid;
			} else if (g > 0) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		throw new IllegalStateException();
	}

	@Test
	public void test() {
		System.out.println(guessNumber(10));
	}
}
