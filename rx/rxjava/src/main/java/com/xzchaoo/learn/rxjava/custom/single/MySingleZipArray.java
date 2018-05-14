package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleZipArray<T, R> extends Single<R> {
  /**
   * 源头的Single数组
   */
  final SingleSource<? extends T>[] sources;
  /**
   * zip函数
   */
  final Function<? super Object[], ? extends R> zipper;

  public MySingleZipArray(SingleSource<? extends T>[] sources, Function<? super Object[], ? extends R> zipper) {
    this.sources = sources;
    this.zipper = zipper;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super R> observer) {
    SingleSource<? extends T>[] sources = this.sources;
    int n = sources.length;

    // if (n == 1) {
    //   // 特殊处理
    //   return;
    // }

    val parent = new ZipParent<T, R>(observer, n, zipper);
    observer.onSubscribe(parent);

    for (int i = 0; i < n; ++i) {
      if (parent.isDisposed()) {
        return;
      }

      SingleSource<? extends T> source = sources[i];
      if (source == null) {
        parent.onChildError(i, new NullPointerException("One of the sources is null"));
        return;
      }

      source.subscribe(parent.observers[i]);
    }
  }

  static final class ZipParent<T, R> extends AtomicInteger implements Disposable {

    final SingleObserver<? super R> actual;
    final Function<? super Object[], ? extends R> zipper;
    final ChildObserver<T>[] observers;
    final Object[] childResults;

    @SuppressWarnings("unchecked")
    ZipParent(SingleObserver<? super R> actual, int n, Function<? super Object[], ? extends R> zipper) {
      super(n);
      this.actual = actual;
      this.zipper = zipper;
      this.observers = new ChildObserver[n];
      this.childResults = new Object[n];
      for (int i = 0; i < n; ++i) {
        this.observers[i] = new ChildObserver(this, i);
      }
    }

    @Override
    public void dispose() {
      if (getAndSet(0) > 0) {
        for (ChildObserver<T> childObserver : observers) {
          childObserver.dispose();
        }
      }
    }

    void onChildSuccess(int index, Object result) {
      childResults[index] = result;
      if (decrementAndGet() == 0) {
        R zipResult;
        try {
          zipResult = ObjectHelper.requireNonNull(zipper.apply(childResults), "The zipper returned a null value");
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          actual.onError(e);
          return;
        }
        actual.onSuccess(zipResult);
      }
    }

    void disposeExcept(int index) {
      for (int i = 0; i < observers.length; ++i) {
        if (i != index) {
          observers[i].dispose();
        }
      }
    }

    void onChildError(int index, Throwable error) {
      if (getAndSet(0) > 0) {
        disposeExcept(index);
        actual.onError(error);
      } else {
        RxJavaPlugins.onError(error);
      }
    }

    @Override
    public boolean isDisposed() {
      return get() <= 0;
    }
  }

  static final class ChildObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
    final ZipParent<T, ?> parent;
    final int index;

    ChildObserver(ZipParent<T, ?> parent, int index) {
      this.parent = parent;
      this.index = index;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.setOnce(this, d);
    }

    @Override
    public void onSuccess(T t) {
      parent.onChildSuccess(index, t);
    }

    @Override
    public void onError(Throwable e) {
      parent.onChildError(index, e);
    }

    void dispose() {
      DisposableHelper.dispose(this);
    }
  }
}
