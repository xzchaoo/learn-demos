package com.xzchaoo.learn.rxjava.examples.agg;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
}

public class AggTest {
	@Test
	public void test() throws InterruptedException {
		Object requestDTO = new ArrayList<>();
		SearchContext ctx = new SearchContext();
		Disposable d = Single.just(ctx)
			.doOnSuccess(this::doSearchControl)
			.flatMapPublisher(this::splitToSubContexts)//10:10 0 0
			.observeOn(Schedulers.io())
			.flatMapSingle(this::doConcurrentSearch, true, 8)
			.observeOn(Schedulers.computation())
			.flatMapSingle(this::executeFirstStrategy, true, 2)
			.toList()
			.map(contexts -> mergeContext(ctx, contexts))
			.doOnSuccess(this::executeMergedStrategy)
			.subscribe(success -> {
				System.out.println("成功");
				//success
			}, error -> {
				//error
				error.printStackTrace();
			});
		System.out.println("请求是异步的 所以马上就会执行到这里 不会阻塞在上面");
		while (!d.isDisposed()) {
			Thread.sleep(1000);
		}
	}

	private Flowable<SearchContext> splitToSubContexts(SearchContext searchContext) {
		return Flowable.fromIterable(searchContext.engines)
			.map(engine -> copyContext(searchContext, engine));
	}

	private void executeMergedStrategy(SearchContext context) {
		System.out.println("执行merged策略");
	}

	private SearchContext mergeContext(SearchContext ctx, List<SearchContext> contexts) {
		//merged into ctx
		return ctx;
	}

	private Single<SearchContext> doConcurrentSearch(SearchContext searchContext) {
		if (searchContext.engines.size() == 1) {
			return this.coreEngineSearch(searchContext);
		} else {
			return this.coreEngineSearch(searchContext);
		}
	}

	private Single<SearchContext> executeFirstStrategy(SearchContext searchContext) {
		System.out.println("execute first strategy");
		return Single.just(searchContext);
	}

	private SearchContext copyContext(SearchContext originalContext, String engine) {
		//copy here
		SearchContext ctx = new SearchContext();
		ctx.engines = Collections.singletonList(engine);
		return ctx;
	}

	private Single<SearchContext> coreEngineSearch(SearchContext subContext) {
		return Single.just(subContext)
			.flatMap(e -> {
				//find cache if need
				//doSearchHere async or sync is ok!
				//save to cache
				return Single.timer(3, TimeUnit.SECONDS).map(x -> subContext);
			}).timeout(10, TimeUnit.SECONDS)
			.retry(1);
	}

	private void doSearchControl(SearchContext searchContext) {
		searchContext.engines = Arrays.asList("a", "b", "c");
	}
}
