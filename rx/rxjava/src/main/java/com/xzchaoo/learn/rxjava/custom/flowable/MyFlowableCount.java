package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public class MyFlowableCount<T> extends Flowable<Long> {
  private final Flowable<T> source;

  public MyFlowableCount(Flowable<T> source) {
    this.source = source;
  }

  @Override
  protected void subscribeActual(Subscriber<? super Long> subscriber) {
    source.subscribe(new CountSubscriber<>(subscriber));
  }

  static final class CountSubscriber<T> extends DeferredScalarSubscription<Long>
    implements FlowableSubscriber<T> {
    Subscription s;
    long count;

    CountSubscriber(Subscriber<? super Long> subscriber) {
      super(subscriber);
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
      ++count;
    }

    @Override
    public void onError(Throwable t) {
      actual.onError(t);
    }

    @Override
    public void onComplete() {
      super.complete(count);
    }

    @Override
    public void cancel() {
      super.cancel();
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }
}
