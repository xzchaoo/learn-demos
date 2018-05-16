package com.xzchaoo.learn.rxjava.custom.single;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzchaoo
 * @date 2018/5/10
 */
public class MySingleCache<T> extends Single<T> {
  private final SingleCacheObserver[] TERMINATED = new SingleCacheObserver[0];
  private final SingleCacheObserver[] EMPTY = new SingleCacheObserver[0];

  private final SingleSource<? extends T> source;
  private AtomicBoolean first = new AtomicBoolean(true);
  private ConcurrentLinkedQueue<SingleCacheObserver<T>> pendingObservers = new ConcurrentLinkedQueue<>();
  private Throwable error;
  private T value;

  private AtomicReference<SingleCacheObserver[]> pending = new AtomicReference<>(EMPTY);

  public MySingleCache(SingleSource<? extends T> source) {
    this.source = source;
  }


  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    SingleCacheObserver<T> child = new SingleCacheObserver<>(this, observer);
    observer.onSubscribe(child);
    if (add(child)) {
      if (child.isDisposed()) {
        removeChild(child);
      }
    } else {
      onFinished(observer);
      return;
    }
    if (first.compareAndSet(false, true)) {
      subscribeUpstream();
    }
  }

  private boolean add(SingleCacheObserver<T> singleCacheObserver) {
    for (; ; ) {
      SingleCacheObserver[] state = pending.get();
      if (state == TERMINATED) {
        return false;
      }
      int n = state.length;
      SingleCacheObserver[] newState = new SingleCacheObserver[n + 1];
      System.arraycopy(state, 0, newState, 0, n);
      newState[n] = singleCacheObserver;
      if (pending.compareAndSet(state, newState)) {
        return true;
      }
    }
  }

  @SuppressWarnings("unchecked")
  void onSuccess(T value) {
    this.value = value;
    // 组织其他线程继续往pending里加入元素
    for (SingleCacheObserver<T> sco : pending.getAndSet(TERMINATED)) {
      if (!sco.isDisposed()) {
        sco.observer.onSuccess(value);
      }
    }
  }

  @SuppressWarnings("unchecked")
  void onError(Throwable error) {
    this.error = error;
    for (SingleCacheObserver<T> sco : pending.getAndSet(TERMINATED)) {
      if (!sco.isDisposed()) {
        sco.observer.onError(error);
      }
    }
  }

  private void onFinished(SingleObserver<? super T> observer) {
    observer.onSubscribe(EmptyDisposable.INSTANCE);
    if (error != null) {
      observer.onError(error);
    } else {
      observer.onSuccess(value);
    }
  }

  private void subscribeUpstream() {
    source.subscribe(new CacheObserver<T>(this));
  }

  // 如果能保证下游不会并发调用dispose 那么它就可以不用原子性
  private void removeChild(SingleCacheObserver<T> child) {
    for (; ; ) {
      SingleCacheObserver[] state = pending.get();
      int n = state.length;
      if (n == 0) {
        return;
      }
      int index = -1;
      for (int i = 0; i < n; ++i) {
        if (state[i] == child) {
          index = i;
          break;
        }
      }
      if (index < 0) {
        return;
      }
      SingleCacheObserver[] newState;
      if (n == 1) {
        newState = EMPTY;
      } else {
        newState = new SingleCacheObserver[n - 1];
        System.arraycopy(state, 0, newState, 0, index);
        System.arraycopy(state, index + 1, newState, index, n - index - 1);
      }
      if (pending.compareAndSet(state, newState)) {
        return;
      }
    }
  }

  static class CacheObserver<T> implements SingleObserver<T> {

    final MySingleCache<T> parent;

    CacheObserver(MySingleCache<T> parent) {
      this.parent = parent;
    }

    @Override
    public void onSubscribe(Disposable d) {
      // 我们不会dispose上游 所以不必处理
    }

    @Override
    public void onSuccess(T t) {
      parent.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
      parent.onError(e);
    }

  }

  static class SingleCacheObserver<T> extends AtomicBoolean implements Disposable {
    final SingleObserver<? super T> observer;
    final MySingleCache<T> parent;

    SingleCacheObserver(MySingleCache<T> parent, SingleObserver<? super T> observer) {
      this.parent = parent;
      this.observer = observer;
    }

    @Override
    public void dispose() {
      if (compareAndSet(false, true)) {
        parent.removeChild(this);
      }
    }

    @Override
    public boolean isDisposed() {
      return get();
    }
  }
}
