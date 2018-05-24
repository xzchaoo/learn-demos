package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeTakeUntilMaybe<T, U> extends Maybe<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final MaybeSource<U> until;

  public MyMaybeTakeUntilMaybe(MaybeSource<T> source, MaybeSource<U> until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    // 1. 立即被dispose

    InnerObserver<T, U> parent = new InnerObserver<>(observer);
    observer.onSubscribe(parent);

    until.subscribe(parent.untilObserver);
    source.subscribe(parent);
  }

  static final class InnerObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final UntilObserver<U> untilObserver;

    InnerObserver(MaybeObserver<? super T> observer) {
      this.observer = observer;
      this.untilObserver = new UntilObserver<>(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T t) {
      DisposableHelper.dispose(untilObserver);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onSuccess(t);
      }
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.dispose(untilObserver);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    @Override
    public void onComplete() {
      DisposableHelper.dispose(untilObserver);
      if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        observer.onComplete();
      }
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
      DisposableHelper.dispose(untilObserver);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    void onUntilComplete() {
      if (DisposableHelper.dispose(this)) {
        observer.onComplete();
      }
    }

    void onUntilError(Throwable e) {
      if (DisposableHelper.dispose(this)) {
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }
  }

  static final class UntilObserver<U> extends AtomicReference<Disposable> implements MaybeObserver<U> {
    final InnerObserver<?, U> parent;

    UntilObserver(InnerObserver<?, U> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(U u) {
      parent.onUntilComplete();
    }

    @Override
    public void onError(Throwable e) {
      parent.onUntilError(e);
    }

    @Override
    public void onComplete() {
      parent.onUntilComplete();
    }
  }
}
