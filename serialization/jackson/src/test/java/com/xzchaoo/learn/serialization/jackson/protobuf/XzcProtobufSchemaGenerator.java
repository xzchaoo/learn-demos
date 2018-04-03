package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.TypeElement;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class XzcProtobufSchemaGenerator extends ProtobufSchemaGenerator {
  private XzcCustom xzcCustom;
  private final List<TypeElement> customGlobalTypes;

  public XzcProtobufSchemaGenerator(XzcCustom xzcCustom) {
    this.xzcCustom = xzcCustom;

    FieldElement valueField = FieldElement.builder()
      .label(FieldElement.Label.OPTIONAL)
      .type(DataType.ScalarType.INT64)
      .name("value")
      .tag(1)
      .build();

    MessageElement longWrapperMessage = MessageElement.builder()
      .name("LongWrapper")
      .addField(valueField)
      .build();


    FieldElement yearField = FieldElement.builder()
      .label(FieldElement.Label.OPTIONAL)
      .type(DataType.ScalarType.INT32)
      .name("year")
      .tag(1)
      .build();
    FieldElement monthField = FieldElement.builder()
      .label(FieldElement.Label.OPTIONAL)
      .type(DataType.ScalarType.INT32)
      .name("month")
      .tag(2)
      .build();
    FieldElement dayField = FieldElement.builder()
      .label(FieldElement.Label.OPTIONAL)
      .type(DataType.ScalarType.INT32)
      .name("day")
      .tag(3)
      .build();

    MessageElement jdk8LocalDate = MessageElement.builder()
      .name("Jdk8LocalDate")
      .addField(yearField)
      .addField(monthField)
      .addField(dayField)
      .build();

    customGlobalTypes = asList(longWrapperMessage, jdk8LocalDate);
  }

  @Override
  public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {
    _rootType = type;
    MessageElementVisitor visitor = new XzcMessageElementVisitor(_provider, type, _definedTypeElementBuilders,
      _isNested, xzcCustom);
    _builder = visitor;
    _definedTypeElementBuilders.addTypeElement(type, visitor, _isNested);
    return visitor;
  }

  public ProtobufSchema getGeneratedSchema(boolean appendDependencies) throws JsonMappingException {
    if (_rootType == null || _builder == null) {
      throw new IllegalStateException(
        "No visit methods called on " + getClass().getName() + ": no schema generated");
    }

    Collection<TypeElement> types;
    if (appendDependencies) {
      types = this.buildWithDependencies();
    } else {
      types = new LinkedList<>();
      types.add(build());
    }
    types.addAll(customGlobalTypes);

    return NativeProtobufSchema.construct(_rootType.getRawClass().getName(),
      types).forFirstType();
  }

}
