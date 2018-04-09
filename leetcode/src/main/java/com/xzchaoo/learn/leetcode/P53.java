package com.xzchaoo.learn.leetcode;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
public class P53 {
  public int maxSubArray(int[] nums) {
    int result;
    int lastF;
    result = lastF = nums[0];
    for (int i = 1; i < nums.length; ++i) {
      int f = Math.max(lastF + nums[i], nums[i]);
      result = Math.max(result, f);
      lastF = f;
    }
    return result;
  }
}
