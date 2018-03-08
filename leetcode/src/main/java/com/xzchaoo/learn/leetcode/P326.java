package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P326 {
	public boolean isPowerOfThree(int n) {
		//3^20==1162261467
		return n > 0 && 1162261467 % n == 0;
	}
}
