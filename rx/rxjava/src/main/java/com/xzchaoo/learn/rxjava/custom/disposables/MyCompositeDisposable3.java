package com.xzchaoo.learn.rxjava.custom.disposables;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.util.ExceptionHelper;

/**
 * @author xzcha
 * @date 2018/5/11
 */
public class MyCompositeDisposable3 implements Disposable {
  // lazy 初始化list
  private List<Disposable> list;
  private volatile boolean disposed;

  public void add(Disposable d) {
    // 快速判断
    if (disposed) {
      d.dispose();
      return;
    }

    // 这里需要隐式锁
    synchronized (this) {
      if (disposed) {
        d.dispose();
        return;
      }
      if (list == null) {
        list = new ArrayList<>();
      }
      list.add(d);
    }

  }

  @Override
  public void dispose() {
    if (disposed) {
      return;
    }
    // 其实这里可以用lazy更新 但还是算了 数量不多
    List<Disposable> listT;
    synchronized (this) {
      if (disposed) {
        return;
      }
      disposed = true;
      listT = list;
      list = null;
    }
    dispose(listT);
  }

  private void dispose(List<Disposable> listT) {
    if (listT == null) {
      return;
    }
    List<Throwable> errors = null;
    for (Disposable d : listT) {
      if (!d.isDisposed()) {
        try {
          d.dispose();
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          if (errors == null) {
            errors = new ArrayList<>();
          }
          errors.add(ex);
        }
      }
    }
    if (errors != null) {
      if (errors.size() == 1) {
        throw ExceptionHelper.wrapOrThrow(errors.get(0));
      }
      throw new CompositeException(errors);
    }
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  public int size() {
    // fast path
    if (disposed) {
      return 0;
    }
    // 加sync应该是为了保证 list 的可见性
    synchronized (this) {
      List<Disposable> listT = this.list;
      return listT == null ? 0 : listT.size();
    }
  }
}
