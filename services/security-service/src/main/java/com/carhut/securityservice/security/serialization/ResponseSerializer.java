package com.carhut.securityservice.security.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

public class ResponseSerializer<BodyType> {

    public ResponseSerializer() {

    }

    public BodyType deserialize(String object, Class<BodyType> clazz) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(object, clazz);
        } catch (InvalidDefinitionException e) {
            System.out.println("Cannot deserialize object " + object + " to BodyType.");
            return null;
        }
    }
}
