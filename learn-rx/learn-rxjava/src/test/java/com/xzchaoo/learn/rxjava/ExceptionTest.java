package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Single;

/**
 * created by zcxu at 2017/11/20
 *
 * @author zcxu
 */
public class ExceptionTest {

  @Test
  public void test() {
    Single.just(1)
        .map(x -> {
          throw new IllegalArgumentException();
        })
        .doOnSuccess(e -> {
          //这个success不会执行
          System.out.println("成功");
        })
        //当失败的时候 吞掉失败的消息 并且返回2 不太推荐, 至少打印一下异常信息
        .onErrorReturnItem(2)
        .doOnError(e -> {
          System.out.println("失败");
        }).subscribe();
  }
}
