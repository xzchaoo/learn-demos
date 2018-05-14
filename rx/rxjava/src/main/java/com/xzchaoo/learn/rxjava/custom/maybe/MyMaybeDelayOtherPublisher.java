package com.xzchaoo.learn.rxjava.custom.maybe;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeDelayOtherPublisher<T, U> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Publisher<U> otherPublisher;

  public MyMaybeDelayOtherPublisher(MaybeSource<T> source, Publisher<U> otherPublisher) {
    this.source = source;
    this.otherPublisher = otherPublisher;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new DelayObserver<>(observer, otherPublisher));
  }

  static final class DelayObserver<T, U> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Publisher<U> otherPublisher;
    final OtherPublisherSubscriber<T, U> subscriber;
    Disposable d;

    DelayObserver(MaybeObserver<? super T> observer, Publisher<U> otherPublisher) {
      this.observer = observer;
      this.subscriber = new OtherPublisherSubscriber<T, U>(observer);
      this.otherPublisher = otherPublisher;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      subscriber.value = t;
      schedule();
    }

    @Override
    public void onError(Throwable e) {
      subscriber.error = e;
      schedule();
    }

    @Override
    public void onComplete() {
      schedule();
    }

    void schedule() {
      // 延迟发射
      otherPublisher.subscribe(subscriber);
    }

    @Override
    public void dispose() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
      SubscriptionHelper.cancel(subscriber);
    }

    @Override
    public boolean isDisposed() {
      return SubscriptionHelper.isCancelled(subscriber.get());
    }
  }

  static final class OtherPublisherSubscriber<T, U> extends AtomicReference<Subscription> implements FlowableSubscriber<U> {
    final MaybeObserver<? super T> observer;

    Throwable error;
    T value;

    OtherPublisherSubscriber(MaybeObserver<? super T> observer) {
      this.observer = observer;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.setOnce(this, s)) {
        s.request(Long.MAX_VALUE);
      }
    }

    @Override
    public void onNext(U u) {
      Subscription s = get();
      if (s != SubscriptionHelper.CANCELLED) {
        lazySet(SubscriptionHelper.CANCELLED);
        s.cancel();
        onComplete();
      }
    }

    @Override
    public void onError(Throwable t) {
      Throwable e = error;
      if (e != null) {
        observer.onError(new CompositeException(e, t));
      } else {
        observer.onError(t);
      }
    }

    @Override
    public void onComplete() {
      Throwable e = error;
      if (e != null) {
        observer.onError(e);
      } else {
        T v = value;
        if (v != null) {
          observer.onSuccess(v);
        } else {
          observer.onComplete();
        }
      }
    }
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

}
