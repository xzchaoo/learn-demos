package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeErrorCallable<T> extends Maybe<T> {
  private final Callable<? extends Throwable> errorSupplier;

  public MyMaybeErrorCallable(Callable<? extends Throwable> errorSupplier) {
    this.errorSupplier = errorSupplier;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Throwable e;
    try {
      e = errorSupplier.call();
    } catch (Throwable error) {
      Exceptions.throwIfFatal(error);
      EmptyDisposable.error(error, observer);
      return;
    }
    observer.onSubscribe(Disposables.disposed());
    observer.onError(e);
  }
}
