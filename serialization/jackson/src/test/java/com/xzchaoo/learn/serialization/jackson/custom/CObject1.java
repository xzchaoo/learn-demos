package com.xzchaoo.learn.serialization.jackson.custom;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * created by xzchaoo at 2017/10/28
 *
 * @author xzchaoo
 */
public class CObject1 {
	private int id;
	private CGender cGender;
	private CObject2 co2;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CGender getcGender() {
		return cGender;
	}

	public void setcGender(CGender cGender) {
		this.cGender = cGender;
	}

	public CObject2 getCo2() {
		return co2;
	}

	public void setCo2(CObject2 co2) {
		this.co2 = co2;
	}

	@Override
	public String toString() {
		return "CObject1{" +
			"id=" + id +
			", cGender=" + cGender +
			", co2=" + co2 +
			'}';
	}
}
