package com.xzchaoo.learn.rxjava.custom;

import com.xzchaoo.learn.rxjava.custom.single.MySingleDelay;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleDelayTest {
  @Test
  public void test() {
    long begin = System.currentTimeMillis();
    val result = new MySingleDelay<>(Single.just(1), 3, TimeUnit.SECONDS, Schedulers.computation(), false).blockingGet();
    System.out.println(result);
    System.out.println(System.currentTimeMillis() - begin);
  }
}
