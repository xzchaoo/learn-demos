package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.service.DangerousService;
import com.xzchaoo.learn.test.demo20180412.service.DangerousTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 注意这里反而要将 ds 给 prepare掉
 *
 * @author xzchaoo
 * @date 2018/4/12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DangerousService.class})
public class DangerousServiceTest {
  @Test
  public void test() throws Exception {
    DangerousService ds = new DangerousService();
    DangerousTask dt = PowerMockito.mock(DangerousTask.class);
    PowerMockito.whenNew(DangerousTask.class).withAnyArguments().thenReturn(dt);
    ds.run();
  }
}
