package com.xzchaoo.learn.javassist;

import org.junit.Test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * @author xzchaoo
 * @date 2018/6/4
 */
public class FooClientTest {
  @Test
  public void test() throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass serviceClientBaseCC = cp.get("com.xzchaoo.learn.javassist.ServiceClientBase");

    CtClass fooClientCC = cp.makeClass("com.xzchaoo.learn.javassist.FooClient", serviceClientBaseCC);
    fooClientCC.setGenericSignature(new SignatureAttribute.ClassSignature(null, new SignatureAttribute.ClassType(
      serviceClientBaseCC.getName(),
      new SignatureAttribute.TypeArgument[]{
        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(fooClientCC.getName()))
      }
    ), null).encode());
    fooClientCC.addConstructor(new CtConstructor(null, fooClientCC));
    CtConstructor constructor = new CtConstructor(new CtClass[]{
      cp.get("java.lang.String"),
      cp.get("java.lang.String"),
      cp.get("java.lang.String")
    }, fooClientCC);
    constructor.setModifiers(Modifier.PRIVATE);
    constructor.setBody("System.out.println($1 + $2 + $3);");
    fooClientCC.addConstructor(constructor);
    ConstPool constPool = fooClientCC.getClassFile().getConstPool();


    CtMethod getInstanceCM = new CtMethod(fooClientCC, "getInstance", new CtClass[0], fooClientCC);
    getInstanceCM.setBody("{return internalGetInstance($class);}");
    AnnotationsAttribute getInstanceAnnotations = new AnnotationsAttribute(constPool, AnnotationsAttribute
      .visibleTag);
    Annotation annotation1 = new Annotation("javax.xml.bind.annotation.XmlAttribute", constPool);
    annotation1.addMemberValue("name", new StringMemberValue("haha", constPool));
    getInstanceAnnotations.addAnnotation(annotation1);

    getInstanceCM.getMethodInfo().addAttribute(getInstanceAnnotations);

    fooClientCC.addMethod(getInstanceCM);

    AnnotationsAttribute annotationAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
    Annotation annotation = new Annotation("javax.xml.bind.annotation.XmlRootElement", constPool);
    annotation.addMemberValue("name", new StringMemberValue("haha", constPool));
    annotationAttr.addAnnotation(annotation);

    fooClientCC.getClassFile().addAttribute(annotationAttr);


    fooClientCC.writeFile();
  }
}
