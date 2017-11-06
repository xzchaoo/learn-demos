package com.xzchaoo.learn.log;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogApp.class);

	@Test
	public void test1() {
		LOGGER.info("{}", StringUtils.leftPad("xxx",5));
	}
}
