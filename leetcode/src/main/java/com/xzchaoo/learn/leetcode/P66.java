package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P66 {
	public int[] plusOne(int[] digits) {
		int[] r = new int[digits.length];
		System.arraycopy(digits, 0, r, 0, digits.length);
		int z = 1;
		for (int i = r.length - 1; i >= 0; --i) {
			int x = r[i] + z;
			r[i] = x % 10;
			z = x / 10;
			if (z == 0) {
				break;
			}
		}
		if (z > 0) {
			int[] r2 = new int[r.length + 1];
			System.arraycopy(r, 0, r2, 1, r.length);
			r2[0] = z;
			return r2;
		}
		return r;
	}
}
