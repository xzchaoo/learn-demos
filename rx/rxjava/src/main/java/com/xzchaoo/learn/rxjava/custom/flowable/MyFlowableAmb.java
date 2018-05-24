package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/14
 */
public final class MyFlowableAmb<T> extends Flowable<T> {
  private final Publisher<? extends T>[] sources;

  public MyFlowableAmb(Publisher<? extends T>[] sources) {
    this.sources = sources;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> subscriber) {
    int n = sources.length;
    if (n == 0) {
      EmptySubscription.complete(subscriber);
      return;
    }
    if (n == 1) {
      sources[0].subscribe(subscriber);
      return;
    }
    new AmbCoordinator<>(sources, subscriber).subscribe();
  }

  static final class AmbCoordinator<T> implements Subscription {
    final Publisher<? extends T>[] sources;
    final Subscriber<? super T> subscriber;
    final AtomicBoolean once = new AtomicBoolean(false);
    final InnerSubscriber<T>[] childSubscribers;

    // 0表示还没有判断出winner
    // >0 表示减一后的值是索引
    // -1表示已经cancel
    AtomicInteger winnerIndex = new AtomicInteger(0);
    InnerSubscriber<T> winner;
    static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];


    AmbCoordinator(Publisher<? extends T>[] sources, Subscriber<? super T> subscriber) {
      this.sources = sources;
      this.subscriber = subscriber;
      this.childSubscribers = new InnerSubscriber[sources.length];
    }

    void subscribe() {
      int n = sources.length;
      for (int i = 0; i < n; i++) {
        childSubscribers[i] = new InnerSubscriber<>(this, i, subscriber);
      }
      winnerIndex.lazySet(0);
      subscriber.onSubscribe(this);
      for (int i = 0; i < n; ++i) {
        if (winnerIndex.get() != 0) {
          return;
        }
        sources[i].subscribe(childSubscribers[i]);
      }
    }

    void onChildNext(InnerSubscriber<T> is, T t) {
      subscriber.onSubscribe(this);
      // cancel other
      if (once.compareAndSet(false, true)) {
        // win
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        int w = winnerIndex.get();
        if (w > 0) {
          childSubscribers[w - 1].request(n);
        } else if (w == 0) {
          for (InnerSubscriber<T> is : childSubscribers) {
            is.request(n);
          }
        }
      }
    }

    boolean win(int index) {
      int index2 = index + 1;
      if (winnerIndex.get() == 0 && winnerIndex.compareAndSet(0, index2)) {
        InnerSubscriber<T>[] iss = this.childSubscribers;
        int n = iss.length;
        for (int i = 0; i < n; ++i) {
          if (i != index) {
            iss[i].cancel();
          }
        }
        return true;
      }
      return false;
    }

    @Override
    public void cancel() {
      if (winnerIndex.get() != -1) {
        winnerIndex.lazySet(-1);
        for (InnerSubscriber<T> is : childSubscribers) {
          is.cancel();
        }
      }
    }
  }

  static class InnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
    final AmbCoordinator<T> parent;
    final int index;
    final Subscriber<? super T> actual;
    final AtomicLong missedRequested = new AtomicLong();

    boolean won;

    InnerSubscriber(AmbCoordinator<T> parent, int index, Subscriber<? super T> actual) {
      this.parent = parent;
      this.index = index;
      this.actual = actual;
    }

    @Override
    public void onSubscribe(Subscription s) {
      SubscriptionHelper.deferredSetOnce(this, missedRequested, s);
    }

    void request(long n) {
      SubscriptionHelper.deferredRequest(this, missedRequested, n);
    }

    @Override
    public void onNext(T t) {
      if (won) {
        actual.onNext(t);
      } else {
        if (parent.win(index)) {
          won = true;
          actual.onNext(t);
        } else {
          get().cancel();
        }
      }
      // win
      parent.onChildNext(this, t);
    }

    @Override
    public void onError(Throwable t) {
      if (won) {
        actual.onError(t);
      } else {
        if (parent.win(index)) {
          won = true;
          actual.onError(t);
        } else {
          get().cancel();
          RxJavaPlugins.onError(t);
        }
      }
    }

    @Override
    public void onComplete() {
      if (won) {
        actual.onComplete();
      } else {
        if (parent.win(index)) {
          won = true;
          actual.onComplete();
        } else {
          get().cancel();
        }
      }
    }

    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
  }
}
