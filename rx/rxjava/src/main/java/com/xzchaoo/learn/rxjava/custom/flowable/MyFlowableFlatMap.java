// package com.xzchaoo.learn.rxjava.custom.flowable;
//
// import org.reactivestreams.Publisher;
// import org.reactivestreams.Subscriber;
// import org.reactivestreams.Subscription;
//
// import java.util.concurrent.Callable;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.concurrent.atomic.AtomicLong;
//
// import io.reactivex.Flowable;
// import io.reactivex.FlowableSubscriber;
// import io.reactivex.exceptions.Exceptions;
// import io.reactivex.functions.Function;
// import io.reactivex.internal.functions.ObjectHelper;
// import io.reactivex.internal.fuseable.HasUpstreamPublisher;
// import io.reactivex.internal.operators.flowable.FlowableScalarXMap;
// import io.reactivex.internal.queue.MpscLinkedQueue;
// import io.reactivex.internal.subscriptions.SubscriptionHelper;
// import io.reactivex.internal.util.AtomicThrowable;
//
// /**
//  * @author zcxu
//  * @date 2018/5/14 0014
//  */
// public class MyFlowableFlatMap<T, U> extends Flowable<U> implements HasUpstreamPublisher<T> {
//   private final Flowable<T> source;
//   private final Function<? super T, ? extends Publisher<? extends U>> mapper;
//   private final boolean delayErrors;
//   private final int maxConcurrency;
//   private final int bufferSize;
//
//   public MyFlowableFlatMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends U>> mapper, boolean
//     delayErrors, int maxConcurrency, int bufferSize) {
//     this.source = source;
//     this.mapper = mapper;
//     this.delayErrors = delayErrors;
//     this.maxConcurrency = maxConcurrency;
//     this.bufferSize = bufferSize;
//   }
//
//   @Override
//   public Publisher<T> source() {
//     return source;
//   }
//
//   @Override
//   protected void subscribeActual(Subscriber<? super U> subscriber) {
//     if (FlowableScalarXMap.tryScalarXMapSubscribe(source, subscriber, mapper)) {
//       return;
//     }
//     FlatMapSubscriber<T, U> parent = new FlatMapSubscriber<>(subscriber, mapper, delayErrors, maxConcurrency,
//       bufferSize);
//     subscriber.onSubscribe(parent);
//     parent.subscribe();
//   }
//
//   static final class FlatMapSubscriber<T, U> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
//     private final Subscriber<? super U> subscriber;
//     private final Function<? super T, ? extends Publisher<? extends U>> mapper;
//     private final boolean delayErrors;
//     private final int maxConcurrency;
//     private final int bufferSize;
//     private MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
//
//     Subscription s;
//     volatile boolean cancelled;
//     volatile boolean done;
//     AtomicThrowable errs = new AtomicThrowable();
//     AtomicLong requested = new AtomicLong(0);
//
//     FlatMapSubscriber(Subscriber<? super U> subscriber, Function<? super T, ? extends Publisher<? extends U>> mapper,
//                       boolean delayErrors, int maxConcurrency, int bufferSize) {
//       this.subscriber = subscriber;
//       this.mapper = mapper;
//       this.delayErrors = delayErrors;
//       this.maxConcurrency = maxConcurrency;
//       this.bufferSize = bufferSize;
//       childSubscribers = new ChildSubscriber[maxConcurrency];
//     }
//
//     @Override
//     public void onSubscribe(Subscription s) {
//       if (SubscriptionHelper.validate(this.s, s)) {
//         this.s = s;
//         this.subscriber.onSubscribe(this);
//         if (!cancelled) {
//           if (maxConcurrency == Integer.MAX_VALUE) {
//             s.request(Long.MAX_VALUE);
//           } else {
//             s.request(maxConcurrency);
//           }
//         }
//       }
//     }
//
//     @Override
//     public void onNext(T t) {
//       if (done) {
//         return;
//       }
//       Publisher<? extends U> p;
//       try {
//         p = ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
//       } catch (Throwable e) {
//         Exceptions.throwIfFatal(e);
//         s.cancel();
//         onError(e);
//         return;
//       }
//       if (p instanceof Callable) {
//         U u;
//         try {
//           u = ((Callable<U>) p).call();
//         } catch (Throwable e) {
//           Exceptions.throwIfFatal(e);
//           errs.addThrowable(e);
//           drain();
//           return;
//         }
//         if (u != null) {
//
//         } else {
//
//         }
//       } else {
//
//       }
//     }
//
//     void drain() {
//       if (getAndIncrement() == 0) {
//         drainLoop();
//       }
//     }
//
//     void drainLoop() {
//       Subscriber<? super U> subscriber = this.subscriber;
//       int missed = 1;
//       for (; ; ) {
//         missed = addAndGet(-missed);
//         if (missed == 0) {
//           break;
//         }
//       }
//     }
//
//     @Override
//     public void onError(Throwable t) {
//
//     }
//
//     @Override
//     public void onComplete() {
//
//     }
//
//     @Override
//     public void request(long n) {
//       if (SubscriptionHelper.validate(n)) {
//         requested.addAndGet(n);
//
//       }
//     }
//
//     @Override
//     public void cancel() {
//       cancelled = true;
//       this.s.cancel();
//       queue.clear();
//     }
//
//     void subscribe() {
//
//     }
//   }
//
//   static final class ChildSubscriber<T> implements FlowableSubscriber<T> {
//
//     @Override
//     public void onSubscribe(Subscription s) {
//
//     }
//
//     @Override
//     public void onNext(T t) {
//
//     }
//
//     @Override
//     public void onError(Throwable t) {
//
//     }
//
//     @Override
//     public void onComplete() {
//
//     }
//   }
// }
