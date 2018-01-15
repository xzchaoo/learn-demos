package com.xzchaoo.learn.db.hibernate.entity;

/**
 * @author zcxu
 * @date 2018/1/11
 */
public enum Gender {
	MALE('F'), FEMALE('F');
	private final char code;

	Gender(char code) {
		this.code = code;
	}

	public static Gender fromCode(char code) {
		if (code == 'm' || code == 'M') {
			return MALE;
		}
		if (code == 'f' || code == 'F') {
			return FEMALE;
		}
		throw new IllegalArgumentException();
	}

	public char getCode() {
		return code;
	}
}
