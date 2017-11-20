package com.xzchaoo.learn.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zcxu
 */
@Aspect
@Component
public class XxxAspect {

	//定义切点
	//通过给参数命名的方式就可以拿到这个annotation对象, 否则需要自己再去拿annotation实例
	//public修饰符可以省略 默认是 public /*package*/ protected都可以拦截到
	//第1个*表示返回值类型任意
	//*.. 表示任意包及其子包下
	//最后一个* 表示任何类
	//括号里的内容表示参数, .. 表示任意多的参数, 也可以这样 (java.lang.String, ..), *还可以表示1个任意参数
	//within/@within 可以用于限制在哪个包(?其他参数貌似也行?) 这几个参数一直没分清楚
	//this/target spring是基于注解的, 此时this==target, 对于AspectJ就不一样了
	//args/@args 限制参数
	//@annotation 限制方法的注解
	//bean(bean名) spring特殊支持
	//execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)throws-pattern?)
	@Pointcut(value = "execution(* *..*(..)) && @annotation(timed)", argNames = "timed")
	private void p1(Timed timed) {
	}

	//around可以注入 pjp, 其他advice只能注入 JoinPoint
	//表达式可以 逻辑操作符连接 && || !
	@Around(value = "p1(timed)", argNames = "pjp,timed")
	public Object beforeP1(ProceedingJoinPoint pjp, Timed timed) throws Throwable {
		//TODO 不支持重载
		final MethodSignature ms = (MethodSignature) pjp.getSignature();
		final Method method = ms.getMethod();
		//final Timed timed = method.getAnnotation(Timed.class);
		final String action = timed.action();
		final String op = timed.op().isEmpty() ? method.getName() : timed.op();
		//TODO Exception handle
		long begin = System.currentTimeMillis();
		try {
			return pjp.proceed();
		} finally {
			long end = System.currentTimeMillis();
			System.out.println(action + "." + op + ".time=" + (end - begin));
			System.out.println(action + "." + op + ".error=" + 0);
		}
	}
}
