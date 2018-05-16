package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeContains<T> extends Single<Boolean> implements HasUpstreamMaybeSource {
  private final MaybeSource<? extends T> source;
  private final T value;

  public MyMaybeContains(MaybeSource<? extends T> source, T value) {
    this.source = source;
    this.value = value;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Boolean> observer) {
    source.subscribe(new ContainsObserver(observer, value));
  }

  @Override
  public MaybeSource source() {
    return source;
  }

  static final class ContainsObserver implements MaybeObserver<Object>, Disposable {

    final SingleObserver<? super Boolean> observer;
    final Object value;
    Disposable d;

    ContainsObserver(SingleObserver<? super Boolean> observer, Object value) {
      this.observer = observer;
      this.value = value;
    }

    @Override
    public void onSubscribe(Disposable d) {
      // 这里了应该是为了防止 上游的 Disposable 直接传递给底层的observer 从而导致熔合时绕过本OP
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(Object t) {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(ObjectHelper.equals(t, this.value));
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      observer.onError(e);
    }

    @Override
    public void onComplete() {
      d = DisposableHelper.DISPOSED;
      observer.onSuccess(false);
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
