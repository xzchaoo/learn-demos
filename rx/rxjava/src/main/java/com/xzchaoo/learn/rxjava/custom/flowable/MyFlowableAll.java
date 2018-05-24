package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
@BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
public class MyFlowableAll<U> extends MyFlowableUpstream<Boolean, U> {
  private final Predicate<? super U> predicate;

  public MyFlowableAll(Flowable<U> source, Predicate<? super U> predicate) {
    super(source);
    this.predicate = predicate;
  }

  @Override
  protected void subscribeActual(Subscriber<? super Boolean> subscriber) {
    source.subscribe(new AllSubscriber<>(subscriber, predicate));
  }

  static final class AllSubscriber<U> extends DeferredScalarSubscription<Boolean> implements FlowableSubscriber<U> {
    final Predicate<? super U> predicate;

    Subscription s;
    boolean done;

    AllSubscriber(Subscriber<? super Boolean> subscriber, Predicate<U> predicate) {
      super(subscriber);
      this.predicate = predicate;
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
    public void onNext(U t) {
      if (!done) {
        return;
      }
      boolean b;
      try {
        b = predicate.test(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        s.cancel();
        actual.onError(e);
        return;
      }
      if (!b) {
        done = true;
        s.cancel();
        complete(false);
      }
    }

    @Override
    public void onError(Throwable t) {
      if (!done) {
        done = true;
        actual.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (!done) {
        done = true;
        complete(true);
      }
    }

    @Override
    public void cancel() {
      super.cancel();
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }
}
