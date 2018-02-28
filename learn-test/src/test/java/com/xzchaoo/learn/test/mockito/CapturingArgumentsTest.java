package com.xzchaoo.learn.test.mockito;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class CapturingArgumentsTest {
  @Test
  public void test() {
    List<String> list = mock(List.class);
    list.get(7);
    ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(int.class);
    verify(list).get(argument.capture());
    assertThat(argument.getValue()).isEqualTo(7);
    //可以用reset清空一个mock对象的状态
    //reset(list);
  }
}
