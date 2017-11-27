package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P21 {
	private static class ListNode {
		int val;
		com.xzchaoo.learn.leetcode.P21.ListNode next;

		ListNode(int x) {
			val = x;
		}
	}

	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		ListNode head = null;
		ListNode tail = null;
		while (l1 != null || l2 != null) {
			ListNode n;
			if (l1 == null) {
				n = new ListNode(l2.val);
				l2 = l2.next;
			} else if (l2 == null) {
				n = new ListNode(l1.val);
				l1 = l1.next;
			} else {
				int cmp = Integer.compare(l1.val, l2.val);
				if (cmp <= 0) {
					n = new ListNode(l1.val);
					l1 = l1.next;
				} else {
					n = new ListNode(l2.val);
					l2 = l2.next;
				}
			}
			if (tail == null) {
				head = tail = n;
			} else {
				tail.next = n;
				tail = n;
			}
		}
		return head;
	}
}
