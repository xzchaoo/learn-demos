package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/12
 */
1实现有问题 这里故意制造一个错误 引起注意

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
    SourceObserver<T, U> so = new SourceObserver<>(observer, fallback);
    observer.onSubscribe(so);

    source.subscribe(so);
    timeout.subscribe(so.to);
  }

  static final class SourceObserver<T, U> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final MaybeSource<T> fallback;
    final TimeoutObserver<U> to;
    final FallbackObserver<T> fo;

    AtomicReference<Disposable> d = new AtomicReference<>();
    Disposable dd;
    AtomicReference<Disposable> tod = new AtomicReference<>();
    //AtomicReference<Disposable> fod;

    SourceObserver(MaybeObserver<? super T> observer, MaybeSource<T> fallback) {
      this.observer = observer;
      this.fallback = fallback;
      this.to = new TimeoutObserver<>(this);
      this.fo = fallback == null ? null : new FallbackObserver<>(this);
      // this.fod = fallback == null ? null : new AtomicReference<>();
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this.d, d)) {
        this.dd = d;
      }
    }

    @Override
    public void onSuccess(T t) {
      DisposableHelper.dispose(tod);
      if (d.compareAndSet(dd, DisposableHelper.DISPOSED)) {
        observer.onSuccess(t);
      }
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.dispose(tod);
      if (d.compareAndSet(dd, DisposableHelper.DISPOSED)) {
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    @Override
    public void onComplete() {
      DisposableHelper.dispose(tod);
      if (d.compareAndSet(dd, DisposableHelper.DISPOSED)) {
        observer.onComplete();
      }
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(d);
      DisposableHelper.dispose(tod);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(d.get());
    }

    void onTimeout() {
      Disposable empty = Disposables.empty();
      if (this.d.compareAndSet(dd, empty)) {
        this.dd.dispose();
        if (fo == null) {
          this.d.lazySet(DisposableHelper.DISPOSED);
          observer.onError(new TimeoutException());
        } else {
          fallback.subscribe(fo);
        }
      }
    }

    void onTimeoutError(Throwable e) {
      final Disposable dd = d.getAndSet(DisposableHelper.DISPOSED);
      if (dd != DisposableHelper.DISPOSED) {
        dd.dispose();
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }
  }

  static final class FallbackObserver<T> implements MaybeObserver<T> {

    final SourceObserver<T, ?> parent;

    FallbackObserver(SourceObserver<T, ?> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(parent.d, d);
    }

    @Override
    public void onSuccess(T t) {
      DisposableHelper.dispose(parent.d);
      parent.observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.dispose(parent.d);
      parent.observer.onError(e);
    }

    @Override
    public void onComplete() {
      DisposableHelper.dispose(parent.d);
      parent.observer.onComplete();
    }
  }

  static final class TimeoutObserver<U> implements MaybeObserver<U> {
    private final SourceObserver<?, U> parent;

    TimeoutObserver(SourceObserver<?, U> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(parent.tod, d);
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
  }
}
