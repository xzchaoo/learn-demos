package com.xzchaoo.learn.jdk8;

import org.junit.Test;

/**
 * @author xzchaoo
 * @date 2018/5/27
 */
public class VolatileTest {
  private int count = 0;
  private boolean on = true;

  @Test
  public void test() throws InterruptedException {
    // println 内部用了锁 锁的释放与获取具有 happens-before 关系, 再加上顺序规则 和 传递性 会导致 on=false 被另外一个线程看见
    // 我们可以先用println来证明我们的程序没问题 然后再注释掉println 就可以验证工作缓存带来的影响了
    Thread t1 = new Thread(() -> {
      System.out.println("T1 开始");
      int lastCount = count;
      while (on) {
        lastCount = count + 1;
        count = lastCount;
        // System.out.println("t1 " + lastCount);
      }
      System.out.println("t1 finished " + lastCount);
    });

    Thread t2 = new Thread(() -> {
      System.out.println("T2 开始");
      while (true) {
        int lastCount = count;
         System.out.println("t2 " + lastCount);
        if (lastCount > 100_0000) {
          on = false;
          System.out.println("t2 finished " + count);
          break;
        }
      }
    });
    t1.start();
    t2.start();
    t1.join();
    t2.join();
  }
}
