package com.xzchaoo.learn.serialization.jackson.stream;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * JsonAutoDetect用于控制 字段 getter方法 setter方法 构造函数 等的可见性 如果字段可见的话 那么所有字段默认都会成为json的属性
 * created by xzchaoo at 2017/12/5
 *
 * @author xzchaoo
 */
@JsonAutoDetect
public class JsonStreamTest {
  @Test
  public void test() throws IOException {
    JsonFactory f = new JsonFactory();

    //f的主要功能就是用来创建 generator 和 parser
    //一旦创建出第一个generator或parser后就不能再修改f了
    //g和p只能用于操作基本数据类型
    //ObjectCodec 用于实现 读写复杂类型的操作

    StringWriter sw = new StringWriter();
    JsonGenerator g = f.createGenerator(sw);
    g.writeStartObject();
    g.writeNumberField("id", 1);
    g.writeStringField("name", "xzc");
    g.writeArrayFieldStart("array");
    g.writeString("aa");
    g.writeString("bb");
    g.writeEndArray();
    g.writeEndObject();
    g.flush();

    JsonParser p = f.createParser(new StringReader(sw.toString()));
    if (p.nextToken() != JsonToken.START_OBJECT) {
      throw new IllegalStateException();
    }
    String fieldName = null;
    while (true) {
      //每个token的可能取值可以参考 JsonToken 的静态变量
      //每个token的id的取值也是固定的 可以参考静态变量
      JsonToken t = p.nextToken();
      if (t == null || t == JsonToken.END_OBJECT) {
        System.out.println("final =" + t);
        break;
      }
      if (t == JsonToken.FIELD_NAME) {
        //这几种方式都可以拿到名字
        //fieldName = p.getText();
        //fieldName = p.getValueAsString();
        fieldName = p.getCurrentName();
      } else if (t.isScalarValue()) {
        //Parser具有记忆性 此时可以拿到 FIELD_NAME
        //System.out.println(p.getCurrentName());
        System.out.println(fieldName + "=" + p.getText());
        fieldName = null;
      } else if (t == JsonToken.START_ARRAY) {
        System.out.println("skipChildren");
        p.skipChildren();
      } else {
        System.out.println("else " + t);
      }
    }
//		while (p.nextToken() != JsonToken.END_OBJECT) {
//			String name = p.getCurrentName();
//			JsonToken t = p.nextToken();
//			System.out.println(name + " " + t);
//		}
  }
}
