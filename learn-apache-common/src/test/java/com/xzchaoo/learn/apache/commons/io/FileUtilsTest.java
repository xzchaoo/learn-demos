package com.xzchaoo.learn.apache.commons.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {
	@Ignore
	@Test
	public void test() throws Exception {
		FileAlterationMonitor monitor = new FileAlterationMonitor(5000);
		FileAlterationObserver fab = new FileAlterationObserver(
			"C:/temp",
			file -> file.getName().equals("1.txt"),
			IOCase.SENSITIVE
		);
		fab.addListener(new FileAlterationListenerAdaptor() {
			@Override
			public void onFileChange(File file) {
				System.out.println("onFileChange");
			}
		});
		monitor.addObserver(fab);
		monitor.start();
		//JDK7以下可以用 Charsets.UTF_8
	}

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
