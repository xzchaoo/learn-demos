package com.xzchaoo.learn.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P119 {
	public List<Integer> getRow(int rowIndex) {
		++rowIndex;
		List<Integer> r0 = Collections.singletonList(1);
		List<Integer> r = r0;
		for (int i = 2; i <= rowIndex; ++i) {
			r = new ArrayList<>(2 * i - 1);
			for (int j = 1; j <= i; ++j) {
				if (j == 1) {
					r.add(1);
				} else if (j == i) {
					r.add(1);
				} else {
					r.add(r0.get(j - 1) + r0.get(j - 2));
				}
			}
			r0 = r;
		}
		return r;
	}
}
