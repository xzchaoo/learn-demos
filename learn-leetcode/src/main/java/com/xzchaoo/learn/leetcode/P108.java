package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P108 {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public TreeNode sortedArrayToBST(int[] nums) {
		if (nums.length == 0) {
			return null;
		}
		return dfs(nums, 0, nums.length - 1);
	}

	private TreeNode dfs(int[] nums, int left, int right) {
		int mid = (left + right) >> 1;
		TreeNode n = new TreeNode(nums[mid]);
		if (left < mid) {
			n.left = dfs(nums, left, mid - 1);
		}
		if (right > mid) {
			n.right = dfs(nums, mid + 1, right);
		}
		return n;
	}

	public static void main(String[] args) {
		TreeNode n = new P108().sortedArrayToBST(new int[]{3, 5, 8});
		System.out.println(n);
	}
}
