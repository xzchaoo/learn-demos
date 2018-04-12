package com.xzchaoo.learn.test.demo20180412.model;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class User {
  private int id;
  private String username;

  private String extraInfo1;
  private String extraInfo2;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getExtraInfo1() {
    return extraInfo1;
  }

  public void setExtraInfo1(String extraInfo1) {
    this.extraInfo1 = extraInfo1;
  }

  public String getExtraInfo2() {
    return extraInfo2;
  }

  public void setExtraInfo2(String extraInfo2) {
    this.extraInfo2 = extraInfo2;
  }
}
