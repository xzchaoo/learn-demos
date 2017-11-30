package com.xzchaoo.learn.log.log4j2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by zcxu at 2017/11/30
 *
 * @author zcxu
 */
public class Log4j2Test {
	private static final Logger LOGGER = LoggerFactory.getLogger(Log4j2Test.class);

	@Test
	public void test() {
		LOGGER.debug("deb");
		LOGGER.info("asdf");
	}
}
