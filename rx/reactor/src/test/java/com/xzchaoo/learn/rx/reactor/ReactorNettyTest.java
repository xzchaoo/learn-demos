package com.xzchaoo.learn.rx.reactor;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.reactivestreams.Publisher;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.ByteBufFlux;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientOptions;
import reactor.ipc.netty.http.client.HttpClientResponse;
import reactor.ipc.netty.http.websocket.WebsocketInbound;
import reactor.ipc.netty.http.websocket.WebsocketOutbound;

/**
 * @author xzchaoo
 * @date 2017/12/31
 */
public class ReactorNettyTest {
  @Test
  public void test() throws InterruptedException {
    HttpClient hc = HttpClient.builder()
      .options(new Consumer<HttpClientOptions.Builder>() {
        @Override
        public void accept(HttpClientOptions.Builder builder) {
          // builder.poolResources()
        }
      })
      .build();
    String s = hc.get("http://www.qq.com").flatMap(resp -> {
      String contentType = resp.responseHeaders().get("Content-Type");
      System.out.println(contentType);
      String charset = StringUtils.substringAfter(contentType, "=");
      return ByteBufFlux.fromInbound(resp.receiveContent()).aggregate().asString(Charset.forName(charset));
    }).block();
    System.out.println(s);
  }

  @Test
  public void test2() throws InterruptedException {
    HttpClient hc = HttpClient.create();
    hc.ws("wss://broadcastlv.chat.bilibili.com:2245/sub").subscribe(new Consumer<HttpClientResponse>() {
      @Override
      public void accept(HttpClientResponse httpClientResponse) {
        System.out.println("accept " + httpClientResponse.status());
        httpClientResponse.receiveWebsocket(new BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>>() {
          @Override
          public Publisher<Void> apply(WebsocketInbound websocketInbound, WebsocketOutbound websocketOutbound) {
            websocketOutbound.options(b -> b.flushOnEach());

            JSONObject data = new JSONObject();
            data.put("uid", 1);
            data.put("roomid", 5503854);
            byte[] bytes = data.toString().getBytes(StandardCharsets.UTF_8);

            ByteBuf first = Unpooled.buffer(16 + bytes.length);
            // 总厂
            first.writeInt(16 + bytes.length);
            first.writeShort(16);
            first.writeShort(1);
            first.writeInt(7);
            first.writeInt(1);
            first.writeBytes(bytes);

            websocketOutbound.send(Mono.just(first.retain()));

            // websocketInbound.aggregateFrames().receive().asString().subscribe(System.out::println);

            websocketInbound.aggregateFrames().receiveFrames().subscribe(f -> {
              System.out.println("f " + f);
            });

            // websocketInbound.receiveFrames().subscribe(new Consumer<WebSocketFrame>() {
            //   @Override
            //   public void accept(WebSocketFrame webSocketFrame) {
            //     System.out.println("收到帧 " + webSocketFrame);
            //   }
            // });

            return Flux.never();
          }
        }).subscribe();
      }
    });
    Thread.sleep(20000);
  }
}
