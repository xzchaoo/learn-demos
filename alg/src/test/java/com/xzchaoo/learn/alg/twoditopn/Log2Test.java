package com.xzchaoo.learn.alg.twoditopn;

import org.junit.Test;

/**
 * @author xzchaoo
 * @date 2018/5/24
 */
public class Log2Test {
  public static int getPrefixZeroCount(int x) {
    if (x == 0) {
      return 32;
    }
    int c = 0;
    if ((x >>> 16) == 0) {
      c += 16;
      x <<= 16;
    }
    if ((x >>> 24) == 0) {
      c += 8;
      x <<= 8;
    }
    if ((x >>> 28) == 0) {
      c += 4;
      x <<= 4;
    }
    if ((x >>> 30) == 0) {
      c += 2;
      x <<= 2;
    }
    System.out.println(x);
    if ((x >>> 31) == 0) {
      c += 1;
      x <<= 1;
    }
    if ((x & 0x80000000) == 0) {
      c += 1;
    }
    return c;
  }

  @Test
  public void test() {
    System.out.println(getPrefixZeroCount(Integer.MAX_VALUE));
  }
}
