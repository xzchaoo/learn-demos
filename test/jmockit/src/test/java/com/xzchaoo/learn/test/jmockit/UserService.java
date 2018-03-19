package com.xzchaoo.learn.test.jmockit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserService {
  private UserDao userDao;
  private BookDao bookDao;

  //jmockit 可以支持 PostConstruct 注解
  @PostConstruct
  public void init() {
    System.out.println("UserService init!");
  }

  @PreDestroy
  public void destroy() {
    System.out.println("destroy");
  }

  public String foo() {
    UserDao.static1();
    new BarDao().badSave();
    FooEmail fooEmail = new FooEmailImpl();
    fooEmail.send("foo");
    userDao.log();
    userDao.log();
    userDao.log();
    return userDao.find();
  }
}
