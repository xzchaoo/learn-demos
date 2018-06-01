package com.xzchaoo.learn.test.jmockit.demo20180413.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

/**
 * @author zcxu
 * @date 2018/6/1 0001
 */
public class ThreadPoolService {
  private Executor executor;

  public void runInExecutor(Runnable r) {
    CountDownLatch cdl = new CountDownLatch(1);
    executor.execute(new Runnable() {
      @Override
      public void run() {
        try {
          r.run();
        } finally {
          cdl.countDown();
        }
      }
    });
    cdl.countDown();
  }
}
