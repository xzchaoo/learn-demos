package com.xzchaoo.learn.mapstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/22
 *
 * @author xzchaoo
 */
public class User {
	private int id;
	private String username;
	private List<String> channels;
	private List<String> channels2;
	private LocalDateTime registerAt;
	private Gender gender;
	private UserType type;
	private Calendar e1;
	private Date e2;
	private LocalDate e3;
	private LocalDateTime e4;
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

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}

	public LocalDateTime getRegisterAt() {
		return registerAt;
	}

	public void setRegisterAt(LocalDateTime registerAt) {
		this.registerAt = registerAt;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public Calendar getE1() {
		return e1;
	}

	public void setE1(Calendar e1) {
		this.e1 = e1;
	}

	public Date getE2() {
		return e2;
	}

	public void setE2(Date e2) {
		this.e2 = e2;
	}

	public LocalDate getE3() {
		return e3;
	}

	public void setE3(LocalDate e3) {
		this.e3 = e3;
	}

	public LocalDateTime getE4() {
		return e4;
	}

	public void setE4(LocalDateTime e4) {
		this.e4 = e4;
	}

	public List<String> getChannels2() {
		return channels2;
	}

	public void setChannels2(List<String> channels2) {
		this.channels2 = channels2;
	}
}
