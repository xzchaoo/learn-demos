package com.xzchaoo.learn.mapstruct;

import java.util.Date;

/**
 * created by xzchaoo at 2017/11/22
 *
 * @author xzchaoo
 */
public class UserEntity {
	private Long id;
	private String username;
	private String password;
	private String channels;
	private String channels2;
	private Date registerAt;
	private String gender;
	private int userType;
	private String e1;
	private String e2;
	private String e3;
	private String e4;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public Date getRegisterAt() {
		return registerAt;
	}

	public void setRegisterAt(Date registerAt) {
		this.registerAt = registerAt;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getE1() {
		return e1;
	}

	public void setE1(String e1) {
		this.e1 = e1;
	}

	public String getE2() {
		return e2;
	}

	public void setE2(String e2) {
		this.e2 = e2;
	}

	public String getE3() {
		return e3;
	}

	public void setE3(String e3) {
		this.e3 = e3;
	}

	public String getE4() {
		return e4;
	}

	public void setE4(String e4) {
		this.e4 = e4;
	}

	public boolean hasE4() {
		return true;
	}

	public String getChannels2() {
		return channels2;
	}

	public void setChannels2(String channels2) {
		this.channels2 = channels2;
	}
}

