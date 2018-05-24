package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeSubscribeOn<T> extends Maybe<T> {
  private final MaybeSource<T> source;
  private final Scheduler scheduler;

  public MyMaybeSubscribeOn(MaybeSource<T> source, Scheduler scheduler) {
    this.source = source;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Inner<T> inner = new Inner<>(source, observer);
    observer.onSubscribe(inner);
    DisposableHelper.replace(inner, scheduler.scheduleDirect(inner));
  }

  static class Inner<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Runnable, Disposable {
    final MaybeSource<T> source;
    final MaybeObserver<? super T> observer;

    Inner(MaybeSource<T> source, MaybeObserver<? super T> observer) {
      this.source = source;
      this.observer = observer;
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
    public void run() {
      if (!isDisposed()) {
        source.subscribe(this);
      }
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T t) {
      observer.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      observer.onComplete();
    }
  }
}
