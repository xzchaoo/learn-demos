package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P58 {
	public int lengthOfLastWord(String s) {
		int i1 = s.length() - 1;
		while (i1 >= 0 && s.charAt(i1) == ' ') {
			--i1;
		}
		if (i1 < 0) {
			return 0;
		}
		int i2 = i1;
		while (i2 >= 0 && s.charAt(i2) != ' ') {
			--i2;
		}
		return i1 - i2;
	}
}

