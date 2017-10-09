package com.xzchaoo.learn.spring.expression;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.junit.Assert.*;

public class ExpressionTest {
	@Test
	public void test1() {
		//可以独立使用
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("'Hello World'");
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("a", 123);
		String message = (String) exp.getValue(context);
		assertEquals("Hello World", message);
		assertEquals(123, parser.parseExpression("#a").getValue(context, Integer.class).intValue());

		User1 user1 = new User1();
		user1.setId(1);
		user1.setName("xzc");
		context = new StandardEvaluationContext(user1);
		System.out.println(parser.parseExpression("#root.getName()").getValue(context));

		context = new StandardEvaluationContext();
		ApplicationContext ac = new AnnotationConfigApplicationContext(Configuration1.class);
		context.setBeanResolver(new BeanFactoryResolver(ac));
		assertEquals("bar", parser.parseExpression("@userService.foo()").getValue(context, String.class));
		System.out.println(parser.parseExpression("@userService.foo().length()").getValue(context));
	}
}
