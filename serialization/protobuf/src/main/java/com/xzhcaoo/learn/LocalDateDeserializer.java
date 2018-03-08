package com.xzhcaoo.learn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.xzhcaoo.learn.LocalDateWrapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author zcxu
 * @date 2017/12/21
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ProtobufMapper pm = (ProtobufMapper) p.getCodec();
		LocalDateWrapper ldw = pm.readValue(p, LocalDateWrapper.class);
		return LocalDate.of(ldw.getYear(), ldw.getMonth(), ldw.getDay());
	}
}
