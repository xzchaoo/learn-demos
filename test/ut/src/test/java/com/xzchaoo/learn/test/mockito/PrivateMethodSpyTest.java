package com.xzchaoo.learn.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * @author xzchaoo
 * @date 2018/4/9
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.xzchaoo.learn.test.mockito.PrivateObject")
public class PrivateMethodSpyTest {
  @Test
  public void test() throws Exception {
    PrivateObject po = spy(new PrivateObject());
    doNothing().when(po, "bar", anyString());
    po.foo();
  }
}
