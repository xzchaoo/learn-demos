package com.xzchaoo.learn.rxjava.examples.scanvideo3;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/3/5 0005
 */
public class RxUtils {
  public static <T> Single<T> toSingle(Callable<ListenableFuture<T>> c) {
    Preconditions.checkNotNull(c);
    return Single.create(emitter -> {
      ListenableFuture<T> lf = c.call();
      emitter.setCancellable(() -> lf.cancel(true));
      Futures.addCallback(lf, new FutureCallback<T>() {
        @Override
        public void onSuccess(@Nullable T result) {
          emitter.onSuccess(result);
        }

        @Override
        public void onFailure(Throwable t) {
          emitter.onError(t);
        }
      }, MoreExecutors.directExecutor());
    });
  }
}
