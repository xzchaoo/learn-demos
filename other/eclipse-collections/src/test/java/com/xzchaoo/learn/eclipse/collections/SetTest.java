package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.set.FixedSizeSet;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * created by xzchaoo at 2017/11/25
 *
 * @author xzchaoo
 */
public class SetTest {
  @Test
  public void test_singleton() {
    //大小fixed不可改变 因此不能删除和添加
    FixedSizeSet<Integer> set = Sets.fixedSize.with(1);
    assertThat(set.size()).isEqualTo(1);

    //这两个有什么区别
    ImmutableSet<Integer> set2 = Sets.immutable.of(1);
    assertThat(set2.size()).isEqualTo(1);
  }

  @Test
  public void test() {
    ImmutableSet<Integer> s = Sets.immutable.of(1, 2, 3);
    s.castToSet();
  }
}
