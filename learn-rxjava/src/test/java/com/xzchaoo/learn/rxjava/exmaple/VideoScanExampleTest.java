package com.xzchaoo.learn.rxjava.exmaple;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是一个批量查询视频信息的例子
 */
public class VideoScanExampleTest {
	private static ListenableFuture<String> getVideoByAid(int aid) {
		SettableFuture<String> future = SettableFuture.create();
		new Thread(() -> {
			try {
				//System.out.println("sleep " + Thread.currentThread());
				int time = ThreadLocalRandom.current().nextInt(1000);
				Thread.sleep(time + 500);
				future.set(Integer.toString(time));
			} catch (Exception e) {
				future.setException(e);
			}
		}).start();
		return future;
	}

	@Test
	public void test() throws InterruptedException {
		AtomicInteger ai = new AtomicInteger();

		//现在想要爬取 [100,200) 的视频信息
		Disposable d = Flowable.range(100, 100).map(aid -> {
			//利用一个context类来封装一个请求的相关的信息, 否则如果你的map嵌套很深的话 你就不知道最初或中间的某些对象了
			ScanVideoContext ctx = new ScanVideoContext();
			ctx.aid = aid;
			return ctx;
		}).flatMapSingle(ctx -> Single.<ScanVideoContext>create(e -> {
			System.out.println("1. 现在的并发量是  " + ai.incrementAndGet() + " " + Thread.currentThread());
			//这里模拟发送异步HTTP请求
			ListenableFuture<String> future = getVideoByAid(ctx.aid);
			Futures.addCallback(future, new FutureCallback<String>() {
				@Override
				public void onSuccess(String response) {
					ai.decrementAndGet();
					//System.out.println("2. 现在的并发量是  " + ai.decrementAndGet() + " " + Thread.currentThread());
					ctx.response = response;
					e.onSuccess(ctx);
				}

				@Override
				public void onFailure(Throwable t) {
					e.onError(t);
				}
			});
			//这里限制了最多16并发
		}), true, 16)
			//在IO线程上订阅 其实无所谓的 因为请求是异步的 不阻塞
			.subscribeOn(Schedulers.io())
			//在计算线程回调
			.observeOn(Schedulers.computation())
			.doOnNext(ctx -> {
				//这里仅仅是为了打印信息而已
				System.out.println(ctx.aid + " " + ctx.response + " " + Thread.currentThread());
			})
			.toList()//聚合成list
			.subscribe(contexts -> {
				System.out.println("结果是 " + contexts.size());
			});
		System.out.println("Flowable已经返回");
		System.out.println(d.isDisposed());
		//d.dispose();
		Thread.sleep(200000);
	}

	@Ignore
	@Test
	public void test1() throws InterruptedException {
		//下面的代码会产生一个背压错误
		//搜索 [100,200) 的视频
		Disposable d = Flowable.range(100, 100).map(aid -> {
			ScanVideoContext ctx = new ScanVideoContext();
			ctx.aid = aid;
			return ctx;
		}).flatMap(ctx -> {
			//每个请求可以分裂成多个小请求
			//int delay = ThreadLocalRandom.current().nextInt(2000);
			if (ctx.aid == 101) {
				return Flowable.interval(1, TimeUnit.MILLISECONDS).take(1000).doOnNext(x -> {
					System.out.println("产生了一个元素 " + x);
				});
			}
			return Flowable.intervalRange(0, 10, 0, 1, TimeUnit.SECONDS).map(x -> {
				System.out.println("产生了一个元素 " + ctx.aid);
				return x;
			});//.delay(delay, TimeUnit.MILLISECONDS);
		}, false, 4, 128)//这里的4表示最多允许4个大请求并发flatMap 128表示每个子flowable的buffer大小, 在数量比较小的时候这个buffer的值不一定准确
			//一旦上游产生的数量超过这个buffer值, 就会产生背压, 如果上游不支持背压, 就会抛出异常, 此时如果 delayError==true 就会推迟这个error直到整个执行结束才会触发error

			//.observeOn(Schedulers.computation(), true, 4)
			.flatMapSingle(x -> Single.create(s -> {
				//注意这个Single需要在IO线程里执行 因为它用了 sleep
				Thread.sleep(100);
				System.out.println("处理了一个元素 " + x);
				s.onSuccess(x);
			}).subscribeOn(Schedulers.io()), true, 16)//这里的16表示最多允许16个Single并发构建
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.computation(), true, 1)
			.toList()
			.subscribe(contexts -> {
				System.out.println("结果是 " + contexts.size());
			});
		System.out.println(d.isDisposed());
		//d.dispose();
		Thread.sleep(20000);
	}
}

class ScanVideoContext {
	public int aid;
	public String response;
	//public StringBuilder sb = new StringBuilder();
}