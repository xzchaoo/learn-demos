package com.xzchaoo.learn.rxjava;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * @author zcxu
 * @date 2017/12/13
 */
public class LogTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

	@Test
	public void test_sequence() {
		//TODO 这里是为了弄清楚 doFinally 等方法到底是在什么时候被执行
		//特别是当有线程切换的情况时
		Single.just(1)
			.doOnSuccess(e -> {
				System.out.println(1);
			}).doFinally(() -> {
			System.out.println(2);
		}).doOnSuccess(e -> {
			System.out.println(3);
		}).map(x -> x * x)
			.doOnSuccess(e -> {
				System.out.println(4);
			})
			.flatMap(Single::just)
			.doOnSuccess(e -> {
				System.out.println(5);
			})
			.subscribe();
	}

	@Test
	public void test1() {
		//slf4j的log MDC机制依赖于线程本地变量 但是rx里边会经常切换线程

		MDC.put("a", "1");
		Map<String, String> mdc = MDC.getCopyOfContextMap();
		assertEquals("1", MDC.get("a"));
		Single.just(1)
			//这里切线程了 注意 不管 subscribeOn 在多后面 它总是会影响订阅根源的
			.subscribeOn(Schedulers.io())
			.doOnSuccess(ignore -> {
				//这里拿不到值
				assertNull(MDC.get("a"));
			})
			//之类进行恢复
			.doOnSuccess(ignore -> this.restoreMDC(mdc))
			.doOnSuccess(ignore -> {
				assertEquals("1", MDC.get("a"));
			})
			//这里进行清理
			.doFinally(() -> {
				System.out.println("1");
				MDC.clear();
			})
			.doFinally(() -> {
				//TODO 根据rx的定义 这个finally会先执行
				System.out.println("2");
				//assertNull(MDC.get("a"));
			}).blockingGet();
	}

	private void restoreMDC(Map<String, String> mdc) {
		MDC.getMDCAdapter().setContextMap(mdc);
	}
}
