package com.xzchaoo.learn.config.myconfig.biz;

import org.apache.commons.lang3.StringUtils;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Biz2Config extends AbstractBizConfig {

  private List<String> engines = Collections.emptyList();

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


  /**
   * 获取所有配置的引擎
   *
   * @return
   */
  public List<String> getEngines() {
    return engines;
  }

  /**
   * 获取所有配置的坐标
   *
   * @return
   */
  public List<Point> getPoints() {
    return points;
  }
}
