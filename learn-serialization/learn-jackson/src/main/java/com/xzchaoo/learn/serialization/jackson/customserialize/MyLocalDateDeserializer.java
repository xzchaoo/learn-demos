package com.xzchaoo.learn.serialization.jackson.customserialize;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * created by xzchaoo at 2017/10/29
 *
 * @author xzchaoo
 */
public class MyLocalDateDeserializer extends JsonDeserializer<LocalDate> {
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		switch (p.getCurrentTokenId()) {
			case JsonTokenId.ID_NULL:
				return null;
			case JsonTokenId.ID_STRING:
				String text = p.getText();
				if (!text.startsWith("B")) {
					throw new JsonParseException(p, "");
				}
				return LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(text.substring(1)));
			default:
				throw new JsonParseException(p, "");
		}
	}
}
