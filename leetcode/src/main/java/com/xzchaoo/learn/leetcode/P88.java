package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P88 {
	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int[] c = new int[m];
		int i = 0;
		int j = 0;
		int ni = 0;
		while (i < m || j < n) {
			int v;
			if (i < m && j < n) {
				int cmp = Integer.compare(nums1[i], nums2[j]);
				if (cmp <= 0) {
					v = nums1[i];
					++i;
				} else {
					v = nums2[j];
					++j;
				}
			} else if (i < m) {
				v = nums1[i];
				++i;
			} else {
				v = nums2[j];
				++j;
			}
			if (ni < c.length) {
				c[ni++] = v;
			} else {
				nums1[ni++] = v;
			}
		}
		System.arraycopy(c, 0, nums1, 0, c.length);
	}
}
