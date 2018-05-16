package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.ScalarCallable;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeEmpty extends Maybe<Object> implements ScalarCallable<Object> {
  public static final MyMaybeEmpty INSTANCE = new MyMaybeEmpty();

  @Override
  protected void subscribeActual(MaybeObserver<? super Object> observer) {
    EmptyDisposable.complete(observer);
  }

  @Override
  public Object call() {
    // 没有数据
    return null;
  }
}
