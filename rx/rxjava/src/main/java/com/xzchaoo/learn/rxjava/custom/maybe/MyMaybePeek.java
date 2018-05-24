package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybePeek<T> extends Maybe<T> {
  private final MaybeSource<T> source;
  private final Consumer<? super Disposable> subscribeConsumer;
  private final Consumer<? super T> successConsumer;
  private final Consumer<? super Throwable> errorConsumer;
  private final Action completeAction;
  private final Action afterTerminateAction;
  private final Action disposeAction;

  public MyMaybePeek(MaybeSource<T> source, Consumer<? super Disposable> subscribeConsumer, Consumer<? super T> successConsumer, Consumer<? super Throwable> errorConsumer, Action completeAction, Action afterTerminateAction, Action disposeAction) {
    this.source = source;
    this.subscribeConsumer = subscribeConsumer;
    this.successConsumer = successConsumer;
    this.errorConsumer = errorConsumer;
    this.completeAction = completeAction;
    this.afterTerminateAction = afterTerminateAction;
    this.disposeAction = disposeAction;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new InnerObserver<>(observer, subscribeConsumer, successConsumer, errorConsumer, completeAction, afterTerminateAction, disposeAction));
  }

  static final class InnerObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {

    final MaybeObserver<? super T> observer;
    final Consumer<? super Disposable> subscribeConsumer;
    final Consumer<? super T> successConsumer;
    final Consumer<? super Throwable> errorConsumer;
    final Action completeAction;
    final Action afterTerminateAction;
    final Action disposeAction;

    InnerObserver(MaybeObserver<? super T> observer, Consumer<? super Disposable> subscribeConsumer, Consumer<? super T> successConsumer, Consumer<? super Throwable> errorConsumer, Action completeAction, Action afterTerminateAction, Action disposeAction) {
      this.observer = observer;
      this.subscribeConsumer = subscribeConsumer;
      this.successConsumer = successConsumer;
      this.errorConsumer = errorConsumer;
      this.completeAction = completeAction;
      this.afterTerminateAction = afterTerminateAction;
      this.disposeAction = disposeAction;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        try {
          subscribeConsumer.accept(d);
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          d.dispose();
          lazySet(DisposableHelper.DISPOSED);
          EmptyDisposable.error(e, observer);
          return;
        }

        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      if (successConsumer != null) {
        try {
          successConsumer.accept(t);
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          observer.onError(e);
          return;
        }
      }
      observer.onSuccess(t);
      onAfterTerminate();
    }

    @Override
    public void onError(Throwable e) {
      if (errorConsumer != null) {
        try {
          errorConsumer.accept(e);
        } catch (Throwable e2) {
          Exceptions.throwIfFatal(e2);
          observer.onError(new CompositeException(e, e2));
          return;
        }
      }
      observer.onError(e);
      onAfterTerminate();
    }

    @Override
    public void onComplete() {
      if (completeAction != null) {
        try {
          completeAction.run();
        } catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          observer.onError(e);
          return;
        }
      }
      observer.onComplete();
      onAfterTerminate();
    }

    @Override
    public void dispose() {
      Disposable d = getAndSet(DisposableHelper.DISPOSED);
      if (d != DisposableHelper.DISPOSED) {

        if (disposeAction != null) {
          try {
            disposeAction.run();
          } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
          }
        }

        d.dispose();
      }
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    void onAfterTerminate() {
      try {
        afterTerminateAction.run();
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        RxJavaPlugins.onError(ex);
      }
    }
  }
}
