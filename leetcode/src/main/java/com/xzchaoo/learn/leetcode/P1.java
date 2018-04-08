package com.xzchaoo.learn.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P1 {
  public int[] twoSum2(int[] nums, int target) {
    //期望的时间是O(n)
    Map<Integer, Integer> numToIndex = new HashMap<>();
    for (int i = 0; i < nums.length; ++i) {
      int other = target - nums[i];
      Integer otherIndex = numToIndex.get(other);
      if (otherIndex != null) {
        return i < otherIndex ? new int[]{i, otherIndex} : new int[]{otherIndex, i};
      }
      numToIndex.put(nums[i], i);
    }
    return null;
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(new P1().twoSum2(new int[]{3, 2, 4}, 6)));
  }
}
