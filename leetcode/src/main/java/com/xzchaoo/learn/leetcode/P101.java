package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P101 {
	private static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	private boolean isValid(TreeNode l, TreeNode r) {
		if (l == null && r == null) {
			return true;
		}
		if (l == null || r == null || l.val != r.val) {
			return false;
		}
		return isValid(l.left, r.right) && isValid(l.right, r.left);
	}

	public boolean isSymmetric(TreeNode root) {
		return root == null || isValid(root.left, root.right);
	}
}
