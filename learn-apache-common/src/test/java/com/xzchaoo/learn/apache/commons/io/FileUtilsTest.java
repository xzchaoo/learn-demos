package com.xzchaoo.learn.apache.commons.io;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {
	@Test
	public void test1() throws IOException, URISyntaxException {
		URL url = getClass().getClassLoader().getResource("1.txt");
		assertNotNull(url);
		System.out.println(url);
		File file = new File(url.toURI());
		List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		assertEquals(1, lines.size());
		assertEquals("中文", lines.get(0));
		String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8).trim();
		assertEquals("中文", s);
		//FileUtils.readFileToByteArray(new File("1.txt"));

		//FileUtils.writeLines();
		//FileUtils.write();

	}
}
