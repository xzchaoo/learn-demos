package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/10
 */
public class MySingleCreate<T> extends Single<T> {
  private final MySingleOnSubscribe<T> source;

  public MySingleCreate(MySingleOnSubscribe<T> source) {
    this.source = source;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    Emitter<T> emitter = new Emitter<>(observer);
    observer.onSubscribe(emitter);
    try {
      source.subscribe(emitter);
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      emitter.onError(ex);
    }
  }

  static final class Emitter<T> extends AtomicReference<Disposable> implements MySingleEmitter<T>, Disposable {

    SingleObserver<? super T> observer;

    Emitter(SingleObserver<? super T> observer) {
      this.observer = observer;
    }

    @Override
    public void onSuccess(T value) {
      if (get() != DisposableHelper.DISPOSED) {
        Disposable d = getAndSet(DisposableHelper.DISPOSED);
        if (d != DisposableHelper.DISPOSED) {
          try {
            if (value == null) {
              observer.onError(new NullPointerException("onSuccess called with null. Null values are generally not allowed in 2.x operators and sources."));
            } else {
              observer.onSuccess(value);
            }
          } finally {
            if (d != null) {
              d.dispose();
            }
          }
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      if (!tryOnError(t)) {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void setDisposable(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void setCancellable(Cancellable c) {
      setDisposable(new CancellableDisposable(c));
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public boolean tryOnError(Throwable t) {
      if (t == null) {
        t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
      }
      if (get() != DisposableHelper.DISPOSED) {
        Disposable d = getAndSet(DisposableHelper.DISPOSED);
        if (d != DisposableHelper.DISPOSED) {
          try {
            observer.onError(t);
          } finally {
            if (d != null) {
              d.dispose();
            }
          }
          return true;
        }
      }
      return false;
    }
  }
}
