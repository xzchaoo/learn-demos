package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.service.DbManager;
import com.xzchaoo.learn.test.demo20180412.service.LogManager;
import com.xzchaoo.learn.test.demo20180412.utils.LogUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.xzchaoo.learn.test.demo20180412.service.DbManager")
@PrepareForTest(LogUtils.class)
public class LogManagerTest {
  @Test
  public void test_log() {
    mockStatic(DbManager.class);
    mockStatic(LogUtils.class);

    when(LogUtils.add(anyString(), anyString())).thenAnswer(invocation -> {
      String prefix = invocation.getArgument(0);
      String message = invocation.getArgument(1);
      return prefix + " " + message;
    });

    when(DbManager.hello2()).thenReturn("world");
    String result = new LogManager().log();
    assertThat(result).isEqualTo("log world");
    assertThat(result).isNull();
  }
}
