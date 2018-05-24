package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;

/**
 * @author xzchaoo
 * @date 2018/5/14
 */
public class MyFlowableTaskLast<T> extends Flowable<T> {
  private final Flowable<T> source;
  private final int n;

  public MyFlowableTaskLast(Flowable<T> source, int n) {
    this.source = source;
    this.n = n;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    if (n == 0) {
      EmptySubscription.complete(s);
    } else {
      source.subscribe(new TakeLast<>(s, n));
    }
  }

  static final class TakeLast<T> extends ArrayDeque<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> actual;
    final int n;
    Subscription s;
    volatile boolean cancelled;
    volatile boolean done;
    final AtomicLong requested = new AtomicLong();
    /**
     * work in process counter
     */
    final AtomicInteger wip = new AtomicInteger(0);

    TakeLast(Subscriber<? super T> actual, int n) {
      super(n);
      this.actual = actual;
      this.n = n;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        actual.onSubscribe(this);
        s.request(Long.MAX_VALUE);
      }
    }

    @Override
    public void onNext(T t) {
      if (size() == n) {
        poll();
      }
      offer(t);
    }

    @Override
    public void onError(Throwable t) {
      actual.onError(t);
    }

    @Override
    public void onComplete() {
      done = true;
      drain();
    }

    void drain() {
      if (wip.getAndIncrement() == 0) {
        int m = 1;
        Subscriber<? super T> a = this.actual;
        long r = requested.get();
        do {
          if (cancelled) {
            break;
          }
          if (done) {

            long e = 0;
            while (e != r) {
              if (cancelled) {
                return;
              }
              T t = poll();
              if (t == null) {
                a.onComplete();
                return;
              }
              a.onNext(t);
              ++e;
            }
            if (e != 0L && r != Long.MAX_VALUE) {
              r = requested.addAndGet(-e);
            }
          }
          m = wip.addAndGet(-m);
        } while (m != 0);
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        BackpressureHelper.add(requested, n);
        drain();
      }
    }

    @Override
    public void cancel() {
      cancelled = true;
      s.cancel();
    }
  }
}
