package com.xzchaoo.learn.config.myconfig.core;

import com.xzchaoo.learn.config.myconfig.core.utils.ConfigParseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public class ConfigParseUtilsTest {
  @Test
  public void test_parseList() {
    // 兼容空值
    assertThat(ConfigParseUtils.parseList(null, '|', Integer::parseInt)).isNotNull().isEmpty();
    assertThat(ConfigParseUtils.parseList("", '|', Integer::parseInt)).isNotNull().isEmpty();
    assertThat(ConfigParseUtils.parseList("|", '|', Integer::parseInt)).isNotNull().isEmpty();
    assertThat(ConfigParseUtils.parseList("||||||||", '|', Integer::parseInt)).isNotNull()
      .isEmpty();

    List<Integer> result1 = ConfigParseUtils.parseList("1|2|3|4", '|', Integer::parseInt);
    assertThat(result1).containsSequence(1, 2, 3, 4);

    List<String> result2 = ConfigParseUtils.parseList("a,b,c,d", ',', Function.identity());
    assertThat(result2).containsSequence("a", "b", "c", "d");

    List<Point> result3 = ConfigParseUtils.parseList("(1,2)|(3,4)", '|', ConfigParseUtilsTest::parsePoint);
    Assertions.assertThat(result3).containsSequence(new Point(1, 2), new Point(3, 4));

    List<List<Integer>> result4 = ConfigParseUtils.parseList("1,2|3,4", '|', sub -> {
      // noinspection CodeBlock2Expr
      return ConfigParseUtils.parseList(sub, ',', Integer::parseInt);
    });
    assertThat(result4).containsSequence(Lists.newArrayList(1, 2), Lists.newArrayList(3, 4));

    List<EngineAndValue> result5 = ConfigParseUtils.parseList("Ctrip,4|SharedPlatform,4|Amadeus,288|Abacus,32|Consolidator,32|Pricing," +
      "32|CtripIntelligence,64", '|', sub -> {
      String[] ss = StringUtils.split(sub, ',');
      return new EngineAndValue(ss[0], Integer.parseInt(ss[1]));
    });
    Assertions.assertThat(result5).contains(new EngineAndValue("Ctrip", 4), new EngineAndValue("Amadeus", 288));
  }

  @Test
  public void test_parseSet() {
    Set<Integer> result1 = ConfigParseUtils.parseSet("1|2|3|4|4||||", '|', Integer::parseInt);
    assertThat(result1).containsOnly(1, 2, 3, 4);
  }

  @Test
  public void test_parseMap() {
    Map<String, Integer> map1 = ConfigParseUtils.parseMap("a=1|b=2|c=3", '|', '=', Function.identity(),
      Integer::parseInt);
    assertThat(map1).containsEntry("a", 1)
      .containsEntry("b", 2)
      .containsEntry("c", 3);


    Map<String, Integer> map2 = ConfigParseUtils.parseMap("a=1|b=2|*", '|', sub -> {
      if ("*".equals(sub)) {
        return Pair.of("*", 0);
      }
      String[] ss = StringUtils.split(sub, '=');
      return Pair.of(ss[0], Integer.parseInt(ss[1]));
    });
    assertThat(map2).containsEntry("a", 1)
      .containsEntry("b", 2)
      .containsEntry("*", 0);


    Map<String, List<Integer>> map3 = ConfigParseUtils.parseMap("a=1,2|b=3,4|c=5|d=@", '|', '=', Function.identity(),
      valueStr -> {
        if ("@".equals(valueStr)) {
          return new ArrayList<>();
        }
        return ConfigParseUtils.parseList(valueStr, ',', Integer::parseInt);
      });
    assertThat(map3).containsEntry("a", Lists.newArrayList(1, 2))
      .containsEntry("b", Lists.newArrayList(3, 4))
      .containsEntry("c", Lists.newArrayList(5))
      .containsEntry("d", Lists.newArrayList());


//
//    Map<String, Set<String>> map4 = ConfigParseUtils.parseMap("Amadeus:YDSB,LTEB,HMSP,,,,,,,,,,,,;Abacus:YDSB;" +
//      "Ctrip:HMLS", ';', ':', ',', Function.identity(), value -> {
//
//       // return ConfigParseUtils.parseSet(value,',',Function.identity());
//
//    });
//
//    assertThat(map4).containsEntry("Amadeus", Lists.newArrayList("YDSB", "LTEB", "HMSP"))
//      .containsEntry("Abacus", Lists.newArrayList("YDSB"))
//      .containsEntry("Ctrip", Lists.newArrayList("HMLS"));
  }

  private static Point parsePoint(String str) {
    int index = str.indexOf(',');
    String x = str.substring(1, index);
    String y = str.substring(index + 1, str.length() - 1);
    return new Point(Integer.parseInt(x), Integer.parseInt(y));
  }
}
