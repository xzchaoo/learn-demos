package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P121 {
	public int maxProfit(int[] prices) {
		if (prices.length == 0) {
			return 0;
		}
		int min = prices[0];
		int result = 0;
		for (int i = 1; i < prices.length; ++i) {
			result = Math.max(result, prices[i] - min);
			min = Math.min(min, prices[i]);
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(new P121().maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
	}
}
