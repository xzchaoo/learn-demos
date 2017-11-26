package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P7 {
	public int reverse(int x) {
		if (x == Integer.MIN_VALUE) {
			return 0;
		}
		int ret = 0;
		int sign = x > 0 ? 1 : x < 0 ? -1 : 0;
		x = x < 0 ? -x : x;
		while (x != 0) {
			int v = x % 10;
			x /= 10;
			int o = ret * 10 + v;
			if (o / 10 != ret) {
				return 0;
			}
			ret = o;
		}
		return ret * sign;
	}

	public static void main(String[] args) {
		//1534236469
		//1056389759
		System.out.println(new P7().reverse(1534236469));
	}
}
