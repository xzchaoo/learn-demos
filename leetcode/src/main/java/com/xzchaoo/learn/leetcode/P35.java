package com.xzchaoo.learn.leetcode;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P35 {
  public int searchInsert(int[] nums, int target) {
    //二分法寻找插入位置

    //边界处理
    if (nums.length == 0) {
      return 0;
    } else if (target < nums[0]) {
      return 0;
    } else if (target > nums[nums.length - 1]) {
      return nums.length;
    }

    //用ans保存第一个 >= target 的位置
    int left = 0;
    int right = nums.length - 1;
    int ans = -1;
    while (left <= right) {
      int mid = (left + right) >> 1;
      int value = nums[mid];
      if (value >= target) {
        ans = mid;
        right = mid - 1;
      } else {
        left = mid + 1;
      }
    }
    return ans;
  }
}
