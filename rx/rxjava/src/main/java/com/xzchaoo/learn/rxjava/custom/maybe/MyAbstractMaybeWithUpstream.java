package com.xzchaoo.learn.rxjava.custom.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public abstract class MyAbstractMaybeWithUpstream<T, R> extends Maybe<R> implements HasUpstreamMaybeSource<T> {
  protected final MaybeSource<T> source;

  public MyAbstractMaybeWithUpstream(MaybeSource<T> source) {
    this.source = source;
  }

  @Override
  public MaybeSource<T> source() {
    return source;
  }
}
