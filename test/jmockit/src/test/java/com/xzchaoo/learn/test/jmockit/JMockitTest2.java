package com.xzchaoo.learn.test.jmockit;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

import static org.assertj.core.api.Assertions.*;

/**
 * 辅助类 Deencapsulation 可以用于打破封装
 *
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class JMockitTest2 {
  //标记要被测试的对象, 需要注意的是 JMockit 会调用 @PostConstruct/@PreDestroy 可能会有意想不到的NPE
  @Tested
  FooServiceImpl service;

  //标记为 @Injectable 的属性会被注入到 @Tested 里(如果可以的话)
  @Injectable
  UserDao userDao;

  @Test
  public void test_callUserDao() {
    new Expectations() {{
      //这两句加起来 相当于 when(userDao.find()).thenReturn("a");
      userDao.find();
      result = "a";
      result = "b";
      result = "c";
    }};

    assertThat(service.callUserDao()).isEqualTo("a");
    assertThat(service.callUserDao()).isEqualTo("b");
    assertThat(service.callUserDao()).isEqualTo("c");
    assertThat(service.callUserDao()).isEqualTo("c");

    new Verifications() {{
      //验证 userDao.find() 方法被调用4次
      userDao.find();
      times = 4;
    }};

  }

  @Test
  public void test_callException() {
    new Expectations() {{
      //这两句加起来 相当于 when(userDao.find()).thenThrow(new RuntimeException);
      userDao.throwAnException();
      result = new RuntimeException("foo exception message");
      //通过下面的方式也可以抛异常
//      result = new Delegate<UserDao>() {
//        public String throwAnException() {
//          throw new IllegalArgumentException();
//        }
//      };
    }};
    Throwable throwable = catchThrowable(() -> service.callException());
    assertThat(throwable).hasMessage("foo exception message");
  }

  @Test
  public void test_callStaticMethod() {
    //只有被mock的方法会受影响, 其余方法还是按原来的代码执行
    //可以将MockUp做成普通的类, 然后在需要用到它的地方 new 一个即可, 这样可以达到mock复用
    new MockUp<FooUtils>() {
      @Mock
      void $clinit(Invocation invocation) {
        //这里放空实现 表示镇压了FooUtils的静态构造函数
        //镇压了静态构造函数之后 所以依赖于静态构造函数的初始化动作就都没了 比如 字段的初始化赋值
        //如果初始值是 静态字面值常量 那么该字段的初始化不是在静态构造函数里初始化的, 因此依旧会有值
      }

      //这里要mock掉hello方法
      //保证方法的签名和原来的方法一致, 由于内部类不允许有静态域, 所以不需要加static, 方法可见性可以不用写
      //这种方式支持mock 所有类型 (static/private/final) 的方法
      //throws 签名可以不匹配
      @Mock
      String hello(Invocation invocation, String name) throws Exception {
        invocation.proceed();
        return "mocked";
      }
    };
    assertThat(service.callStaticMethod()).isEqualTo("mocked");
    assertThat(FooUtils.returnHello()).isEqualTo("hello");
    //由于静态构造函数被镇压了 所以值为null
    assertThat(FooUtils.STATIC_1).isEqualTo("static1");
    //System.out.println(FooUtils.class.getField("STATIC_1").get(null));
    assertThat(FooUtils.STATIC_2).isNull();
    assertThat(FooUtils.generate("foo")).isEqualTo("foo");
    assertThat(FooUtils.STATIC_3).isNull();
  }

  @Test
  public void test_callStaticMethod2() {
    //只有被mock的方法会受影响, 其余方法还是按原来的代码执行
    new MockUp<FooUtils>() {
      @Mock
      void $clinit(Invocation invocation) {
        //这里放空实现 表示镇压了FooUtils的静态构造函数
        //镇压了静态构造函数之后 所以依赖于静态构造函数的初始化动作就都没了 比如 字段的初始化赋值
        //如果初始值是 静态字面值常量 那么该字段的初始化不是在静态构造函数里初始化的, 因此依旧会有值
      }

      //这里要mock掉hello方法
      //保证方法的签名和原来的方法一致, 由于内部类不允许有静态域, 所以不需要加static, 方法可见性可以不用写
      //这种方式支持mock 所有类型 (static/private/final) 的方法
      @Mock
      String hello(String name) {
        return "mocked2";
      }
    };
    assertThat(service.callStaticMethod()).isEqualTo("mocked2");
    assertThat(FooUtils.returnHello()).isEqualTo("hello");
    //由于静态构造函数被镇压了 所以值为null
    assertThat(FooUtils.STATIC_1).isEqualTo("static1");
    //System.out.println(FooUtils.class.getField("STATIC_1").get(null));
    assertThat(FooUtils.STATIC_2).isNull();
    assertThat(FooUtils.generate("foo")).isEqualTo("foo");
    assertThat(FooUtils.STATIC_3).isNull();
  }

  //这个 @Mocked 写在方法参数上 表示作用于仅限于这个方法
  @Test
  public void test_sendEmail(@Mocked FooEmailImpl fooEmail) {
    new Expectations() {{
      fooEmail.send(anyString);
      result = null;
    }};
    service.sendEmail("foo");
    //@被Mocked标记的对象 它的所有方法都返回 "null值", 除非方法被mock了
    assertThat(fooEmail.otherMethod()).isNull();
  }

  //和上面的 fooEmail 相比, 这个对象的可见性扩大了, 是整个测试类
  //@被Mocked标记的对象 它的所有方法都返回 "null值", 除非方法被mock了
  @Mocked
  CallHttpTask callHttpTask;

  @Test
  public void test_callHttp() {
    new Expectations() {{
      //Expectations的构造方法不允许抛异常 因此这里必须要捕获...
      try {
        callHttpTask.execute();
        result = 1;
        //这里mock了一个静态方法
        callHttpTask.returnAbc();
        result = "aabbcc";
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }};
    assertThat(service.callHttp()).isEqualTo(1);
    assertThat(callHttpTask.returnAbc()).isEqualTo("aabbcc");
  }
}
