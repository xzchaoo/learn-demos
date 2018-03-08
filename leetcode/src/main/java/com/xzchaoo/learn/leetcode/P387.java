package com.xzchaoo.learn.leetcode;

/**
 * created by zcxu at 2017/11/28
 *
 * @author zcxu
 */
public class P387 {
	/**
	 * 找出第一个不重复的char, 效率尽量高, 只有小写字母
	 *
	 * @param s
	 * @return
	 */
	public int firstUniqChar(String s) {
		int[] count = new int[26];
		for (int i = 0; i < s.length(); ++i) {
			++count[s.charAt(i) - 'a'];
		}
		for (int i = 0; i < s.length(); ++i) {
			if (count[s.charAt(i) - 'a'] == 1) {
				return s.charAt(i);
			}
		}
		return -1;
	}
}
