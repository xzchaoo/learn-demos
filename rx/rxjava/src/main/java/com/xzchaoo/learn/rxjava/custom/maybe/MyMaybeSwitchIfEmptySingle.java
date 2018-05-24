package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeSwitchIfEmptySingle<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final SingleSource<T> fallback;

  public MyMaybeSwitchIfEmptySingle(MaybeSource<T> source, SingleSource<T> fallback) {
    this.source = source;
    this.fallback = fallback;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, fallback));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, SingleObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final SingleSource<T> fallback;
    boolean fallbackMode;

    InnerObserver(MaybeObserver<? super T> observer, SingleSource<T> fallback) {
      this.observer = observer;
      this.fallback = fallback;
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (fallbackMode) {
        DisposableHelper.replace(this, d);
      } else {
        if (DisposableHelper.setOnce(this, d)) {
          observer.onSubscribe(this);
        }
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
  }
}
