package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.DefinedTypeElementBuilders;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtoBufSchemaVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaHelper;

public class XzcMessageElementVisitor extends MessageElementVisitor {
  private XzcCustom xzcCustom;

  public XzcMessageElementVisitor(SerializerProvider provider, JavaType type, DefinedTypeElementBuilders definedTypeElementBuilders, boolean isNested, XzcCustom xzcCustom) {
    super(provider, type, definedTypeElementBuilders, isNested);
    //在这里覆盖掉元素名
    _builder.name(buildName(type));
    this.xzcCustom = xzcCustom;
  }

  private String buildName(JavaType type) {
    return type.getRawClass().getName();
  }


  protected DataType getDataType(JavaType type) throws JsonMappingException {
    XzcPbTypeCustom custom = xzcCustom.getCustom(type);
    if (custom != null) {
      return custom.getDataType();
    }
    if (!_definedTypeElementBuilders.containsBuilderFor(type)) { // No self ref
      if (isNested(type)) {
        if (!_nestedTypes.contains(type)) { // create nested type
          _nestedTypes.add(type);
          ProtoBufSchemaVisitor builder = acceptTypeElement(_provider, type,
            _definedTypeElementBuilders, true);
          DataType scalarType = builder.getSimpleType();
          if (scalarType != null) {
            return scalarType;
          }
          _builder.addType(builder.build());
        }
      } else { // track non-nested types to generate them later
        ProtoBufSchemaVisitor builder = acceptTypeElement(_provider, type,
          _definedTypeElementBuilders, false);
        DataType scalarType = builder.getSimpleType();
        if (scalarType != null) {
          return scalarType;
        }
      }
    }
    return DataType.NamedType.create(buildName(type));
  }

  protected boolean isNested(JavaType type) {
    Class<?> match = type.getRawClass();
    for (Class<?> cls : _type.getRawClass().getDeclaredClasses()) {
      if (cls == match) {
        return true;
      }
    }
    return false;
  }

  protected ProtoBufSchemaVisitor acceptTypeElement(SerializerProvider provider, JavaType type,
                                                    DefinedTypeElementBuilders definedTypeElementBuilders, boolean isNested) throws JsonMappingException {
    JsonSerializer<Object> serializer = provider.findValueSerializer(type, null);
    ProtoBufSchemaVisitor visitor = new XzcProtoBufSchemaVisitor(provider, definedTypeElementBuilders, isNested, xzcCustom);
    serializer.acceptJsonFormatVisitor(visitor, type);
    return visitor;
  }

}
