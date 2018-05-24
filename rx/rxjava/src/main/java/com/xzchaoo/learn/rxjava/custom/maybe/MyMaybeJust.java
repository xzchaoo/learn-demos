package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.fuseable.ScalarCallable;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public class MyMaybeJust<T> extends Maybe<T> implements ScalarCallable<T> {
  private final T value;

  public MyMaybeJust(T value) {
    this.value = value;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Disposable d = Disposables.empty();
    observer.onSubscribe(d);
    if (!d.isDisposed()) {
      observer.onSuccess(value);
    }

//    observer.onSubscribe(Disposables.disposed());
//    observer.onSuccess(value);
  }

  @Override
  public T call() {
    return value;
  }
}
