package com.xzchaoo.learn.db.mongodb;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class EmbeddedObject1 {
    private int int1;
    private float float1;
    private double double1;
    private String string1;

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public float getFloat1() {
        return float1;
    }

    public void setFloat1(float float1) {
        this.float1 = float1;
    }

    public double getDouble1() {
        return double1;
    }

    public void setDouble1(double double1) {
        this.double1 = double1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    @Override
    public String toString() {
        return "EmbeddedObject1{" +
            "int1=" + int1 +
            ", float1=" + float1 +
            ", double1=" + double1 +
            ", string1='" + string1 + '\'' +
            '}';
    }
}
