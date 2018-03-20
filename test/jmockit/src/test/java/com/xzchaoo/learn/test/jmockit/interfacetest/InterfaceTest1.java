package com.xzchaoo.learn.test.jmockit.interfacetest;

import org.junit.Test;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Tested;

import static org.assertj.core.api.Assertions.*;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
public class InterfaceTest1 {
  @Tested
  Service1Impl service;
  @Injectable
  Dao1 dao1;

  @Test
  public void test() {
    new Expectations() {{
      dao1.f1();
      result = "123";
    }};
    assertThat(service.f1()).isEqualTo("123");
    new FullVerifications() {{

    }};
  }
}
