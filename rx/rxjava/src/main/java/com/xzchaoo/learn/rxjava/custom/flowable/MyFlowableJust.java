package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.subscriptions.ScalarSubscription;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
@BackpressureSupport(BackpressureKind.FULL)
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
