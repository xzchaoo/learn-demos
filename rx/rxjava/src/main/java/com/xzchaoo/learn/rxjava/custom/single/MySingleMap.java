package com.xzchaoo.learn.rxjava.custom.single;

import java.util.function.Function;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleMap<T, R> extends Single<R> {
  private final SingleSource<? extends T> source;
  private final Function<? super T, ? extends R> mapper;

  public MySingleMap(SingleSource<? extends T> source, Function<? super T, ? extends R> mapper) {
    this.source = source;
    this.mapper = mapper;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super R> observer) {
    source.subscribe(new SingleMap<T, R>(observer, mapper));
  }

  /**
   * map的功能仅仅只是转换 因此它只需要拦截onSuccess操作 其它操作都是 "穿透" 到底层
   *
   * @param <T>
   * @param <R>
   */
  static final class SingleMap<T, R> implements SingleObserver<T> {
    final SingleObserver<? super R> observer;
    final Function<? super T, ? extends R> mapper;

    SingleMap(SingleObserver<? super R> observer, Function<? super T, ? extends R> mapper) {
      this.observer = observer;
      this.mapper = mapper;
    }

    @Override
    public void onSubscribe(Disposable d) {
      observer.onSubscribe(d);
    }

    @Override
    public void onSuccess(T t) {
      R mapResult;
      try {
        mapResult = ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null value");
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        observer.onError(ex);
        return;
      }
      observer.onSuccess(mapResult);
    }

    @Override
    public void onError(Throwable e) {
      observer.onError(e);
    }
  }
}
