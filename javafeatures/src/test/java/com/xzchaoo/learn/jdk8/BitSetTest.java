package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.BitSet;

import static org.assertj.core.api.Assertions.*;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class BitSetTest {
  @Test
  public void test() {
    //方便做未操作
    BitSet bs = new BitSet(128);
    bs.set(1);
    assertThat(bs.get(1)).isTrue();
  }

  @Test
  public void test2() {
    System.out.println(new byte[0]);
  }
}
