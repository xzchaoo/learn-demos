package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P83 {
	public ListNode deleteDuplicates(ListNode li) {
		ListNode head = null;
		ListNode tail = null;
		while (li != null) {
			if (tail == null) {
				head = tail = new ListNode(li.val);
			} else if (tail.val != li.val) {
				tail.next = new ListNode(li.val);
				tail = tail.next;
			}
			li=li.next;
		}
		return head;
	}
}
