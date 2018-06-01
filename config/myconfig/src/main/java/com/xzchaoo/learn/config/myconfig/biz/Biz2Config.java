package com.xzchaoo.learn.config.myconfig.biz;

import org.apache.commons.lang3.StringUtils;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Biz2Config extends AbstractBizConfig {
  @Getter
  private List<String> engines = Collections.emptyList();
  @Getter
  private List<Point> points = Collections.emptyList();

  @Override
  public void refresh() {
    engines = parseList("biz2.engines", '|');
    points = parseList("biz.points", '|', this::parsePoint);
  }

  private Point parsePoint(String s) {
    // x,y
    String[] ss = StringUtils.split(s, ',');
    return new Point(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
  }
}
