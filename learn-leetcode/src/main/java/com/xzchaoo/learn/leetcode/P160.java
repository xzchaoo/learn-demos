package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P160 {
	public class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
			next = null;
		}
	}

	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		//令 c1 = [a] + null + [b] + null;
		//令 c2 = [b] + null + [a] + null;
		//如果a和b长度相同, 那么在到达第一个null之前就已经有结果了
		//如果a何b长度不同, 则c1和c2长度相同, 此时只需要比较每个对应的元素看引用是否相同即可
		ListNode a = headA;
		ListNode b = headB;
		while (a != b) {
			a = a == null ? headB : a.next;
			b = b == null ? headA : b.next;
		}
		return a;
	}
}
