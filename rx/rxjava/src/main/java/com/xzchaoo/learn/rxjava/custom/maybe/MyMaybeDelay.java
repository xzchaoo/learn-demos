package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeDelay<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final long time;
  private final TimeUnit unit;
  private final Scheduler scheduler;

  public MyMaybeDelay(MaybeSource<T> source, long time, TimeUnit unit, Scheduler scheduler) {
    this.source = source;
    this.time = time;
    this.unit = unit;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new DelaySubscriber<T>(observer, time, unit, scheduler));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class DelaySubscriber<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    final MaybeObserver<? super T> observer;
    final long time;
    final TimeUnit unit;
    final Scheduler scheduler;

    Throwable error;
    T value;


    DelaySubscriber(MaybeObserver<? super T> observer, long time, TimeUnit unit, Scheduler scheduler) {
      this.observer = observer;
      this.time = time;
      this.unit = unit;
      this.scheduler = scheduler;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onError(Throwable e) {
      error = e;
      schedule();
    }

    @Override
    public void onComplete() {
      schedule();
    }

    @Override
    public void onSuccess(T value) {
      this.value = value;
      schedule();
    }

    void schedule() {
      DisposableHelper.replace(this, scheduler.scheduleDirect(this, time, unit));
    }

    @Override
    public void run() {
      Throwable e = this.error;
      if (e != null) {
        observer.onError(e);
      } else {
        final T v = this.value;
        if (v != null) {
          observer.onSuccess(v);
        } else {
          observer.onComplete();
        }
      }
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
