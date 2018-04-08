package com.xzchaoo.learn.leetcode;

/**
 * @author zcxu
 * @date 2018/4/3 0003
 */
public class P14 {
  public String longestCommonPrefix(String[] strs) {
    if (strs.length == 0) {
      return "";
    }
    String s0 = strs[0];
    int len = s0.length();
    for (int i = 0; i < len; ++i) {
      char c = s0.charAt(i);
      for (String s : strs) {
        if (s.length() == i || s.charAt(i) != c) {
          return s0.substring(0, i);
        }
      }
    }
    return s0;
  }
}
