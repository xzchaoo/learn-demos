package com.xzchaoo.learn.spring.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TaskTestConfig.class, TaskTestConfig2.class})
public class TaskTest {
	@Test
	public void test1() {
	}
}
