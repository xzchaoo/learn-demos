package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeTimeoutMaybe<T, U> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final MaybeSource<U> timeout;
  private final MaybeSource<T> fallback;

  public MyMaybeTimeoutMaybe(MaybeSource<T> source, MaybeSource<U> timeout, MaybeSource<T> fallback) {
    this.source = source;
    this.timeout = timeout;
    this.fallback = fallback;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    // 1. 安全性
    //   1. onSubscribe 立即dispose是否有影响
    // 2. 正确性

    SourceObserver<T, U> so = new SourceObserver<>(observer, fallback);
    observer.onSubscribe(so);
    source.subscribe(so);
    timeout.subscribe(so.to);
  }

  static final class SourceObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final MaybeSource<T> fallback;
    final TimeoutObserver<U> to;
    FallbackObserver<T> fo;
    Disposable d;

    SourceObserver(MaybeObserver<? super T> observer, MaybeSource<T> fallback) {
      this.observer = observer;
      this.fallback = fallback;
      this.to = new TimeoutObserver<>(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.replace(this, d)) {
        this.d = d;
      }
    }

    @Override
    public void onSuccess(T t) {
      DisposableHelper.dispose(to);
      if (compareAndSet(d, DisposableHelper.DISPOSED)) {
        observer.onSuccess(t);
      }
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.dispose(to);
      if (compareAndSet(d, DisposableHelper.DISPOSED)) {
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    @Override
    public void onComplete() {
      DisposableHelper.dispose(to);
      if (compareAndSet(d, DisposableHelper.DISPOSED)) {
        observer.onComplete();
      }
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
      DisposableHelper.dispose(to);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    void onTimeout() {
      Disposable dd = this.d;
      if (fallback == null) {
        if (compareAndSet(dd, DisposableHelper.DISPOSED)) {
          dd.dispose();
          observer.onError(new TimeoutException());
        }
      } else {
        fo = new FallbackObserver<>(this);
        if (compareAndSet(dd, fo)) {
          dd.dispose();
          fallback.subscribe(fo);
        }
      }
    }

    void onTimeoutError(Throwable e) {
      if (compareAndSet(d, DisposableHelper.DISPOSED)) {
        d.dispose();
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }
  }

  static final class FallbackObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {

    final SourceObserver<T, ?> parent;

    FallbackObserver(SourceObserver<T, ?> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T t) {
      parent.observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      parent.observer.onError(e);
    }

    @Override
    public void onComplete() {
      parent.observer.onComplete();
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }

  static final class TimeoutObserver<U> extends AtomicReference<Disposable> implements MaybeObserver<U>, Disposable {
    private final SourceObserver<?, U> parent;

    TimeoutObserver(SourceObserver<?, U> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(U u) {
      parent.onTimeout();
    }

    @Override
    public void onError(Throwable e) {
      parent.onTimeoutError(e);
    }

    @Override
    public void onComplete() {
      parent.onTimeout();
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }
}
