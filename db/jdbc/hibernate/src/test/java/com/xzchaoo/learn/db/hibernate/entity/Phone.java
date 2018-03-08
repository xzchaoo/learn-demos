package com.xzchaoo.learn.db.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Entity
@Table(name = "user_phones")
public class Phone {
	@Id
	@GeneratedValue
	private Long id;
	private String phoneNumber;
	@Enumerated(EnumType.ORDINAL)
	private PhoneType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PhoneType getType() {
		return type;
	}

	public void setType(PhoneType type) {
		this.type = type;
	}
}
