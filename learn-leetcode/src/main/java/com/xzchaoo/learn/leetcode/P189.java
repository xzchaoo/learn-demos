package com.xzchaoo.learn.leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P189 {
	public void rotate(int[] nums, int k) {
		int n = nums.length;
		boolean[] processed = new boolean[nums.length];
		for (int e = 0; e < nums.length; ++e) {
			if (processed[e]) {
				continue;
			}
			int i = e;
			int t = nums[e];
			while (true) {
				processed[i] = true;
				int ii = (i + k) % n;
				int tt = nums[ii];
				nums[ii] = t;
				i = ii;
				t = tt;
				if (i == e) {
					break;
				}
			}
		}
	}

	@Test
	public void test() {
		int[] a = {1, 2, 3, 4, 5, 6};
		rotate(a, 2);
		System.out.println(Arrays.toString(a));
	}
}
