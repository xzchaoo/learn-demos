package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeDelayWithCompletable<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Completable completable;

  public MyMaybeDelayWithCompletable(MaybeSource<T> source, Completable completable) {
    this.source = source;
    this.completable = completable;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new DelayObserver<>(observer, completable));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class DelayObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Completable completable;

    DelayObserver(MaybeObserver<? super T> observer, Completable completable) {
      this.observer = observer;
      this.completable = completable;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      subscribeNext(null, t);
    }

    @Override
    public void onError(Throwable e) {
      subscribeNext(e, null);
    }

    @Override
    public void onComplete() {
      subscribeNext(null, null);
    }

    void subscribeNext(Throwable error, T value) {
      completable.subscribe(new InnerCompletableObserver<>(this, observer, error, value));
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

  static final class InnerCompletableObserver<T> implements CompletableObserver {
    final AtomicReference<Disposable> parent;
    final MaybeObserver<T> observer;
    final Throwable error;
    final T value;

    InnerCompletableObserver(AtomicReference<Disposable> parent, MaybeObserver<T> observer, Throwable error, T value) {
      this.parent = parent;
      this.observer = observer;
      this.error = error;
      this.value = value;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(parent, d);
    }

    @Override
    public void onComplete() {
      Throwable e = this.error;
      if (e != null) {
        observer.onError(e);
      } else {
        T t = this.value;
        if (t != null) {
          observer.onSuccess(t);
        } else {
          observer.onComplete();
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      Throwable e = this.error;
      if (e != null) {
        observer.onError(new CompositeException(e, t));
      } else {
        observer.onError(t);
      }
    }
  }
}
