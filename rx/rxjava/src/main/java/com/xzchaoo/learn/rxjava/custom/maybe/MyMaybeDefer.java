package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeDefer<T> extends Maybe<T> {
  private final Callable<? extends Maybe<? extends T>> maybeSupplier;

  public MyMaybeDefer(Callable<? extends Maybe<? extends T>> maybeSupplier) {
    this.maybeSupplier = maybeSupplier;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    final Maybe<? extends T> source;

    try {
      source = ObjectHelper.requireNonNull(maybeSupplier.call(), "The maybeSupplier returned a null MaybeSource");
    } catch (Throwable e) {
      Exceptions.throwIfFatal(e);
      EmptyDisposable.error(e, observer);
      return;
    }

    source.subscribe(observer);
  }
}