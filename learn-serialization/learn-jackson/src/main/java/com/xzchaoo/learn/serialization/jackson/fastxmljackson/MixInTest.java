package com.xzchaoo.learn.serialization.jackson.fastxmljackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity.SecurityUser;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.mixin.SecurityUserMixin;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.mixin.SecurityUserMixin2;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2017/6/15.
 */
public class MixInTest {
	ObjectMapper om;

	@Before
	public void before(){
		om = new ObjectMapper();
	}

	@Test
	public void test1() throws Exception {
		//通过mixin 可以将SecurityUserMixin上的注解应用到SecurityUser上
		//om.addMixIn(SecurityUser.class, SecurityUserMixin.class);
		om.registerModule(new SimpleModule() {
			@Override
			public void setupModule(SetupContext context) {
				super.setupModule(context);
				context.setMixInAnnotations(SecurityUser.class, SecurityUserMixin.class);
			}
		});

		System.out.println(om.readValue("{\"id\":123,\"name\":\"xzc\",\"age\":3}", SecurityUser.class));
	}

	@Test
	public void test2() throws Exception {
		//通过mixin 可以将SecurityUserMixin上的注解应用到SecurityUser上
		//om.addMixIn(SecurityUser.class, SecurityUserMixin.class);
		om.registerModule(new SimpleModule() {
			@Override
			public void setupModule(SetupContext context) {
				super.setupModule(context);
				context.setMixInAnnotations(SecurityUser.class, SecurityUserMixin2.class);
			}
		});

		System.out.println(om.readValue("{\"id\":123,\"name\":\"xzc\",\"age\":3}", SecurityUser.class));
	}
}
