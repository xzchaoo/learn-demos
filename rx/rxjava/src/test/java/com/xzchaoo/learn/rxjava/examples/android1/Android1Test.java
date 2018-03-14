package com.xzchaoo.learn.rxjava.examples.android1;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.xzchaoo.learn.rxjava.examples.scanvideo3.RxUtils;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/3/14 0014
 */
public class Android1Test {
  //这个http请求是我们模拟的 不支持提前cancel
  //正常情况下市面上的HTTP客户端都是支持cancel的
  private ListenableFuture<String> rawAsyncHttp(String param) {
    SettableFuture<String> sf = SettableFuture.create();
    new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      sf.set("response " + param);
    }).start();
    return sf;
  }

  private Single<String> asyncHttp(String param) {
    return RxUtils.toSingle(() -> rawAsyncHttp(param));
  }

  private String complexComputation(String param) {
    //这里用sleep来模拟复杂运算
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return "complex " + param;
  }

  private void showMessage(String message) {
    //它需要做主线程的判断, 这里就不判断了
    System.out.println(message);
  }

  @Test
  public void onClickEvent() throws Exception {
    Object clickEvent = new Object();
    showMessage("开始发出HTTP请求");
    Disposable disposable = asyncHttp("param")
      .doOnSuccess(response -> showMessage("获取到response, 开始复杂计算"))
      .observeOn(Schedulers.computation())
      .map(this::complexComputation)
      //这里避免使用observeOn, 会导致一个不必要的线程切换, 线程切换让 showMessage 自己去做就行了
      .doOnSuccess(response -> showMessage("计算成功"))
      //这里其实是要切换到android的UI线程 由于我不在Android环境, 所以这里先用IO线程代替一下
      .observeOn(Schedulers.io())
      .subscribe(this::renderImage, error -> System.out.println("处理过程中发生异常 " + error));

    //可以解开下面的注释 试试效果
    //Thread.sleep(500);
    //disposable.dispose();
    //System.out.println("dispose");

    //妥善保存 disposable 变量
    //可以用 disposable.dispose(); 提前结束掉整个流程
    //由于整个步骤是不阻塞的 我们这里需要sleep一下 否则UT马上就结束了
    Thread.sleep(5000);
  }

  private void renderImage(String result) {
    System.out.println("在UI线程 渲染结果 " + result);
  }
}
