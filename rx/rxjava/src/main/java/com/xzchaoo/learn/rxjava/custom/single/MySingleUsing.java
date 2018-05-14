package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * TODO 有点混乱, 上游是否可以保证 一旦它调用了 onError onSuccess 方法, 那么它就可以被重复d?
 *
 * @author xzcha
 * @date 2018/5/10
 */
public final class MySingleUsing<T, R> extends Single<T> {
  /**
   * 提供资源
   */
  private final Callable<R> resourceSupplier;
  /**
   * Single的创建函数
   */
  private final Function<? super R, SingleSource<? extends T>> singleFunction;
  /**
   * 资源消灭器
   */
  private final Consumer<? super R> disposer;
  /**
   * 是否先于success或error方法之前先将资源消灭
   */
  private final boolean eager;

  public MySingleUsing(Callable<R> resourceSupplier, Function<? super R, SingleSource<? extends T>> singleFunction, Consumer<? super R> disposer, boolean eager) {
    this.resourceSupplier = resourceSupplier;
    this.singleFunction = singleFunction;
    this.disposer = disposer;
    this.eager = eager;
  }


  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    R resource;
    try {
      resource = resourceSupplier.call();
    } catch (Throwable e) {
      // 资源还没创建完成就失败了 提前失败
      Exceptions.throwIfFatal(e);
      EmptyDisposable.error(e, observer);
      return;
    }

    // 创建Single
    SingleSource<? extends T> source;
    try {
      source = ObjectHelper.requireNonNull(singleFunction.apply(resource), "The singleFunction returned a null SingleSource");
    } catch (Throwable e) {
      // 创建失败
      Exceptions.throwIfFatal(e);

      // 提前释放R
      if (eager) {
        try {
          disposer.accept(resource);
        } catch (Throwable e2) {
          Exceptions.throwIfFatal(e2);
          // 不隐藏掉错误
          e = new CompositeException(e, e2);
        }
      }

      EmptyDisposable.error(e, observer);

      if (!eager) {
        try {
          disposer.accept(resource);
        } catch (Throwable ex2) {
          Exceptions.throwIfFatal(ex2);
          // 没有人可以汇报了 只能报错
          RxJavaPlugins.onError(ex2);
        }
      }
      return;
    }

    source.subscribe(new Proxy<>(observer, disposer, resource, eager));
  }

  static class Proxy<T, R> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    final SingleObserver<? super T> observer;
    final Consumer<? super R> disposer;
    final R resource;
    final boolean eager;

    Proxy(SingleObserver<? super T> observer, Consumer<? super R> disposer, R resource, boolean eager) {
      this.observer = observer;
      this.disposer = disposer;
      this.resource = resource;
      this.eager = eager;
    }

    @Override
    public void onSubscribe(Disposable d) {
      // 理论上如果 onSubscribe被调用 那么这个分支一定会走进去
      if (DisposableHelper.set(this, d)) {
        observer.onSubscribe(this);
      }
    }

    private AtomicBoolean once = new AtomicBoolean(false);


    @Override
    public void onSuccess(T t) {
      // TODO 实现的时候用了2个原子类 能不能节省为1个
      // TODO 保证只被d一次

      if (eager && once.compareAndSet(false, true)) {
        try {
          disposer.accept(resource);
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          observer.onError(ex);
          return;
        }
      }

      observer.onSuccess(t);

      if (!eager) {
        disposeAfter();
      }
    }

    private void disposeAfter() {
      if (once.compareAndSet(false, true)) {
        try {
          disposer.accept(resource);
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          RxJavaPlugins.onError(ex);
        }
      }
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.dispose(this);

      // 理论上来说此时 上游已经被d(要么是别人d它, 要么是它自己d自己)了
      if (eager && once.compareAndSet(false, true)) {
        try {
          disposer.accept(resource);
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          e = new CompositeException(e, ex);
        }
      }

      observer.onError(e);

      if (!eager) {
        disposeAfter();
      }
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
      disposeAfter();
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }
}
