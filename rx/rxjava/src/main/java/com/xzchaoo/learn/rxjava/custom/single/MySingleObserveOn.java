package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author xzchaoo
 * @date 2018/5/10
 */
public class MySingleObserveOn<T> extends Single<T> {
  final SingleSource<T> source;
  final Scheduler scheduler;

  public MySingleObserveOn(SingleSource<T> source, Scheduler scheduler) {
    this.source = source;
    this.scheduler = scheduler;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    source.subscribe(new Proxy<>(observer, scheduler));
  }

  static final class Proxy<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    private final SingleObserver<? super T> observer;
    private final Scheduler scheduler;

    public Proxy(SingleObserver<? super T> observer, Scheduler scheduler) {
      this.observer = observer;
      this.scheduler = scheduler;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        observer.onSubscribe(this);
      }
    }

    @Override
    public void onSuccess(T t) {
      // 这里的if判断是的多余的
      // 不过我看到rx很多地方都没这个判断, 是否是因为这个判断的代价比较大?
      // 当 scheduleDirect 的代价比较大的时候 这个优化就有用了
      // TODO 我看到rx里很多地方一个类实现了多个接口 而不是用匿名内部类(应该是为了避免创建太多对象) 或 lambda表达式(可能是为了兼容)
      DisposableHelper.replace(this, scheduler.scheduleDirect(() -> observer.onSuccess(t)));

//      if (!isDisposed()) {
//        DisposableHelper.replace(this, scheduler.scheduleDirect(() -> observer.onSuccess(t)));
//      }
    }

    @Override
    public void onError(Throwable e) {
      DisposableHelper.replace(this, scheduler.scheduleDirect(() -> observer.onError(e)));

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
