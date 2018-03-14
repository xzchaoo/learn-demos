package com.xzchaoo.learn.jdk8.threadpool;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThreadPoolTest {
  @Test
  public void shutdownTest() throws InterruptedException {
    ExecutorService es = Executors.newFixedThreadPool(1);
    es.execute(() -> {
      while (true) {//wrong
        //while (!Thread.currentThread().isInterrupted()) { //right
        System.out.println("ok");
      }
    });
    Thread.sleep(1000);
    es.shutdownNow();
    System.out.println("here");
    Thread.sleep(3000);
  }

  private static void sleep(int mills) {
    try {
      Thread.sleep(mills);
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }

  Runnable r = () -> {
    try {
      sleep(2000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Thread t = Thread.currentThread();
    System.out.println(t.getName() + " " + t.hashCode() + " " + t.isInterrupted());
  };

  @Test
  public void test_size_1() throws InterruptedException {
    //初始化数量为 corePoolSize 当囤积的任务数量超过了 BlockingQueue 的容量时 就会逐渐增加新线程数到 maximumPoolSize 如果还超过BlockingQueue 那么就要用 RejectedExecutionHandler
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      1, 4,
      1, TimeUnit.SECONDS,//线程存活超过5秒就要灭亡 当然了总是会保留 corePoolSize个线程
      new ArrayBlockingQueue<Runnable>(1)//最多囤积1个任务
    );
    tpe.execute(r);
    sleep(100);
    assertEquals(1, tpe.getPoolSize());
    assertEquals(0, tpe.getQueue().size());

    tpe.execute(r);
    sleep(100);
    assertEquals(1, tpe.getPoolSize());
    assertEquals(1, tpe.getQueue().size());

    tpe.execute(r);
    sleep(100);
    assertEquals(2, tpe.getPoolSize());
    assertEquals(1, tpe.getQueue().size());

    tpe.execute(r);
    sleep(100);
    assertEquals(3, tpe.getPoolSize());
    assertEquals(1, tpe.getQueue().size());

    tpe.execute(r);
    sleep(100);
    assertEquals(4, tpe.getPoolSize());
    assertEquals(1, tpe.getQueue().size());

    try {
      tpe.execute(r);
      assertTrue(false);
    } catch (RejectedExecutionException e) {
    }

    for (int i = 0; i < 5; ++i) {
      sleep(1000);
      System.out.println(tpe);
    }

    assertEquals(1, tpe.getPoolSize());
    assertEquals(0, tpe.getQueue().size());

    //最终存活下来的线程不一定 总之只会存活下来一个线程就是了
    tpe.execute(r);
    sleep(2500);

    tpe.shutdown();
    tpe.awaitTermination(1, TimeUnit.SECONDS);
    assertTrue(tpe.isShutdown());
    assertTrue(tpe.isTerminated());
    //shutdownNow 会打断线程
    //tpe.shutdownNow();
  }

  @Test
  public void test_changePoolSize() throws InterruptedException {
    //结论: TPE 支持动态修改线程大小 已经在执行的任务不会被打断 多余的线程池将会渐渐被回收
    //修改线程池大小的时候要注意 core 和 max
    //如果用的是 LinkedBlockingQueue 那么此时max不生效
    //但是要注意 不能将 max 设置比core小 但可以将core设置比max大

    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      1, 1,
      5, TimeUnit.SECONDS,
      new LinkedBlockingQueue<>()
    );
    tpe.execute(r);
    sleep(100);
    assertEquals(1, tpe.getPoolSize());
    sleep(100);
    tpe.execute(r);
    assertEquals(1, tpe.getPoolSize());
    tpe.execute(r);
    tpe.execute(r);
    tpe.execute(r);

    tpe.setMaximumPoolSize(2);
    tpe.setCorePoolSize(2);

    sleep(100);
    assertEquals(2, tpe.getPoolSize());
    assertEquals(2, tpe.getActiveCount());

    tpe.setCorePoolSize(1);
    tpe.setMaximumPoolSize(1);

    sleep(3000);

    assertEquals(1, tpe.getPoolSize());//此时已经启动的线程数
    assertEquals(1, tpe.getActiveCount());//正在执行任务的线程数
    assertEquals(2, tpe.getQueue().size());//阻塞的任务数

    //以下属性不常用
    assertEquals(2, tpe.getCompletedTaskCount());//已完成的任务数

    //提交过的任务数 = 已经执行 + 正在执行 + 等待执行
    assertEquals(5, tpe.getTaskCount());

    System.out.println(tpe);
    tpe.shutdown();
    tpe.awaitTermination(1, TimeUnit.MINUTES);
  }

  @Test
  public void test1() throws InterruptedException {
    //初始化数量为 corePoolSize 当囤积的任务数量超过了 BlockingQueue 的容量时 就会逐渐增加新线程数到 maximumPoolSize 如果还超过BlockingQueue 那么就要用 RejectedExecutionHandler
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      4, 4,
      5, TimeUnit.SECONDS,
      new LinkedBlockingQueue<>()
    );
    AtomicInteger ai = new AtomicInteger(0);
    IntStream.range(0, 5).forEach(x -> {
      tpe.execute(new Runnable() {
        @Override
        public void run() {
          ai.incrementAndGet();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
    });
    Thread.sleep(500);
    System.out.println(tpe);
    assertEquals(4, ai.get());
    Thread.sleep(1200);
    assertEquals(5, ai.get());
    //已经在队列里的任务会继续等待执行
    Thread.sleep(1200);
    tpe.shutdownNow();
    //tpe.shutdown();
  }
}
