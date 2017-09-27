package com.xzchaoo.learn.guava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class FutureTest {
	@Test
	public void test_SettableFuture() {
		SettableFuture<String> sf = SettableFuture.create();
		sf.addCallback(new FutureCallback<String>() {
			@Override
			public void onSuccess(@Nullable String result) {
				System.out.println(result);
			}

			@Override
			public void onFailure(Throwable t) {

			}
		}, MoreExecutors.directExecutor());
		sf.set("a");
	}

	@Test
	public void test_allAsList() {
		SettableFuture<Integer> sf1 = SettableFuture.create();
		SettableFuture<Integer> sf2 = SettableFuture.create();
		ListenableFuture<List<Integer>> allFuture = Futures.allAsList(sf1, sf2);
		allFuture.addListener(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(allFuture.get());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, MoreExecutors.directExecutor());
		sf1.set(1);
		sf2.set(2);
	}

	@Test
	public void test_timeout() throws InterruptedException {
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		try {
			SettableFuture<Integer> sf = SettableFuture.create();
			Futures.addCallback(Futures.withTimeout(sf, 1, TimeUnit.SECONDS, ses), new FutureCallback<Integer>() {
				@Override
				public void onSuccess(@Nullable Integer result) {
					System.out.println(result);
				}

				@Override
				public void onFailure(Throwable t) {
					t.printStackTrace();
				}
			}, MoreExecutors.directExecutor());
			Thread.sleep(1500);
		} finally {
			ses.shutdownNow();
		}
	}
}
