package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * TODO 完善注释
 *
 * @author zcxu
 * @date 2018/1/17
 */
public class MyDeferCompletable extends Completable {
	private final CompletableSource source;
	private final long timeout;
	private final TimeUnit unit;
	private final Scheduler scheduler;

	public MyDeferCompletable(
		CompletableSource source,
		long timeout,
		TimeUnit unit,
		Scheduler scheduler
	) {
		this.source = source;
		this.timeout = timeout;
		this.unit = unit;
		this.scheduler = scheduler;
	}

	@Override
	protected void subscribeActual(CompletableObserver s) {
		CompositeDisposable set = new CompositeDisposable();
		s.onSubscribe(set);
		source.subscribe(new TimeOutObserver(set, s));
	}

	final class TimeOutObserver extends AtomicBoolean implements CompletableObserver {
		CompositeDisposable set;
		CompletableObserver s;

		TimeOutObserver(CompositeDisposable set, CompletableObserver s) {
			this.set = set;
			this.s = s;
		}


		@Override
		public void onSubscribe(Disposable d) {
			set.add(d);
		}

		@Override
		public void onComplete() {
			Disposable disposable = scheduler.scheduleDirect(new DelayCompleteTask(), timeout, unit);
			set.add(disposable);
		}

		@Override
		public void onError(Throwable e) {
			if (compareAndSet(false, true)) {
				//立即错误
				set.dispose();
				s.onError(e);
			} else {
				RxJavaPlugins.onError(e);
			}
		}

		final class DelayCompleteTask implements Runnable {

			@Override
			public void run() {
				if (compareAndSet(false, true)) {
					//delay complete}
					set.clear();
					s.onComplete();
				}
			}
		}
	}
}
