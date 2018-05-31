package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 因为上游已经考虑被压了, 所以我们不需要考虑被压
 * 每size个元素放在一个集合里 然后发射给下游
 * 每次往后条skip个元素, 因此可能出现重叠或者间隙或者刚刚好
 *
 * @author xzchaoo
 * @date 2018/5/29
 */
public class MyFlowableBuffer<T, C extends Collection<? super T>> extends Flowable<C> implements
  HasUpstreamPublisher<T> {
  private final Flowable<T> source;
  private final Callable<C> bufferSupplier;
  private final int size;
  private final int skip;

  public MyFlowableBuffer(Flowable<T> source, Callable<C> bufferSupplier, int size, int skip) {
    this.source = source;
    this.bufferSupplier = bufferSupplier;
    this.size = size;
    this.skip = skip;
  }

  @Override
  protected void subscribeActual(Subscriber<? super C> s) {
    if (size == skip) {
      source.subscribe(new ExactSubscriber<>(bufferSupplier, s, size));
    } else if (skip < size) {
      source.subscribe(new OverlaySubscriber<>(bufferSupplier, s, size, skip));
    } else {
      throw new IllegalStateException();
    }
  }

  @Override
  public Publisher<T> source() {
    return source;
  }

  static class ExactSubscriber<T, C extends Collection<? super T>> implements FlowableSubscriber<T>, Subscription {
    final Callable<C> bufferSupplier;
    final Subscriber<? super C> subscriber;
    final int limit;

    int size;
    Subscription s;
    C buffer;

    /**
     * done是个普通变量 应该是用于防止cancel之后上游继续发射元素 导致违反协议
     * 但所有地方都需要考虑这些问题吗
     */
    boolean done;

    ExactSubscriber(Callable<C> bufferSupplier, Subscriber<? super C> subscriber, int limit) {
      this.bufferSupplier = bufferSupplier;
      this.subscriber = subscriber;
      this.limit = limit;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        subscriber.onSubscribe(this);
      }
    }

    @Override
    public void onNext(T t) {
      C b = buffer;
      if (b == null) {
        try {
          b = ObjectHelper.requireNonNull(bufferSupplier.call(), "bufferSupplier returned a null value");
        } catch (Exception e) {
          Exceptions.throwIfFatal(e);
          cancel();
          onError(e);
          return;
        }
        buffer = b;
      }
      b.add(t);
      int i = size + 1;
      if (i == limit) {
        size = 0;
        buffer = null;
        subscriber.onNext(b);
      } else {
        size = i;
      }
    }

    @Override
    public void onError(Throwable t) {
      if (done) {
        RxJavaPlugins.onError(t);
      }
      done = true;
      subscriber.onError(t);
    }

    @Override
    public void onComplete() {
      if (done) {
        return;
      }
      done = true;
      C b = buffer;
      buffer = null;
      if (b != null && !b.isEmpty()) {
        subscriber.onNext(b);
      }
      subscriber.onComplete();
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        // 直接透传给上游就行
        s.request(BackpressureHelper.multiplyCap(n, limit));
      }
    }

    @Override
    public void cancel() {
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }

  static class OverlaySubscriber<T, C extends Collection<? super T>> implements FlowableSubscriber<T>, Subscription {

    final Callable<C> bufferSupplier;
    final Subscriber<? super C> subscriber;
    final int size;
    final int skip;

    Subscription s;
    boolean done;
    int index;
    C buffer;
    C buffer2;

    OverlaySubscriber(Callable<C> bufferSupplier, Subscriber<? super C> subscriber, int size, int skip) {
      this.bufferSupplier = bufferSupplier;
      this.subscriber = subscriber;
      this.size = size;
      this.skip = skip;
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        subscriber.onSubscribe(this);
      }
    }

    @Override
    public void onNext(T t) {
      if (done) {
        return;
      }
      C b = buffer;
      if (b == null) {
        try {
          b = ObjectHelper.requireNonNull(bufferSupplier.call(), "bufferSupplier returned a null value");
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          cancel();
          onError(ex);
          return;
        }
        buffer = b;
      }

      b.add(t);
      int i = index + 1;

      if (i >= skip) {
        C b2 = buffer2;
        if (b2 == null) {
          try {
            b2 = ObjectHelper.requireNonNull(bufferSupplier.call(), "bufferSupplier returned a null value");
          } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            cancel();
            onError(ex);
            return;
          }
          buffer2 = b2;
        }
        b2.add(t);
      }

      if (i == size) {
        index = size - skip;
        buffer = buffer2;
        buffer2 = null;
        subscriber.onNext(b);
      } else {
        index = i;
      }
    }

    @Override
    public void onError(Throwable t) {
      if (done) {
        RxJavaPlugins.onError(t);
        return;
      }
      done = true;
      subscriber.onError(t);
    }

    @Override
    public void onComplete() {
      if (done) {
        return;
      }
      done = true;
      C b = buffer;
      buffer = null;
      buffer2 = null;
      if (b != null && b.isEmpty()) {
        subscriber.onNext(b);
      }
      subscriber.onComplete();
    }

    private AtomicBoolean first = new AtomicBoolean(false);

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        // TODO 如何计算
        if (first.get() && first.compareAndSet(false, true)) {
          long r = BackpressureHelper.addCap(size, BackpressureHelper.multiplyCap(n - 1, skip));
          s.request(r);
        } else {
          s.request(BackpressureHelper.multiplyCap(n, skip));
        }
      }
    }

    @Override
    public void cancel() {
      s.cancel();
      s = SubscriptionHelper.CANCELLED;
    }
  }
}
