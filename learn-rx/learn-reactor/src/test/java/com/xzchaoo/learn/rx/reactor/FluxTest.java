package com.xzchaoo.learn.rx.reactor;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * created by xzchaoo at 2017/12/4
 *
 * @author xzchaoo
 */
public class FluxTest {
	@Test
	public void test_next() {
		//next就是返回第一个元素
		assertThat(Flux.just(1, 2, 3).next().block()).isEqualTo(1);
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
			//TODO 是否是线程安全
			//这个就和rxjava的create一样了, 不能抛异常
			new Thread(() -> {
				fs.next("a");
				fs.next("a");
				fs.complete();
			}).start();
		}).subscribe(System.out::println);
	}

	@Test
	public void test_generate_1() throws Exception {
		Flux.generate(new Consumer<SynchronousSink<Integer>>() {
			@Override
			public void accept(SynchronousSink<Integer> ss) {
				//方法结束之前必须调完ss 不允许在其他线程里调用
				try {
					//next方法只能被调用一次 也就是只能发射一个元素 没搞懂为什么...
					//一旦调用第二次, 方法就会在next里卡死
					System.out.println("next");
					ss.next(1);
					ss.complete();
				} catch (Exception e) {
					ss.error(e);
				}
			}
		}).subscribe(System.out::println);
	}

	@Test
	public void test_generate_2() {
		//生成器模式
		List<Integer> list = Flux.generate(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				//初始状态是0
				return 0;
			}
		}, new BiFunction<Integer, SynchronousSink<Integer>, Integer>() {
			@Override
			public Integer apply(Integer state, SynchronousSink<Integer> ss) {
				System.out.println("state=" + state);
				ss.next(state + 1);
				if (state == 1) {
					ss.complete();
				}
				return state + 1;
			}
		}, new Consumer<Integer>() {
			@Override
			public void accept(Integer s) {
				System.out.println("最终状态是" + s);
			}
		}).collectList()
			.block();
		assertEquals(Arrays.asList(1, 2), list);
	}

	@Test
	public void test_range() {
		int s = Flux.range(1, 10)
			.reduce(0, (sum, x) -> sum + x)
			.block();
		assertEquals(55, s);
	}
}
