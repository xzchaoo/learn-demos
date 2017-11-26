package com.xzchaoo.learn.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P3 {
	public int lengthOfLongestSubstring(String s) {
		int length = 0;
		Set<Character> set = new HashSet<>();
		int begin = 0;
		int index = 0;
		for (char c : s.toCharArray()) {
			if (set.add(c)) {
				//
			} else {
				length = Math.max(length, set.size());
				while (true) {
					char c2 = s.charAt(begin++);
					set.remove(c2);
					if (c == c2) {
						break;
					}
				}
				set.add(c);
			}
			++index;
		}
		return Math.max(set.size(), length);
	}

	public static void main(String[] args) {
		System.out.println(new P3().lengthOfLongestSubstring("dvdf"));
	}
}
