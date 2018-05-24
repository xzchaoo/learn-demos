package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author zcxu
 * @date 2018/5/15 0015
 */
public final class MyFlowableCompletable<T, R> extends Completable {
  private final Flowable<T> source;
  private final boolean delayErrors;
  private final int maxConcurrency;
  private final Function<? super T, CompletableSource> mapper;

  public MyFlowableCompletable(Flowable<T> source, boolean delayErrors, int maxConcurrency, Function<? super T,
    CompletableSource> mapper) {
    this.source = source;
    this.delayErrors = delayErrors;
    this.maxConcurrency = maxConcurrency;
    this.mapper = mapper;
  }

  @Override
  protected void subscribeActual(CompletableObserver s) {
    Coodinator<T> parent = new Coodinator<>(s, delayErrors, maxConcurrency, mapper);
    s.onSubscribe(parent);
    source.subscribe(parent);
  }

  static final class Coodinator<T> implements FlowableSubscriber<T>,
    Disposable {
    final CompletableObserver observer;
    final boolean delayErrors;
    final int maxConcurrency;
    final AtomicReference<Subscription> s = new AtomicReference<>();
    final Function<? super T, CompletableSource> mapper;
    CompositeDisposable cd = new CompositeDisposable();

    AtomicInteger counter = new AtomicInteger();
    AtomicThrowable errors = new AtomicThrowable();

    Coodinator(CompletableObserver observer, boolean delayErrors, int maxConcurrency, Function<? super T,
      CompletableSource> mapper) {
      this.observer = observer;
      this.delayErrors = delayErrors;
      this.maxConcurrency = maxConcurrency;
      this.mapper = mapper;
      counter.lazySet(1);
    }

    @Override
    public void dispose() {
      if (s.get() != SubscriptionHelper.CANCELLED) {
        s.getAndSet(SubscriptionHelper.CANCELLED);
      }
    }

    @Override
    public boolean isDisposed() {
      return s.get() == SubscriptionHelper.CANCELLED;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
      if (s.compareAndSet(null, subscription)) {
        subscription.request(Long.MAX_VALUE);
      } else {
        subscription.cancel();
      }
    }

    @Override
    public void onNext(T t) {
      // nothing
      CompletableSource source;
      try {
        source = ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null value");
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        // TODO 这个错误不能delay吧?
        // TODO protect
        observer.onError(e);
        return;
      }
      if (counter.incrementAndGet() > 0) {
        source.subscribe(new ChildObserver(this));
      }
    }

    @Override
    public void onError(Throwable e) {

      // TODO protect
      observer.onError(e);
      // RxJavaPlugins.onError(e);
    }

    @Override
    public void onComplete() {
      checkComplete();
    }

    void checkComplete() {
      if (counter.decrementAndGet() == 0) {
        Throwable error = errors.terminate();
        if (error == null) {
          observer.onComplete();
        } else {
          observer.onError(error);
        }
      }
    }

    void onChildSubscribe(Disposable d) {
      cd.add(d);
    }

    /**
     * 会有多线程
     *
     * @param d
     */
    void onChildComplete(Disposable d) {
      cd.remove(d);
      checkComplete();
    }

    /**
     * 会有多线程
     *
     * @param e
     */
    void onChildError(Throwable e) {
      if (delayErrors) {
        if (errors.addThrowable(e)) {
          checkComplete();
        } else {
          RxJavaPlugins.onError(e);
        }
      } else {
        // TODO protect
        observer.onError(e);
      }
    }
  }

  static final class ChildObserver implements CompletableObserver, Disposable {
    final Coodinator<?> parent;
    Disposable d;

    ChildObserver(Coodinator<?> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        parent.onChildSubscribe(d);
      }
    }

    @Override
    public void onComplete() {
      parent.onChildComplete(d);
    }

    @Override
    public void onError(Throwable e) {
      parent.onChildError(e);
    }

    @Override
    public void dispose() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
    }

    @Override
    public boolean isDisposed() {
      return d.isDisposed();
    }
  }
}
