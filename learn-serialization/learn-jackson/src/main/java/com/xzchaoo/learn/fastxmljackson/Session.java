package com.xzchaoo.learn.fastxmljackson;

/**
 * Created by Administrator on 2017/4/13.
 */
public class Session {
	private int a;

	public Session(int a) {
		this.a = a;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "Session{" +
			"a=" + a +
			'}';
	}
}
