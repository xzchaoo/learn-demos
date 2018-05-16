package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/14
 */
public final class MyFlowableLimit<T> extends Flowable<T> {
  private final Flowable<T> source;
  private final long limit;

  public MyFlowableLimit(Flowable<T> source, long limit) {
    this.source = source;
    this.limit = limit;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> actual) {
    if (limit == 0) {
      EmptySubscription.complete(actual);
      return;
    }
    source.subscribe(new LimitSubscriber<>(actual, limit));
  }

  static final class LimitSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> actual;
    Subscription s;
    long remaining;

    LimitSubscriber(Subscriber<? super T> actual, long limit) {
      this.actual = actual;
      this.remaining = limit;
      lazySet(limit);
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        actual.onSubscribe(this);
      }
    }

    @Override
    public void onNext(T t) {
      long r = remaining;
      if (r > 0) {
        remaining = --r;
        actual.onNext(t);
        if (r == 0) {
          s.cancel();
          actual.onComplete();
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      if (remaining > 0) {
        actual.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (remaining > 0) {
        actual.onComplete();
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        for (; ; ) {
          long e = get();
          if (e == 0) {
            break;
          }
          long r;
          if (e <= n) {
            r = e;
          } else {
            r = n;
          }
          long u = e - r;
          if (compareAndSet(e, u)) {
            s.request(r);
            break;
          }
        }
      }
    }

    @Override
    public void cancel() {
      s.cancel();
    }
  }
}
