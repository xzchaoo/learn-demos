package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzchaoo
 * @date 2018/5/14
 */
public class MyFlowableTaskLastOne<T> extends Flowable<T> {
  private final Flowable<T> source;

  public MyFlowableTaskLastOne(Flowable<T> source) {
    this.source = source;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    source.subscribe(new TakeLastOne<>(s));
  }

  static final class TakeLastOne<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
    Subscription s;

    TakeLastOne(Subscriber<? super T> actual) {
      super(actual);
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        actual.onSubscribe(this);
        s.request(Long.MAX_VALUE);
      }
    }

    @Override
    public void onNext(T t) {
      value = t;
    }

    @Override
    public void onError(Throwable t) {
      value = null;
      actual.onError(t);
    }

    @Override
    public void onComplete() {
      T v = this.value;
      if (v != null) {
        complete(v);
      } else {
        actual.onComplete();
      }
    }

    @Override
    public void cancel() {
      super.cancel();
      s.cancel();
    }
  }
}
