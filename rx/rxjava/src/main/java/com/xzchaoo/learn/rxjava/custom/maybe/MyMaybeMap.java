package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public class MyMaybeMap<T, U> extends MyAbstractMaybeWithUpstream<T, U> {
  private final Function<? super T, ? extends U> mapper;

  public MyMaybeMap(MaybeSource<T> source, Function<? super T, ? extends U> mapper) {
    super(source);
    this.mapper = mapper;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super U> observer) {
    observer.onSubscribe(new MapSubscriber<>(observer, mapper));
  }

  static final class MapSubscriber<T, U> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super U> observer;
    final Function<? super T, ? extends U> mapper;
    Disposable d;

    MapSubscriber(MaybeObserver<? super U> observer, Function<? super T, ? extends U> mapper) {
      this.observer = observer;
      this.mapper = mapper;
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
      U u;
      try {
        u = ObjectHelper.requireNonNull(mapper.apply(t), "The zipper returned a null value");
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        observer.onError(e);
        return;
      }
      observer.onSuccess(u);
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
