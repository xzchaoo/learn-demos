package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P122 {
	public int maxProfit(int[] prices) {
		if (prices.length == 0) {
			return 0;
		}
		int s = 0;
		int min = prices[0];
		for (int i = 1; i < prices.length; ++i) {
			if (prices[i] < prices[i - 1]) {
				//出现下降
				s += prices[i - 1] - min;
				min = prices[i];
			}
			if (i == prices.length - 1) {
				s += prices[i] - min;
			}
		}
		return s;
	}
}
