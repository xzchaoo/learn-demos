package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public class MyMaybeToSingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Callable<? extends T> defaultValueSupplier;

  public MyMaybeToSingle(MaybeSource<T> source, Callable<? extends T> defaultValueSupplier) {
    this.source = source;
    this.defaultValueSupplier = defaultValueSupplier;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    source.subscribe(new ToSingleObserver<>(observer, defaultValueSupplier));
  }

  static final class ToSingleObserver<T> implements MaybeObserver<T>, Disposable {
    final SingleObserver<? super T> observer;
    final Callable<? extends T> defaultValueSupplier;
    Disposable d;

    ToSingleObserver(SingleObserver<? super T> observer, Callable<? extends T> defaultValueSupplier) {
      this.observer = observer;
      this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      d = DisposableHelper.DISPOSED;

      T value;
      try {
        value = ObjectHelper.requireNonNull(defaultValueSupplier.call(), "The defaultValueSupplier returned a null value");
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        observer.onError(e);
        return;
      }

      observer.onSuccess(value);
    }

    @Override
    public void onSuccess(T o) {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(o);
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
