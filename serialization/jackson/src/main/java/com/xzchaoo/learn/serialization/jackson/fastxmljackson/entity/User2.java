package com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class User2 {
	private int id;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	private double money;
	private boolean active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
