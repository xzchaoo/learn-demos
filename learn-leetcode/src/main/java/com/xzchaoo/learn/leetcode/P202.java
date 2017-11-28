package com.xzchaoo.learn.leetcode;

import org.junit.Test;

import java.util.Set;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P202 {
	private int getSum(int n) {
		int sum = 0;
		while (n > 0) {
			int x = n % 10;
			sum += x * x;
			n /= 10;
		}
		return sum;
	}

	public boolean isHappy(int n) {
		int fast = n;
		int slow = n;
		do {
			fast = getSum(fast);
			fast = getSum(fast);
			slow = getSum(slow);
		} while (fast != slow);
		return fast == 1;
	}

	@Test
	public void test() {
		for (int i = 1; i <= 10000; ++i) {
			System.out.println(i);
			System.out.println(isHappy(i));
		}
	}
}
