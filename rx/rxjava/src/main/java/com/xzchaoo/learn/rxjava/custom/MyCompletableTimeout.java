package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author xzchaoo
 * @date 2018/1/19
 */
public class MyCompletableTimeout extends Completable {
  private final CompletableSource source;
  private final long timeout;
  private final TimeUnit unit;
  private final Scheduler scheduler;
  private final CompletableSource other;

  public MyCompletableTimeout(CompletableSource source, long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
    this.source = source;
    this.timeout = timeout;
    this.unit = unit;
    this.scheduler = scheduler;
    this.other = other;
  }

  @Override
  protected void subscribeActual(CompletableObserver s) {
    //1. 按照契约 回调 onSubscribe
    CompositeDisposable cd = new CompositeDisposable();
    s.onSubscribe(cd);

    //2. 启动定时器, 时间到了之后dispose掉1.的订阅, 并且引发一个 TimeoutException
    AtomicBoolean once = new AtomicBoolean();

    //3. 向source进行订阅
    cd.add(scheduler.scheduleDirect(() -> {
      //有3个地方存在资源竞争 因此用 once 来确保只有一处成功
      //4. 有资源竞争
      if (once.compareAndSet(false, true)) {
        cd.clear();
        if (other == null) {
          s.onError(new TimeoutException());
        } else {
          other.subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
              cd.add(d);
            }

            @Override
            public void onComplete() {
              cd.dispose();
              s.onComplete();
            }

            @Override
            public void onError(Throwable e) {
              cd.dispose();
              s.onError(e);
            }
          });
        }
      }
    }, timeout, unit));

    source.subscribe(new MyTimeoutCompletableObserver(cd, once, s));
  }

  static final class MyTimeoutCompletableObserver implements CompletableObserver {
    private final CompositeDisposable cd;
    private final AtomicBoolean once;
    private final CompletableObserver s;

    MyTimeoutCompletableObserver(CompositeDisposable cd, AtomicBoolean once, CompletableObserver s) {
      this.cd = cd;
      this.once = once;
      this.s = s;
    }

    @Override
    public void onSubscribe(Disposable d) {
      cd.add(d);
    }

    @Override
    public void onComplete() {
      if (once.compareAndSet(false, true)) {
        cd.dispose();
        s.onComplete();
      }
    }

    @Override
    public void onError(Throwable e) {
      if (once.compareAndSet(false, true)) {
        cd.dispose();
        s.onError(e);
      } else {
        RxJavaPlugins.onError(e);
      }
    }
  }
}
