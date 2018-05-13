package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public final class MyMaybeObserveOn<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Scheduler scheduler;

  public MyMaybeObserveOn(MaybeSource<T> source, Scheduler scheduler) {
    this.source = source;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, scheduler));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    final MaybeObserver<? super T> observer;
    final Scheduler scheduler;

    Throwable error;
    T value;

    InnerObserver(MaybeObserver<? super T> observer, Scheduler scheduler) {
      this.observer = observer;
      this.scheduler = scheduler;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      this.value = t;
      schedule();
    }

    @Override
    public void onError(Throwable e) {
      this.error = e;
      schedule();
    }

    @Override
    public void onComplete() {
      schedule();
    }

    void schedule() {
      DisposableHelper.replace(this, scheduler.scheduleDirect(this));
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
      Throwable e = this.error;
      if (e != null) {
        observer.onError(e);
      } else {
        T v = this.value;
        if (v != null) {
          observer.onSuccess(v);
        } else {
          observer.onComplete();
        }
      }
    }
  }
}
