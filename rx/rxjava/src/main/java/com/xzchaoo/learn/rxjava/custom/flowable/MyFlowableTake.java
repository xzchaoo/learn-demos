package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzcha
 * @date 2018/5/13
 */
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
    final long limit;
    long remaning;
    boolean done;
    Subscription s;
    long requested;

    TakeSubscriber(Subscriber<? super T> subscriber, long limit) {
      this.subscriber = subscriber;
      this.limit = limit;
      this.remaning = limit;
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
      if (!done && remaning-- > limit) {
        subscriber.onNext(t);
        if (remaning == 0) {
          s.cancel();
          onComplete();
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      if (!done) {
        done = true;
        subscriber.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (!done) {
        done = true;
        subscriber.onComplete();
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        if (!done && requestUpstream.compareAndSet(false, true)) {
          if (requested + n <= limit) {
            requested += n;
            this.s.request(n);
          } else {
            long nn = limit - requested;
            if (nn > 0) {
              this.s.request(nn);
            }
          }
        }
      }
    }

    private AtomicBoolean requestUpstream = new AtomicBoolean(false);

    @Override
    public void cancel() {
      done = true;
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }
}
