package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P67 {
	public String addBinary(String a, String b) {
		StringBuilder sb = new StringBuilder();
		int i = a.length() - 1;
		int j = b.length() - 1;
		boolean z = false;
		while (i >= 0 || j >= 0 || z) {
			int x = 0;
			if (i >= 0) {
				x += a.charAt(i) == '1' ? 1 : 0;
				--i;
			}
			if (j >= 0) {
				x += b.charAt(j) == '1' ? 1 : 0;
				--j;
			}
			x += z ? 1 : 0;
			if (x > 1) {
				x -= 2;
				z = true;
			} else {
				z = false;
			}
			sb.append(x);
		}
		return sb.reverse().toString();
	}
}
