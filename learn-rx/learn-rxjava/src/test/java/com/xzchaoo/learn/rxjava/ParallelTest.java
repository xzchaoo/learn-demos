package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * created by zcxu at 2017/11/20
 *
 * @author zcxu
 */
public class ParallelTest {

  private AtomicInteger ai = new AtomicInteger(0);

  /**
   * 利用rxjava可以容易实现一些多线程消费的场景
   */
  @Test
  public void test_1() {
    //现在有100个任务, 每个任务耗时100几毫秒 现在先共用10个线程来跑这些任务
    //下面的方案具备 工作窃取 的能力

    //产生100个任务
    List<String> result = Flowable.range(1, 100)
        //使用 flatMapSingle, 配合 maxConcurrency 达到并发处理的效果
        //maxConcurrency 的本意是控制 "e->Flowable, 直到complete" 这个过程 的并发量
        //由于我们用的是Single, 因此就直接降级成控制并发量了
        //根据请求需要使用 delayErrors=true 将error推迟到最后
        .flatMapSingle(e -> {
          return Single.fromCallable(() -> process(e))
              .subscribeOn(Schedulers.io());
        }, true, 10)
        //将所有结果合并成一个List
        .toList()
        //阻塞等待最终的结果
        .blockingGet();
    System.out.println(result);
  }

  @Test
  public void test2() {
    //新版的rxjava有一个parallel, 语义明确, 使用方便, 但不支持 工作窃取
    List<Integer> result = Flowable.range(1, 100)
        //按照轮流的方式 拆分成10个并发
        //对处理的顺序没有保证
        .parallel(10)
        //这些并发处理发生在IO线程上
        .runOn(Schedulers.io())
        //对每个元素的处理
        .doOnNext(this::process)
        //将并发流合并成一个流
        .sequential()
        .toList()
        .blockingGet();
    System.out.println(result);
  }

  private String process(Integer e) throws Exception {
    System.out.println(ai.incrementAndGet());
    int mills = 100 + ThreadLocalRandom.current().nextInt(900);
    Thread.sleep(mills);
    ai.decrementAndGet();
    return e.toString();
  }
}
