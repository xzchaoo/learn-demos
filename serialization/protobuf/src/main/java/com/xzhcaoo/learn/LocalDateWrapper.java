package com.xzhcaoo.learn;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zcxu
 * @date 2017/12/21
 */
public class LocalDateWrapper {
	@JsonProperty(index=1)
	private int year;
	@JsonProperty(index=2)
	private int month;
	@JsonProperty(index=3)
	private int day;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
