package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P186 {
	public String convertToTitle(int n) {
		StringBuilder sb = new StringBuilder();
		int r = 0;
		while (n > 0) {
			int z = n % 26;
			if (z <= 0) {
				z += 26;
				r = -1;
			}
			sb.append((char) ('A' + z - 1));
			n = n / 26 + r;
		}
		return sb.reverse().toString();
	}

	public static void main(String[] args) {
		//System.out.println(new P186().convertToTitle(53));
		for (int i = 1; i <= 53; ++i) {
			System.out.println(i + " " + new P186().convertToTitle(i));
		}
	}
}