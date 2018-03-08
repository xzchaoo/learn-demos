package com.xzchaoo.learn.rxjava.examples.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xzchaoo
 * @date 2018/1/3
 */
public class ThreadPoolTest {
	@Test
	public void test() {
		//想象这是一个全局线程池
		ExecutorService globalThreadPool = Executors.newFixedThreadPool(8, new ThreadFactoryBuilder().setNameFormat("haha-%d").build());
		//包装成scheduler
		Scheduler s = Schedulers.from(globalThreadPool);

		//现在有100个任务 想用4个线程去并发跑它
		List<Integer> result = Flowable.range(1, 100)
			.concatMapEager(x -> Flowable.fromCallable(() -> {
				System.out.println("正在处理 " + x + " " + Thread.currentThread().getName());
				Thread.sleep(1000);
				return x * x;
			}).subscribeOn(s), 4, 1)
			.toList()
			.blockingGet();

		System.out.println(result);

		globalThreadPool.shutdown();
	}
}
