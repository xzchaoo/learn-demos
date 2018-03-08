package com.xzchaoo.learn.rxjava.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public final class MySingleTimer2 extends Single<Long> {
	final long delay;
	final TimeUnit unit;
	final Scheduler scheduler;

	public MySingleTimer2(long delay, TimeUnit unit, Scheduler scheduler) {
		this.delay = delay;
		this.unit = unit;
		this.scheduler = scheduler;
	}

	@Override
	protected void subscribeActual(SingleObserver<? super Long> s) {
		MySingleTimer2Disposable disposable = new MySingleTimer2Disposable(s);
		s.onSubscribe(disposable);
		Disposable taskDisposable = scheduler.scheduleDirect(disposable, delay, unit);
		disposable.setTaskDispose(taskDisposable);
	}

	static final class MySingleTimer2Disposable extends AtomicReference<Disposable> implements Disposable, Runnable {

		private final SingleObserver<? super Long> actual;

		public MySingleTimer2Disposable(SingleObserver<? super Long> actual) {
			this.actual = actual;
		}

		@Override
		public void run() {
			actual.onSuccess(0L);
		}

		@Override
		public void dispose() {
			DisposableHelper.dispose(this);
		}

		@Override
		public boolean isDisposed() {
			return DisposableHelper.isDisposed(get());
		}

		void setTaskDispose(Disposable d) {
			DisposableHelper.replace(this, d);
		}
	}
}
