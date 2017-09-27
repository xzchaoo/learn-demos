package com.xzchaoo.learn.apache.commons.text;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.StrBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

public class RandomStringGeneratorTest {
	@Test
	public void test_RandomStringGenerator() {
		RandomStringGenerator rsg = new RandomStringGenerator.Builder()
			.withinRange(0, 127)
			.filteredBy(
				CharacterPredicates.DIGITS,
				CharacterPredicates.LETTERS
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
		StrBuilder sb = new StrBuilder();
		//用法和jdk的SB类似
	}
}
