package com.xzchaoo.learn.test.powermock;

import com.xzchaoo.learn.test.powermock.ignore.FooUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.setInternalState;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
//@PowerMockIgnore("com.xzchaoo.learn.test.powermock.ignore.*")
@PrepareForTest(FooUtils.class)
@RunWith(PowerMockRunner.class)
//@SuppressStaticInitializationFor("com.xzchaoo.learn.test.powermock.ignore.FooUtils")
public class PMTest1 {
  @Test
  public void test() {
    mockStatic(FooUtils.class);
    assertThat(FooUtils.a).isEqualTo("a");
    setInternalState(FooUtils.class, "a", "b");
    assertThat(FooUtils.a).isEqualTo("b");
  }

  @Test
  public void test3() throws UnsupportedEncodingException {
    mockStatic(URLEncoder.class);
    when(URLEncoder.encode(anyString(), anyString())).thenReturn("bbb");
    String result = FooUtils.encode("a", "utf-8");
    assertThat(result).isEqualTo("bbb");
  }
}
