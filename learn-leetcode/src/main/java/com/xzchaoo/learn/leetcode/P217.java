package com.xzchaoo.learn.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P217 {
	public boolean containsDuplicate(int[] nums) {
		Set<Integer> set = new HashSet<>();
		for (int n : nums) {
			if (!set.add(n)) {
				return true;
			}
		}
		return false;
	}
}
