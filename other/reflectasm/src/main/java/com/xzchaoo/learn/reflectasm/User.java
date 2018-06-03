package com.xzchaoo.learn.reflectasm;

/**
 * @author xzchaoo
 * @date 2018/6/4
 */
public class User {
  private int id;
  private String username;

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

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      '}';
  }
}
