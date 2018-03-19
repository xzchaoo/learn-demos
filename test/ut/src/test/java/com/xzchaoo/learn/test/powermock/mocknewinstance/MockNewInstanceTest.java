package com.xzchaoo.learn.test.powermock.mocknewinstance;

import com.xzchaoo.learn.test.User;
import com.xzchaoo.learn.test.UserDaoImpl;
import com.xzchaoo.learn.test.powermock.ignore.FooUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * https://github.com/powermock/powermock/wiki/Suppress-Unwanted-Behavior 镇压不想要的行为
 * SuppressStaticInitializationFor
 * https://github.com/powermock/powermock/wiki/Mock-System
 * https://github.com/powermock/powermock/wiki/Mock-Policies
 * 使用 WhiteBox 绕过封装
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
@RunWith(PowerMockRunner.class)
//以下情况, 类A需要被prepare
//在A里 new B, B需要被mock
//类A的final方法需要被mock
//类A的静态方法需要被mock
//类A调用了类B的静态方法, 类B的静态方法需要被mock
@PrepareForTest(value = {FooService.class, UserDaoImpl.class, FooUtils.class})
public class MockNewInstanceTest {
  @InjectMocks
  FooService service;
  @Mock
  UserDaoImpl userDao;

  @Test
  public void test() throws Exception {
    mockStatic(FooUtils.class);
    when(FooUtils.encode(anyString(), anyString())).thenReturn("aaa");
    //PowerMockito.verifyNew()
    //UserDaoImpl userDao = PowerMockito.mock(UserDaoImpl.class);
    PowerMockito.when(userDao.findUserByUserId(anyInt())).thenReturn(new User(2, "xx"));

    //相比jmock 这没有比较方便
    PowerMockito.doAnswer(new Answer<Object>() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        System.out.println("f1");
        return null;
      }
    }).when(userDao).f1(anyInt());


    PowerMockito.whenNew(UserDaoImpl.class).withAnyArguments().thenReturn(userDao);
    service.foo();
    //verifyPrivate()
  }

  /**
   * mock是可以覆盖的, 但不推荐
   * 特别是当你mock一个方法要抛异常时
   * 第2次mock的时候就抛异常了
   */
  @Test
  public void test_cover() {
    //这样覆盖是不行的 因为第二行, 在mock方法被调用之前会先调用 findUserByUserId 方法, 而它抛出了异常
//    when(userDao.findUserByUserId(1)).thenThrow(new RuntimeException());
//    when(userDao.findUserByUserId(1)).thenThrow(new RuntimeException());

    //这样覆盖是可以的
    doThrow(new RuntimeException()).when(userDao).findUserByUserId(1);
    doThrow(new RuntimeException()).when(userDao).findUserByUserId(1);
  }
}
