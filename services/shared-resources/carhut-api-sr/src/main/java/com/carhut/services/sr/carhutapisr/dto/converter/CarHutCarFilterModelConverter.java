package com.carhut.services.sr.carhutapisr.dto.converter;

import com.carhut.commons.model.CarHutCarFilterModel;
import com.carhut.services.sr.carhutapisr.dto.TypeCorrectCarHutCarFilterDto;

import java.sql.Date;

public class CarHutCarFilterModelConverter {

    public TypeCorrectCarHutCarFilterDto convertToTypeCorrectCarHutCarFilterDto(CarHutCarFilterModel filter) {
        Integer brandId = filter.getBrandId() != null ? Integer.parseInt(filter.getBrandId()) : null;
        Integer modelId = filter.getModelId() != null ? Integer.parseInt(filter.getModelId()) : null;
        Integer priceFrom = filter.getPriceFrom() != null ? Integer.parseInt(filter.getPriceFrom()) : null;
        Integer priceTo = filter.getPriceTo() != null ? Integer.parseInt(filter.getPriceTo()) : null;
        Integer mileageFrom = filter.getMileageFrom() != null ? Integer.parseInt(filter.getMileageFrom()) : null;
        Integer mileageTo = filter.getMileageTo() != null ? Integer.parseInt(filter.getMileageTo()) : null;
        Integer registrationFrom = filter.getRegistrationFrom() != null
                ? Integer.parseInt(filter.getRegistrationFrom()) : null;
        Integer registrationTo = filter.getRegistrationTo() != null
                ? Integer.parseInt(filter.getRegistrationTo()) : null;
        Integer powerFrom = filter.getPowerFrom() != null ? Integer.parseInt(filter.getPowerFrom()) : null;
        Integer powerTo = filter.getPowerTo() != null ? Integer.parseInt(filter.getPowerTo()) : null;
        Integer displacementFrom = filter.getDisplacementFrom() != null
                ? Integer.parseInt(filter.getDisplacementFrom()) : null;
        Integer displacementTo = filter.getDisplacementTo() != null
                ? Integer.parseInt(filter.getDisplacementTo()) : null;
        return new TypeCorrectCarHutCarFilterDto(brandId,
                modelId, filter.getCarTypes(), priceFrom, priceTo, mileageFrom, mileageTo, registrationFrom,
                registrationTo, filter.getSeatingConfig(), filter.getDoors(), filter.getLocation(),
                filter.getPostalCode(), filter.getFuelType(), powerFrom, powerTo, displacementFrom, displacementTo,
                filter.getGearbox(), filter.getPowertrain(), filter.getModels());
    }

}
