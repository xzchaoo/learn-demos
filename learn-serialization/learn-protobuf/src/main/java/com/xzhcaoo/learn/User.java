package com.xzhcaoo.learn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

/**
 * @author zcxu
 * @date 2017/12/21
 */
public class User {
	@JsonProperty(index = 1)
	private int id;
	@JsonProperty(index = 2)
	private String username;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonProperty(index = 3)
	private LocalDate birthday;

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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", username='" + username + '\'' +
			", birthday=" + birthday +
			'}';
	}
}

