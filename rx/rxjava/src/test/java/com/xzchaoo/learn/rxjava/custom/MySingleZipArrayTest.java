package com.xzchaoo.learn.rxjava.custom;

import com.xzchaoo.learn.rxjava.custom.single.MySingleZipArray;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleZipArrayTest {
  @Test
  public void test() {
    Single<Integer> single1 = Single.just(1).delay(3, TimeUnit.SECONDS).doOnDispose(() -> {
      System.out.println("1被d了");
    });
    Single<Integer> single2 = Single.<Integer>error(new RuntimeException()).delay(1, TimeUnit.SECONDS, true);
    new Thread(() -> {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
    final val result = new MySingleZipArray<Integer, String>(new SingleSource[]{single1, single2}, (results) -> {
      return results[0].toString() + results[1];
    }).blockingGet();
    System.out.println(result);
  }
}
