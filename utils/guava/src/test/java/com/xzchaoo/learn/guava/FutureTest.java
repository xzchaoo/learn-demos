package com.xzchaoo.learn.guava;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class FutureTest {
  @Test
  public void test_SettableFuture() throws InterruptedException {
    // sf暴露了一些方法 可以让调用方控制该future何时完成或失败
    SettableFuture<String> sf = SettableFuture.create();
    CountDownLatch cdl = new CountDownLatch(1);
    sf.addCallback(new FutureCallback<String>() {
      @Override
      public void onSuccess(@Nullable String result) {
        System.out.println(result);
        cdl.countDown();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    }, MoreExecutors.directExecutor());
    sf.set("a");
    cdl.await();
  }

  @Test
  public void test_allAsList() {
    // 全部成功才算成功
    SettableFuture<Integer> sf1 = SettableFuture.create();
    SettableFuture<Integer> sf2 = SettableFuture.create();
    ListenableFuture<List<Integer>> allFuture = Futures.allAsList(sf1, sf2);
    allFuture.addListener(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println(allFuture.get());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, MoreExecutors.directExecutor());
    sf1.set(1);
    sf2.set(2);
  }

  @Test
  public void test_timeout() throws InterruptedException {
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    try {
      //sf是原始的F
      SettableFuture<Integer> sf = SettableFuture.create();

      //具备timeout能力的F
      ListenableFuture<Integer> timeoutF = Futures.withTimeout(sf, 1, TimeUnit.SECONDS, ses);

      Futures.addCallback(timeoutF, new FutureCallback<Integer>() {
        @Override
        public void onSuccess(@Nullable Integer result) {
          System.out.println(result);
        }

        @Override
        public void onFailure(Throwable t) {
          t.printStackTrace();
        }
      }, MoreExecutors.directExecutor());

      //强制睡觉1.5秒 上述onFailure会收到超时
      Thread.sleep(1500);
    } finally {
      ses.shutdownNow();
    }
  }

  /**
   * guava23之后新增了流式API, 可以更流畅地进行编程
   * 功能并没有增强, Futures 本来已经提供了这些功能
   *
   * @throws Exception
   */
  @Test
  public void test_fluent() throws Exception {
    final ListenableFuture<String> f1 = Futures.immediateFuture("123");
    final FluentFuture<String> f2 = FluentFuture.from(f1)
      .transform(Integer::parseInt, MoreExecutors.directExecutor())
      .transform(x -> x * x, MoreExecutors.directExecutor())
      .transformAsync(x -> {
        final SettableFuture<String> sf = SettableFuture.create();
        new Thread(() -> {
          try {
            Thread.sleep(3000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          sf.set("i am result");
        }).start();
        return sf;
      }, MoreExecutors.directExecutor());

    final String result = f2.get();
    System.out.println(result);
  }
}
