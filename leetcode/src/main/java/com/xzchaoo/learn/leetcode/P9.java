package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P9 {
  public boolean isPalindrome(int x) {
    if (x == 0) {
      return true;
    }
    //颠倒之后要去掉前导0
    if (x % 10 == 0) {
      return false;
    }
    int r = 0;
    //记下原始x
    int o = x;
    while (x > 0) {
      int n = r * 10 + x % 10;
      if (n / 10 != r) {
        //说明发生了溢出 因为x肯定在int范围里 所以结果肯定是false
        return false;
      }
      r = n;
      x /= 10;
    }
    return r == o;
  }
}
