package com.xzchaoo.learn.rxjava.custom.disposables;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.disposables.Disposable;

/**
 * @author xzcha
 * @date 2018/5/11
 */
public class MyCompositeDisposable1 implements Disposable {
  private static final Disposable[] EMPTY = new Disposable[0];
  private static final Disposable[] DISPOSED = new Disposable[0];
  private AtomicReference<Disposable[]> state = new AtomicReference<>(EMPTY);

  // remove方法也可以用类似的原子方式实现
  public void add(Disposable d) {
    for (; ; ) {
      Disposable[] state = this.state.get();
      if (state == DISPOSED) {
        d.dispose();
        return;
      }
      int n = state.length;
      Disposable[] newState = new Disposable[n + 1];
      System.arraycopy(state, 0, newState, 0, n);
      newState[n] = d;
      if (this.state.compareAndSet(state, newState)) {
        return;
      }
    }
  }

  @Override
  public void dispose() {
    for (Disposable d : this.state.getAndSet(DISPOSED)) {
      if (!d.isDisposed()) {
        d.dispose();
      }
    }
  }

  @Override
  public boolean isDisposed() {
    return state.get() == DISPOSED;
  }
}
