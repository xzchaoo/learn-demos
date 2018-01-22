package com.xzchaoo.learn.rxjava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

import static org.junit.Assert.assertTrue;

/**
 * 待学习
 * cache
 * compose
 * concatMap
 * debounce
 * delay
 * groupJoin
 * lift
 * parallel
 * ParallelFlowable
 * repeat
 * replay
 * ConnectableFlowable
 * publish
 * rebatchRequests
 * throttleFirst
 * share
 * <p>
 * join
 * 左右两边发射出的元素各有一个生命时间, 在它生命时间结束之前
 * 和能另外一边发射的还未过期的元素进行组合
 * <p>
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

	private static ListenableFuture<String> fa() {
		return createSF("1", 1000);
	}

	private static ListenableFuture<String> fb() {
		return createSF("2", 1000);
	}

	private static ListenableFuture<String> fc() {
		return createSF("3", 1000);
	}

	private static ListenableFuture<String> createSF(String value, int mills) {
		SettableFuture<String> sf = SettableFuture.create();
		new Thread(() -> {
			try {
				Thread.sleep(mills);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sf.set(value);
		}).start();
		sf.cancel(true);
		return sf;
	}

	private <T> Single<T> futureToSingle(Callable<ListenableFuture<T>> c) {
		return Single.create(se -> {
			ListenableFuture<T> f = c.call();
			se.setCancellable(() -> f.cancel(true));
			Futures.addCallback(f, new FutureCallback<T>() {
				@Override
				public void onSuccess(@Nullable T result) {
					se.onSuccess(result);
				}

				@Override
				public void onFailure(Throwable t) {
					se.onError(t);
				}
			}, MoreExecutors.directExecutor());
		});
	}

	//异步调用接口A 根据其结果可能会异步调用 接口B 或 接口C
	@Test
	public void test_dynamic() {
		String value = futureToSingle(SingleTest::fa)
			.flatMap(x -> {
				if ("1".equals(x)) {
					return futureToSingle(SingleTest::fb).map(y -> x + y);
				} else {
					return futureToSingle(SingleTest::fc).map(y -> x + y);
				}
			})
			.blockingGet();
		System.out.println(value);
	}

	@Test
	public void test_then() {
		//对应 reactor 的then操作

		long begin = System.currentTimeMillis();
		Single<Integer> s1 = Single.fromCallable(() -> {
			System.out.println("想象我是第1个异步");
			return 1;
		}).toCompletable()//通过这个方法可以将single转成completable 从而获得 andThen 的能力
			.delay(1, TimeUnit.SECONDS)
			.andThen(Single.fromCallable(() -> {
				System.out.println("想象我是第2个异步");
				return 2;
			}));

		//强制延迟
		Single<Integer> c2 = s1.zipWith(Single.timer(1500, TimeUnit.MILLISECONDS), (x, ignore) -> x);

		c2.blockingGet();
		System.out.println("耗时" + (System.currentTimeMillis() - begin));
	}

	@Test
	public void test_lifecycle() throws InterruptedException {
		Disposable ss = Single.create(se -> {
			//create的执行线程 默认是当前线程, 如果调用了 subscribeOn 那么就是指定的线程
			mark("create 1");
			//se.onError(new RuntimeException());
			Thread.sleep(500);
			se.onSuccess("ok");
		}).subscribeOn(Schedulers.io())
			.doAfterTerminate(() -> {
				//terminal只会在 onError 和 onSuccess 之后执行, onDispose 是不执行的!
				mark("dat 2");
			})
			.doAfterTerminate(() -> {
				mark("dat 6");//6比2早 越晚注册越早执行
			})
			.doFinally(() -> {
				mark("finally 7");//finally早于terminal finally会在 onError onSuccess onDispose 后执行
			})
			.doFinally(() -> {//出现并发执行了!
				mark("finally 8 sleep");
				Thread.sleep(5000);
				mark("finally 8");//8比7早
			})
			.doOnSuccess(e -> {
				mark("3");
			})
			.observeOn(Schedulers.computation())//会影响之后的执行
			.doOnSuccess(e -> {//出现并发执行了!
				mark("4 sleep");
				Thread.sleep(5000);
				mark("4");
			})
			.doOnDispose(() -> {
				System.out.println("dispose了");
			})
			.doOnSuccess(e -> {
				mark("5");
			})
			.doFinally(() -> {
				mark("finally 9");//8比7早
			}).doOnError(e -> {
				System.out.println("error了");
			})
			.toCompletable()
			.doFinally(() -> {
				mark("finally 10");
			})
			.doOnComplete(() -> {
				mark("complete 1");
			})
			.subscribeOn(Schedulers.io())
			.subscribe();
		System.out.println("完");
		Thread.sleep(10000);
		//ss.dispose();
		System.out.println("zheli");
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
