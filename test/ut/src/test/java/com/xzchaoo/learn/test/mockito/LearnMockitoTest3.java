package com.xzchaoo.learn.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * 这里介绍 {@link Mock} 的使用
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
@RunWith(MockitoJUnitRunner.class)
public class LearnMockitoTest3 {
  @Mock
  private List<String> list;

  @Test
  public void test() {
    when(list.size()).thenReturn(2);
    assertThat(list.size()).isEqualTo(2);
  }
}
