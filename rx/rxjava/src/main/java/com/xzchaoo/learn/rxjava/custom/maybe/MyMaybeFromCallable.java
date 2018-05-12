package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeFromCallable<T> extends Maybe<T> implements Callable<T> {
  private final Callable<T> callable;

  public MyMaybeFromCallable(Callable<T> callable) {
    this.callable = callable;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Disposable d = Disposables.empty();
    observer.onSubscribe(d);

    if (!d.isDisposed()) {

      T v;
      try {
        v = callable.call();
      } catch (Exception e) {
        Exceptions.throwIfFatal(e);
        if (!d.isDisposed()) {
          observer.onError(e);
        } else {
          RxJavaPlugins.onError(e);
        }
        return;
      }

      if (!d.isDisposed()) {
        if (v != null) {
          observer.onSuccess(v);
        } else {
          observer.onComplete();
        }
      }

    }
  }

  @Override
  public T call() throws Exception {
    return callable.call();
  }
}
