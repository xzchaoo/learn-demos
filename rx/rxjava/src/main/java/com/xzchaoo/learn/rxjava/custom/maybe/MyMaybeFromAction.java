package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeFromAction<T> extends Maybe<T> implements Callable<T> {
  private final Action action;

  public MyMaybeFromAction(Action action) {
    this.action = action;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Disposable d = Disposables.empty();
    observer.onSubscribe(d);
    if (!d.isDisposed()) {
      try {
        action.run();
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!d.isDisposed()) {
          EmptyDisposable.error(e, observer);
        } else {
          RxJavaPlugins.onError(e);
        }
        return;
      }
      if (!d.isDisposed()) {
        observer.onComplete();
      }
    }
  }

  @Override
  public T call() throws Exception {
    action.run();
    return null;
  }
}
