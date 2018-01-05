package com.xzchaoo.learn.rxjava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/1/4
 */
public class SingleZipTest {

	private static String f1(String param) {
		//假设耗时800毫秒
		try {
			System.out.println("开始执行f1");
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "同步调用方法1的结果(" + param + ")";
	}

	//f2是一个旧方法 它用的是 ListenableFuture 来实现回调
	private static ListenableFuture<String> f2(String param) {
		SettableFuture<String> sf = SettableFuture.create();
		new Thread(() -> {
			System.out.println("开始执行f2");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sf.set("异步调用方法2的结果(" + param + ")");
		}).start();
		return sf;
	}

	//因为它不支持Single, 所以这里提供一个方法将它转成一个Single
	//可以写成一个utils方法
	private <T> Single<T> toSingle(Callable<ListenableFuture<T>> c) {
		return Single.create(se -> {
			ListenableFuture<T> f = c.call();
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

	@Test
	public void test() throws InterruptedException {
		//因为f1是同步的 让它在IO线程上执行 此处的 Scheduler 可以认为是一个线程池的封装
		Single<String> s1 = Single.fromCallable(() -> f1("参数1")).subscribeOn(Schedulers.io());
		Single<String> s2 = toSingle(() -> f2("参数2")).timeout(1200, TimeUnit.MILLISECONDS);

		//根据zip的特性, 一个失败就算整个失败了 如果一个失败不算总体的失败, 并且流程想要继续走下去, 就需要各自处理失败情况
		Single<String> s3 = s1.zipWith(s2, (r1, r2) -> {
			//对两个结果进行合并, 并返回合并的结果
			return r1 + " " + r2;
		});

		System.out.println("当执行到这里的时候 其实f1和f2都不会被真的执行");

		//只有等到队s3进行 subscribe, 整个执行流程才会开始
		long begin = System.currentTimeMillis();
		s3.doFinally(() -> {
			System.out.println("我是finally");
		}).subscribe(finalResult -> {

			System.out.println("我是合并的结果 " + finalResult);
			long end = System.currentTimeMillis();
			System.out.println("总共耗时=" + (end - begin) + "毫秒");

		}, error -> {
			//其中任何一个步骤发生了错误 就会到这里
			//error.printStackTrace();
			System.out.println("发生了错误");
			error.printStackTrace();
		});


		System.out.println("上面的是异步的, 很快就会执行到我");

		//强制等待一下
		Thread.sleep(2000);
	}
}
