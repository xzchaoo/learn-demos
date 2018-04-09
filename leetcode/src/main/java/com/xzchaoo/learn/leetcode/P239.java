package com.xzchaoo.learn.leetcode;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
public class P239 {

  private int[] result;
  private int resultIndex;
  private int size;

  public static void main(String[] args) {
    new P239().maxSlidingWindow(new int[]{9, 11}, 2);
  }

  public int[] maxSlidingWindow(int[] nums, int k) {
    size = nums.length;
    if (size == 0 || k == 1) {
      return nums;
    }
    result = new int[size - k + 1];
    for (int i = 0; i <= size - k; i += k) {
      process(nums, i, (i + k < size ? i + k : size) - 1, k);
    }
    return result;
  }

  private void process(int[] nums, int begin, int mid, int k) {
    int range = k < (size - mid) ? k : (size - mid);
    int[] f1 = cf0(nums, mid, -1, k);
    int[] f2 = cf0(nums, mid, 1, range);
    for (int i = 0; i < range; ++i) {
      result[resultIndex++] = f1[k - i - 1] > f2[i] ? f1[k - i - 1] : f2[i];
    }
  }

  private int[] cf0(int[] nums, int begin, int step, int size2) {
    int[] f = new int[size2];
    f[0] = nums[begin];
    for (int i = 1; i < f.length; ++i) {
      begin += step;
      f[i] = f[i - 1] > nums[begin] ? f[i - 1] : nums[begin];
    }
    return f;
  }
}
