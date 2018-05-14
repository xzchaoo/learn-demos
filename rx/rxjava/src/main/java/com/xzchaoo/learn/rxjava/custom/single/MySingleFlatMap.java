package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * @author xzcha
 * @date 2018/5/10
 */
public class MySingleFlatMap<T, R> extends Single<R> {
  private final SingleSource<T> source;
  private final Function<? super T, SingleSource<? extends R>> mapper;

  public MySingleFlatMap(SingleSource<T> source, Function<? super T, SingleSource<? extends R>> mapper) {
    this.source = source;
    this.mapper = mapper;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super R> observer) {
    source.subscribe(new FlatMapProxy1<T, R>(observer, mapper));
  }

  static final class FlatMapProxy1<T, R> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    final SingleObserver<? super R> observer;
    final Function<? super T, SingleSource<? extends R>> mapper;

    public FlatMapProxy1(SingleObserver<? super R> observer, Function<? super T, SingleSource<? extends R>> mapper) {
      this.observer = observer;
      this.mapper = mapper;
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void dispose() {
      // 下游可以d我
      DisposableHelper.dispose(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
      // 我订阅上游成功 这时我才让下游完成订阅初始化
      // 真实的顺序是 下游订阅我, 我去订阅上游, 等上游告诉我说我订阅成功了 我才告诉下游说下游订阅成功了
      // TODO 我认为这里的setOnce 保护是多余的 此时能触碰到当前实例的 有且只有上游
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      // 上游成功

      SingleSource<? extends R> source2;
      try {
        source2 = ObjectHelper.requireNonNull(mapper.apply(t), "The single returned by the mapper is null");
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        observer.onError(ex);
        return;
      }
      // 其实这个动作可以不要 仅仅只是为了如果真的发生这种情况时 可以节省一次订阅
      if (!isDisposed()) {
        source2.subscribe(new FlatMapProxy2<>(this, observer));
      }
    }

    @Override
    public void onError(Throwable e) {
      // 上游返回一个错误
      observer.onError(e);
    }

    static final class FlatMapProxy2<R> implements SingleObserver<R> {
      final AtomicReference<Disposable> parent;
      final SingleObserver<? super R> observer;

      FlatMapProxy2(AtomicReference<Disposable> parent, SingleObserver<? super R> observer) {
        this.parent = parent;
        this.observer = observer;
      }

      @Override
      public void onSubscribe(Disposable d) {
        DisposableHelper.replace(parent, d);
      }

      @Override
      public void onSuccess(R r) {
        observer.onSuccess(r);
      }

      @Override
      public void onError(Throwable e) {
        observer.onError(e);
      }
    }
  }
}
