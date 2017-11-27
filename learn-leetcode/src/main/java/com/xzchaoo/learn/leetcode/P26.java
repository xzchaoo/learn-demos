package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P26 {
	public int removeDuplicates(int[] nums) {
		if (nums.length == 0) {
			return 0;
		}
		int i = 0;
		int v = nums[0];
		for (int num : nums) {
			if (v != num) {
				nums[i++] = v;
				v = num;
			}
		}
		nums[i++] = v;
		return i;
	}
}
