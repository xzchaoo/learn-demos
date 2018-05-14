package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * @author xzcha
 * @date 2018/5/10
 */
public class MySingleDefer<T> extends Single<T> {
  private final Callable<? extends SingleSource<? extends T>> singleSupplier;

  public MySingleDefer(Callable<? extends SingleSource<? extends T>> singleSupplier) {
    this.singleSupplier = singleSupplier;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    SingleSource<? extends T> source;
    try {
      source = ObjectHelper.requireNonNull(singleSupplier.call(), "The singleSupplier returned a null SingleSource");
    } catch (Exception e) {
      Exceptions.throwIfFatal(e);
      // 为了保证一定给客户端一个 onSubscribe 回调吗?
      EmptyDisposable.error(e, observer);
      return;
    }
    source.subscribe(observer);
  }
}
