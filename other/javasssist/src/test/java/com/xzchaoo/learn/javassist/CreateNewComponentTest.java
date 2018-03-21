package com.xzchaoo.learn.javassist;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

/**
 * @author zcxu
 * @date 2018/3/21 0021
 */
public class CreateNewComponentTest {
  private static final String PACKAGE = "com.xzchaoo.learn.javassist";
  ClassPool cp;

  @Before
  public void before() {
    cp = ClassPool.getDefault();
  }


  @Test
  public void test_createNewInterface() throws CannotCompileException, NotFoundException, IOException {
    CtClass ifooInterface = cp.makeInterface(PACKAGE + ".IFoo");
    //扩展2个接口
    ifooInterface.setSuperclass(cp.get(Serializable.class.getName()));
    ifooInterface.setSuperclass(cp.get(Cloneable.class.getName()));

    String classGenericSignature = new SignatureAttribute.ClassSignature(new SignatureAttribute.TypeParameter[]{
      new SignatureAttribute.TypeParameter("N", new SignatureAttribute.ClassType(Number.class.getName()), null)
    }).encode();
    ifooInterface.setGenericSignature(classGenericSignature);

    //添加一个方法
    CtMethod fooMethod = CtNewMethod.make("String foo();", ifooInterface);

    //这里添加一个泛型方法 它的参数来自类型上的参数
    //注意添加泛型方法时的签名, 一开始需要用泛型擦除后的签名(甚至类型不对也新 主要是要能先初始化出Method对象)
    //然后再修改方法的泛型签名 修改为具体类型

    //看这里写的是String
    CtMethod getNumberMethod = CtNewMethod.make("String getNumber();", ifooInterface);
    //这里通过 TypeVariable=N 将returnType 修改成N 因此其实上述的String写成什么都无所谓 只要是一个存在的对象就行了
    getNumberMethod.setGenericSignature(
      new SignatureAttribute.MethodSignature(null, null, new SignatureAttribute.TypeVariable("N"), null)
        .encode()
    );

    //这里添加了第二个泛型方法
    CtMethod plusNumberMethod = CtNewMethod.make("String plusNumber(Number value);", ifooInterface);
    plusNumberMethod.setGenericSignature(
      //这里提供方法本身的泛型信息
      new SignatureAttribute.MethodSignature(new SignatureAttribute.TypeParameter[]{
        new SignatureAttribute.TypeParameter("A", new SignatureAttribute.ClassType(Number.class.getName()), null)

      },
        //这里提供参数的泛型信息
        new SignatureAttribute.Type[]{
          new SignatureAttribute.TypeVariable("A"),
        },
        //这里提供返回值的泛型信息
        new SignatureAttribute.TypeVariable("N"), null)
        .encode()
    );

    ifooInterface.addMethod(fooMethod);
    ifooInterface.addMethod(getNumberMethod);
    ifooInterface.addMethod(plusNumberMethod);

    ifooInterface.writeFile();
  }

  @Test
  public void test_createGenericClass() throws Exception {
    CtClass g1Class = cp.makeClass(PACKAGE + ".G1");

    System.out.println(new SignatureAttribute.TypeParameter("A").toString());

    g1Class.setGenericSignature(new SignatureAttribute.ClassSignature(new SignatureAttribute.TypeParameter[]{
      new SignatureAttribute.TypeParameter("A", new SignatureAttribute.ClassType(Number.class.getName()), null)
    }).encode());

    CtField f = new CtField(CtClass.intType, "g", g1Class);
    //f.setGenericSignature(new SignatureAttribute.TypeVariable("A").encode());
    f.setGenericSignature("TA;");
    g1Class.addField(f);

    g1Class.writeFile();
  }

  @Test
  public void test_createNewClass() throws CannotCompileException, NotFoundException, IOException, BadBytecode {
    //public abstract CNCT1{
    //
    // }
    //只能用于创建类哦 接口/注解 用另外一个
    CtClass fooClass = cp.makeClass(PACKAGE + ".Foo");
    fooClass.setModifiers(Modifier.ABSTRACT);

    CtField loggerField = CtField.make("private static final Object LOGGER = new Object();", fooClass);
    fooClass.addField(loggerField);

    //添加一个 字段/get/set 方法
    CtField int1Field = new CtField(CtClass.intType, "int1", fooClass);
    int1Field.setModifiers(Modifier.PRIVATE);
    fooClass.addField(int1Field, CtField.Initializer.constant(123));

    CtMethod getInt1Method = new CtMethod(CtClass.intType, "getInt1", null, fooClass);
    getInt1Method.setBody("return this.int1;");
    fooClass.addMethod(getInt1Method);

    //可以使用便捷方法
    fooClass.addMethod(CtNewMethod.setter("setInt1", int1Field));


    //添加另外一个字段
    CtField float1Field = new CtField(CtClass.floatType, "float1", fooClass);
    float1Field.setModifiers(Modifier.PRIVATE);
    //TODO 貌似这里写成 0.5 会产生 0.5D, 这会导致一个编译错误 但为何javassist没有报错?
    fooClass.addField(float1Field, "0.5f");

    //short也有类似的问题 这里似乎不会做检查的... 建议通过 constant
    fooClass.addField(new CtField(CtClass.shortType, "short1", fooClass), CtField.Initializer.constant(1));

    fooClass.writeFile();
  }
}
