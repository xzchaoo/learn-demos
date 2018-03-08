package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 一开始认为 cache OP 似乎没啥用, 后来在Spring5的代码里看到一种用法, 真的是厉害了:
 * 有一些程序 为了追求极致的性能, 连初始化操作都是异步的(比如QConfig的第一次初始化)
 * 有一些初始化方法, 他们的执行是同步的, 而且他们又依赖于QConfig的结果, 但此时QConfig并不一定已经初始化完了, 那怎么办, 其实此时他们只能block直到Q初始化完
 * 有另外一些初始化方法, 它们本身可以是异步的, 它们依赖Q的结果, 因此可以在Q执行完之后再去初始化
 * 再Q的内部实现里, 是使用了Guava的LF, 它们会有一个 initFuture 用于表示初始化是否完成 (没记错的话 initFuture==null 时表示已经初始化完了, !=null时表示正在初始化, 或者总是不为null, 有点忘记了 可以再去看一下源代码)
 * 不过不管是哪一种实现 都可以让我们解决上述两个问题
 * <p>
 * 但是需要注意使用cache有一个陷阱 cache本身是线程安全的, 当有多个同时订阅的时候, 最早的那个订阅将会真正导致上游的订阅
 * 假设订阅1在线程A执行, 订阅2在线程B执行, 假设订阅1先发生, 那么真正的订阅将会在A执行, 并且订阅2的后续操作将会在线程A执行 [要理解这个情况必须先掌握RX的线程池相关的概念]
 *
 * @author zcxu
 * @date 2018/1/9
 */
public class CacheTest {
  @Test
  public void test2() throws InterruptedException {
    AtomicInteger callCounter = new AtomicInteger(0);

    Single<String> originalS = Single.fromCallable(() -> {
      //假设程序初始化的时候发出一个http请求 到服务端获取一些数据
      //假设程序有4个地方需要依赖这个数据 但是只想发出一次请求
      callCounter.incrementAndGet();
      System.out.println("开始睡觉");
      Thread.sleep(1000);
      return "data";
    }).subscribeOn(Schedulers.io());

    //此时S具备cache功能了
    Single<String> cachedS = originalS.cache();

    ExecutorService es = Executors.newFixedThreadPool(4);
    try {
      for (int i = 0; i < 4; ++i) {
        int ii = i;
        es.submit(() -> {
          cachedS.subscribe(result -> System.out.println(ii + " 结果是" + result));
        });
      }
      Thread.sleep(1500);
      assertThat(callCounter).hasValue(1);
    } finally {
      es.shutdownNow();
    }
  }

  @Test
  public void test() throws InterruptedException {
    SingleSubject<Boolean> ss = SingleSubject.create();
    Completable c = ss.doFinally(() -> {
      //ss=null
      System.out.println("finally");
    }).doFinally(() -> {
      System.out.println("Sdf");
    }).toCompletable()
      //.onTerminateDetach()
      .cache();

    c.subscribe(() -> {
      Thread.sleep(3000);
      System.out.println("回调1");
    });
    c.subscribe(() -> {
      Thread.sleep(3000);
      System.out.println("回调2");
    });

    new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ss.onSuccess(true);
    }).start();


    Thread.sleep(10000);
    c.subscribe(() -> {
      System.out.println("回调3");
    });
  }
}
