package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P167 {
	public int[] twoSum(int[] numbers, int target) {
		int i = 0;
		int j = numbers.length - 1;
		while (i <= j) {
			int v = numbers[i] + numbers[j];
			int cmp = Integer.compare(v, target);
			if (cmp == 0) {
				break;
			}
			if (cmp < 0) {
				++i;
			} else {
				--j;
			}
		}
		return new int[]{i+1, j+1};
	}
}
