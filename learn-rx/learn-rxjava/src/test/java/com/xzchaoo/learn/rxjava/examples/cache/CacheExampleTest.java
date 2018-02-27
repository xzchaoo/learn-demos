package com.xzchaoo.learn.rxjava.examples.cache;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.internal.operators.flowable.FlowablePublish;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.subjects.SingleSubject;

/**
 * @author xzchaoo
 * @date 2018/1/13
 */
public class CacheExampleTest {
  //访问速度 1~10ms
  private Maybe<String> l1Cache(Object request) {
    return Maybe.defer(() -> {
      return Maybe.empty();
    });
  }

  //访问速度10ms~100ms
  private Maybe<String> l2Cache(Object request) {
    return Maybe.defer(() -> {
      return Maybe.empty();
    });
  }

  //访问速度 100ms~1s
  private Single<String> db(Object request) {
    return Single.defer(() -> {
      return Single.just(request.toString());
    });
  }

  @Test
  public void test() throws InterruptedException {
    Object request = "haha";
    Single<String> s = l1Cache(request)
      .switchIfEmpty(l2Cache(request))
      .switchIfEmpty(db(request));
    s.subscribe(result -> {
      System.out.println(result);
    }, error -> {
      error.printStackTrace();
    });
    //System.out.println("我是不阻塞的 所以这里要sleep");
    Thread.sleep(100);
  }
}
