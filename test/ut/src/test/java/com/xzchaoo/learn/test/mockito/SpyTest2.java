package com.xzchaoo.learn.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
@RunWith(MockitoJUnitRunner.class)
public class SpyTest2 {

  @Spy
  ObjectMapper objectMapper;

  @Spy
  @InjectMocks
  LogConverter logConverter;

  @InjectMocks
  LogService logService;

  @Test
  public void test() {
    logService.send("a");
  }
}
