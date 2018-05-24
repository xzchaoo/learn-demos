package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzchaoo
 * @date 2018/5/10
 */
public class MySingleError<T> extends Single<T> {
  final Callable<? extends Throwable> errorSupplier;

  public MySingleError(Callable<? extends Throwable> errorSupplier) {
    this.errorSupplier = errorSupplier;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    Throwable error;
    try {
      error = errorSupplier.call();
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      error = ex;
    }
    EmptyDisposable.error(error, observer);
  }
}
