package com.xzchaoo.learn.serialization.jackson.fastxmljackson;

import com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity.User2;

import java.time.LocalDate;

public abstract class TestBase {
	protected User2 user2 = new User2();

	public TestBase() {
		user2.setId(1);
		user2.setName("n2");
		user2.setBirthday(LocalDate.of(2017, 1, 1));
		user2.setMoney(4);
		user2.setActive(true);
	}
}
