package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamSingleSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public class MyMaybeFromSingle<T> extends Maybe<T> implements HasUpstreamSingleSource<T> {
  private final SingleSource<T> source;

  public MyMaybeFromSingle(SingleSource<T> source) {
    this.source = source;
  }

  @Override
  public SingleSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer));
  }

  static final class InnerObserver<T> implements SingleObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    Disposable d;

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
    public void onSuccess(T t) {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(t);
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
