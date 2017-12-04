package com.xzchaoo.learn.rxjava.examples.lowpriceprocessor;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * created by xzchaoo at 2017/10/29
 *
 * @author xzchaoo
 */
public class LowPriceProcessorTest {
	@Test
	public void test1() throws InterruptedException {
		//PublishProcessor 不能subscribeOn一起用
	}

	@Test
	public void test() throws InterruptedException {
		ProcessContext context = new ProcessContext();
		CountDownLatch cdl = new CountDownLatch(1);
		this.request(context)
			.timeout(250, TimeUnit.MILLISECONDS)
			.observeOn(Schedulers.computation())
			.doOnSuccess(this::next1)
			.doOnSuccess(this::next2)
			.subscribe(new SingleObserver<ProcessContext>() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onSuccess(ProcessContext processContext) {
					cdl.countDown();
				}

				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
				}
			});
		cdl.await();
	}

	private void next1(ProcessContext context) {
		System.out.println("next1");
	}

	private void next2(ProcessContext context) {
		System.out.println("next2");
	}

	private Single<ProcessContext> request(ProcessContext context) {
		System.out.println("request");
		int mills = 100 + ThreadLocalRandom.current().nextInt(100);
		return Single.timer(mills, TimeUnit.MILLISECONDS).map(x -> context);
	}
}
