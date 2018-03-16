package com.xzchaoo.learn.test.jmockit;

import org.junit.Test;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

import static org.assertj.core.api.Assertions.assertThat;

public class JMockitTest1 {

  //自动创建对象 并且注入属性 可以有多个
  //@Tested(fullyInitialized = true)
  @Tested
  UserService userService;

  @Injectable
  UserDao userDao;

  @Test
  public void test() {
    new Expectations() {{
      //这里进行mock
      userDao.find();
      result = "abc";
    }};
    val result = userService.foo();
    assertThat(result).isEqualTo("abc");

    //这里进行验证
    new Verifications() {{
      userDao.find();
      times = 1;
    }};
  }
}
