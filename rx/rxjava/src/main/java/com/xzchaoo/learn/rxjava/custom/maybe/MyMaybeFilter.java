package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeFilter<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Predicate<? super T> predicate;

  public MyMaybeFilter(MaybeSource<T> source, Predicate<? super T> predicate) {
    this.source = source;
    this.predicate = predicate;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new FilterObserver<>(observer, predicate));
  }

  static final class FilterObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Predicate<? super T> predicate;

    Disposable d;

    FilterObserver(MaybeObserver<? super T> observer, Predicate<? super T> predicate) {
      this.observer = observer;
      this.predicate = predicate;
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
      boolean b;

      try {
        b = predicate.test(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        observer.onError(e);
        return;
      }
      if (b) {
        observer.onSuccess(t);
      } else {
        observer.onComplete();
      }
    }

    @Override
    public void onError(Throwable e) {
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      observer.onComplete();
    }

    @Override
    public void dispose() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
    }

    @Override
    public boolean isDisposed() {
      return d.isDisposed();
    }
  }
}
