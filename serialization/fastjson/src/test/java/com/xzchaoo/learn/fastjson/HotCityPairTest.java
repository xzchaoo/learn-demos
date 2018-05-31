package com.xzchaoo.learn.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author xzchaoo
 * @date 2018/5/29
 */
public class HotCityPairTest {
  @Test
  public void test() throws Exception {
    InputStream is = getClass().getClassLoader().getResourceAsStream("response.json");
    JSONObject r = JSON.parseObject(is, StandardCharsets.UTF_8, JSONObject.class);
    JSONArray ja = r.getJSONObject("data").getJSONArray("hotCityPair");
    int n = ja.size();
    for (int i = 0; i < n; i++) {
      JSONObject o = ja.getJSONObject(i);
      String d = o.getString("departureCityCode");
      String a = o.getString("arrivalCityCode");
      System.out.println(d + "-" + a);
    }
  }
}
