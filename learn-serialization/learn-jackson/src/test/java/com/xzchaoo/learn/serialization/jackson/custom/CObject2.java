package com.xzchaoo.learn.serialization.jackson.custom;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * created by xzchaoo at 2017/10/28
 *
 * @author xzchaoo
 */
public class CObject2 {
	private int id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "CObject2{" +
			"id=" + id +
			", birthday=" + birthday +
			'}';
	}
}
