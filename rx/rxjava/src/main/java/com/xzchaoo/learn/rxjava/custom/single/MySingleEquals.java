package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/10
 */
public final class MySingleEquals<T> extends Single<Boolean> {
  private final SingleSource<? extends T> first;
  private final SingleSource<? extends T> second;

  public MySingleEquals(SingleSource<? extends T> first, SingleSource<? extends T> second) {
    this.first = first;
    this.second = second;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Boolean> observer) {
    CompositeDisposable cd = new CompositeDisposable();
    observer.onSubscribe(cd);

    Proxy proxy = new Proxy(observer, cd);

    first.subscribe(new SingleObserver<T>() {
      @Override
      public void onSubscribe(Disposable d) {
        cd.add(d);
      }

      @Override
      public void onSuccess(T t) {
        proxy.onChildSuccess(0, t);
      }

      @Override
      public void onError(Throwable e) {
        proxy.onChildError(0, e);
      }
    });
    second.subscribe(new SingleObserver<T>() {
      @Override
      public void onSubscribe(Disposable d) {
        cd.add(d);
      }

      @Override
      public void onSuccess(T t) {
        proxy.onChildSuccess(1, t);
      }

      @Override
      public void onError(Throwable e) {
        proxy.onChildError(1, e);
      }
    });
  }

  static final class Proxy {
    final SingleObserver<? super Boolean> observer;
    final AtomicInteger ai = new AtomicInteger(0);
    final Object[] values = new Object[2];
    final CompositeDisposable cd;

    Proxy(SingleObserver<? super Boolean> observer, CompositeDisposable cd) {
      this.observer = observer;
      this.cd = cd;
    }

    void onChildSuccess(int i, Object t) {
      values[i] = t;
      if (ai.incrementAndGet() == 2) {
        boolean result;
        try {
          result = values[0].equals(values[1]);
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          observer.onError(ex);
          return;
        }
        // ?
        observer.onSuccess(result);
      }
    }

    void onChildError(int i, Throwable e) {
      // 如果两个S都失败了 需要保证只被d一次
      for (; ; ) {
        int state = ai.get();
        if (state >= 2) {
          // 对于第二个error也不能忽略 需要打印信息
          RxJavaPlugins.onError(e);
          return;
        }
        if (ai.compareAndSet(state, 2)) {
          // TODO 先d再E 还是 先E再d ?
          // 看了一下其他代码, 好像是先d在E
          cd.dispose();
          observer.onError(e);
          return;
        }
      }
    }
  }
}
