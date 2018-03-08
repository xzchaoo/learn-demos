package com.xzchaoo.learn.other.compress;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class XZTest {
	@Test
	public void test() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; ++i) {
			sb.append("admin");
		}
		String s = sb.toString();
		for (FilterOptions fo : Arrays.asList(new LZMA2Options())) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XZOutputStream xzos = new XZOutputStream(baos, fo);
			xzos.write(s.getBytes(StandardCharsets.UTF_8));
			xzos.finish();
			xzos.close();
			System.out.println(baos.size());
			XZInputStream xzis = new XZInputStream(new ByteArrayInputStream(baos.toByteArray()));
			assertEquals(s, IOUtils.toString(xzis, StandardCharsets.UTF_8));
		}
	}
}
