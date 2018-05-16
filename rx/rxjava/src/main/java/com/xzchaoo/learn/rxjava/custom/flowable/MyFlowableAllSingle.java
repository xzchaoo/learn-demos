package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
@BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
public class MyFlowableAllSingle<U> extends Single<Boolean> implements HasUpstreamPublisher<U> {
  private final Flowable<U> source;
  private final Predicate<? super U> predicate;

  public MyFlowableAllSingle(Flowable<U> source, Predicate<? super U> predicate) {
    this.source = source;
    this.predicate = predicate;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Boolean> observer) {
    source.subscribe(new AllSubscriber<>(observer, predicate));
  }

  @Override
  public Publisher<U> source() {
    return source;
  }

  static final class AllSubscriber<U> implements FlowableSubscriber<U>, Disposable {
    final SingleObserver<? super Boolean> observer;
    final Predicate<U> predicate;

    Subscription s;
    boolean done;

    AllSubscriber(SingleObserver<? super Boolean> observer, Predicate<U> predicate) {
      this.observer = observer;
      this.predicate = predicate;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        observer.onSubscribe(this);
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
        done = true;
        s.cancel();
        observer.onError(e);
        return;
      }
      if (!b) {
        done = true;
        s.cancel();
        observer.onSuccess(false);
      }
    }

    @Override
    public void onError(Throwable t) {
      if (!done) {
        done = true;
        observer.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (!done) {
        done = true;
        observer.onSuccess(true);
      }
    }

    @Override
    public void dispose() {
      done = true;
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }

    @Override
    public boolean isDisposed() {
      return done;
    }
  }
}
