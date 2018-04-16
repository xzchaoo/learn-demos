package com.xzchaoo.learn.rx.reactor;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2017/6/6.
 */
@ChannelHandler.Sharable
public class BilibiliLiveMessageEncoder extends MessageToByteEncoder<BilibiliLiveMessage> {
  @Override
  protected void encode(ChannelHandlerContext ctx, BilibiliLiveMessage msg, ByteBuf out) throws Exception {
    String data = msg.getData() == null ? null : msg.getData().toJSONString();
    byte[] bytes = data == null ? null : data.getBytes(StandardCharsets.UTF_8);
    int length = 16 + (bytes == null ? 0 : bytes.length);
    out.ensureWritable(length);
    out.writeInt(length);
    out.writeShort(msg.getH1());
    out.writeShort(msg.getH2());
    out.writeInt(msg.getType());
    out.writeInt(msg.getH3());
    if (bytes != null) {
      out.writeBytes(bytes);
    }
  }
}
