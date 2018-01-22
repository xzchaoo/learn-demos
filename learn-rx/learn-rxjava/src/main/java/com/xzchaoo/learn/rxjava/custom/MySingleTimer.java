package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zcxu
 * @date 2018/1/17
 */
public final class MySingleTimer extends Single<Long> {
	final long delay;
	final TimeUnit unit;
	final Scheduler scheduler;

	public MySingleTimer(long delay, TimeUnit unit, Scheduler scheduler) {
		this.delay = delay;
		this.unit = unit;
		this.scheduler = scheduler;
	}

	@Override
	protected void subscribeActual(SingleObserver<? super Long> observer) {
		CompositeDisposable set = new CompositeDisposable();
		observer.onSubscribe(set);
		Disposable disposable = scheduler.scheduleDirect(new Runnable() {
			@Override
			public void run() {
				System.out.println("Sdfsdf;;");
				if (!set.isDisposed()) {
					observer.onSuccess(0L);
				}
			}
		}, delay, unit);
		set.add(disposable);
	}
}
