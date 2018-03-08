package com.xzchaoo.learn.archaius2;

/**
 * Created by Administrator on 2017/6/9.
 */
public class CustomType {
	private int id;
	private String name;

	/**
	 * 任何类型 只要提供一个 valueOf 就可以从字符串转过来
	 *
	 * @param str
	 * @return
	 */
	public static CustomType valueOf(String str) {
		String[] ss = str.split(" ");
		if (ss.length != 2) {
			throw new IllegalArgumentException();
		}
		CustomType ct = new CustomType();
		ct.setId(Integer.parseInt(ss[0]));
		ct.setName(ss[1]);
		return ct;
	}

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

	@Override
	public String toString() {
		return "CustomType{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
