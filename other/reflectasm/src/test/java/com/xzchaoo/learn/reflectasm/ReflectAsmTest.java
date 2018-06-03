package com.xzchaoo.learn.reflectasm;

import com.esotericsoftware.reflectasm.MethodAccess;

import org.junit.Test;

/**
 * @author xzchaoo
 * @date 2018/6/4
 */
public class ReflectAsmTest {
  @Test
  public void test() {
    // FieldAccess 只能用于public field
    // 因此用得最多的还是FieldAccess

    User user = new User();
    MethodAccess ma = MethodAccess.get(User.class);
    int setIdIndex = ma.getIndex("setId");
    ma.invoke(user, setIdIndex, 1);
    System.out.println(user);
  }
}
