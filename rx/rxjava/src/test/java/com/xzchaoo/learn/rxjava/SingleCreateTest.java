package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Single;
import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/5/10
 */
public class SingleCreateTest {
  @Test
  public void test() {
    Object result = Single.create(emitter -> {
      val once = new AtomicBoolean(false);
      emitter.setCancellable(() -> {
        System.out.println("cancellable总是会被调用");
        if (once.compareAndSet(false, true)) {
          System.out.println("过早死亡");
        }
      });
      new Thread(() -> {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (once.compareAndSet(false, true)) {
          emitter.onSuccess("123");
        }
      }).start();
    }).blockingGet();
    System.out.println(result);
  }
}
