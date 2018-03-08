package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Flowable;

/**
 * @author zcxu
 * @date 2018/2/1
 */
public class FirstTest {
  @Test
  public void test() {
    Flowable.just(1, 2).doOnCancel(() -> {
      System.out.println("被下游cancel");
    }).doOnNext(e -> {
      System.out.println("上游产生了元素 " + e);
    }).firstOrError().subscribe(e -> {
      System.out.println("下游收到元素 " + e);
    });
  }
}
