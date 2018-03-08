package com.xzchaoo.learn.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * created by xzchaoo at 2017/11/29
 *
 * @author xzchaoo
 */
public class P219 {
	public boolean containsNearbyDuplicate(int[] nums, int k) {
		Set<Integer> set = new HashSet<>(k * 2);
		int mi = (k + 1) < nums.length ? (k + 1) : nums.length;
		for (int i = 0; i < mi; ++i) {
			if (!set.add(nums[i])) {
				return true;
			}
		}
		for (int i = k + 1; i < nums.length; ++i) {
			set.remove(nums[i - k - 1]);
			if (!set.add(nums[i])) {
				return true;
			}
		}
		return false;
	}
}
