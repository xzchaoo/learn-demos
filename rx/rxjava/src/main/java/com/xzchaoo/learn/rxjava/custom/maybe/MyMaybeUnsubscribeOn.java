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
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeUnsubscribeOn<T> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Scheduler scheduler;

  public MyMaybeUnsubscribeOn(MaybeSource<T> source, Scheduler scheduler) {
    this.source = source;
    this.scheduler = scheduler;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, scheduler));
  }

  static final class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    final MaybeObserver<? super T> observer;
    final Scheduler scheduler;
    Disposable dBackup;

    InnerObserver(MaybeObserver<? super T> observer, Scheduler scheduler) {
      this.observer = observer;
      this.scheduler = scheduler;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        this.dBackup = d;
        observer.onSubscribe(this);
      }
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

    @Override
    public void dispose() {
      if (compareAndSet(dBackup, DisposableHelper.DISPOSED)) {
        scheduler.scheduleDirect(this);
      }
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void run() {
      dBackup.dispose();
    }
  }
}
