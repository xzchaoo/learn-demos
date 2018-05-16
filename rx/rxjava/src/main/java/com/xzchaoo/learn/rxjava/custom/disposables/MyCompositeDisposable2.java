package com.xzchaoo.learn.rxjava.custom.disposables;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author xzchaoo
 * @date 2018/5/11
 */
public class MyCompositeDisposable2 implements Disposable {
  private List<Disposable> list = new ArrayList<>();
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
      // 根据java内存可见性可以保证 list!=null
      list.add(d);
    }

  }

  @Override
  public void dispose() {
    if (disposed) {
      return;
    }
    // 其实这里可以用lazy更新 但还是算了 数量不多
    disposed = true;
    List<Disposable> listT;
    synchronized (this) {
      listT = list;
      list = null;
    }
    if (listT != null) {
      for (Disposable d : listT) {
        if (!d.isDisposed()) {
          d.dispose();
        }
      }
    }
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}
