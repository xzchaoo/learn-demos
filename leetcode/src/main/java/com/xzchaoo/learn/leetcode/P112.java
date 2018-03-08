package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P112 {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public boolean hasPathSum(TreeNode root, int sum) {
		if (root == null) {
			return false;
		}
		if (root.left == null && root.right == null) {
			return root.val == sum;
		}
		if (root.left != null && hasPathSum(root.left, sum - root.val)) {
			return true;
		}
		if (root.right != null && hasPathSum(root.right, sum - root.val)) {
			return true;
		}
		return false;
	}
}
