package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P20 {
  //用一个栈来做
  public boolean isValid(String s) {
    char[] stack = new char[s.length()];
    int topIndex = -1;
    for (char c : s.toCharArray()) {
      if (c == ')' || c == ']' || c == '}') {
        char rc = c == ')' ? '(' : c == ']' ? '[' : '{';
        if (topIndex == -1 || stack[topIndex--] != rc) {
          return false;
        }
      } else {
        stack[++topIndex] = c;
      }
    }
    return topIndex == -1;
  }

  public static void main(String[] args) {
    System.out.println(new P20().isValid("()[]{}"));
  }
}
