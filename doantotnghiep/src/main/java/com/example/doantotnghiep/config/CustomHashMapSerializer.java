package com.example.doantotnghiep.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class CustomHashMapSerializer extends JsonSerializer<Map<?, ?>> {
    @Override
    public void serialize(Map<?, ?> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                // Handle null key
                jsonGenerator.writeFieldName("null");
            } else {
                jsonGenerator.writeFieldName(entry.getKey().toString());
            }

            jsonGenerator.writeObject(entry.getValue());
        }

        jsonGenerator.writeEndObject();
    }
}
