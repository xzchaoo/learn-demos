package com.xzchaoo.learn.serialization.jackson.fastxmljackson.protobuf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MyBigDecimalDeserializer extends StdScalarDeserializer<BigDecimal> {
	protected MyBigDecimalDeserializer() {
		super(BigDecimal.class);
	}

	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		BigDecimalWrapper bdw = (p.getCodec()).readValue(p, BigDecimalWrapper.class);
		return new BigDecimal(bdw.getRaw());

//
//		switch (p.getCurrentTokenId()) {
//			case JsonTokenId.ID_STRING:
//				String text = p.getText().trim();
//				// note: no need to call `coerce` as this is never primitive
//				if (_isEmptyOrTextualNull(text)) {
//					_verifyNullForScalarCoercion(ctxt, text);
//					return getNullValue(ctxt);
//				}
//				text = text.substring(0, text.length() - 1);
//				_verifyStringForScalarCoercion(ctxt, text);
//				try {
//					return new BigDecimal(text);
//				} catch (IllegalArgumentException iae) {
//				}
//				return (BigDecimal) ctxt.handleWeirdStringValue(_valueClass, text,
//					"not a valid representation");
//			case JsonTokenId.ID_START_ARRAY:
//				return _deserializeFromArray(p, ctxt);
//		}
//		// Otherwise, no can do:
//		return (BigDecimal) ctxt.handleUnexpectedToken(_valueClass, p);
	}
}
