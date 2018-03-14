package com.xzchaoo.learn.thymeleaf;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.ArrayList;
import java.util.List;

public class ThymeleafTest {
  @Test
  public void test() {
    TemplateEngine te = new TemplateEngine();
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.HTML);
    te.addTemplateResolver(templateResolver);
    templateResolver.setSuffix(".html");
    Context context = new Context();
    context.setVariable("page", Page.builder().title("我是标题").build());
    context.setVariable("page", Page.builder().title("我是标题").build());
    context.setVariable("name", "aa");
    context.setVariable("username", "aa");
    context.setVariable("userList", createUserList());
    System.out.println(te.process("1", context));
  }

  private List<User> createUserList() {
    List<User> list = new ArrayList<>();
    for (int i = 0; i < 10; ++i) {
      User user = new User();
      user.setId(i);
      user.setUsername("xzc" + i);
      list.add(user);
    }
    return list;
  }
}
