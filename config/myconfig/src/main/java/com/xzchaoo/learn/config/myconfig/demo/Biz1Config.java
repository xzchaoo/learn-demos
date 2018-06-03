package com.xzchaoo.learn.config.myconfig.demo;

import java.time.LocalDate;

/**
 * 按照业务名称进行分类
 * 一种用法, 为每个属性建立一个方法, 通过方法来获取配置 而不是将 key 散落到程序的各个角落
 * 避免每次都输入默认值
 *
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
