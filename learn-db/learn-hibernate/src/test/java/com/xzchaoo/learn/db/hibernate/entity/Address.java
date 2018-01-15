package com.xzchaoo.learn.db.hibernate.entity;

import javax.persistence.Embeddable;

/**
 * 复合主键的类必须实现 hashCode equals方法
 * @author zcxu
 * @date 2018/1/11
 */
@Embeddable
public class Address {
	//@Column(name = "address_province")
	private String province;

	//@Column(name = "address_city")
	private String city;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}

