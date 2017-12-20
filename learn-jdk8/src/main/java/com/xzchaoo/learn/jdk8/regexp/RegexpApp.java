package com.xzchaoo.learn.jdk8.regexp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/3/21.
 */
public class RegexpApp {
	@Test
	public void test_matches_and_find() {
		Pattern p = Pattern.compile("xzc");
		String s = "xzc";

		Matcher m = p.matcher(s);
		//matches要求完全匹配 相当于是在开头和结尾加入了 ^ 和 $
		assertTrue(m.matches());

		//相当于 group0 matches 和 find 都可以使用
		assertEquals(s, m.group());

		//matcher其实s是有状态的 matcher 和 find混用会导致出错/
		m = p.matcher(s);
		//find可以只匹配一部分
		assertTrue(m.find());

		s = "xzc2";
		m = p.matcher(s);
		assertFalse(m.matches());

		s = "xzc2";
		m = p.matcher(s);
		assertTrue(m.find());
	}

	@Test
	public void test_find() {
		Pattern p = Pattern.compile("xzc(\\d)");
		String s = "xzc1_xzc2_xzc3";
		Matcher m = p.matcher(s);
		assertFalse(m.matches());

		m = p.matcher(s);

		assertTrue(m.find());
		assertEquals("xzc1", m.group(0));
		assertEquals("1", m.group(1));
		assertEquals(0, m.start());
		assertEquals(4, m.end());
		assertEquals(3, m.start(1));
		assertEquals(4, m.end(1));


		assertTrue(m.find());
		assertEquals("xzc2", m.group(0));
		assertEquals("2", m.group(1));
		assertEquals(5, m.start());
		assertEquals(9, m.end());
		assertEquals(8, m.start(1));
		assertEquals(9, m.end(1));

		assertTrue(m.find());
		assertEquals("xzc3", m.group(0));
		assertEquals("3", m.group(1));
		assertEquals(10, m.start());
		assertEquals(14, m.end());
		assertEquals(13, m.start(1));
		assertEquals(14, m.end(1));

		s = "xzc2";
		p = Pattern.compile("xzc(?<id>\\d)");
		m = p.matcher(s);
		assertTrue(m.find());
		assertEquals("2", m.group("id"));
	}

	@Test
	public void test_replaceAll() {
		//这个是String的方法
		String s = "kw1|kw2,kw33";
		String s2 = s.replaceAll("kw\\d+", "[*]");
		assertEquals("[*]|[*],[*]", s2);
	}

	@Test
	public void test_placeholder() {
		String template = "hello ${name}, now is ${time}";
		Pattern p = Pattern.compile("\\$\\{(\\w+)\\}");
		Matcher m = p.matcher(template);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String key = m.group(1);
			if (key.equals("name")) {
				m.appendReplacement(sb, "xzc");
			} else if (key.equals("time")) {
				m.appendReplacement(sb, "2017-03-21 14:54:00");
			}
		}
		m.appendTail(sb);
		String s = sb.toString();
		assertEquals("hello xzc, now is 2017-03-21 14:54:00", s);
	}

	@Test
	public void test_split_1() {
		String s = "a,,,..c.d/e";
		//某个字符串用 ,./ 分隔
		String[] ss = s.split("[,./]");
		//[a, , , , , c, d, e]
		//需要自己去掉空白
		List<String> list = new ArrayList<String>();
		for (String s0 : ss) {
			if (!s0.isEmpty()) {
				list.add(s0);
			}
		}
		assertEquals(Arrays.asList("a", "c", "d", "e"), list);


		//某个字符串用 ,./ 分隔
		ss = s.split("[,./]", 1);
		//limit用于限制结果的数量 如果设置成1 那么结果绝对不会超过1
		//<=0 表示 无限制
		//System.out.println(Arrays.asList(ss));
		//assertEquals(Arrays.asList("a", "c", "d", "e"), list);
		assertEquals(1, ss.length);
	}
}
