package com.xzchaoo.learn.jdk8.threadpool;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * created by xzchaoo at 2017/12/3
 *
 * @author xzchaoo
 */
public class ScheduleExecutorServiceTest {
	@Test
	public void test() {
		//具备调度能力的schedule
		//ses只具备简单的调度能力 如果想要实现 cron 那么需要自己定义一个封装
		//很不幸 ses 不支持对进入的任务进行wrapper 所以需要自己再ses的基础之上包装一层
		ScheduledThreadPoolExecutor ses = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
		ses.shutdownNow();
	}
}
