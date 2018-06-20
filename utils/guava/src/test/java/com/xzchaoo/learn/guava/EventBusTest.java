package com.xzchaoo.learn.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import org.junit.Test;

import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/6/20
 */
public class EventBusTest {
  @Test
  public void test() {
    final EventBus b = new EventBus();
    final Subscriber subscriber = new Subscriber();
    b.register(subscriber);
    b.post("aa");
    assertThat(subscriber.isReceived()).isTrue();
  }

  @Getter
  public static class Subscriber {
    private boolean received;

    @AllowConcurrentEvents
    @Subscribe
    public void onEvent(String str) {
      System.out.println(str);
      received = true;
    }
  }
}
