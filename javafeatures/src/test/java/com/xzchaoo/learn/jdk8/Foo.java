package com.xzchaoo.learn.jdk8;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * since 最小需要<strong>JDK8</strong>的<b>支持</b>
 * <p>默认情况下 内容是不会换行的, 如果需要换行通常手动加一个p元素
 * <ul>
 * <li>我是第1项</li>
 * <li>我是第2项</li>
 * <li>我是第3项</li>
 * <li>我是第4项</li>
 * <li>我是第5项</li>
 * <li>我是第6项</li>
 * </ul>
 *
 * @param <T> 泛型参数
 * @author zcxu
 * @date 2018/4/2 0002
 * @since 1.8
 */
public class Foo<T> {
  /**
   * 这是method1方法, 如果执行失败会抛出 {@link IOException} 异常的
   *
   * @param a 参数a的描述
   * @param b 参数B的描述
   * @return 返回值的描述
   * @throws IOException 如果...则抛出异常
   * @see Object
   */
  public List<String> method1(String a, @SuppressWarnings("unused") int b) throws IOException {
    return asList("1", a);
  }

  /**
   * @throws IOException if ...
   * @deprecated 这个方法被废弃了 不推荐使用, use {@link #newMethod(int)} instead
   */
  @Deprecated
  public void deprecatedMethod() throws IOException {

  }

  /**
   * 这是新的方法 <code>这里可以放一些代码?</code>
   *
   * @param i 需要做加法的参数
   * @return {@code i + 1}
   */
  public int newMethod(int i) {
    return i + 1;
  }
}
