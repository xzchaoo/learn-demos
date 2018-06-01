package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Tested;
import mockit.integration.junit4.JMockit;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(JMockit.class)
public class UserServiceTest {
  @Tested
  UserService service;


}
