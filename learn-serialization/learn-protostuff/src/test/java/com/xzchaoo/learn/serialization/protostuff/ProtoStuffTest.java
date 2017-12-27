package com.xzchaoo.learn.serialization.protostuff;

import com.xzhcaoo.learn.User;

import org.junit.Test;

import java.io.IOException;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufOutput;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author zcxu
 * @date 2017/12/22
 */
public class ProtoStuffTest {
	@Test
	public void test() throws IOException {
		Schema<User> s = RuntimeSchema.getSchema(User.class);
		LinkedBuffer lb = LinkedBuffer.allocate();
		User u = new User();
		u.setId(1L);
		u.setUsername("haa");
		s.writeTo(new ProtobufOutput(lb), u);
		LinkedBuffer.writeTo(System.out, lb);
	}
}
