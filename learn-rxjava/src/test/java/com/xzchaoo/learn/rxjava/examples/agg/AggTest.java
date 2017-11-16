package com.xzchaoo.learn.rxjava.examples.agg;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * created by xzchaoo at 2017/11/17
 *
 * @author xzchaoo
 */
class SearchContext {
	public List<String> engines;
}

public class AggTest {
	@Test
	public void test() {
		SearchContext ctx = new SearchContext();
		Single.just(ctx)
			.doOnSuccess(this::doSearchControl)
			.flatMapPublisher(this::splitToSubContexts)
			.observeOn(Schedulers.io())
			.flatMapSingle(this::doConcurrentSearch, true, 40)
			.observeOn(Schedulers.computation())
			.flatMapSingle(this::executeFirstStrategy, true, 8)
			.toList()
			.map(contexts -> mergeContext(ctx, contexts))
			.doOnSuccess(this::executeMergedStrategy)
			.subscribe(success -> {
				//success
			}, error -> {
				//error
				error.printStackTrace();
			});
	}

	private Flowable<SearchContext> splitToSubContexts(SearchContext searchContext) {
		return Flowable.fromIterable(searchContext.engines)
			.map(engine -> copyContext(searchContext, engine));
	}

	private void executeMergedStrategy(SearchContext context) {
	}

	private SearchContext mergeContext(SearchContext ctx, List<SearchContext> contexts) {
		//merged into ctx
		return ctx;
	}

	private Single<SearchContext> doConcurrentSearch(SearchContext searchContext) {
		if (searchContext.engines.size() == 1) {
			return Single.just(searchContext).flatMap(this::coreEngineSearch);
		} else {
			//do other search
			return Single.just(searchContext).flatMap(this::coreEngineSearch);
		}
	}

	private Single<SearchContext> executeFirstStrategy(SearchContext searchContext) {
		//execute first strategy
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
