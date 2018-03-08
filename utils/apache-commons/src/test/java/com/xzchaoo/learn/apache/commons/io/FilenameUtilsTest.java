package com.xzchaoo.learn.apache.commons.io;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilenameUtilsTest {
	@Test
	public void test() {
		assertEquals("2.txt", FilenameUtils.getBaseName("c:/1/2.txt.png"));
		assertEquals("log", FilenameUtils.getExtension("/var/1.txt.log"));
		assertEquals("", FilenameUtils.getExtension("/var/1"));

		String filename = "C:/commons/io/../lang\\project.xml";
		String normalized = FilenameUtils.separatorsToWindows(FilenameUtils.normalize(filename));
		assertEquals("C:\\commons\\lang\\project.xml", normalized);
	}
}
