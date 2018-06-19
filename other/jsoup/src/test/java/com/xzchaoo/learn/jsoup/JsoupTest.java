package com.xzchaoo.learn.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.IOException;

import lombok.val;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 其实jsoup蛮早前就开始用了, 它支持类似jquery的查询语法, 主要用于解析html, pyhton也有一个类似的叫做 BeautifulSoup
 *
 * @author xzchaoo
 * @date 2018/6/19
 */
public class JsoupTest {
  @Test
  public void test() throws IOException {
    val is = getClass().getClassLoader().getResourceAsStream("1.html");
    val d = Jsoup.parse(is, "UTF-8", "");
    // Elements 是一个 ArrayList 这跟JQuery是类似的
    Element contentD = d.selectFirst("div.content");
    assertThat(d.title()).isEqualTo("i_am_title");
    assertThat(contentD.selectFirst("> .content_title").text()).isEqualTo("i am h2");
    assertThat(contentD.selectFirst("> .content_title > a").attr("href")).isEqualTo("http://www.baidu.com");
    val lis = contentD.selectFirst(".content_main > ul").select("> li");
    int index = 0;
    for (val li : lis) {
      assertThat(li.text()).isEqualTo("item " + ++index);
    }
  }
}
