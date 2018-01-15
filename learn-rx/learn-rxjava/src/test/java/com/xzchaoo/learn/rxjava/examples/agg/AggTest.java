package com.xzchaoo.learn.rxjava.examples.agg;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * created by xzchaoo at 2017/11/17
 *
 * @author xzchaoo
 */
class SearchContext {
	public List<String> engines;
	public Object result;
	public Throwable exception;
}

public class AggTest {
	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < 10; ++i) {
			//一个用户请求是一个context
			//将请求转成context的步骤可以是任意的 不一定非要做成 Reactive Style
			SearchContext ctx = new SearchContext();

			long begin = System.currentTimeMillis();
			Disposable d = doSearch(ctx)
				.subscribe(success -> {
					System.out.println("总耗时 = " + (System.currentTimeMillis() - begin));
					System.out.println("执行结束 " + success.result);
					//success
				}, error -> {
					//其实不会发生error 因为我们在上游已经将error全部处理了
					//error
					error.printStackTrace();
				});

			System.out.println("请求是异步的 所以马上就会执行到这里 不会阻塞在上面");
			while (!d.isDisposed()) {
				Thread.sleep(1000);
			}
		}
	}

	private Single<SearchContext> doSearch(SearchContext rootSearchContext) {
		return Single.defer(() -> {
			List<SearchContext> subContexts = splitToSubContexts(rootSearchContext);
			return Flowable.fromIterable(subContexts)
				//这里不用IO线程 因此 订阅 这个动作发生在当前线程上(哪个线程触发了subscribe, 就在那个线程上)
				//.observeOn(Schedulers.io())
				.flatMapSingle(this::doCoreSearch, true, 8)
				//.concatMapEagerDelayError(this::doCoreSearch, 8, 8, true)
				.toList()
				.observeOn(Schedulers.computation())
				//注意这里的contexts2的顺序跟原来不一样 如果需要保持一致 可以自行操作 或 用 concatMap
				.map(contexts2 -> mergeContext(rootSearchContext, contexts2))
				.doOnSuccess(this::executeMergedStrategy);
		});
	}


	private ListenableFuture<String> searchCombinedPU(String pattern) {
		SettableFuture<String> sf = SettableFuture.create();

		System.out.println("多票 " + pattern);
		new Thread(() -> {
			try {
				if ("bb".equals(pattern)) {
					Thread.sleep(2000);
				} else {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("多票 " + pattern + " 结束");
			sf.set("pattern=" + pattern + "成功");
		}).start();

		return sf;
	}

	private <T> Single<T> toSingle(Callable<ListenableFuture<T>> c) {
		return Single.create(se -> {
			ListenableFuture<T> lf = c.call();
			Futures.addCallback(lf, new FutureCallback<T>() {
				@Override
				public void onSuccess(@Nullable T t) {
					se.onSuccess(t);
				}

				@Override
				public void onFailure(Throwable throwable) {
					se.onError(throwable);
				}
			});
			se.setCancellable(() -> lf.cancel(true));
		});
	}

	/**
	 * 进行核心查询, 主要就是发请求了
	 *
	 * @param context
	 * @return
	 */
	private Single<SearchContext> doCoreSearch(SearchContext context) {
		String engine = context.engines.get(0);
		Single<SearchContext> s;
		if ("CombinedPU".equals(engine)) {
			System.out.println(System.currentTimeMillis() + " 开始查询 " + context.engines + " 引擎");
			List<String> combinedPUPattern = Arrays.asList("aa", "bb", "cc");
			s = Flowable.fromIterable(combinedPUPattern)
				.flatMapMaybe(pattern -> {
					return toSingle(() -> searchCombinedPU(pattern))
						.timeout(500, TimeUnit.MILLISECONDS)
						.toMaybe()
						.doOnError(error -> {
							System.out.println("模式 " + pattern + " 失败, 但不影响整体");
						}).onErrorComplete();
				}, true, 2)
				.toList()
				.map(allPatternResult -> {
					context.result = allPatternResult;
					return context;
				});
		} else {
			s = Single.create(se -> {
				Thread t = new Thread(() -> {
					try {
						System.out.println(System.currentTimeMillis() + " 开始查询 " + context.engines + " 引擎");
						if (engine.equals("Ctrip")) {
							Thread.sleep(200);
						} else if (engine.equals("SharedPlatform")) {
							//会导致超时
							Thread.sleep(2000);
						} else {
							Thread.sleep(1000);
						}
						System.out.println(System.currentTimeMillis() + " " + context.engines + " 引擎查询结束 结果是OK");
						context.result = "OK";
						se.onSuccess(context);
					} catch (Exception e) {
						System.out.println(System.currentTimeMillis() + " " + context.engines + " 引擎查询异常 " + e);
						se.onError(e);
					}
				});
				se.setCancellable(t::interrupt);
				t.start();
			});
		}
		return s.timeout(1500, TimeUnit.MILLISECONDS)
			//.observeOn(Schedulers.computation())
			//executeFirstStrategy
			.flatMap(context2 -> {
				//.doOnSuccess(this::executeFirstStrategy)
				return this.executeFirstStrategy(context2).andThen(Single.just(context2));
			})
			.onErrorReturn(error -> {
				context.exception = error;
				return context;
			});
	}

	private List<String> getQueryEngines(SearchContext rootSearchContext) {
		return Arrays.asList("Ctrip", "SharedPlatform", "CharacterPlatform", "Pricing", "CombinedPU", "Spring");
	}

	private List<SearchContext> splitToSubContexts(SearchContext searchContext) {
		return getQueryEngines(searchContext)
			.stream()
			.map(engine -> copyContext(searchContext, engine))
			.collect(Collectors.toList());
	}

	private void executeMergedStrategy(SearchContext context) throws InterruptedException {
		//merged策略耗时1秒
		Thread.sleep(1000);
		System.out.println("执行merged策略");
	}

	private SearchContext mergeContext(SearchContext rootContext, List<SearchContext> subContexts) {
		StringBuilder sb = new StringBuilder();
		for (SearchContext subContext : subContexts) {
			if (subContext.exception != null) {
				sb.append(subContext.engines.get(0)).append("=失败");
			} else {
				sb.append(subContext.engines.get(0)).append("=").append(subContext.result);
			}
			sb.append(' ');
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		rootContext.result = sb.toString();
		return rootContext;
	}

	private Completable executeFirstStrategy(SearchContext searchContext) throws InterruptedException {
		//第一个策略耗时1秒 如果不是多线程执行的话 一下子就可以看出来了!
		Thread.sleep(1000);
		System.out.println(System.currentTimeMillis() + " " + searchContext.engines + " execute first strategy");
		return Completable.complete();
	}

	private SearchContext copyContext(SearchContext originalContext, String engine) {
		//copy here
		SearchContext ctx = new SearchContext();
		ctx.engines = Collections.singletonList(engine);
		return ctx;
	}
}
