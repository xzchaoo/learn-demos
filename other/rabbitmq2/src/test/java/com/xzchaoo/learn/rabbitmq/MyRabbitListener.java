package com.xzchaoo.learn.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * TODO 千万注意 除了fanout外 必须要指定 key 否则会导致binding时抛异常 但spring并不会终止流程 导致后续bindings全部没有执行 详情见代码  org.springframework.amqp.rabbit.core.RabbitAdmin#declareBindings(com.rabbitmq.client.Channel, org.springframework.amqp.core.Binding...)
 *
 * @author xzchaoo
 * @date 2018/4/5
 */
@Component
public class MyRabbitListener {
  @Autowired
  private RabbitTemplate rabbitTemplate;

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue,//("q1"),
    exchange = @Exchange(value = "app.simple4", type = "fanout")
  ))
  public void onMessage41(String data) {
    System.out.println("onMessage41 " + data);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue,//("q2"),
    exchange = @Exchange(value = "test.simple4", type = "fanout")
  ))
  public void onMessage42(String data) {
    System.out.println("onMessage42 " + data);
  }

  //step 1
  @RabbitListener(
    bindings = {@QueueBinding(
      value = @Queue("step1"),
      exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
      key = "step1"
    )}
  )
  public void onMessage(Message message, @Payload Integer value) {
    value = value * 10 + value;
    rabbitTemplate.convertAndSend("", "step2", value);
  }

  // step 2
  @RabbitListener(
    bindings = {@QueueBinding(
      value = @Queue("step2"),
      exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
      key = "step2"
    )}
  )
  @SendTo("step3")
  public int step2(Message message, @Payload Integer value) {
    return value;
  }


  // step 3
  @RabbitListener(queues = "step3")
  public void step3(Message message, @Payload Integer value) {
    System.out.println("step3 " + value);
  }

  //
  //
  @RabbitListener(queues = {"test.simple2"})
  public void onMessage2(String data) {
    System.out.println("onMessage2 " + data + " " + Thread.currentThread());
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple3"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple3"
  ))
  public void onMessage3(String data) {
    System.out.println("onMessage3 " + data);
  }

  @RabbitListener(queues = {"test.simple5"})
  public void onMessage5(MessageEntity msg) {
    System.out.println("onMessage5 " + msg);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple6"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple6"
  ))
  public void onMessage6(String msg) {
    System.out.println("onMessage6 " + msg);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple7"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple7"
  ))
  public void onMessage7(String msg) {
    System.out.println("onMessage7 " + msg);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple8"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple8"
  ))
  public void onMessage8(String msg) {
    System.out.println("onMessage8 " + msg);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple9"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple9"
  ))
  public void onMessage9(String msg) {
    System.out.println("onMessage9 " + msg);
  }

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue("test.simple10"),
    exchange = @Exchange(value = "", ignoreDeclarationExceptions = "true"),
    key = "test.simple10"
  ))
  public void onMessage10(String msg) {
    System.out.println("onMessage10 " + msg);
  }

}
