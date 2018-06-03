package com.xzchaoo.learn.config.myconfig.demo;


import com.xzchaoo.learn.config.myconfig.core.parser.Parser;

import org.apache.commons.lang3.StringUtils;

import java.awt.Point;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class PointParser implements Parser<Point> {
  @Override
  public Point parse(String str) {
    String[] ss = StringUtils.split(str, ',');
    return new Point(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
  }
}
