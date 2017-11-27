package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P38 {
	public String countAndSay(int n) {
		if (n == 1) {
			return "1";
		}
		StringBuilder osb = new StringBuilder("1");
		for (int i = 2; i <= n; ++i) {
			StringBuilder nsb = new StringBuilder();
			int count = 1;
			char last = osb.charAt(0);
			for (int j = 1; j < osb.length(); ++j) {
				char c = osb.charAt(j);
				if (c == last) {
					++count;
				} else {
					nsb.append(count).append(last);
					count = 1;
					last = c;
				}
			}
			nsb.append(count).append(last);
			osb = nsb;
		}
		return osb.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; ++i) {
			System.out.println(new P38().countAndSay(i + 1));
		}
	}
}
