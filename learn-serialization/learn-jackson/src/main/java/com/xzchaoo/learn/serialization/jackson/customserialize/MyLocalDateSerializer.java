package com.xzchaoo.learn.serialization.jackson.customserialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * created by xzchaoo at 2017/10/29
 *
 * @author xzchaoo
 */
public class MyLocalDateSerializer extends JsonSerializer<LocalDate> {
	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value == null) {
			System.out.println("value is null");
			gen.writeNull();
		} else {
			gen.writeString("B" + DateTimeFormatter.ofPattern("yyyyMMdd").format(value));
		}
	}
}
