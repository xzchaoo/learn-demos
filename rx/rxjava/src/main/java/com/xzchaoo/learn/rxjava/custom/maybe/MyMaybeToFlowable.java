package com.xzchaoo.learn.rxjava.custom.maybe;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeToFlowable<T> extends Flowable<T> implements HasUpstreamMaybeSource<T> {
  private final MaybeSource<T> source;

  public MyMaybeToFlowable(MaybeSource<T> source) {
    this.source = source;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    source.subscribe(new InnerObserver<>(s));
  }

  static final class InnerObserver<T> extends DeferredScalarSubscription<T> implements MaybeObserver<T>, Subscription {
    Disposable d;

    InnerObserver(Subscriber<? super T> subscriber) {
      super(subscriber);
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.d, d)) {
        this.d = d;
        actual.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      super.complete(t);
    }

    @Override
    public void onError(Throwable e) {
      actual.onError(e);
    }

    @Override
    public void onComplete() {
      actual.onComplete();
    }

    @Override
    public void cancel() {
      d.dispose();
      d = DisposableHelper.DISPOSED;
      super.cancel();
    }
  }
}
