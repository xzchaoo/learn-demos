package com.xzchaoo.learn.javassist;

import java.io.Serializable;

public abstract class AbstractCar<A extends Serializable, B> {
  private final int int1;
  private B b;

  protected AbstractCar(int int1) {
    this.int1 = int1;
  }

  protected int getInt1() {
    return int1;
  }

  public abstract A getA();

  public B getB() {
    return b;
  }

  public void setB(B b) {
    this.b = b;
  }
}
