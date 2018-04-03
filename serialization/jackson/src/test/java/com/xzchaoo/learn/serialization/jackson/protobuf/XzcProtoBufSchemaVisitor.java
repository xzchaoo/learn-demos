package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.DefinedTypeElementBuilders;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtoBufSchemaVisitor;

public class XzcProtoBufSchemaVisitor extends ProtoBufSchemaVisitor {
  private XzcCustom xzcCustom;

  public XzcProtoBufSchemaVisitor(SerializerProvider provider, DefinedTypeElementBuilders defBuilders, boolean isNested, XzcCustom xzcCustom) {
    super(provider, defBuilders, isNested);
    this.xzcCustom = xzcCustom;
  }

  @Override
  public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {
    MessageElementVisitor visitor = new XzcMessageElementVisitor(_provider, type, _definedTypeElementBuilders,
      _isNested, xzcCustom);
    _builder = visitor;
    _definedTypeElementBuilders.addTypeElement(type, visitor, _isNested);
    return visitor;
  }
}
