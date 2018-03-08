package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P27 {
	public int removeElement(int[] nums, int val) {
		int i = 0;
		for (int num : nums) {
			if (num != val) {
				nums[i++] = num;
			}
		}
		return i;
	}
}
