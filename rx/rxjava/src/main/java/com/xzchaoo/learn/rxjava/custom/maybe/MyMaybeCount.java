package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public class MyMaybeCount<T> extends Single<Long> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;

  public MyMaybeCount(MaybeSource<T> source) {
    this.source = source;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Long> observer) {
    source.subscribe(new CountObserver(observer));
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  static final class CountObserver implements MaybeObserver<Object>, Disposable {
    final SingleObserver<? super Long> observer;
    Disposable d;

    CountObserver(SingleObserver<? super Long> observer) {
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
    public void onSuccess(Object o) {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(1L);
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(0L);
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
