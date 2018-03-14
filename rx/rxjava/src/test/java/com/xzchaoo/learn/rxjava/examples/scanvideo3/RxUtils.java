package com.xzchaoo.learn.rxjava.examples.scanvideo3;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/3/5 0005
 */
public class RxUtils {
  public static <T> Single<T> toSingle(Callable<ListenableFuture<T>> c) {
    return toSingle(c, true);
  }

  public static <T> Single<T> toSingle(Callable<ListenableFuture<T>> c, boolean mayInterruptIfRunning) {
    Preconditions.checkNotNull(c);
    return Single.create(emitter -> {
      ListenableFuture<T> lf = c.call();
      emitter.setCancellable(() -> lf.cancel(mayInterruptIfRunning));
      Futures.addCallback(lf, new FutureCallback<T>() {
        @Override
        public void onSuccess(@Nullable T result) {
          //IO
          //System.out.println(Thread.currentThread().getName());
          if (result == null) {
            emitter.onError(new NoSuchElementException());
          } else {
            emitter.onSuccess(result);
          }
        }

        @Override
        public void onFailure(Throwable t) {
          emitter.onError(t);
        }
      }, MoreExecutors.directExecutor());
    });
  }

  public static <T> Maybe<T> toMaybe(Callable<ListenableFuture<T>> c) {
    return toMaybe(c, true);
  }

  public static <T> Maybe<T> toMaybe(Callable<ListenableFuture<T>> c, boolean mayInterruptIfRunning) {
    Preconditions.checkNotNull(c);
    return Maybe.create(emitter -> {
      ListenableFuture<T> lf = c.call();
      emitter.setCancellable(() -> lf.cancel(mayInterruptIfRunning));
      Futures.addCallback(lf, new FutureCallback<T>() {
        @Override
        public void onSuccess(@Nullable T result) {
          if (result == null) {
            emitter.onComplete();
          } else {
            emitter.onSuccess(result);
          }
        }

        @Override
        public void onFailure(Throwable t) {
          emitter.onError(t);
        }
      }, MoreExecutors.directExecutor());
    });
  }
}
