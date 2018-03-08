package com.xzchaoo.learn.apache.commons.text;

import org.apache.commons.text.StrBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zcxu
 * @date 2018/2/27 0027
 */
public class StrBuilderTest {
  @Test
  public void test() {
    //提供和JDK的SB类似的功能 但是会稍微方便/灵活一些 比如支持append null
    //JDK append null 会产生 null 字符串
    //而StrBuilder默认不会产生字符串
    StrBuilder sb = new StrBuilder();
    sb.append((String) null);
    String str = sb.toString();
    assertThat(str).isEmpty();
  }

  @Test
  public void test_jdk() {
    //发现一个IDEA的提示bug: IDEA 会提示或下面3行代码可以转成 String sb = null; 但其实这是有问题的
    //这里将这个提示镇压掉
    @SuppressWarnings("StringBufferReplaceableByString")
    StringBuilder sb = new StringBuilder();
    sb.append((String) null);
    String str = sb.toString();
    assertThat(str).isEqualTo("null");
  }
}
