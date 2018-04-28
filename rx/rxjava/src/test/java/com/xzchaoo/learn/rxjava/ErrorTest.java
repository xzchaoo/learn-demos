package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/4/26
 */
public class ErrorTest {

  @Test
  public void test() {
    // 在回调里抛异常不会被error回调捕获 这会导致最终被线程异常处理器捕获
    Single.just(1).map(x -> {
      throw new RuntimeException("fail");
    }).subscribe(result -> {

    }, error -> {
      System.out.println("Error " + error);
    });
  }
}
