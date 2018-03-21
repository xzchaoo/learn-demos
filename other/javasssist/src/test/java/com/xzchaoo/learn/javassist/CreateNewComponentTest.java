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
 * 生成一些泛型的类或接口是比较麻烦的 因此例子都以这些为主, 只要你懂了这些, 那么非泛型的普通类就简单了
 *
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


  /**
   * 生成一个泛型接口
   * 1. 2个泛型参数
   * 2. 2个泛型方法
   * 3. 扩展2个接口
   *
   * @throws Exception
   */
  @Test
  public void test_createGenericInterface() throws Exception {
    CtClass ifooInterface = cp.makeInterface(PACKAGE + ".IFoo");

    //扩展2个接口, 虽然方法名叫做 set... 对于接口其实是add的意思
    ifooInterface.setSuperclass(cp.get(Serializable.class.getName()));
    ifooInterface.setSuperclass(cp.get(Cloneable.class.getName()));

    //修改接口的签名,  添加泛型参数 <N extends Number, M extends Serializable>
    String classGenericSignature = new SignatureAttribute.ClassSignature(new SignatureAttribute.TypeParameter[]{
      new SignatureAttribute.TypeParameter("N", new SignatureAttribute.ClassType(Number.class.getName()), null),
      new SignatureAttribute.TypeParameter("M", new SignatureAttribute.ClassType(Serializable.class.getName()), null)
    }).encode();
    ifooInterface.setGenericSignature(classGenericSignature);

    //添加一个普通方法, 这个和类的操作是一样的
    ifooInterface.addMethod(CtNewMethod.make("String foo();", ifooInterface));

    //添加 N getNumber(M m); 方法
    //这里添加一个泛型方法 它的参数来自类型上的参数
    //注意添加泛型方法时的签名, 一开始需要用泛型擦除后的签名(返回类型可以先用其它类型 主要是要能先初始化出Method对象)
    //然后再修改方法的泛型签名 修改为具体类型
    CtMethod getNumberMethod = CtNewMethod.make("String getNumber(Object m);", ifooInterface);
    //这里通过 TypeVariable=N 将returnType 修改成N 因此其实上述的String写成什么都无所谓 只要是一个存在的对象就行了
    getNumberMethod.setGenericSignature(new SignatureAttribute.MethodSignature(
      null,
      new SignatureAttribute.Type[]{
        new SignatureAttribute.TypeVariable("M")
      },
      new SignatureAttribute.TypeVariable("N"),
      null
    ).encode());
    ifooInterface.addMethod(getNumberMethod);

    //这里添加了第二个泛型方法 <A extends Number, B extends M> N plusNumber(A a, B b);
    CtMethod plusNumberMethod = CtNewMethod.make("String plusNumber(Object a, Object b);", ifooInterface);
    plusNumberMethod.setGenericSignature(
      //这里提供方法本身的泛型信息
      new SignatureAttribute.MethodSignature(new SignatureAttribute.TypeParameter[]{
        new SignatureAttribute.TypeParameter("A", new SignatureAttribute.ClassType(Number.class.getName()), null),
        new SignatureAttribute.TypeParameter("B", new SignatureAttribute.TypeVariable("M"), null),

      },
        //这里提供参数的泛型信息
        new SignatureAttribute.Type[]{
          new SignatureAttribute.TypeVariable("A"),
          new SignatureAttribute.TypeVariable("B"),
        },
        //这里提供返回值的泛型信息
        new SignatureAttribute.TypeVariable("N"), null)
        .encode()
    );
    ifooInterface.addMethod(plusNumberMethod);

    ifooInterface.writeFile();
  }

  /**
   * 生成一个泛型类
   * 1. 继承 Object
   *
   * @throws Exception
   */
  @Test
  public void test_createGenericClass() throws Exception {
    CtClass g1Class = cp.makeClass(PACKAGE + ".G1");
    g1Class.addInterface(cp.get("java.io.Serializable"));
    g1Class.addInterface(cp.get("java.lang.Cloneable"));
    g1Class.setSuperclass(cp.get(AbstractCar.class.getName()));

    //有2个泛型参数 一个是A 透传给父类 指定父类的泛型参数B为String 自己额外新增一个泛型参数C
    g1Class.setGenericSignature(new SignatureAttribute.ClassSignature(
      new SignatureAttribute.TypeParameter[]{
        new SignatureAttribute.TypeParameter("A", new SignatureAttribute.ClassType(Number.class.getName()), null),
        new SignatureAttribute.TypeParameter("C")
      },
      new SignatureAttribute.ClassType(AbstractCar.class.getName(), new SignatureAttribute.TypeArgument[]{
        new SignatureAttribute.TypeArgument(new SignatureAttribute.TypeVariable("A")),
        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("String"))
      }),
      new SignatureAttribute.ClassType[]{
        new SignatureAttribute.ClassType("java.io.Serializable"),
        new SignatureAttribute.ClassType("java.lang.Cloneable")
      })
      .encode()
    );

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

    CtMethod varargsMethod = CtNewMethod.make("public int getSum(int[] args){int sum=0;for(int i=0;i<args.length;++i){sum+=args[i];}return sum;}", fooClass);
    varargsMethod.setModifiers(varargsMethod.getModifiers() | Modifier.VARARGS);
    fooClass.addMethod(varargsMethod);

    fooClass.writeFile();
  }
}
