package com.xzchaoo.learn.ehcache2;

import java.io.Serializable;

/**
 * Created by zcxu on 2016/11/11.
 */
public class User implements Serializable {
	private int id;
	private String username;
	private int gender;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", username='" + username + '\'' +
			", gender=" + gender +
			", password='" + password + '\'' +
			'}';
	}
}
