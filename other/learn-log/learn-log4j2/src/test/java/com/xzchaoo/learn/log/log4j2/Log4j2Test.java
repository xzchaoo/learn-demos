package com.xzchaoo.learn.log.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.EntryMessage;
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
	org.apache.logging.log4j.Logger log = LogManager.getLogger(Log4j2Test.class);

	@Test
	public void test() {
		EntryMessage e = log.traceEntry();
		LOGGER.debug("deb");
		LOGGER.info("asdf");
		log.traceExit(e);
	}
}
