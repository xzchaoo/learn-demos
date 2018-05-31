package com.xzchaoo.learn.config.myconfig.biz;

import java.time.LocalDate;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Biz1Config extends AbstractBizConfig {
  public String getUsername() {
    return config.getString("biz1.username");
  }

  public String getPassword() {
    return config.getString("biz1.password");
  }

  public String getUrl() {
    return config.getString("biz1.url");
  }

  /**
   * 获取生日
   *
   * @return
   */
  public LocalDate getBirthday() {
    return LocalDate.parse(config.getString("biz1.birthday"));
  }
}
