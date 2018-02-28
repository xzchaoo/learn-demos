package com.xzchaoo.learn.test.mockito;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 这里介绍 {@link Mock} 的使用
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class LearnMockitoTest4 {
  @Mock
  private List<String> list;

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Test
  public void test() {
    when(list.size()).thenReturn(2);
    assertThat(list.size()).isEqualTo(2);
  }
}
