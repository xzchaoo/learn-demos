package com.xzchaoo.learn.rxjava.custom.single;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

/**
 * @author xzcha
 * @date 2018/5/10
 */
public interface MySingleEmitter<T> {

  void onSuccess(@NonNull T t);

  void onError(@NonNull Throwable t);

  void setDisposable(@Nullable Disposable s);

  void setCancellable(@Nullable Cancellable c);

  boolean isDisposed();

  @Experimental
  boolean tryOnError(@NonNull Throwable t);
}
