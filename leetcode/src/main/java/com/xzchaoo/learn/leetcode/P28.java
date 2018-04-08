package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P28 {
  public int strStr(String haystack, String needle) {
    int len1 = haystack.length();
    int len2 = needle.length();
    if (len2 > len1) {
      return -1;
    }
    int maxIndex = len1 - len2;
    for (int i = 0; i <= maxIndex; ++i) {
      boolean valid = true;
      for (int j = 0; j < len2; ++j) {
        if (haystack.charAt(i + j) != needle.charAt(j)) {
          valid = false;
          break;
        }
      }
      if (valid) {
        return i;
      }
    }
    return -1;
  }

  public static void main(String[] args) {
    System.out.println(new P28().strStr("hello", "a"));
  }
}
