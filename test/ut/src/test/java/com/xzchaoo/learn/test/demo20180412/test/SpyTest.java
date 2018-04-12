package com.xzchaoo.learn.test.demo20180412.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class SpyTest {
  @Test
  public void test() {
    List<Integer> list = new ArrayList<>();
    List<Integer> spyList = spy(list);
    // error
    // when(spyList.get(1)).thenReturn(1);

    // right
    // doReturn(1).when(spyList).get(1);
  }
}
