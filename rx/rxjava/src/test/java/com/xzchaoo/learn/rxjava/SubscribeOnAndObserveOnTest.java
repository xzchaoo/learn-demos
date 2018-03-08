package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/2/1
 */
public class SubscribeOnAndObserveOnTest {
  @Test
  public void test() {
    Single<Integer> s1 = Single.fromCallable(() -> {
      System.out.println("开始睡觉  " + Thread.currentThread());
      Thread.sleep(1000);
      return 123;
    }).cache();
    s1.subscribeOn(Schedulers.io()).subscribe(result -> {
      System.out.println("收到结果" + result + " " + Thread.currentThread());
      Thread.sleep(2000);
    });
    System.out.println(s1.subscribeOn(Schedulers.computation()).blockingGet());
  }
}
