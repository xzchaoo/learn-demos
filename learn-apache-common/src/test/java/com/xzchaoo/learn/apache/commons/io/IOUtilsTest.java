package com.xzchaoo.learn.apache.commons.io;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class IOUtilsTest {
	@Test
	public void test1() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream("中文".getBytes(StandardCharsets.UTF_8));
		try {
			String s = IOUtils.toString(IOUtils.buffer(bais), StandardCharsets.UTF_8);
			assertEquals("中文", s);
		} finally {
			IOUtils.closeQuietly(bais);
		}
	}
}
