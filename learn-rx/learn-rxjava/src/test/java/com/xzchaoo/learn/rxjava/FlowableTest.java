package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * 千万注意,flowable不支持null元素, 遇到这种情况可以考虑用 flatMapSingle 先处理一下
 * created by xzchaoo at 2017/11/17
 *
 * @author xzchaoo
 */
public class FlowableTest {
	private static String tname() {
		return Thread.currentThread().getName();
	}

	private static void mark(String tag) {
		System.out.println(tag + " " + tname());
	}

	@Test
	public void test_publish() throws InterruptedException {
//		Flowable<Integer> f = Flowable.create(se -> {
//			System.out.println("doCreate");
//			se.onNext(1);
//			se.onNext(2);
//			se.onNext(3);
//			se.onComplete();
//		}, BackpressureStrategy.BUFFER);
		PublishProcessor<Integer> pp = PublishProcessor.create();
		pp.onNext(1);

		Flowable<Integer> f = Flowable.interval(450, TimeUnit.MILLISECONDS)
			.map(x -> x.intValue() + 1);
		//f.map(x -> x * x).observeOn(Schedulers.io()).forEach(System.out::println);
		ConnectableFlowable<Integer> c = f.publish();
		c.observeOn(Schedulers.computation()).map(x -> 0).doOnComplete(() -> {
			System.out.println("complete");
		}).forEach(System.out::println);
		Thread.sleep(1000);
//		Flowable<Integer> ff = c.autoConnect();
		Flowable<Integer> ff = c.refCount();
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		//c.connect();
		Thread.sleep(1000);
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		ff.limit(1).observeOn(Schedulers.io()).forEach(System.out::println);
		System.out.println("asdf");
//		ff.observeOn(Schedulers.io()).forEach(System.out::println);
//		ff.observeOn(Schedulers.io()).forEach(System.out::println);
//		ff.observeOn(Schedulers.io()).forEach(System.out::println);
		//c.connect();
		Thread.sleep(1000);
	}

	@Test
	public void test2() {
		Flowable.using(
			() -> 1,
			source -> Flowable.range(1, source),
			source -> {
			});
	}

	@Test
	public void test_flatMapSingle() throws InterruptedException {
		//flatMap的结果是否保持有序?
		Flowable.just(1L, 2L)
			.flatMapSingle(x -> {
				if (x == 1) {
					return Single.timer(2, TimeUnit.SECONDS).map(ignore -> x);
				} else {
					return Single.timer(1, TimeUnit.SECONDS).map(ignore -> x);
				}
			}).blockingForEach(System.out::println);
		Thread.sleep(4000);
	}

	@Test
	public void test_delay() {
		Flowable.just(1, 2, 3)
			.delay(1, TimeUnit.SECONDS)//会延迟1秒 然后突然发射出3个对象
			.blockingForEach(System.out::println);
	}

	@Test
	public void test_reduce() {
		Integer sum = Flowable.range(1, 10)
			.reduce(0, (sum0, x) -> sum0 + x)
			.blockingGet();
		System.out.println(sum);
	}

	@Test
	public void test_zip() {
		Flowable.zip(
			Flowable.interval(1, TimeUnit.MILLISECONDS).take(100).doOnNext(System.out::println),
			Flowable.interval(1, TimeUnit.SECONDS).map(x -> (char) ('a' + x.intValue())).map(Object::toString),
			(a, b) -> a + b,
			true, 3
		).blockingForEach(System.out::println);
	}

	@Test
	public void test_retry() {
		//这是一个比较复杂的retry版本
		//最多重试3次, 第一次失败过1秒才重试 第2次失败过2秒才重试 第3次失败3秒后才重试
		AtomicInteger ai = new AtomicInteger(0);
		Flowable.just(1, 2, 3)
			.doOnNext(e -> {
				if (e == 2 && ai.get() < 2) {
					ai.incrementAndGet();
					throw new IllegalStateException();
				}
			})
			.retryWhen(ef -> {
				//retryWhen会给你一个 Flowable<Throwable> 用于发射失败事件
				return ef.zipWith(
					//这里的3表示最多重试3次
					Flowable.range(1, 3),
					(e, i) -> {
						//这里需要传递一个合并函数
						System.out.println("第" + i + "次失败");
						//返回i*i 可以实现 第i次失败 间隔i*i 秒后才重试
						return i;
					}
				).flatMapSingle(s -> Single.timer(s, TimeUnit.SECONDS));
			})
			.blockingForEach(System.out::println);
	}

