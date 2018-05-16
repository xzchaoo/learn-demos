package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Subscriber;

import java.util.function.BiFunction;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.DeferredScalarSubscriber;

/**
 * @author xzchaoo
 * @date 2018/5/15
 */
public final class MyFlowableReduce<T> extends Flowable<T> {
  private final Flowable<T> source;
  private final BiFunction<T, T, T> reducer;

  public MyFlowableReduce(Flowable<T> source, BiFunction<T, T, T> reducer) {
    this.source = source;
    this.reducer = reducer;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    source.subscribe(new ReduceSubscriber<>(s, reducer));
  }

  static final class ReduceSubscriber<T> extends DeferredScalarSubscriber<T, T> {
    private final BiFunction<T, T, T> reducer;

    ReduceSubscriber(Subscriber<? super T> actual, BiFunction<T, T, T> reducer) {
      super(actual);
      this.reducer = reducer;
    }

    @Override
    public void onNext(T o) {
      hasValue = true;
      T value = this.value;
      if (value == null) {
        this.value = o;
      } else {
        this.value = ObjectHelper.requireNonNull(reducer.apply(value, o), "The reducer returned a  null value");
      }
    }
  }
}
