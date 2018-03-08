package com.xzchaoo.learn.bsh;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * created by xzchaoo at 2017/12/9
 *
 * @author xzchaoo
 */
public class BshTest {
	@Test
	public void test() throws EvalError, IOException {
		Interpreter i = new Interpreter();  // Construct an interpreter
		i.set("foo", 5);                    // Set variables
		i.set("date", new Date());

		Date date = (Date) i.get("date");    // retrieve a variable

// Eval a statement and get the result
		i.eval("bar = foo*10");
		System.out.println(i.get("bar"));

// Source an external script file
		//i.source("somefile.bsh");
	}
}
