package com.xzchaoo.learn.test.demo20180413.mockito;

import com.xzchaoo.learn.test.demo20180413.dao.UserDao;
import com.xzchaoo.learn.test.demo20180413.model.User;
import com.xzchaoo.learn.test.demo20180413.service.impl.UserHelper;
import com.xzchaoo.learn.test.demo20180413.service.impl.UserServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
  @InjectMocks
  UserServiceImpl service;
  @Mock
  UserDao userDao;
  @Spy
  UserHelper userHelper;

  @Test
  public void test_findById() {
    User u1 = new User();
    u1.setId(1);
    u1.setUsername("xxx1");
    when(userDao.findById(1)).thenReturn(u1);

    assertThat(service.findById(1)).isSameAs(u1);
    assertThat(service.findById(2)).isNull();

    // 验证Mokc对象 userDao 被调用了2次 findById 方法, 以任意的int
    verify(userDao, times(2)).findById(anyInt());
  }

  @Test
  public void test_fillInfo() {
    User u1 = new User();
    u1.setId(1);
    u1.setUsername("xxx1");

    service.fillInfo(u1);
    assertThat(u1.getExtraInfo1()).isEqualTo("945220b105cb4c8af2f829c1b25f2069");
  }
}

