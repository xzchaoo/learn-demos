package com.xzhcaoo.learn;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author zcxu
 * @date 2017/12/21
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {
	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		LocalDateWrapper ldw = new LocalDateWrapper();
		ldw.setYear(value.getYear());
		ldw.setMonth(value.getMonthValue());
		ldw.setDay(value.getDayOfMonth());
		gen.writeObject(ldw);
		//JsonSerializer<Object> js = serializers.findValueSerializer(LocalDateWrapper.class);
		//js.serialize
	}

	@Override
	public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
		//super.acceptJsonFormatVisitor(visitor, type);
		//visitor.expectObjectFormat();
		JavaType ldwt = visitor.getProvider().constructType(LocalDateWrapper.class);
		visitor.getProvider().findValueSerializer(ldwt).acceptJsonFormatVisitor(visitor, type);
	}
}
