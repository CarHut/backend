package com.carhut.models.carhut.converters;

import com.carhut.requests.requestmodels.ModelsPostModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class BrandsAndModelsAttributeConverter implements AttributeConverter<List<ModelsPostModel>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ModelsPostModel> modelsPostModel) {
        try {
            return objectMapper.writeValueAsString(modelsPostModel);
        }
        catch (Exception e) {
            System.out.println("Cannot convert modelsPostModel to string value.");
            return null;
        }
    }

    @Override
    public List<ModelsPostModel> convertToEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Cannot convert string json to ModelsPostModel.class.");
            return null;
        }
    }
}
