package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.DangerousService;
import com.xzchaoo.learn.test.demo20180413.service.DangerousTask;

import org.junit.Before;
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
public class DangerousServiceTest2 {
  @Before
  public void before() {
    PowerMockito.suppress(PowerMockito.constructor(DangerousTask.class));
  }

  @Test
  public void test() throws Exception {
    new DangerousService().run();
  }

  @Test
  public void test2() {
    new DangerousTask();
  }
}
