package com.xzchaoo.learn.test.jmockit.demo20180413.service;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mockit.Injectable;
import mockit.Tested;

/**
 * @author zcxu
 * @date 2018/6/1 0001
 */
public class ThreadPoolServiceTest {
  // @Tested(fullyInitialized = true, availableDuringSetup = true)
  @Tested//(fullyInitialized = true)
  ThreadPoolService service;

  @Injectable
  Executor executor = Executors.newSingleThreadExecutor();

  @Test
  public void test() {
    service.runInExecutor(new Runnable() {
      @Override
      public void run() {
        System.out.println("run!");
      }
    });
  }
}