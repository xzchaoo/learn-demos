package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/28
 *
 * @author xzchaoo
 */
public class P206 {
	public class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}

	public ListNode reverseList(ListNode head) {
		ListNode tail = null;
		while (head != null) {
			ListNode n = new ListNode(head.val);
			n.next = tail;
			tail = n;
			head = head.next;
		}
		return tail;
	}
}
