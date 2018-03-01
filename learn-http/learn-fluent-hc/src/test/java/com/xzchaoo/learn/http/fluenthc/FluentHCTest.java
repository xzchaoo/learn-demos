package com.xzchaoo.learn.http.fluenthc;

import org.apache.http.client.fluent.Request;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//除非是非常紧急 不然没啥用
public class FluentHCTest {
  @Test
  public void test1() throws IOException {
    System.out.println(Request.Get("http://www.bilibili.com")
      .execute()
      .returnContent()
      .asString(StandardCharsets.UTF_8));
  }
}
