package com.xzchaoo.learn.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P1 {
	public int[] twoSum2(int[] nums, int target) {
		//期望的时间是O(n)
		Map<Integer, Integer> numToIndex = new HashMap<>();
		for (int i = 0; i < nums.length; ++i) {
			int other = target - nums[i];
			Integer otherIndex = numToIndex.get(other);
			if (otherIndex != null) {
				return i < otherIndex ? new int[]{i, otherIndex} : new int[]{otherIndex, i};
			}
			numToIndex.put(nums[i], i);
		}
		return null;
	}


	public int[] twoSum(int[] nums, int target) {
		//O(nlogn)
		//需要建立一个结构体保存对象原本的index
		Arrays.sort(nums);
		int i = 0, j = nums.length - 1;
		boolean found = false;
		while (i < j) {
			int sum = nums[i] + nums[j];
			if (sum == target) {
				found = true;
				break;
			} else if (sum > target) {
				--j;
			} else {
				++i;
			}
		}
		if (!found) {
			throw new IllegalStateException();
		}
		return new int[]{i, j};
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(new P1().twoSum2(new int[]{3, 2, 4}, 6)));
	}
}
