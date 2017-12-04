package com.xzchaoo.learn.rx.reactor;

import org.junit.Test;

import java.time.Duration;
import java.util.function.Consumer;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import static org.junit.Assert.*;

/**
 * created by xzchaoo at 2017/12/4
 *
 * @author xzchaoo
 */
public class MonoTest {
	@Test
	public void test_just() {
		//mono相当于rxjava种的maybe
		//注意reactor的consumer是不能抛出异常的...
		Integer r = Mono.just(2).map(x -> x * x).block();
		assertEquals(4, r.intValue());
	}

	@Test
	public void test_delay() {
		int r = Mono.delay(Duration.ofMillis(300)).map(x -> 1).block();
		assertEquals(1, r);

		r = Mono.just(1).delayElement(Duration.ofMillis(200)).block();
		assertEquals(1, r);
	}

	@Test
	public void test_create() {
		String s = Mono.create((Consumer<MonoSink<String>>) ms -> new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ms.success("a");
		}).start()).map(String::toUpperCase)
			.block();
		assertEquals("A", s);
	}
}
