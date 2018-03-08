package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P141 {
	class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
			next = null;
		}

	}

	public boolean hasCycle(ListNode head) {
		//这个策略可行 但是会破坏原链表
		//网上的解法是 用2个指针, 一个每次走一步, 另一个每次走2步, 如果有循环, 那么两个指针早晚会==
		ListNode mark = new ListNode(0);
		while (head != null) {
			if (head.next == mark) {
				return true;
			}
			ListNode o = head;
			head = head.next;
			o.next = mark;
		}
		return false;
	}
}
