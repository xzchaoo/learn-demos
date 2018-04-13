package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.service.DbManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.xzchaoo.learn.test.demo20180412.service.DbManager")
public class DbManagerTest {
  @Test
  public void test_hello() {
    assertThat(DbManager.hello()).isEqualTo("world");

    // final 常量的赋值发生在类准备阶段
    assertThat(DbManager.REAL_CONSTANT_VALUE).isEqualTo("REAL_CONSTANT_VALUE");

    // 该字段的赋值发生在 类初始化阶段, 但初始化被镇压了 因此为null
    assertThat(DbManager.CLINIT_VALUE).isNull();
    assertThat(DbManager.CLINIT_METHOD_CALL).isNull();
  }
}
