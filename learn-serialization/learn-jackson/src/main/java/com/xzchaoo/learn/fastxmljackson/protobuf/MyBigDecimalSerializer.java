package com.xzchaoo.learn.fastxmljackson.protobuf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MyBigDecimalSerializer extends StdSerializer<BigDecimal> {
	public MyBigDecimalSerializer() {
		super(BigDecimal.class);
	}

	@Override
	public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
		//super.acceptJsonFormatVisitor(visitor, typeHint);
		//this.visitIntFormat();
		//visitor.getProvider().getTypeFactory()
		//visitor.expectStringFormat(typeHint);
		SerializerProvider p = visitor.getProvider();
		JavaType bdw = p.constructType(BigDecimalWrapper.class);
		//JsonObjectFormatVisitor v = visitor.expectObjectFormat(typeHint);
		//visitor.expect
		//BeanDescription bd = p.getConfig().introspect(bdw);
		//visitor.expectObjectFormat(typeHint);
		p.findValueSerializer(bdw).acceptJsonFormatVisitor(visitor, typeHint);

		//visitor.expectObjectFormat(bdw);
		//new BeanSerializerFactory().
		//visitor.getProvider()
		//new BeanSerializerBuilder(bd).build().acceptJsonFormatVisitor(visitor, bdw);
		//visitor.expectObjectFormat(visitor.getProvider().constructType(BigDecimalWrapper.class));
//		JsonObjectFormatVisitor v = visitor.expectObjectFormat(visitor.getProvider().constructType(String.class));
//		if (v != null) {
////v.optionproperty
//		}
	}

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		//gen.writeString(value.toString() + "A");
		String s = value.toString();
		BigDecimalWrapper bdw = new BigDecimalWrapper();
		bdw.setRaw(s);
		bdw.setLength(s.length());
		gen.writeObject(bdw);
	}
}
