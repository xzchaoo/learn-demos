package com.xzchaoo.learn.test.demo20180413.mockito;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class MockitoBasicTest {

  @Test
  public void test_mock() {
    List<Integer> list = mock(List.class);
    when(list.get(0)).thenReturn(1);
    when(list.get(1)).thenReturn(2);
    when(list.size()).thenReturn(2);

    // 除了直接mock返回值 通过一个方法来生成返回值
    // 对于所有 x>5 的请求 list.get(x) 返回 x*x
    when(list.get(intThat(x -> x > 5))).thenAnswer(invocation -> {
      Integer index =

        invocation.getArgument(0);
      return index * index;
    });

    assertThat(list.size()).isEqualTo(2);
    assertThat(list.get(0)).isEqualTo(1);
    assertThat(list.get(1)).isEqualTo(2);
    assertThat(list.get(6)).isEqualTo(36);
    assertThat(list.get(7)).isEqualTo(49);
  }

  /**
   * spy方法用于增强一个已有的对象, 使得它可以被mock, 没有被mock的方法会"打穿"到原来的对象上
   */
  @Test
  public void test_spy_1() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    List<Integer> spiedList = spy(list);

    // right 为了支持spy对象和void方法, mockito 也支持下面的mock方式
    doReturn(2).when(spiedList).get(1);

    // 这是错误的做法 当调用 spiedList.get(1) 时, 该方法没有被mock, 因此会触发原有对象的方法, 导致范围溢出
    // when(spiedList.get(1)).thenReturn(2);

    // 打穿到原来的对象上
    assertThat(spiedList.size()).isEqualTo(1);
    assertThat(spiedList.get(0)).isEqualTo(1);

    // 这是mock方法
    assertThat(spiedList.get(1)).isEqualTo(2);
  }

  @Test
  public void test_spy_2() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    List<Integer> spiedList = spy(list);

    doReturn(2).when(spiedList).get(anyInt());

    assertThat(spiedList.size()).isEqualTo(1);
    assertThat(spiedList.get(0)).isEqualTo(2);
    assertThat(spiedList.get(1)).isEqualTo(2);
  }
}
