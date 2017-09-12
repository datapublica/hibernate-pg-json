package com.datapublica.pg.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.io.IOException;

/**
 * Inspired by Regis Leray's impl but adjusted for the needs
 *
 * @author Loic Petit
 */
public class JsonType extends RawJsonType {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private ObjectWriter writer;
    private JavaType type;
    private ObjectReader reader;

    protected JsonType() {
    }

    public JsonType(Class clazz, boolean isBinary) {
        this(SimpleType.construct(clazz), isBinary);
    }

    public JsonType(JavaType type, boolean isBinary) {
        init(type, isBinary);
    }

    protected void init(JavaType type, boolean isBinary) {
        super.init(isBinary);
        this.type = type;
        this.reader = MAPPER.reader(type);
        this.writer = MAPPER.writerWithType(type);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    protected Object deserialize(String content)  {
        try {
            return reader.readValue(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String serialize(Object object)  {
        try {
            return writer.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class returnedClass() {
        return type.getRawClass();
    }
}