package com.xzchaoo.learn.config.myconfig.biz.parsers;

import java.awt.Point;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class PointParser implements Parser<Point> {
  @Override
  public Point parse(Class<Point> clazz, String str) {
    // 1,2
    return new Point(1, 2);
  }
}
