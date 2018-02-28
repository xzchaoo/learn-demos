package com.xzchaoo.learn.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoAnnotationTest {
  @InjectMocks
  private UserService userService;
  @Mock
  private UserDao userDao;
  @Captor
  private ArgumentCaptor<Integer> captor;
  //@Spy

  @Test
  public void test() {
    when(userDao.bar(captor.capture())).thenReturn("bar");
    assertThat(userService.foo()).isEqualTo("bar");
    assertThat(captor.getValue()).isEqualTo(1);
  }
}
