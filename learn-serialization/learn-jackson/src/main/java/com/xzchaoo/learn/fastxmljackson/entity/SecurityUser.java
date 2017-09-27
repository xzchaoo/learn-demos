package com.xzchaoo.learn.fastxmljackson.entity;

/**
 * 假设这是一个第三方的类 无法对它添加 jackson 的注解
 * Created by Administrator on 2017/6/15.
 */
public class SecurityUser {
	private final int id;

	private final String name;

	private int age;

	public SecurityUser(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "SecurityUser{" +
			"id=" + id +
			", name='" + name + '\'' +
			", age=" + age +
			'}';
	}
}
