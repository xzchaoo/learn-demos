package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

/**
 * @author xzchaoo
 * @date 2018/5/15
 */
public final class MyFlowableConcatArray<T> extends Flowable<T> {
  final Publisher<? extends T>[] sources;
  final boolean delayErrors;

  public MyFlowableConcatArray(Publisher<? extends T>[] sources, boolean delayErrors) {
    this.sources = sources;
    this.delayErrors = delayErrors;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    Publisher<? extends T>[] sources = this.sources;
    int n = sources.length;
    if (n == 0) {
      EmptySubscription.complete(s);
    } else if (n == 1) {
      sources[0].subscribe(s);
    } else {
      ConcatCoordinator<T> parent = new ConcatCoordinator<>(sources, delayErrors, s);
      s.onSubscribe(parent);
      parent.subscribeNext();
    }
  }

  static final class ConcatCoordinator<T> extends AtomicReference<Subscription> implements Subscription {
    final Publisher<? extends T>[] sources;
    final boolean delayErrors;
    final Subscriber<? super T> actual;
    volatile boolean canceled;
    int index = 0;
    final AtomicLong totalRequested = new AtomicLong(0);
    final AtomicLong currentRequested = new AtomicLong();
    long emitted = 0;
    // add errors?

    ConcatCoordinator(Publisher<? extends T>[] sources, boolean delayErrors, Subscriber<? super T> actual) {
      this.sources = sources;
      this.delayErrors = delayErrors;
      this.actual = actual;
    }

    void subscribeNext() {
      Publisher<? extends T>[] sources = this.sources;
      int n = sources.length;
      int index = this.index;

      if (canceled) {
        return;
      }
      ++index;
      if (index == n) {
        // finished
        if (emitted < currentRequested.get()) {
          currentRequested.get()-emitted;
        }
        emitted = 0;
        actual.onComplete();
      }
      this.index = index;
      sources[index].subscribe(new ChildSubscriber<>(this, actual));
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        SubscriptionHelper.deferredRequest(this, totalRequested, n);
      }
    }

    @Override
    public void cancel() {
      canceled = true;
      SubscriptionHelper.cancel(this);
    }

    void onChildSubscribe(Subscription s) {
      if (SubscriptionHelper.replace(this, s)) {
        long r = totalRequested.getAndSet(0L);
        if (r != 0L) {
          s.request(r);
        }
      }
    }

    void onChildError(Throwable t) {
      actual.onError(t);
    }

    void onChildComplete() {
      this.subscribeNext();
    }
  }

  static final class ChildSubscriber<T> implements FlowableSubscriber<T> {
    final ConcatCoordinator<T> parent;
    final Subscriber<? super T> actual;

    ChildSubscriber(ConcatCoordinator<T> parent, Subscriber<? super T> actual) {
      this.parent = parent;
      this.actual = actual;
    }

    @Override
    public void onSubscribe(Subscription s) {
      parent.onChildSubscribe(s);
    }

    @Override
    public void onNext(T t) {
      actual.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
      parent.onChildError(t);
    }

    @Override
    public void onComplete() {
      parent.onChildComplete();
    }
  }
}
