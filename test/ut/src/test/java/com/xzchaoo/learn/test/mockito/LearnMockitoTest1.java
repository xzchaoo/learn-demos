package com.xzchaoo.learn.test.mockito;

import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class LearnMockitoTest1 {
  @Test
  public void test() {
    List<String> list = mock(List.class);
    when(list.size()).thenReturn(22);
    list.add("one");
    list.add("two");
    list.add("two");
    list.clear();
    assertThat(list.size()).isEqualTo(22);

    verify(list, times(1)).add(argThat(x -> x.startsWith("one")));
    verify(list, times(2)).add("two");
    //verify(list).add(anyString());
    verify(list).clear();
    verify(list, times(1)).size();
  }

  @Test
  public void test_String() {
    List<String> list = mock(List.class);
    doThrow(new RuntimeException()).when(list).clear();

    when(list.get(anyInt())).thenReturn("V");
    assertThat(list.get(7)).isEqualTo("V");
    verify(list, description("i am desc")).get(intThat(x -> x > 5));
    verify(list).get(anyInt());

    //mockito 提供了一些any之类的匹配符 但是 他们不能和常量一起用
    //如果要和常量一起用则必须用 eq 操作符

    try {
      list.clear();
      throw new AssertionFailedError();
    } catch (RuntimeException e) {
    }
  }

  @Test
  public void test_inOrder() {
    List<String> list = mock(List.class);

    list.add("a");
    list.add("b");

    //verify的时候必须严格按照list被调用的顺序
    InOrder inOrder = inOrder(list);
    inOrder.verify(list).add("a");
    inOrder.verify(list).add("b");

    //断言说没有更多的交互操作
    //verifyNoMoreInteractions(list);
  }

  @Test
  public void test_consecutiveCalls() {
    List<String> list = mock(List.class);
    when(list.size()).thenReturn(1, 2, 3);

    //如果再次对size方法进行mock 则上一句相当于就无效了
    //when(list.size()).thenReturn(4);

    assertThat(list.size()).isEqualTo(1);
    assertThat(list.size()).isEqualTo(2);
    assertThat(list.size()).isEqualTo(3);
    //超过3个以后就一直是返回最后1个了
    assertThat(list.size()).isEqualTo(3);
  }

  @Test
  public void test_thenAnswer() {
    List<String> list = mock(List.class);
    when(list.get(eq(7))).then(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        Integer index = invocation.getArgument(0);
        return index.toString();
      }
    });
    assertThat(list.get(7)).isEqualTo("7");
    //验证 list.get(anyInt()); 方法被调用了1次
    verify(list, times(1)).get(anyInt());
  }

  /**
   * 对于void方法 如果需要对它进行mock, 那么通常是要让它抛异常(不然不需要mock)
   */
  @Test(expected = IllegalStateException.class)
  public void test_mock_void() {
    List<String> list = mock(List.class);
    //clear是void方法
    doThrow(new IllegalStateException()).when(list).clear();
    list.clear();
  }
}
