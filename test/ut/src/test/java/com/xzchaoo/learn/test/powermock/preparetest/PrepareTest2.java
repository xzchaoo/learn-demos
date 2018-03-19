package com.xzchaoo.learn.test.powermock.preparetest;

import com.xzchaoo.learn.test.User;
import com.xzchaoo.learn.test.UserDaoImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * 在A里 new B, B需要被mock, 则A需要被Prepare
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
@PrepareForTest({PrepareService.class})
@RunWith(PowerMockRunner.class)
public class PrepareTest2 {
  @InjectMocks
  PrepareService service;
  @Mock
  UserDaoImpl dao;

  @Test
  public void test() throws Exception {
    when(dao.findUserByUserId(anyInt())).thenReturn(new User(1, "xaa"));
    whenNew(UserDaoImpl.class).withAnyArguments().thenReturn(dao);
    assertThat(service.callNewInstance()).isEqualTo("xaa");
  }
}
