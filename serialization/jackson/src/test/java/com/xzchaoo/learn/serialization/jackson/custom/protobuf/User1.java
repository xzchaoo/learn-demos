package com.xzchaoo.learn.serialization.jackson.custom.protobuf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

public class User1 {
  //required = true 会影响PB的生成
  @JsonProperty(value = "id", index = 2)
  private int id;

  @JsonProperty(value = "username", index = 1)
  private String username;

  @JsonProperty(value = "date", index = 3)
  private Date date;

  //	@JsonDeserialize(using = MyBigDecimalDeserializer.class)
  //@JsonSerialize(converter = BigDecimalToStringConverter.class,typing = JsonSerialize.Typing.DYNAMIC)
  //@JsonSerialize(using = MyBigDecimalSerializer.class, converter = BigDecimalToStringConverter.class)
  //@JsonSerialize(contentAs = BigDecimalWrapperConverter.class)
  //@JsonSerialize(contentAs = BigDecimalWrapper.class)
  @JsonProperty(value = "money", index = 4)
  @JsonSerialize(converter = BigDecimalWrapperConverter.class)
  @JsonDeserialize(converter = WrapperToBigDecimalConverter.class)
  private BigDecimal money;
  //	@JsonSerialize(using = MyBigDecimalSerializer.class)

  @JsonProperty(value = "card", index = 5)
  private Card card;

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public BigDecimal getMoney() {
    return money;
  }

  public void setMoney(BigDecimal money) {
    this.money = money;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
