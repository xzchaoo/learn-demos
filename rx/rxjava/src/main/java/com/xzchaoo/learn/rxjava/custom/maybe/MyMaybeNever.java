package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeNever extends Maybe<Object> {
  public static final MyMaybeNever INSTANCE = new MyMaybeNever();

  @Override
  protected void subscribeActual(MaybeObserver<? super Object> observer) {
    observer.onSubscribe(EmptyDisposable.NEVER);
  }
}
