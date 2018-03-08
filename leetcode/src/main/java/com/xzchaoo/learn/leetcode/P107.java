package com.xzchaoo.learn.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P107 {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public List<List<Integer>> levelOrderBottom(TreeNode root) {
		List<List<Integer>> list = new ArrayList<>();
		dfs(root, list, 0);
		List<List<Integer>> rlist = new ArrayList<>(list.size());
		for (int i = list.size() - 1; i >= 0; --i) {
			rlist.add(list.get(i));
		}
		return rlist;
	}

	private void dfs(TreeNode root, List<List<Integer>> list, int deep) {
		if (root == null) {
			return;
		}
		if (list.size() <= deep) {
			list.add(new ArrayList<>());
		}
		list.get(deep).add(root.val);
		dfs(root.left, list, deep + 1);
		dfs(root.right, list, deep + 1);
	}
}
