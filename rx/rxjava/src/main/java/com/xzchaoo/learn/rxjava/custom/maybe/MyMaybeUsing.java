package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public class MyMaybeUsing<T, R> extends Maybe<T> {
  private final Function<? super R, MaybeSource<? extends T>> sourceSupplier;
  private final Callable<? extends R> resourceSupplier;
  private final Consumer<? super R> resourceDisposer;
  private final boolean eager;

  public MyMaybeUsing(Function<? super R, MaybeSource<? extends T>> sourceSupplier, Callable<? extends R> resourceSupplier, Consumer<? super R> resourceDisposer, boolean eager) {
    this.sourceSupplier = sourceSupplier;
    this.resourceSupplier = resourceSupplier;
    this.resourceDisposer = resourceDisposer;
    this.eager = eager;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    // create resource first
    R resource;
    try {
      resource = resourceSupplier.call();
    } catch (Exception e) {
      Exceptions.throwIfFatal(e);
      EmptyDisposable.error(e, observer);
      return;
    }

    // create MaybeSource
    MaybeSource<? extends T> source;
    try {
      source = ObjectHelper.requireNonNull(sourceSupplier.apply(resource), "The sourceSupplier returned a null MaybeSource");
    } catch (Throwable e) {
      Exceptions.throwIfFatal(e);
      if (eager) {
        try {
          resourceDisposer.accept(resource);
        } catch (Throwable e2) {
          Exceptions.throwIfFatal(e2);
          EmptyDisposable.error(new CompositeException(e, e2), observer);
          return;
        }
      }

      try {
        EmptyDisposable.error(e, observer);
      } finally {
        if (!eager) {
          try {
            resourceDisposer.accept(resource);
          } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(e2);
          }
        }
      }

      return;
    }

    source.subscribe(new UsingObserver<T, R>(observer, resource, resourceDisposer, eager));

  }

  static final class UsingObserver<T, R> extends AtomicReference<R> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> observer;
    final Consumer<? super R> resourceDisposer;
    final boolean eager;
    Disposable d;

    UsingObserver(MaybeObserver<? super T> observer, R resource, Consumer<? super R> resourceDisposer, boolean eager) {
      this.observer = observer;
      this.resourceDisposer = resourceDisposer;
      this.eager = eager;
      lazySet(resource);
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
      d = DisposableHelper.DISPOSED;
      if (eager) {
        R r = getAndSet(null);
        if (r != null) {
          try {
            resourceDisposer.accept(r);
          } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            observer.onError(e);
            return;
          }
        }
      }

      try {
        observer.onSuccess(t);

      } finally {
        if (!eager) {
          disposeResourceAfter();
        }
      }
    }

    void disposeResourceAfter() {
      R r = getAndSet(null);
      if (r != null) {
        try {
          resourceDisposer.accept(r);
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          RxJavaPlugins.onError(e);
        }
      }
    }

    @Override
    public void onError(Throwable e) {
      d = DisposableHelper.DISPOSED;
      if (eager) {
        R r = getAndSet(null);
        if (r != null) {
          try {
            resourceDisposer.accept(r);
          } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            e = new CompositeException(e, e2);
          }
        }
      }

      try {
        observer.onError(e);
      } finally {
        if (!eager) {
          disposeResourceAfter();
        }
      }
    }

    @Override
    public void onComplete() {
      d = DisposableHelper.DISPOSED;

      if (eager) {
        R r = getAndSet(null);
        if (r != null) {
          try {
            resourceDisposer.accept(r);
          } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            observer.onError(e2);
            return;
          }
        }
      }

      try {
        observer.onComplete();
      } finally {
        if (!eager) {
          disposeResourceAfter();
        }
      }

    }

    @Override
    public void dispose() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
      disposeResourceAfter();
    }

    @Override
    public boolean isDisposed() {
      return d.isDisposed();
    }
  }
}
