package com.xzchaoo.learn.config.myconfig.biz.parsers;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Parsers {
  public static final Parser<String> STRING = (clazz, x) -> x;
  public static final Parser<Integer> INTEGER = (clazz, x) -> Integer.parseInt(x);
  public static final Parser<Float> FLOAT = (clazz, x) -> Float.parseFloat(x);
  public static final Parser<Double> DOUBLE = (clazz, x) -> Double.parseDouble(x);
  // TODO 需要注意
  public static final Parser<Boolean> BOOLEAN = (clazz, x) -> Boolean.parseBoolean(x);
  public static final Parser<Long> LONG = (clazz, x) -> Long.parseLong(x);
}
