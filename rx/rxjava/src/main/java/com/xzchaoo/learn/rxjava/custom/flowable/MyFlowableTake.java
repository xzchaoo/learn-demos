package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * TODO 实现有问题
 *
 * @author xzchaoo
 * @date 2018/5/13
 */
@Deprecated
@BackpressureSupport(BackpressureKind.PASS_THROUGH)
public class MyFlowableTake<T> extends Flowable<T> {
  private final Flowable<T> source;
  private final long limit;

  public MyFlowableTake(Flowable<T> source, long limit) {
    this.source = source;
    this.limit = limit;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> subscriber) {
    if (limit == 0) {
      EmptySubscription.complete(subscriber);
    } else {
      source.subscribe(new TakeSubscriber<>(subscriber, limit));
    }
  }

  static final class TakeSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> subscriber;
    long remaining;
    Subscription s;

    TakeSubscriber(Subscriber<? super T> subscriber, long limit) {
      this.subscriber = subscriber;
      this.remaining = limit;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        subscriber.onSubscribe(this);
      }
    }

    @Override
    public void onNext(T t) {
      long r = remaining;
      if (r > 0) {
        remaining = --r;
        subscriber.onNext(t);
        if (r == 0) {
          s.cancel();
          subscriber.onComplete();
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      if (remaining > 0) {
        remaining = 0;
        subscriber.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (remaining > 0) {
        subscriber.onComplete();
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        this.s.request(n);
      }
    }

    @Override
    public void cancel() {
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }
}
