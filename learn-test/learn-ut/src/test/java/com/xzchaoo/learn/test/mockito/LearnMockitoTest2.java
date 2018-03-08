package com.xzchaoo.learn.test.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * 这里介绍 {@link org.mockito.Mock} 的使用
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class LearnMockitoTest2 {
  @Mock
  private List<String> list;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void test() {
    when(list.size()).thenReturn(2);
    assertThat(list.size()).isEqualTo(2);
  }
}
