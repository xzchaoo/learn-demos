package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public final class MyMaybeOnErrorReturn<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Callable<? extends T> fallbackSupplier;

  public MyMaybeOnErrorReturn(MaybeSource<T> source, Callable<? extends T> fallbackSupplier) {
    this.source = source;
    this.fallbackSupplier = fallbackSupplier;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, fallbackSupplier));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class InnerObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Callable<? extends T> fallbackSupplier;
    Disposable d;

    public InnerObserver(MaybeObserver<? super T> observer, Callable<? extends T> fallbackSupplier) {
      this.observer = observer;
      this.fallbackSupplier = fallbackSupplier;
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
      observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      T fallbackValue;
      try {
        fallbackValue = ObjectHelper.requireNonNull(fallbackSupplier.call(), "The fallbackSupplier returned a null value");
      } catch (Exception e2) {
        Exceptions.throwIfFatal(e2);
        observer.onError(new CompositeException(e, e2));
        return;
      }
      observer.onSuccess(fallbackValue);
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
