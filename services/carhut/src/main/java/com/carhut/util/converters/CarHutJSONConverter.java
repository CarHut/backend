package com.carhut.util.converters;

import com.carhut.models.carhut.CarHutCar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CarHutJSONConverter {

    public List<CarHutCar> deserializeListOfCarHutCars(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<CarHutCar> deserializedCars;
        try {
            deserializedCars = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return deserializedCars;
    }


}
