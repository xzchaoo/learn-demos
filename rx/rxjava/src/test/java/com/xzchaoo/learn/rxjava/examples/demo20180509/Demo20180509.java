package com.xzchaoo.learn.rxjava.examples.demo20180509;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class Demo20180509 {
  private static final Logger LOGGER = LoggerFactory.getLogger(Demo20180509.class);

  @Test
  public void test() {
    // 重试10次 每次delay3秒

    int times = 10;
    int delaySeconds = 3;

    // 先调第一个服务 它会返回一个token
    // 然后你需要不停地用这个token为参数去调用另外一个接口 直到有返回数据 最多重试10次 每次间隔3秒
    // 注意, 如果是token这里失败了, 那么是不会重试的, 如果需要重试, 你需要自己实现
    Single<String> tokenSingle = firstCallSoa();
    final Object result = tokenSingle.flatMap(token -> {
      return Flowable.range(0, times)
        //.concatMap(ignore -> getData(token, delaySeconds).toFlowable())
        .flatMapMaybe(ignore -> getData(token, delaySeconds), false, 1)
        .firstOrError();
    }).blockingGet();
    System.out.println(result);
  }

  private Maybe<Object> getData(String token, int delaySeconds) {
    return callSoa(token)
      .toMaybe()
      .onErrorResumeNext(error -> {
        if (error instanceof IOException) {
          LOGGER.warn("我在获取数据的过程中发生网络错误, 但我认为这是一次重试失败, 会继续重试", error);
        } else {
          LOGGER.warn("我在获取数据的过程中发生其它错误, 但我认为这是一次重试失败, 会继续重试", error);
        }
        return Maybe.empty().delay(delaySeconds, TimeUnit.SECONDS);
      })
      .flatMap(result -> {
        if (result.equals("data is not ready")) {
          return Maybe.empty().delay(delaySeconds, TimeUnit.SECONDS);
        } else {
          return Maybe.just(result);
        }
      });
  }

  private AtomicInteger ai = new AtomicInteger(0);

  /**
   * 调用这个服务, 返回一个token
   *
   * @return
   */
  private Single<String> firstCallSoa() {
    return Single.just("token=123");
  }

  private Single<Object> callSoa(String token) {
    System.out.println("call soa");
    if (!token.equals("token=123")) {
      return Single.error(new IllegalArgumentException());
    }
    int c = ai.incrementAndGet();
    if (c == 2) {
      return Single.error(new IOException("network error"));
    } else if (c == 3) {
      return Single.just("data is ready");
    } else {
      return Single.just("data is not ready");
    }
  }
}
