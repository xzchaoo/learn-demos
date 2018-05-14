package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.subscriptions.ScalarSubscription;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public final class MyFlowableJust<T> extends Flowable<T> implements ScalarCallable<T> {
  private final T value;

  public MyFlowableJust(T value) {
    this.value = value;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    s.onSubscribe(new ScalarSubscription<>(s, value));
  }

  @Override
  public T call() {
    return value;
  }
}
