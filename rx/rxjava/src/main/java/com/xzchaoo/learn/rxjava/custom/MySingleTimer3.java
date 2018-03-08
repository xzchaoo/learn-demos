package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.disposables.SequentialDisposable;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public final class MySingleTimer3 extends Single<Long> {
	final long delay;
	final TimeUnit unit;
	final Scheduler scheduler;

	public MySingleTimer3(long delay, TimeUnit unit, Scheduler scheduler) {
		this.delay = delay;
		this.unit = unit;
		this.scheduler = scheduler;
	}

	@Override
	protected void subscribeActual(SingleObserver<? super Long> s) {
		SequentialDisposable sd = new SequentialDisposable();
		s.onSubscribe(sd);
		sd.replace(scheduler.scheduleDirect(new MySingleTimer3Task(sd, s), delay, unit));
	}

	static final class MySingleTimer3Task implements Runnable {
		private final SingleObserver<? super Long> actual;

		public MySingleTimer3Task(SequentialDisposable sd, SingleObserver<? super Long> actual) {
			this.actual = actual;
		}

		@Override
		public void run() {
			actual.onSuccess(0L);
		}
	}
}
