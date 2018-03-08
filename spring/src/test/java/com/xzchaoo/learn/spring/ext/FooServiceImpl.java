package com.xzchaoo.learn.spring.ext;

/**
 * @author xzchaoo
 * @date 2017/12/29
 */
public class FooServiceImpl implements FooService {
	private String info;

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
