package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Thread)
public class StateFixtureTest {
	private int count = -1;

	@Setup()
	public void setup() {

	}

	@TearDown
	public void check() {
		assert count > Math.PI : "Nothing changed?";
	}

}
