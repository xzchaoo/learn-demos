package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public final class MyMaybeToCompletable<T> extends Completable implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;

  public MyMaybeToCompletable(MaybeSource<T> source) {
    this.source = source;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
    source.subscribe(new InnerObserver<T>(observer));
  }

  static final class InnerObserver<T> implements MaybeObserver<T>, Disposable {
    final CompletableObserver observer;
    Disposable d;

    InnerObserver(CompletableObserver observer) {
      this.observer = observer;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      d = DisposableHelper.DISPOSED;
      observer.onComplete();
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      d = DisposableHelper.DISPOSED;
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
