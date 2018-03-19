package com.xzchaoo.learn.test.powermock.preparetest;

import com.xzchaoo.learn.test.UserDaoImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * A调用了B的静态方法, B的final方法需要被mock, 则B需要被prepare
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
@PrepareForTest({UserDaoImpl.class})
@RunWith(PowerMockRunner.class)
public class PrepareTest3 {
  @InjectMocks
  PrepareService service;
  @Mock
  UserDaoImpl dao;

  @Test
  public void test() throws Exception {
    when(dao.f2()).thenReturn("xaa");
    assertThat(service.callFinalMethod()).isEqualTo("xaa");
  }
}
