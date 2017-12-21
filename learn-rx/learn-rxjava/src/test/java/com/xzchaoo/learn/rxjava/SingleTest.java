package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

import static org.junit.Assert.*;

/**
 * created by zcxu at 2017/10/31
 *
 * @author zcxu
 */
public class SingleTest {
	private static String tname() {
		return Thread.currentThread().getName();
	}

	private static void mark(String tag) {
		System.out.println(tag + " " + tname());
	}

	@Test
	public void test_lifecycle() {
		Single.create(se -> {
			//create的执行线程 默认是当前线程, 如果调用了 subscribeOn 那么就是指定的线程
			mark("1");
			Thread.sleep(500);
			se.onSuccess("ok");
		}).subscribeOn(Schedulers.io())
			.doAfterTerminate(() -> {
				//terminal只会在 onError 和 onSuccess 之后执行, onDispose 是不执行的!
				mark("2");
			})
			.doAfterTerminate(() -> {
				mark("6");//6比2早 越晚注册越早执行
			})
			.doFinally(() -> {
				mark("7");//finally早于terminal finally会在 onError onSuccess onDispose 后执行
			})
			.doFinally(() -> {
				mark("8");//8比7早
			})
			.doOnSuccess(e -> {
				mark("3");
			})
			.observeOn(Schedulers.computation())//会影响之后的执行
			.doOnSuccess(e -> {
				mark("4");
			})
			.doOnSuccess(e -> {
				mark("5");
			}).blockingGet();
	}

	@Test
	public void test_timeout() {
		//某个操作 执行不能超过2秒, 最多重试1次, 每次重试会间隔1秒
		AtomicInteger ai = new AtomicInteger(0);
		long begin = System.currentTimeMillis();
		Single.create(se -> {
			int c = ai.incrementAndGet();
			if (c <= 2) {
				Thread.sleep(1500);
			} else {
				se.onSuccess("ok");
			}
		}).timeout(1, TimeUnit.SECONDS)
			.onErrorResumeNext(e -> Single.timer(500, TimeUnit.MILLISECONDS).flatMap(x -> Single.error(e)))
			.retry(2)
			.subscribeOn(Schedulers.io())
			.blockingGet();
		long duration = System.currentTimeMillis() - begin;
		assertTrue(duration >= 3000 && duration < 3500);
	}

	@Test
	public void test_sleep_block_mainThread() {
		System.out.println(
			Single.create(se -> {
				System.out.println(Thread.currentThread().getName());
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					System.out.println("线程被打断了 " + e);
				}
				System.out.println("完了");
				se.onSuccess(1);
			}).subscribeOn(Schedulers.io())
				.timeout(1, TimeUnit.SECONDS)//timeout并不会打断线程
				.blockingGet()
		);
	}

	@Test
	public void test3() {
		Single.just(1).doOnSuccess(x -> {
			System.out.println("成功1 " + x);
		}).doOnSuccess(x -> {
			System.out.println("成功1.1 " + x);
		}).map(x -> 2).doOnSuccess(x -> {
			System.out.println("成功2 " + x);
		}).doOnError(e -> {
			System.out.println("出错了");
		}).flatMap(x -> Single.error(new RuntimeException()))
			.doOnError(e -> {
				System.out.println("我才是真的会被回调的onError");
			})
			.subscribe();
	}

	@Test
	public void test2() {
		//注意这里doOnSuccess 和 doAfterTerminate doAfterSuccess 等 并不能直接当成回调来用, 注意看它们的注册顺序和调用顺序
		SingleSubject<Integer> ss = SingleSubject.create();
		ss.doAfterSuccess(e -> {//早注册胜利
			System.out.println("成功0");
		}).doAfterSuccess(e -> {
			System.out.println("成功-1");
		}).doOnSuccess(e -> {
			System.out.println("成功1");
		}).doOnSuccess(e -> {
			System.out.println("成功2");
		}).doOnSuccess(e -> {
			System.out.println("成功3");
		}).doOnSuccess(e -> {
			System.out.println("成功4");
		}).doAfterTerminate(() -> {
			System.out.println("成功5");
		}).doAfterTerminate(() -> {//晚注册胜利
			System.out.println("成功6");//后注册的反而先执行
		}).doFinally(() -> {
			System.out.println("i am finally 1");
		}).doFinally(() -> {//晚注册胜利
			System.out.println("i am finally 2");
		}).subscribe();
		ss.onSuccess(1);
	}

	@Test
	public void test1() {
		Single.error(new RuntimeException())
			.flatMap(e -> {
				System.out.println("如果一开始就成功就会走这里的逻辑");
				return Single.just(e);//.onErrorResumeNext();//后来才失败就会走这里
			})//一开始就失败
			.onErrorResumeNext(e -> {
				System.out.println("失败了, 但是我继续");
				return Single.just(3);
			}).subscribe(e -> {
			System.out.println("成功了");
		}, e -> {
			System.out.println("失败了");
		});
		System.out.println(2);
	}

	@Test
	public void test_create() throws InterruptedException {
		Single.<String>create(se -> {
			Thread t = new Thread(() -> {
				System.out.println("睡觉1秒");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				se.onSuccess("123");
			});
			t.setName("haha");
			t.start();
		}).doOnSuccess(e -> {
			//这个回调在触发线程上
			System.out.println(Thread.currentThread());
		}).subscribe();
		Thread.sleep(2000);
	}
}
