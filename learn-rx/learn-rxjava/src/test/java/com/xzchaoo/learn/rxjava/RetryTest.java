package com.xzchaoo.learn.rxjava;

import io.reactivex.Single;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * created by zcxu at 2017/11/14
 *
 * @author zcxu
 */
public class RetryTest {

  @Test
  public void test1() {
    AtomicInteger ai = new AtomicInteger(0);
    Object x = Single.fromCallable(() -> {
      System.out.println(ai.incrementAndGet());
      throw new IllegalArgumentException();
    }).onErrorResumeNext(e -> Single.timer(1, TimeUnit.SECONDS).flatMap(y -> Single.error(e)))
        .retry(2)
        .blockingGet();
    System.out.println(x);
  }
}
