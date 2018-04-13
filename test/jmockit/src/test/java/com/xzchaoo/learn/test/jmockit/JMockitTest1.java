package com.xzchaoo.learn.test.jmockit;

import org.junit.BeforeClass;
import org.junit.Test;

import lombok.val;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

import static org.assertj.core.api.Assertions.assertThat;

//jmockit自动支持junit4 不需要再 RunWith
public class JMockitTest1 {

  //自动创建对象 并且注入属性 可以有多个
  //@Tested(fullyInitialized = true)
  @Tested
  UserService userService;

  @Injectable
  UserDao userDao;

  @Mocked
  BookDao bookDao;

  @Mocked
  FooEmailImpl fooEmail;

  @BeforeClass
  public static void beforeClass(){
    new MockUp<UserDao>() {
      //可以用于mock静态方法 但内部类不能有静态方法哦 这里需要写成实例方法
      //用 $clinit 指代静态初始化块
      //用 $init 指代构造方法
      @Mock
      void $init(Invocation invocation) {
        System.out.println("UserDao init2");
      }

      @Mock
      void $clinit(Invocation invocation) {
        System.out.println("UserDao Class init2");
      }

      @Mock
      void static1(Invocation invocation) {
        //通过 invocation 可以拿到本次调用的上下文信息
        System.out.println("bad static111");
        //Deencapsulation.setField("", "loginSucceeded", true);
      }
    };
  }

  @Test
  public void test() {
    //需要在类被使用之前进行mockup 废话
    //会影响作用域的
    //UserDao.static1();

    //如何mock静态方法
    new MockUp<BarDao>() {
      //用 $clinit 指代静态初始化块
      //用 $init 指代构造方法

      @Mock
      void $init(Invocation invocation) {
        System.out.println("BarDao init2");
      }

      @Mock
      void $clinit(Invocation invocation) {
        System.out.println("BarDao Class init2");
      }

      @Mock
      public void badSave() {
        System.out.println("bye bye2");
      }
    };


    new Expectations() {{
      //这里进行mock
      userDao.log();
      minTimes = 3;
      maxTimes = 4;
      userDao.find();
      result = "abc";
      bookDao.showBook(anyInt);
      result = "123";
      fooEmail.send(anyString);
      result = new Delegate<Void>() {
        void send(String message) {
          System.out.println("!send " + message);
        }
      };
    }};
    System.out.println(bookDao.showBook(123));
    val result = userService.foo();
    assertThat(result).isEqualTo("abc");
    System.out.println("here");

    //这里进行验证
    new Verifications() {{
      userDao.log();
      times = 3;
      userDao.find();
      times = 1;
    }};
  }
}
