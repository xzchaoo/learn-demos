package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeFromFuture<T> extends Maybe<T> {
  private final Future<T> future;
  private final long timeout;
  private final TimeUnit unit;

  public MyMaybeFromFuture(Future<T> future, long timeout, TimeUnit unit) {
    this.future = future;
    this.timeout = timeout;
    this.unit = unit;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Disposable d = Disposables.empty();
    observer.onSubscribe(d);

    if (!d.isDisposed()) {

      T t;
      try {

        if (timeout <= 0) {
          t = future.get();
        } else {
          t = future.get(timeout, unit);
        }

      } catch (InterruptedException e) {
        if (!d.isDisposed()) {
          observer.onError(e);
        }
        return;
      } catch (ExecutionException e) {
        if (!d.isDisposed()) {
          observer.onError(e.getCause());
        }
        return;
      } catch (TimeoutException e) {
        if (!d.isDisposed()) {
          observer.onError(e);
        }
        return;
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!d.isDisposed()) {
          observer.onError(e);
        }
        return;
      }

      if (!d.isDisposed()) {
        if (t != null) {
          observer.onSuccess(t);
        } else {
          observer.onComplete();
        }
      }

    }
  }
}
