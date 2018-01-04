package com.xzchaoo.learn.rx.reactor;

import org.junit.Test;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.util.context.Context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * 和rxjava几乎一样的: defer delay empty error fromCallable fromIterator ignoreElements
 * 和rxjava不一样, 但有对应体的:
 * first=amb
 * from=fromPublisher
 * <p>
 * 注意 Mono是允许null的, null会被当做没有元素, 但也算在success里!
 * <p>
 * created by xzchaoo at 2017/12/4
 *
 * @author xzchaoo
 */
public class MonoTest {
	@Test
	public void test_and() {
		Mono.create(ms -> {
			ms.success();
		}).and(s -> {
			//do something
			s.onComplete();
		});
	}

	@Test
	public void test_when() throws InterruptedException {
		//当when里的请求都结束之后做一些动作
		Mono.when(
			Mono.delay(Duration.ofSeconds(1)),
			Mono.delay(Duration.ofSeconds(2))
		).then(Mono.just(3))
			.doOnSuccess(System.out::println)
			.subscribe();
		Thread.sleep(3000);
		//Mono.just(1)
	}

	@Test
	public void test_using() {
		//用using块来保证一些资源一定释放
		Mono.using(() -> new Object(), new Function<Object, Mono<?>>() {
			@Override
			public Mono<?> apply(Object resource) {
				return Mono.just(1);
			}
		}, resource -> {
			//resource.release)_
			resource.toString();
		});
	}

	@Test
	public void test_then() {
		//then可以等到上一个Mono结束才开始下一个Mono, 上一个Mono的返回值会被丢弃, 返回的是下一个Mono
		long begin = System.currentTimeMillis();
		Mono.delay(Duration.ofSeconds(1)).then(Mono.delay(Duration.ofSeconds(1))).block();
		long end = System.currentTimeMillis();
		System.out.println(end - begin);

		//这个重载版本其实是丢弃了所有元素
		Mono.delay(Duration.ofSeconds(1)).then();
	}

	@Test
	public void test_empty() {
		//注意这里会调用success
		Mono.empty()
			.doOnSuccess(e -> {
				assertThat(e).isNull();
				System.out.println("success " + e);
			}).block();
	}

	@Test
	public void test_just() {
		//mono相当于rxjava种的maybe
		//注意reactor的consumer是不能抛出异常的...
		Integer r = Mono.just(2).map(x -> x * x).block();
		assertEquals(4, r.intValue());
	}

	@Test
	public void test_delay() {
		int r = Mono.delay(Duration.ofMillis(300)).map(x -> 1).block();
		assertEquals(1, r);

		r = Mono.just(1).delayElement(Duration.ofMillis(200)).block();
		assertEquals(1, r);
	}

	@Test
	public void test_create() {
		String s = Mono.create((Consumer<MonoSink<String>>) ms -> {
				Thread t = new Thread(() -> {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//mono只会发射出第一个元素
					ms.success("a");
					//其它的会被忽略 并不报错
					ms.success("b");
				});
				t.start();
				ms.onCancel(t::interrupt);
			}
		).map(String::toUpperCase)
			.block();
		assertEquals("A", s);
	}

	@Test
	public void test_create2() throws InterruptedException {
		Mono<String> m = Mono.create(ms -> {
			Thread t = new Thread(() -> {
				System.out.println("线程执行了");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ms.success("a");
			});
			t.start();
			//当该mono生命周期结束时候会调用这个
			//正常结束 或者 死于cancel都会
			ms.onDispose(() -> {
				System.out.println("onDispose");
				t.interrupt();
			});
			//当该mono被cancel时会调用这个
			ms.onCancel(() -> {
				System.out.println("onCancel");
				t.interrupt();
			});
		});
		Disposable d = m.subscribe();
		Thread.sleep(10);
		d.dispose();//调用这个方法会导致上游被cancel
		System.out.println(d.isDisposed());
		//d.dispose();
	}

	@Test
	public void test_context() {
		Integer r = Mono.just(1).delayElement(Duration.ofSeconds(1))
			.flatMap(e -> {
				System.out.println(Thread.currentThread());
				return Mono.subscriberContext()
					.flatMap(ctx -> {
						System.out.println(ctx);
						int a = ctx.get("a");
						assertThat(a).isEqualTo(2);
						int rr = e * a;
						return Mono.just(rr).subscriberContext(ctx);
					});
			}).flatMap(e -> {
				System.out.println(Thread.currentThread());
				return Mono.subscriberContext()
					.flatMap(ctx -> {
						System.out.println(ctx);
						int a = ctx.get("a");
						assertThat(a).isEqualTo(2);
						int rr = e * a;
						return Mono.just(rr).subscriberContext(ctx);
					});
			}).doOnSuccess(e -> {
				//?这边没有context!
				Mono.subscriberContext()
					.doOnSuccess(ctx -> {
						System.out.println(ctx);
					}).subscribe();
				//System.out.println((String) Mono.subscriberContext().block().get("a"));
			})
			.subscriberContext(Context.of("a", 2))
			.block();
		System.out.println(r);
	}
}
