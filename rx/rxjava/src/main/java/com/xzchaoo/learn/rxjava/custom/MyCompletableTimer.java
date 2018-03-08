package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author zcxu
 * @date 2018/1/19
 */
public class MyCompletableTimer extends Completable {
  private final long delay;
  private final TimeUnit unit;
  private final Scheduler scheduler;

  public MyCompletableTimer(long delay, TimeUnit unit, Scheduler scheduler) {
    this.delay = delay;
    this.unit = unit;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(CompletableObserver s) {
    //这里会有2个冲突: 下游对我 dispose 和 setFuture
    MyCompletableTimerCO co = new MyCompletableTimerCO(s);
    s.onSubscribe(co);
    co.setFuture(scheduler.scheduleDirect(co, delay, unit));
  }

  /**
   * 每次只有一个活跃的 disposable 添加一个新的就必须要D掉旧的
   */
  static final class MyCompletableTimerCO extends AtomicReference<Disposable> implements Disposable, Runnable {

    private final CompletableObserver s;

    public MyCompletableTimerCO(CompletableObserver s) {
      this.s = s;
    }

    //只会由下游调用
    @Override
    public void dispose() {
      //如果下游 dispose 我的时候 future 已经设置了 那么future就会被D, 否则仅仅只是标记this为DISPOSED
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    //替换定时器
    void setFuture(Disposable d) {
      //如果此时我已经被D了, 那么d也会立即被D掉, 否则CAS(d)
      DisposableHelper.replace(this, d);
    }

    //定时完成
    @Override
    public void run() {
      //如果线程已经跑到这里 并且下游对我dispose了 那么会发生什么?
      //如果 D 发生在onComplete之前 那么s里应该有一个原子标记的?
      //此处没有竞争 因此不需要考虑这个问题
      s.onComplete();
    }
  }

}
