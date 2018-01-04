package com.xzchaoo.learn.spring.targetsource;

/**
 * @author zcxu
 * @date 2018/1/3
 */
public class FooServiceImpl implements FooService {
	private final String foo;

	public FooServiceImpl(String foo) {
		this.foo = foo;
	}

	@Override
	public String foo() {
		return foo;
	}
}
