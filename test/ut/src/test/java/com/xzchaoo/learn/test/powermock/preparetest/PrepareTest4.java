package com.xzchaoo.learn.test.powermock.preparetest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.*;

/**
 * 需要mockA的私有方法 A需要被Prepare, 估计只能和spy一起用了, 不然实在想不出哪里还能用
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
@PrepareForTest({PrepareService.class})
@RunWith(PowerMockRunner.class)
public class PrepareTest4 {
  @Spy
  PrepareService service = new PrepareService();

  @Test
  public void test() throws Exception {
    PowerMockito.when(service, "privateMethod").thenReturn("xaa");
    assertThat(service.callPrivateMethod()).isEqualTo("xaa");
  }
}
