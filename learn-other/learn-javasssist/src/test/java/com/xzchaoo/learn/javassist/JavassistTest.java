package com.xzchaoo.learn.javassist;

import org.junit.Test;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * created by xzchaoo at 2017/12/8
 *
 * @author xzchaoo
 */
public class JavassistTest {
	@Test
	public void test() throws CannotCompileException, NotFoundException, IOException {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("org.xzchaoo.test.MyTest");
		cc.setSuperclass(pool.get("java.lang.Object"));
		cc.addInterface(pool.get("com.xzchaoo.learn.javassist.MyInterface"));

		CtField a = new CtField(CtClass.intType, "a", cc);
		a.setModifiers(AccessFlag.PUBLIC | AccessFlag.STATIC | AccessFlag.FINAL);
		cc.addField(a, CtField.Initializer.constant(3));

		CtMethod say = new CtMethod(pool.get("java.lang.String"), "say", null, cc);
		say.setBody("{int a=1;return \"safd\";}");
		say.insertBefore("Integer i = ($w)5;");
		say.insertBefore("int j = 5;");
		//say.insertBefore("Object o = $sig;");
		//say.insertBefore("Object o = $type;");
		//say.insertBefore("Object o2 = $class;");

		cc.addMethod(say);

		CtMethod test1 = new CtMethod(CtClass.voidType, "test1", null, cc);
		test1.instrument(new ExprEditor(){
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				m.replace("toString()");
			}
		});
		test1.setBody("{}");
		cc.addMethod(test1);
		cc.writeFile();
	}
}
