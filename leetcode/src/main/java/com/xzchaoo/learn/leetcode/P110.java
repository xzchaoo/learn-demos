package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P110 {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	static class E {
		public int height;
		boolean balanced;

		public E(int height, boolean balanced) {
			this.height = height;
			this.balanced = balanced;
		}
	}

	public boolean isBalanced(TreeNode root) {
		return dfs(root).balanced;
	}

	private E dfs(TreeNode root) {
		if (root == null) {
			return new E(0, true);
		}
		E left = dfs(root.left);
		E right = dfs(root.right);
		int height = Math.max(left.height, right.height) + 1;
		boolean balanced = left.balanced && right.balanced && Math.abs(left.height - right.height) <= 1;
		return new E(height, balanced);
	}
}
