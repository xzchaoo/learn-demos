package com.xzchaoo.learn.rxjava.custom.flowable;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public abstract class MyFlowableUpstream<T, U> extends Flowable<T> implements HasUpstreamPublisher<U> {
  protected final Flowable<U> source;

  public MyFlowableUpstream(Flowable<U> source) {
    this.source = source;
  }

  @Override
  public Publisher<U> source() {
    return source;
  }
}
