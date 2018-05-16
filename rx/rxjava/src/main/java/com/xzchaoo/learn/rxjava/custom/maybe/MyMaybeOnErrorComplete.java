package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeOnErrorComplete<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Predicate<? super Throwable> predicate;

  public MyMaybeOnErrorComplete(MaybeSource<T> source, Predicate<? super Throwable> predicate) {
    this.source = source;
    this.predicate = predicate;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, predicate));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static class InnerObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    private Predicate<? super Throwable> predicate;

    Disposable d;

    InnerObserver(MaybeObserver<? super T> observer, Predicate<? super Throwable> predicate) {
      this.observer = observer;
      this.predicate = predicate;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        observer.onSubscribe(d);
      }
    }

    @Override
    public void onSuccess(T t) {
      observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      boolean b;
      try {
        b = predicate.test(e);
      } catch (Throwable e2) {
        Exceptions.throwIfFatal(e2);
        observer.onError(new CompositeException(e, e2));
        return;
      }
      if (b) {
        observer.onComplete();
      } else {
        observer.onError(e);
      }
    }

    @Override
    public void onComplete() {
      observer.onComplete();
    }

    @Override
    public void dispose() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
    }

    @Override
    public boolean isDisposed() {
      return d.isDisposed();
    }
  }
}
