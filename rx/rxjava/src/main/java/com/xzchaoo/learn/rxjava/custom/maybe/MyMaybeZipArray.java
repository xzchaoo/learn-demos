package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public class MyMaybeZipArray<T, U> extends Maybe<U> {
  private final Iterable<MaybeSource<? extends T>> sourceIterable;
  private final MaybeSource<? extends T>[] sources;
  private final Function<? super Object[], ? extends U> zipper;

  public MyMaybeZipArray(Iterable<MaybeSource<? extends T>> sourceIterable, MaybeSource<? extends T>[] sources, Function<? super Object[], ? extends U> zipper) {
    this.sourceIterable = sourceIterable;
    this.sources = sources;
    this.zipper = zipper;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void subscribeActual(MaybeObserver<? super U> observer) {
    MaybeSource<? extends T>[] sources = this.sources;
    int size = sources.length;

    if (size == 0) {
      EmptyDisposable.complete(observer);
    } else if (size == 1) {
      sources[0].subscribe(new MyMaybeMap.MapSubscriber<T, U>(observer, new AsSingleFunction<T, U>(zipper)));
    } else {
      ZipCoordinator<T, U> zc = new ZipCoordinator<>(observer, size, zipper);
      observer.onSubscribe(zc);

      for (int i = 0; i < size; ++i) {
        if (zc.isDisposed()) {
          return;
        }
        MaybeSource<? extends T> source = sources[i];
        // 能否省略这里的NPE检查 对于iterable是不用检查额s
        if (source == null) {
          zc.onChildError(i, new NullPointerException("source is null"));
          return;
        }

        source.subscribe(zc.subscribes[i]);
      }
    }
  }

  static final class ZipCoordinator<T, U> extends AtomicInteger implements Disposable {
    final MaybeObserver<? super U> observer;
    final Function<? super Object[], ? extends U> zipper;
    final Object[] results;
    final InnerSubscriber<T>[] subscribes;

    @SuppressWarnings("unchecked")
    ZipCoordinator(MaybeObserver<? super U> observer, int n, Function<? super Object[], ? extends U> zipper) {
      lazySet(n);
      this.observer = observer;
      this.zipper = zipper;
      this.results = new Object[n];
      this.subscribes = new InnerSubscriber[n];
      for (int i = 0; i < n; ++i) {
        this.subscribes[i] = new InnerSubscriber<>(this, i);
      }
    }

    void onChildSuccess(int index, Object t) {
      results[index] = t;
      if (decrementAndGet() == 0) {
        U u;
        try {
          u = ObjectHelper.requireNonNull(zipper.apply(results), "The zipper returned a null value");
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          observer.onError(e);
          return;
        }
        observer.onSuccess(u);
      }
    }

    void onChildComplete(int index) {
      if (getAndSet(0) > 0) {
        disposeExcept(index);
        observer.onComplete();
      }
    }

    void onChildError(int index, Throwable e) {
      if (getAndSet(0) > 0) {
        disposeExcept(index);
        observer.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    void disposeExcept(int index) {
      InnerSubscriber<T>[] ss = this.subscribes;
      int n = ss.length;
      for (int i = 0; i < index; ++i) {
        ss[i].dispose();
      }
      for (int i = index + 1; i < n; ++i) {
        ss[i].dispose();
      }
    }

    @Override
    public void dispose() {
      if (getAndSet(0) > 0) {
        for (InnerSubscriber<T> s : subscribes) {
          s.dispose();
        }
      }
    }

    @Override
    public boolean isDisposed() {
      return get() <= 0;
    }

  }

  static final class InnerSubscriber<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
    final ZipCoordinator zc;
    final int index;

    InnerSubscriber(ZipCoordinator<T, ?> zc, int index) {
      this.zc = zc;
      this.index = index;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.setOnce(this, d);
    }

    @Override
    public void onSuccess(T t) {
      zc.onChildSuccess(index, t);
    }

    @Override
    public void onError(Throwable e) {
      zc.onChildError(index, e);
    }

    @Override
    public void onComplete() {
      zc.onChildComplete(index);
    }

    void dispose() {
      DisposableHelper.dispose(this);
    }
  }

  static final class AsSingleFunction<T, U> implements Function<T, U> {
    final Function<? super Object[], ? extends U> zipper;

    AsSingleFunction(Function<? super Object[], ? extends U> zipper) {
      this.zipper = zipper;
    }

    @Override
    public U apply(T t) throws Exception {
      return ObjectHelper.requireNonNull(zipper.apply(new Object[]{t}), "The zipper returned a null value");
    }
  }
}
