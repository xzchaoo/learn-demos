package com.xzchaoo.learn.utils.okio;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.GzipSink;
import okio.Okio;

/**
 * Source 类似于 InputStream, Sink 类似于 OutputStream
 * BufferedSource 包装了一个Source, 具备buffered的功能, 并且还能对常见数据类型进行读
 * BufferedSink 包装了一个Sink, jubei buffered的功能, 并且还能对常见的数据类型进行写
 * Buffer 实现了 BufferedSource BufferedSink 接口, 可以作为一块缓存, 类似可自增长的byte[]
 * buffer内部采用分段byte[]来防止连续大块内存
 *
 * @author xzchaoo
 * @date 2018/3/31
 */
public class OkioTest {
  private static final ByteString FOO_HEADER = ByteString.decodeHex("CAFEBABE");

  @Test
  public void test_gzip() {
    OutputStream netOS = new ByteArrayOutputStream();
    BufferedSink sink = Okio.buffer(Okio.sink(netOS));
    GzipSink gzipSink = new GzipSink(sink);
  }

  @Test
  public void test() throws IOException {
    //Buffer is also a Source and Sink
    try ( Buffer buffer = new Buffer() ) {
      buffer.write(ByteString.decodeHex("CAFEBABE"));
      buffer.writeInt(4);
      buffer.writeInt(1);
      buffer.writeByte(1);
      buffer.writeByte(2);
      buffer.writeByte(3);
      buffer.writeByte(4);

      BufferedSource source = buffer;

      ByteString readHeader = source.readByteString(FOO_HEADER.size());
      if (!FOO_HEADER.equals(readHeader)) {
        throw new IOException("Not a illegal java class");
      }
      int length = source.readInt();
      int type = source.readInt();
      byte[] data = source.readByteArray(length);
      System.out.println(source.exhausted());
    }
  }
}