/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 值得一提的是如果我们尽量地面向接口编程的话 那么mock的难度会小很多, 因为都是在mock接口 不用镇压接口实现类里用到的其他类
 */
@RunWith(PowerMockRunner.class)
@MockPolicy({MyPolicy.class, Slf4jMockPolicy.class})
public abstract class TestBase {
}
