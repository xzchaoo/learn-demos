package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public final class MyMaybeOnErrorNext<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Function<? super Throwable, ? extends MaybeSource<T>> resumeFunction;

  public MyMaybeOnErrorNext(MaybeSource<T> source, Function<? super Throwable, ? extends MaybeSource<T>> resumeFunction) {
    this.source = source;
    this.resumeFunction = resumeFunction;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, resumeFunction));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Function<? super Throwable, ? extends MaybeSource<T>> resumeFunction;

    boolean resumeMode;

    public InnerObserver(MaybeObserver<? super T> observer, Function<? super Throwable, ? extends MaybeSource<T>> resumeFunction) {
      this.observer = observer;
      this.resumeFunction = resumeFunction;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (resumeMode) {
        DisposableHelper.replace(this, d);
      } else {
        if (DisposableHelper.setOnce(this, d)) {
          observer.onSubscribe(this);
        }

      }
    }

    @Override
    public void onSuccess(T t) {
      observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      if (resumeMode) {
        observer.onError(e);
      } else {
        MaybeSource<T> newSource;
        try {
          newSource = ObjectHelper.requireNonNull(resumeFunction.apply(e), "The resumeFunction returned a null value");
        } catch (Exception e2) {
          Exceptions.throwIfFatal(e2);
          observer.onError(new CompositeException(e, e2));
          return;
        }
        resumeMode = true;
        newSource.subscribe(this);
      }
    }

    @Override
    public void onComplete() {
      observer.onComplete();
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }
}
