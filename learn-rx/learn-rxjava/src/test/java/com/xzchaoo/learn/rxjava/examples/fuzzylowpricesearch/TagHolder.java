package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.util.HashMap;
import java.util.Map;

/**
 * name和value不能包含 [],= 等字符 否则会造成干扰
 *
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class TagHolder {
  private Map<String, String> tags;
  private String stringCache;

  public TagHolder() {
  }

  public static TagHolder create() {
    return new TagHolder();
  }

  public static TagHolder create(String name, String value) {
    return new TagHolder().add(name, value);
  }

  public TagHolder add(String name, String value) {
    if (tags == null) {
      tags = new HashMap<>();
    }
    tags.put(name, value);
    //设置为null
    stringCache = null;
    return this;
  }

  @Override
  public String toString() {
    if (tags == null || tags.size() == 0) {
      return "";
    }
    if (stringCache == null) {
      StringBuilder sb = new StringBuilder("[[");
      for (Map.Entry<String, String> e : tags.entrySet()) {
        sb.append(e.getKey()).append('=').append(e.getValue()).append(',');
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]]");
      stringCache = sb.toString();
    }
    return stringCache;
  }
}
