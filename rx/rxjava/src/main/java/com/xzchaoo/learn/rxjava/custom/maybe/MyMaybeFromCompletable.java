package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamCompletableSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeFromCompletable<T> extends Maybe<T> implements HasUpstreamCompletableSource {
  private final CompletableSource source;

  public MyMaybeFromCompletable(CompletableSource source) {
    this.source = source;
  }

  @Override
  public CompletableSource source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer));
  }

  static final class InnerObserver<T> implements CompletableObserver, Disposable {
    final MaybeObserver<? super T> observer;
    private Disposable d;

    InnerObserver(MaybeObserver<? super T> observer) {
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
    public void onComplete() {
      d = DisposableHelper.DISPOSED;
      observer.onComplete();
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      observer.onError(e);
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
