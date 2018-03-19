package com.xzchaoo.learn.test.powermock.preparetest;

import com.xzchaoo.learn.test.powermock.ignore.FooUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * A调用了B的静态方法, B的静态方法要被mock, 那么需要Prepare B, 且 mockStatic(B)
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
@PrepareForTest(FooUtils.class)
@RunWith(PowerMockRunner.class)
public class PrepareTest1 {
  @InjectMocks
  PrepareService service;

  @Test
  public void test() throws UnsupportedEncodingException {
    mockStatic(FooUtils.class);
    when(FooUtils.encode(anyString(), anyString())).thenReturn("xaa");
    assertThat(service.callStaticFinalMethod()).isEqualTo("xaa");
  }
}
