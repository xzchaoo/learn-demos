package com.xzchaoo.learn.dropwizard.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

public class App {
	//注册中心
	static final MetricRegistry metrics = new MetricRegistry();

	@Test
	public void test() throws InterruptedException {
		//测量值 会统计 每分钟 每五分钟 每十五分钟的频率
		Meter requests = metrics.meter("requests");
		requests.mark();

		metrics.register(name(App.class, "size"), new Gauge<Integer>() {
			@Override
			public Integer getValue() {
				return new Random().nextInt();
			}
		});

		//计数器 貌似真的只有计数功能 这不会溢出吗? 虽然是个long类型
		Counter pendingJobs = metrics.counter(name(App.class, "pending-jobs"));
		pendingJobs.inc();

		//统计百分位数 平均值 方差 标准差 这个没有时间限制吗
		Histogram responseSizes = metrics.histogram(name(App.class, "response-sizes"));
		responseSizes.update(1);
		responseSizes.update(2);
		responseSizes.update(3);

		//汇报器
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS)
			.build();
		reporter.start(1, TimeUnit.SECONDS);

		Thread.sleep(100000);
	}
}