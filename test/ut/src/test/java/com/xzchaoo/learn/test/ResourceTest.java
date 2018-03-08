package com.xzchaoo.learn.test;

import org.junit.Test;

public class ResourceTest {
	public static class User {
		public int id;
		public String username;
		public String password;
	}

	@Test
	public void test() {

		//System.out.println(new User());
		for (int i = 0; i < 10; ++i) {
			System.out.println(i);
		}
		//URL url = getClass().getClassLoader().getResource("data/1.txt");
		// target/test-classes/data/1.txt
		//System.out.println(url);1
	}
}
