package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeError<T> extends Maybe<T> {
  private final Throwable error;

  public MyMaybeError(Throwable error) {
    this.error = error;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    EmptyDisposable.error(error, observer);
  }
}