	@Test
	public void test_materialize() {
		Flowable.just(1, 2, 3)
			.materialize()
			.blockingForEach(n -> {
				System.out.println(n);
			});
	}

	@Test
	public void test_timestamp() {
		//为每个元素带上一个时间戳
		Flowable.just(1, 2, 3)
			.timestamp()
			.blockingForEach(e -> {
				System.out.println(e.time());
				System.out.println(e.value());
			});
	}

	@Test
	public void test_amb() {
		//哪个flowable先发射出第一个元素, 那么此后就只从它这里消费了
		List<Integer> list = Flowable.ambArray(
			Flowable.just(1, 2, 3).delay(200, TimeUnit.MILLISECONDS),
			Flowable.just(2, 3, 4).delay(100, TimeUnit.MILLISECONDS)
		).toList(3)
			.blockingGet();
		assertEquals(Arrays.asList(2, 3, 4), list);
	}

	@Test
	public void test() {
		AtomicInteger ai = new AtomicInteger();
		List<Integer> list = Flowable.just(1, 2, 3, 4)
			.subscribeOn(Schedulers.io())
			.flatMapSingle(x -> Single.fromCallable(() -> {
				System.out.println(ai.incrementAndGet());
				Thread.sleep(1000);
				ai.decrementAndGet();
				return x;
			}).subscribeOn(Schedulers.io()), true, 2)
			.toList()
			.blockingGet();
		System.out.println(list);
	}

	@Test
	public void test_parallel() {
		//将4个元素 放到2个线程里去执行 每个耗时大概是1秒
		//parallel 不支持工作窃取(你需要使用flatMap)
		//最后合并结果为一个list
		AtomicInteger ai = new AtomicInteger(0);
		List<Integer> list = Flowable.just(1, 2, 3, 4)
			.parallel(2, 5)
			.runOn(Schedulers.io())
			.map(x -> {
				if (x == 1) {
					Thread.sleep(5000);
				}
				System.out.println(ai.incrementAndGet());
				ai.decrementAndGet();
				return x * x;
			})
			.sequential()
			.toList()
			.blockingGet();
		System.out.println(list);
	}

	@Test
	public void test_merge() {
		//将两个F合并起来发射, 这个过程是线程安全的
		Flowable<Integer> f1 = Flowable.<Integer>create(se -> {
			for (int i = 0; i < 1000; ++i) {
				se.onNext(i);
				//Thread.sleep(1);
			}
			se.onComplete();
		}, BackpressureStrategy.BUFFER)
			.subscribeOn(Schedulers.io());
		AtomicInteger ai = new AtomicInteger(0);
		Flowable.merge(f1, f1)
			.blockingForEach(e -> {
				ai.incrementAndGet();
			});
		System.out.println(ai.get());
	}

	/**
	 * 用于分组
	 */
	@Test
	public void test_buffer_1_group() {
		//每2个元素放一组
		List<List<Integer>> list = Flowable.just(1, 2, 3, 4, 5)
			.buffer(2)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());
	}

	@Test
	public void test_buffer_2() {
		//每2个 或 每秒组成一个组 看哪个条件先达到了
		List<List<Integer>> list = Flowable.just(1, 2, 3, 4, 5)
			.buffer(1, TimeUnit.SECONDS, 2)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());

		list = Flowable.interval(400, TimeUnit.MILLISECONDS)
			.map(Long::intValue)
			.map(x -> x + 1)
			.take(5)
			.buffer(1, TimeUnit.SECONDS, 3)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());
	}
}
