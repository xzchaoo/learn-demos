package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleDelay<T> extends Single<T> {
  private final SingleSource<T> source;
  private final long delay;
  private final TimeUnit unit;
  private final Scheduler scheduler;
  private final boolean delayError;

  public MySingleDelay(SingleSource<T> source, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
    this.source = source;
    this.delay = delay;
    this.unit = unit;
    this.scheduler = scheduler;
    this.delayError = delayError;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    // 先把DO构造出来
    DelayObserver<T> delayObserver = new DelayObserver<>(observer, delay, unit, scheduler, delayError);
    observer.onSubscribe(delayObserver);
    source.subscribe(delayObserver);
  }

  static final class DelayObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {

    private final SingleObserver<? super T> observer;
    private final long delay;
    private final TimeUnit unit;
    private final Scheduler scheduler;
    private final boolean delayError;

    public DelayObserver(SingleObserver<? super T> observer, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
      this.observer = observer;
      this.delay = delay;
      this.unit = unit;
      this.scheduler = scheduler;
      this.delayError = delayError;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T t) {
      // 成功回调 需要处理取消情况
      // 如果没有被d
      Disposable disposable = scheduler.scheduleDirect(() -> observer.onSuccess(t), delay, unit);
      DisposableHelper.set(this, disposable);
    }

    @Override
    public void onError(Throwable e) {
      Disposable disposable = scheduler.scheduleDirect(() -> observer.onError(e), delayError ? delay : 0, unit);
      DisposableHelper.set(this, disposable);
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
  }
}
