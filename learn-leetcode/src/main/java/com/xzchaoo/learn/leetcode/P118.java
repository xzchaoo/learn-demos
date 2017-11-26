package com.xzchaoo.learn.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P118 {
	public List<List<Integer>> generate(int numRows) {
		List<List<Integer>> result = new ArrayList<>(numRows);
		if (numRows > 0) {
			result.add(Collections.singletonList(1));
		}
		for (int i = 2; i <= numRows; ++i) {
			List<Integer> r0 = result.get(i - 2);
			List<Integer> r = new ArrayList<>(2 * i - 1);
			result.add(r);
			for (int j = 1; j <= i; ++j) {
				if (j == 1) {
					r.add(1);
				} else if (j == i) {
					r.add(1);
				} else {
					r.add(r0.get(j - 1) + r0.get(j - 2));
				}
			}
		}
		return result;
	}
}
