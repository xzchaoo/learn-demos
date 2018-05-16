package com.xzchaoo.learn.rxjava.custom.maybe;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public final class MyMaybeMergeArray<T> extends Flowable<T> {
  private final MaybeSource<? extends T>[] sources;

  public MyMaybeMergeArray(MaybeSource<? extends T>[] sources) {
    this.sources = sources;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    MaybeSource<? extends T>[] sources = this.sources;
    int n = sources.length;
    MergeHelper<T> helper = new MergeHelper<>(s, n);
    s.onSubscribe(helper);
    for (int i = 0; i < n; ++i) {
      if (helper.isCanceld()) {
        return;
      }
      MaybeSource<? extends T> source = sources[i];
      source.subscribe(helper);
    }
  }

  static final class MergeHelper<T> extends AtomicInteger implements MaybeObserver<T>, Subscription {
    final Subscriber<? super T> subscriber;
    final CompositeDisposable cd = new CompositeDisposable();
    final MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
    final AtomicInteger ai = new AtomicInteger(0);
    final AtomicThrowable error = new AtomicThrowable();
    final AtomicLong requested = new AtomicLong(0);
    long consumed = 0;
    volatile boolean canceled;

    MergeHelper(Subscriber<? super T> subscriber, int n) {
      this.subscriber = subscriber;
      ai.lazySet(n);
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        BackpressureHelper.add(requested, n);
        drain();
      }
    }

    @SuppressWarnings("unchecked")
    private void drain() {
      if (getAndIncrement() != 0) {
        return;
      }

      long c = consumed;
      do {
        long r = requested.get();
        while (c < r) {
          if (canceled) {
            return;
          }
          Object e = queue.poll();
          if (e == null) {
            break;
          }
          if (e != NotificationLite.COMPLETE) {
            subscriber.onNext((T) e);
            ++c;
          }
        }
        Throwable error = this.error.get();
        if (error != null) {
          queue.clear();
          subscriber.onError(this.error.terminate());
          return;
        } else if (ai.get() == 0) {
          subscriber.onComplete();
        }
        consumed = c;
      } while (decrementAndGet() != 0);
    }

    @Override
    public void cancel() {
      if (!canceled) {
        canceled = true;
        cd.dispose();
      }
    }

    boolean isCanceld() {
      return cd.isDisposed();
    }

    @Override
    public void onSubscribe(Disposable d) {
      cd.add(d);
    }

    @Override
    public void onSuccess(T t) {
      ai.decrementAndGet();
      queue.offer(t);
      drain();
    }

    @Override
    public void onError(Throwable e) {
      if (error.addThrowable(e)) {
        cd.dispose();
        queue.offer(NotificationLite.COMPLETE);
        drain();
      } else {
        RxJavaPlugins.onError(e);
      }
    }

    @Override
    public void onComplete() {
      ai.decrementAndGet();
      queue.offer(NotificationLite.COMPLETE);
      drain();
    }
  }
}
