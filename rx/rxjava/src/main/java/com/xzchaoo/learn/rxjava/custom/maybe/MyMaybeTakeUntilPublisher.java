package com.xzchaoo.learn.rxjava.custom.maybe;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeTakeUntilPublisher<T, U> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Publisher<U> until;

  public MyMaybeTakeUntilPublisher(MaybeSource<T> source, Publisher<U> until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    InnerObserver<T, U> parent = new InnerObserver<>(observer);
    observer.onSubscribe(parent);

    until.subscribe(parent.untilSubscriber);
    source.subscribe(observer);
  }

  static final class InnerObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final UntilSubscriber<? super R> untilSubscriber;

    InnerObserver(MaybeObserver<? super T> observer) {
      this.observer = observer;
      this.untilSubscriber = new UntilSubscriber<>(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T t) {
      SubscriptionHelper.cancel(untilSubscriber);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onSuccess(t);
      }
    }

    @Override
    public void onError(Throwable e) {
      SubscriptionHelper.cancel(untilSubscriber);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    @Override
    public void onComplete() {
      SubscriptionHelper.cancel(untilSubscriber);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onComplete();
      }
    }

    void onUntilError(Throwable t) {
      if (DisposableHelper.dispose(this)) {
        observer.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    void onUntilComplete() {
      if (DisposableHelper.dispose(this)) {
        observer.onComplete();
      }
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
      SubscriptionHelper.cancel(untilSubscriber);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }

  static final class UntilSubscriber<U> extends AtomicReference<Subscription> implements Subscriber<U> {
    final InnerObserver<?, U> parent;

    UntilSubscriber(InnerObserver<?, U> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.replace(this, s)) {
        s.request(Long.MAX_VALUE);
      }
    }

    @Override
    public void onNext(U u) {
      SubscriptionHelper.cancel(this);
      parent.onUntilComplete();
    }

    @Override
    public void onError(Throwable t) {
      parent.onUntilError(t);
    }

    @Override
    public void onComplete() {
      parent.onUntilComplete();
    }
  }
}
