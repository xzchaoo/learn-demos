package com.xzchaoo.learn.serialization.jackson.customserialize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

/**
 * created by xzchaoo at 2017/10/29
 *
 * @author xzchaoo
 */
public class Object1 {
	@JsonProperty("id2")
	private int id;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Gender gender;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Gender gender2;

	@JsonSerialize(using = MyLocalDateSerializer.class)
	@JsonDeserialize(using = MyLocalDateDeserializer.class)
	private LocalDate birthday;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Gender getGender2() {
		return gender2;
	}

	public void setGender2(Gender gender2) {
		this.gender2 = gender2;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "Object1{" +
			"id=" + id +
			", gender=" + gender +
			", gender2=" + gender2 +
			", birthday=" + birthday +
			'}';
	}
}
