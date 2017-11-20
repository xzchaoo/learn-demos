package com.xzchaoo.learn.apache.commons.text;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.StrBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringGeneratorTest {

	@Test
	public void test_word() {
		assertEquals("Abc", WordUtils.capitalize("abc"));
		assertEquals("abc", WordUtils.uncapitalize("Abc"));
		assertEquals("XZC", WordUtils.initials("Xu Zhi Chao"));
		//assertEquals("XZC", WordUtils.wrap("1234567890",4));
		//添加换行符
		System.out.println(WordUtils.wrap("1234567890", 4, "\r\n", true));
		System.out.println(WordUtils.wrap("1234567890", 4));
	}

	@Test
	public void test_RandomStringGenerator() {
		RandomStringGenerator rsg = new RandomStringGenerator.Builder()
			.withinRange(0, 0X8F)
			.filteredBy(
				Character::isLowerCase,
				Character::isDigit
			)
			.build();
		System.out.println(rsg.generate(10));
	}

	@Test
	public void test_StringEscapeUtils() {
		System.out.println(StringEscapeUtils.escapeJava("String  = \"a\";"));
	}

	@Test
	public void test_StrBuilder() {
		//用法和jdk的SB类似 但是扩展了一些灵活的方法
		StrBuilder sb = new StrBuilder();
	}
}
