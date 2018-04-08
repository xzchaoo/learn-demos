package com.xzchaoo.learn.leetcode;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
public class P155 {
  class MinStack {
    private int[] values;
    private int[] mins;
    private int size;

    public MinStack() {
      values = new int[2];
      mins = new int[2];
    }

    private void newArray() {
      int nc = values.length * 2;
      int[] newValues = new int[nc];
      int[] newMins = new int[nc];
      System.arraycopy(values, 0, newValues, 0, values.length);
      System.arraycopy(mins, 0, newMins, 0, mins.length);
      values = newValues;
      mins = newMins;
    }

    public void push(int x) {
      if (size == values.length) {
        newArray();
      }
      values[size] = x;
      mins[size] = size == 0 ? x : x < mins[size - 1] ? x : mins[size - 1];
      ++size;
    }


    public void pop() {
      if (size > 0) {
        // check?
        --size;
      }
    }

    public int top() {
      return values[size - 1];
    }

    public int getMin() {
      return size > 0 ? mins[size - 1] : 0;
    }
  }

}
