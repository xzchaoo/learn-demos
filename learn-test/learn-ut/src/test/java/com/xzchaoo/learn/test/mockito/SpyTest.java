package com.xzchaoo.learn.test.mockito;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * spy可以对一个现有对象进行mock 它可以mock某些方法 而对于另外一些方法则允许调用打穿到原始方法
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class SpyTest {
  @Test
  public void test() {
    //使用val的话 推断出来的list类型其实是 ArrayList<String>
    //这会对后续的一些类型推断产生影响 必要的时刻还是不用val 自己写 List<String> 好吧...
    val list = new ArrayList<String>();
    list.add("0");
    list.add("1");
    list.add("2");
    list.add("3");
    List<String> spyList = spy(list);

    //Impossible: 不能这么调用 会导致索引溢出
    //when(spyList.get(4)).thenReturn("4");

    //下面这么做才是对的
    doReturn("4").when(spyList).get(4);

    //注意这里是对 spyList 进行断言 而不是 list, list对象还是原始的 ArrayList 的实例 跟 mock 没关系
    assertThat(spyList.get(0)).isEqualTo("0");
    assertThat(spyList.get(1)).isEqualTo("1");
    assertThat(spyList.get(2)).isEqualTo("2");
    assertThat(spyList.get(3)).isEqualTo("3");
    assertThat(spyList.get(4)).isEqualTo("4");
  }
}
