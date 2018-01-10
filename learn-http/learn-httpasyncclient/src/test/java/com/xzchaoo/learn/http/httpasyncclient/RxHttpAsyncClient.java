package com.xzchaoo.learn.http.httpasyncclient;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * @author xzchaoo
 * @date 2018/1/9
 */
public class RxHttpAsyncClient {
	private HttpAsyncClient hac;

	public RxHttpAsyncClient(HttpAsyncClient hac) {
		this.hac = hac;
	}

	public static RxHttpAsyncClient wrap(HttpAsyncClient hac) {
		return new RxHttpAsyncClient(hac);
	}

	private static class SingleFutureCallback<T> implements FutureCallback<T> {
		private SingleEmitter<T> emitter;

		public SingleFutureCallback(SingleEmitter<T> emitter) {
			this.emitter = emitter;
		}

		@Override
		public void completed(T result) {
			emitter.onSuccess(result);
		}

		@Override
		public void failed(Exception ex) {
			emitter.onError(ex);
		}

		@Override
		public void cancelled() {

		}
	}

	public Single<HttpResponse> execute(HttpUriRequest request) {
		return create(cb -> hac.execute(request, cb));
	}

	public Single<HttpResponse> execute(HttpUriRequest request, HttpContext context) {
		return create(cb -> hac.execute(request, context, cb));
	}

	public Single<HttpResponse> execute(HttpHost host, HttpRequest request) {
		return create(cb -> hac.execute(host, request, cb));
	}

	public Single<HttpResponse> execute(HttpHost host, HttpRequest request, HttpContext context) {
		return create(cb -> hac.execute(host, request, context, cb));
	}

	public <T> Single<T> execute(HttpAsyncRequestProducer producer, HttpAsyncResponseConsumer<T> consumer) {
		return create(cb -> hac.execute(producer, consumer, cb));
	}

	public <T> Single<T> execute(HttpAsyncRequestProducer producer, HttpAsyncResponseConsumer<T> consumer, HttpContext context) {
		return create(cb -> hac.execute(producer, consumer, context, cb));
	}

	private <T> Single<T> create(Function<SingleFutureCallback<T>, Future<T>> callbackConsumer) {
		return Single.create(se -> {
			Future<T> future = callbackConsumer.apply(new SingleFutureCallback<>(se));
			se.setCancellable(() -> future.cancel(true));
		});
	}
}
