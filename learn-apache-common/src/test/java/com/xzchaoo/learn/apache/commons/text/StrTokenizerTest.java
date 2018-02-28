package com.xzchaoo.learn.apache.commons.text;

import org.apache.commons.text.StrTokenizer;
import org.junit.Test;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class StrTokenizerTest {
  @Test
  public void test() {
    StrTokenizer st = new StrTokenizer("var a=1;");
    System.out.println(st.nextToken());
  }
}
