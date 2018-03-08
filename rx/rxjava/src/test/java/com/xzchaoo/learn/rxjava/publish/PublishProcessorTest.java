package com.xzchaoo.learn.rxjava.publish;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/2/27 0027
 */
public class PublishProcessorTest {
  @Test
  public void test() throws InterruptedException {
    //假设有一个数据 每100ms更新一次 并且支持多个订阅 可以通过下面的方式去实现
    //如果没有订阅 那么也不需要100ms更新了

    //TODO 这里100ms更新 可能会有点问题 它是 fixedRate模型, 看能不能做成 fixedDelay 的模型
    //否则可能会有背压问题

    Flowable<String> cf = Flowable.interval(100, 500, TimeUnit.MILLISECONDS)
      .flatMapSingle(ignore -> Single.fromCallable(() -> {
        System.out.println("发出一个http请求");
        //假设这里发出一个http请求
        return "OK";
      }).subscribeOn(Schedulers.io()), false, 1)
      .publish(1)
      .refCount();

    //调用publish返回的是ConnectableFlowable
    //这是一个特殊的F, 需要调用其connect方法才能向上游订阅, 否则即使有人subscribe了也不会订阅上游的


    //refCount的作用是 如果至少有1个订阅者 那么就向上游订阅, 如果订阅者数量减少到0 那么取消订阅 如果又增加大于等于1 那么又继续订阅
    //autoConnect的作用是一旦订阅的数量达到某个值 就向上游订阅 如果之后订阅者数量降低了 那么也不会取消订阅

    Disposable d = cf.subscribe(result -> {
      System.out.println("1 结果更新了=" + result);
    });

    Disposable d2 = cf.subscribe(result -> {
      System.out.println("2 结果更新了=" + result);
    });
    Thread.sleep(700);
    d.dispose();
    d2.dispose();
    Thread.sleep(2500);
    cf.subscribe(result -> {
      System.out.println("3 结果更新了=" + result);
    });
    Thread.sleep(2500);
  }
}
