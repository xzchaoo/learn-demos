package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */


public class P2 {
	static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}

	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode head = null;
		ListNode tail = null;

		int remain = 0;
		while (l1 != null || l2 != null || remain > 0) {
			int value = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + remain;
			ListNode n = new ListNode(value % 10);
			if (tail == null) {
				head = tail = n;
			} else {
				tail.next = n;
				tail = n;
			}
			remain = value / 10;
			l1 = l1 == null ? null : l1.next;
			l2 = l2 == null ? null : l2.next;
		}
		return head;
	}
}
