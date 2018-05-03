package com.xzchaoo.learn.rxjava.examples.demo20180424;

import org.junit.Test;

import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/4/24 0024
 */
public class Demo20180424 {
  @Test
  public void test() {
    Single<Integer> task1 = Single.just(1);
    Single<Integer> task2 = Single.just(1);


    Single<Integer> task3 = task1.flatMap(this::task3);

    Single<String> finalTask = Single.zip(task3, task2, (r3, r2) -> {
      return r3.toString() + r2.toString();
    });

    // finalTask.subscribe();
  }

  public Single<Integer> task3(int task1Result) {
    return Single.just(task1Result * task1Result);
  }
}
