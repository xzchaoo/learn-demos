package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author xzcha
 * @date 2018/5/10
 */
public class MySingleTimer extends Single<Long> {
  private final long time;
  private final TimeUnit unit;
  private final Scheduler scheduler;

  public MySingleTimer(long time, TimeUnit unit, Scheduler scheduler) {
    this.time = time;
    this.unit = unit;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super Long> observer) {
    // 下面是一种方案, 但是创建了2个对象, 假设lambda表达式也算一个
//    SequentialDisposable sd = new SequentialDisposable();
//    observer.onSubscribe(sd);
//    Disposable disposable = scheduler.scheduleDirect(() -> {
//      observer.onSuccess(0L);
//    }, time, unit);
//    sd.update(disposable);
    TimerDisposable td = new TimerDisposable(observer);
    observer.onSubscribe(td);
    Disposable disposable = scheduler.scheduleDirect(td, time, unit);
    td.replace(disposable);
  }

  static final class TimerDisposable extends AtomicReference<Disposable> implements Disposable, Runnable {
    final SingleObserver<? super Long> observer;

    TimerDisposable(SingleObserver<? super Long> observer) {
      this.observer = observer;
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void run() {
      observer.onSuccess(0L);
    }

    void replace(Disposable disposable) {
      DisposableHelper.replace(this, disposable);
    }
  }
}
