package com.xzchaoo.learn.leetcode;

import org.junit.Test;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P125 {
	public boolean isPalindrome2(String s) {
		int i = 0;
		int j = s.length() - 1;
		while (i < j) {

			char c1 = Character.toLowerCase(s.charAt(i));
			while (i < j && !Character.isAlphabetic(c1) && !Character.isDigit(c1)) {
				++i;
				c1 = Character.toLowerCase(s.charAt(i));
			}

			char c2 = Character.toLowerCase(s.charAt(j));
			while (i < j && !Character.isAlphabetic(c2) && !Character.isDigit(c2)) {
				--j;
				c2 = Character.toLowerCase(s.charAt(j));
			}
			if (i > j || c1 != c2) {
				return false;
			}
			++i;
			--j;
		}
		return true;
	}

	public boolean isPalindrome(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (Character.isAlphabetic(c) || Character.isDigit(c)) {
				sb.append(Character.toLowerCase(c));
			}
		}
		int len = sb.length() / 2;
		for (int i = 0; i < len; ++i) {
			if (sb.charAt(i) != sb.charAt(sb.length() - 1 - i)) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void test1() {
		System.out.println(isPalindrome2("Live on evasions? No, I save no evil."));
	}
}
