package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P35 {
	public int searchInsert(int[] nums, int target) {
		if (nums.length > 0) {
			if (target < nums[0]) {
				return 0;
			} else if (target > nums[nums.length - 1]) {
				return nums.length;
			}
		}
		int left = 0;
		int right = nums.length - 1;
		int ans = -1;
		while (left <= right) {
			int mid = (left + right) >> 1;
			int value = nums[mid];
			if (value >= target) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return ans;
	}
}
