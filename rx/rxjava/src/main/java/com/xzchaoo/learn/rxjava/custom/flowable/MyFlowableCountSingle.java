package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
@BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
public class MyFlowableCountSingle<T> extends Single<Long> {
  private final Flowable<T> source;

  public MyFlowableCountSingle(Flowable<T> source) {
    this.source = source;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Long> observer) {
    source.subscribe(new CountSubscriber<>(observer));
  }

  static final class CountSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    final SingleObserver<? super Long> observer;
    Subscription s;
    long count;

    CountSubscriber(SingleObserver<? super Long> observer) {
      this.observer = observer;
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
    public void onNext(T t) {
      ++count;
    }

    @Override
    public void onError(Throwable t) {
      observer.onError(t);
    }

    @Override
    public void onComplete() {
      observer.onSuccess(count);
    }

    @Override
    public void dispose() {
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }

    @Override
    public boolean isDisposed() {
      return s == SubscriptionHelper.CANCELLED;
    }
  }
}
