package com.xzchaoo.learn.db.mongodb.bsonjackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import de.undercouch.bson4jackson.BsonFactory;

/**
 * @author zcxu
 * @date 2017/12/15
 */
public class TestBsonJackson {
	@Test
	public void test() {
		ObjectMapper mapper = new ObjectMapper(new BsonFactory());

	}
}
