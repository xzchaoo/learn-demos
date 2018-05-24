package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/12
 */
public final class MyMaybeFlatMapSingle<T, U> extends Maybe<U> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;
  private final Function<? super T, SingleSource<? extends U>> mapper;

  public MyMaybeFlatMapSingle(MaybeSource<T> source, Function<? super T, SingleSource<? extends U>> mapper) {
    this.source = source;
    this.mapper = mapper;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super U> observer) {
    source.subscribe(new FlatMapObserver<>(observer, mapper));
  }

  static final class FlatMapObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private final MaybeObserver<? super U> observer;
    private final Function<? super T, SingleSource<? extends U>> mapper;

    FlatMapObserver(MaybeObserver<? super U> observer, Function<? super T, SingleSource<? extends U>> mapper) {

      this.observer = observer;
      this.mapper = mapper;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      SingleSource<? extends U> source;
      try {
        source = mapper.apply(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        observer.onError(e);
        return;
      }
      source.subscribe(new InnerObserver<>(this, observer));
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
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }

  static final class InnerObserver<U> implements SingleObserver<U> {
    final AtomicReference<Disposable> parent;
    final MaybeObserver<? super U> observer;

    InnerObserver(AtomicReference<Disposable> parent, MaybeObserver<? super U> observer) {
      this.parent = parent;
      this.observer = observer;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(parent, d);
    }

    @Override
    public void onSuccess(U u) {
      observer.onSuccess(u);
    }

    @Override
    public void onError(Throwable e) {
      observer.onError(e);
    }
  }
}
