package com.xzchaoo.learn.rx.reactor;

import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

/**
 * 比较麻烦的是reactor不允许抛出checked异常
 * created by xzchaoo at 2017/12/4
 *
 * @author xzchaoo
 */
public class FluxTest {
  @Test
  public void test_handle() {
    Flux.just(1, 2, 3, 4)
      .handle(new BiConsumer<Integer, SynchronousSink<Integer>>() {
        @Override
        public void accept(Integer e, SynchronousSink<Integer> ss) {
          //这个sink是SynchronousSink, 因此必须在accept方法结束之前发射元素
          //跟filter有点像, 但是可以发射 error complete 来强制终止整个流程
          if (e % 2 == 0) {
            //最多只能发射一个元素
            ss.next(e);
          }
        }
      }).subscribe(System.out::println);
  }

  @Test
  public void test_next() throws InterruptedException {
    //next就是返回第一个元素
    //assertThat(Flux.just(1, 2, 3).next().block()).isEqualTo(1);
    Flux.just(1, 2, 3).doOnNext(e -> {
      System.out.println("doOnNext 元素=" + e + " 线程=" + Thread.currentThread());
    }).delayElements(Duration.ofSeconds(1))
      .subscribe(e -> {
        System.out.println("subscribe 元素=" + e + " 线程=" + Thread.currentThread());
      });
    System.out.println("不阻塞");
    Thread.sleep(5000);
  }

  @Test
  public void test_collectList() {
    List<Integer> list = Flux.just(1, 2, 3).collectList().block();
    assertThat(list).containsExactly(1, 2, 3);
  }

  @Test
  public void test_then() {
    //会忽略所有元素 直到complete
    Flux.interval(Duration.ofSeconds(1)).take(3).then().doOnSuccess(v -> {
      System.out.println("success");
    }).block();
  }

  @Test
  public void test_error_1() {
    int r = Flux.<Integer>error(new RuntimeException())
      .onErrorReturn(1)
      .blockFirst();
    assertEquals(1, r);
  }

  @Test
  public void test_error_2() {
    AtomicInteger ai = new AtomicInteger();

    int r = Flux.<Integer>create(fs -> {
      if (ai.incrementAndGet() == 1) {
        fs.error(new RuntimeException());
      }
      fs.next(1);
      fs.complete();
    }).retry(1)
      //retryWhen是高级版本 发生的所有错误将会用flux的形式发射出来
//			.retryWhen(new Function<Flux<Throwable>, Publisher<?>>() {
//				@Override
//				public Publisher<?> apply(Flux<Throwable> throwableFlux) {
//					return null;
//				}
//			})
      .blockFirst();
    assertEquals(1, r);
  }

  @Test
  public void test_create() {
    Flux.<String>create(fs -> {
      //这个就和rxjava的create一样了, 不能抛异常
      new Thread(() -> {
        //TODO next是否线程安全
        fs.next("a");
        fs.next("a");
        fs.complete();
      }).start();

    }, FluxSink.OverflowStrategy.BUFFER)
      .subscribe(System.out::println);
  }

  @Test
  public void test_generate_1() throws Exception {
    Flux.generate((Consumer<SynchronousSink<Integer>>) ss -> {
      //方法结束之前必须调完ss 不允许在其他线程里调用 因此它是同步的
      try {
        //next方法只能被调用一次 也就是只能发射一个元素 没搞懂为什么...
        //一旦调用第二次, 方法就会在next里卡死
        System.out.println("next");
        ss.next(1);
        ss.complete();
      } catch (Exception e) {
        ss.error(e);
      }
    }).subscribe(System.out::println);
  }

  @Test
  public void test_generate_2() {
    //生成器模式
    List<Integer> list = Flux.generate(() -> {
      //初始状态是0
      return 0;
    }, (BiFunction<Integer, SynchronousSink<Integer>, Integer>) (state, ss) -> {
      System.out.println("state=" + state);
      ss.next(state + 1);
      if (state == 1) {
        ss.complete();
      }
      return state + 1;
    }, s -> System.out.println("最终状态是" + s)).collectList()
      .block();
    assertEquals(Arrays.asList(1, 2), list);
  }

  @Test
  public void test_generate_mugen() {
    //利用generate可以按需产生无限的序列 但不会淹没内存, 因为它是按需生成的
    List<Integer> list = Flux.<Integer, Integer>generate(() -> 0, (state, ss) -> {
      ss.next(0);
      return state;
    }).take(5)
      .collectList()
      .block();
    assertThat(list).containsExactly(0, 0, 0, 0, 0);
  }

  @Test
  public void test_range() {
    int s = Flux.range(1, 10)
      .reduce(0, (sum, x) -> sum + x)
      .block();
    assertEquals(55, s);
  }
}
