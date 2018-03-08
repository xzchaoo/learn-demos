package com.xzchaoo.learn.serialization.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.val;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class GsonTest {
  @Test
  public void test() {
    //gson是线程安全的 应该在某个地方创建出它 并且定制它 并且在整个应用程序里共享它
    val gson = new GsonBuilder()
      //.serializeNulls() 默认应该是不序列化null的
      .setDateFormat("yyyy-MM-dd HH:mm:ss")
      .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
          if (value == null) {
            out.nullValue();
          } else {
            out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
          }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
          if (!in.hasNext()) {
            return null;
          }
          String value = in.nextString();
          if (value == null) {
            return null;
          }
          return LocalDate.parse(value);
        }
      })
      .create();
    val user = User.builder()
      .id(1)
      .username("aa")
      .date1(new Date())
      .date2(LocalDate.now())
      .build();
    val str = gson.toJson(user);
    System.out.println(str);
    val user2 = gson.fromJson(str, User.class);
    System.out.println(user2);
  }
}
