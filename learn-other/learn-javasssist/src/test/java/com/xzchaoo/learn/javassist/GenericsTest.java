package com.xzchaoo.learn.javassist;

import org.junit.Test;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.SignatureAttribute;

import static javassist.bytecode.SignatureAttribute.ClassSignature;
import static javassist.bytecode.SignatureAttribute.TypeParameter;

/**
 * @author zcxu
 * @date 2018/3/8 0008
 */
public class GenericsTest {
  @Test
  public void test() throws IOException, CannotCompileException, NotFoundException {
    ClassPool cp = ClassPool.getDefault();

    CtClass interface1 = cp.makeInterface("com.xzchaoo.learn.javassist.Interface1");
    ClassSignature cs1 = new ClassSignature(new TypeParameter[]{new TypeParameter("T")});
    interface1.setGenericSignature(cs1.encode());

    CtMethod method = CtMethod.make("int test(int[] args);", interface1);
    method.setModifiers(method.getModifiers() | Modifier.VARARGS | Modifier.ABSTRACT);
    interface1.addMethod(method);

    ClassSignature cs2 = new ClassSignature(new TypeParameter[]{new TypeParameter("T")});
    ClassFile classFile = interface1.getClassFile();

    CtClass interface2 = cp.makeInterface("com.xzchaoo.learn.javassist.Interface2", interface1);
    System.out.println(cs2.encode());
    //new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Number"));
    String encode2 = new ClassSignature(
      new TypeParameter[]{
        new TypeParameter("A", new SignatureAttribute.ClassType("java.lang.Number"), new SignatureAttribute.ClassType[]{new SignatureAttribute.ClassType(interface1.getName())})
      },
      null,
      //接口虽然用的是继承 不过还是要放在接口的参数里 而不是父类
      new SignatureAttribute.ClassType[]{
        new SignatureAttribute.ClassType(interface1.getName(), new SignatureAttribute.TypeArgument[]{
          new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Number"))
        })
      }
    ).encode();
    interface2.setGenericSignature(encode2);
    //ClassFile classFile2 = interface2.getClassFile();
    //classFile2.addAttribute(new SignatureAttribute(classFile.getConstPool(), encode2));

    interface1.writeFile();
    interface2.writeFile();

    CtClass serviceClientBaseCC = cp.get("com.xzchaoo.learn.javassist.ServiceClientBase");
    CtClass fooClientCC = cp.makeClass("com.xzchaoo.learn.javassist.FooClient", serviceClientBaseCC);
    fooClientCC.setGenericSignature(new ClassSignature(null, new SignatureAttribute.ClassType(
      serviceClientBaseCC.getName(),
      new SignatureAttribute.TypeArgument[]{
        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Number"))
      }
    ), null).encode());
    fooClientCC.addConstructor(new CtConstructor(null, fooClientCC));
    CtConstructor constructor2 = new CtConstructor(new CtClass[]{
      CtClass.intType,
      CtClass.floatType,
    }, fooClientCC);
    constructor2.setBody("System.out.println(\"OK\");");
    fooClientCC.addConstructor(constructor2);
    fooClientCC.writeFile();
  }
}
