package com.xzchaoo.learn.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

  @Test
  public void test_find_normal_and_special() throws Exception {
    CloseableHttpClient hc = HttpClients.custom().setMaxConnPerRoute(5).build();
    CloseableHttpResponse resp = hc
        .execute(RequestBuilder.get("http://10.5.80.217:8080/citypair/merged").build());
    JSONObject r = JSON.parseObject(EntityUtils.toString(resp.getEntity()));
    for (String key : r.keySet()) {
      JSONObject e = r.getJSONObject(key);
      if (e.getBooleanValue("normal") && e.getJSONArray("specialAirlines") != null) {
        System.out.println(key);
      }
    }
    resp.close();
  }

  @Test
  public void test1() throws Exception {
    CloseableHttpClient hc = HttpClients.custom().setMaxConnPerRoute(5).build();
    ExecutorService es = Executors.newFixedThreadPool(5);
    LocalDate today = LocalDate.now();
    List<String> lines = Files.readAllLines(Paths.get("d:\\Users\\zcxu\\Desktop\\广告城市对.txt"));
    AtomicInteger ai = new AtomicInteger(0);
    for (String line : lines) {
      es.execute(() -> {
        String[] ss = line.split("\t");
        String departureCityCode = ss[1];
        String arrivalCityCode = ss[2];
        String tripType = ss[3];
        String airline = ss[4];
        String seatGrade = ss[5];
        LocalDate endTime = LocalDate.parse(ss[7]);
        LocalDate beginTime = LocalDate.parse(ss[8]);

        LocalDate owMaxEnd = today.plusDays(90);
        LocalDate rtMaxEnd = today.plusDays(45);

        if (beginTime.isBefore(today)) {
          beginTime = today;
        }

        if ("OW".equals(tripType)) {
          if (endTime.isAfter(owMaxEnd)) {
            endTime = owMaxEnd;
          }
        } else {
          if (endTime.isAfter(rtMaxEnd)) {
            endTime = rtMaxEnd;
          }
        }
        if (beginTime.isAfter(endTime)) {
          return;
        }
        System.out.println(
            departureCityCode + " " + arrivalCityCode + " " + tripType + " " + airline + " "
                + seatGrade + " " + beginTime + " " + endTime);

        HttpUriRequest hur = RequestBuilder
            .get("http://10.15.98.98:8080/lowpricebot/html/updateSingleFlight")
            .addParameter("departCity", departureCityCode)
            .addParameter("arriveCity", arrivalCityCode)
            .addParameter("routeType", "Advertisement")
            .addParameter("tripType", tripType)
            .addParameter("airline", airline)
            .addParameter("seatGrade", seatGrade)
            .addParameter("channel", "")
            .addParameter("subChannel", "")
            .addParameter("departDateBegin", beginTime.toString())
            .addParameter("departDateEnd", endTime.toString())
            .addParameter("offsetBegin", "2")
            .addParameter("offsetEnd", "12")
            .build();

        CloseableHttpResponse resp = null;
        try {
          resp = hc.execute(hur);
          System.out.println(EntityUtils.toString(resp.getEntity()));
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          HttpClientUtils.closeQuietly(resp);
        }
        System.out.println(ai.incrementAndGet());
      });
    }
    Thread.sleep(1000000000);
  }

  @Test
  public void test_buildRequest() {
    RequestBuilder.post("url")
        .addParameter("a", "b")
        .addHeader("a", "b")
        .setEntity(new StringEntity("abc", ContentType.create("text/plain", "utf-8")))
        .setConfig(RequestConfig.custom().build())
        .build();
  }

  @Test
  public void test_entity() {
    //HttpEntity 封装了 http请求的body
    //常见的表单提交是 UrlEncodedFormEntity
    //一些有用的类 ByteArrayEntity InputStreamEntity FileEntity EntityTemplate
    //如果要构造form-data 就需要引入另外一个jar包 httpmime
    //如果要构造application/json 就使用 StringTemplate 封装一下

    MultipartEntityBuilder b = MultipartEntityBuilder.create();
    b.addTextBody("filename", "abc.png");
    b.addBinaryBody("asdf", new byte[0], ContentType.APPLICATION_OCTET_STREAM, "abc.png");
    HttpEntity entity = b.build();
  }

  @Test
  public void test() {
    RequestConfig defaultRC = RequestConfig.custom()
        .setConnectTimeout(1000)
        .setSocketTimeout(1000)
        .setConnectionRequestTimeout(1000)//这个超时是指从连接池里拿一个连接的超时时间
        .build();
    PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
    manager.setMaxTotal(100);
    manager.setDefaultMaxPerRoute(100);
    CloseableHttpClient chc = HttpClientBuilder.create()
        .setConnectionManager(manager)
        .setDefaultRequestConfig(defaultRC)
        .build();
    CloseableHttpResponse resp = null;
    try {
      HttpUriRequest hur = RequestBuilder.get("http://www.bilibili.com")
          .addHeader("a", "b")
          .addParameter("a", "b")
          .build();
      resp = chc.execute(hur);
      String content = EntityUtils.toString(resp.getEntity());
      StatusLine statusLine = resp.getStatusLine();
      Header[] headers = resp.getAllHeaders();
      System.out.println(content);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      HttpClientUtils.closeQuietly(resp);
      HttpClientUtils.closeQuietly(chc);
    }
  }
}
