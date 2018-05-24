package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeSwitchIfEmpty<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final MaybeSource<T> fallback;

  public MyMaybeSwitchIfEmpty(MaybeSource<T> source, MaybeSource<T> fallback) {
    this.source = source;
    this.fallback = fallback;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, fallback));
  }

  static class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final MaybeSource<T> fallback;
    boolean fallbackMode;

    InnerObserver(MaybeObserver<? super T> observer, MaybeSource<T> fallback) {
      this.observer = observer;
      this.fallback = fallback;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (!fallbackMode) {
        if (DisposableHelper.setOnce(this, d)) {
          observer.onSubscribe(this);
        }
      } else {
        DisposableHelper.replace(this, d);
      }
    }

    @Override
    public void onSuccess(T t) {
      observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      if (fallbackMode) {
        observer.onComplete();
      } else {
        if (!isDisposed()) {
          fallbackMode = true;
          fallback.subscribe(this);
        }
      }
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
