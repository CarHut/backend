package com.carhut.services.sr.carhutapisr.service;

import com.carhut.commons.model.CarHutCar;
import com.carhut.commons.model.CarHutCarFilterModel;
import com.carhut.services.sr.carhutapisr.dto.TypeCorrectCarHutCarFilterDto;
import com.carhut.services.sr.carhutapisr.dto.converter.CarHutCarFilterModelConverter;
import com.carhut.services.sr.carhutapisr.repository.resourceprovider.CarHutApiDatabaseResourceManager;
import com.carhut.services.sr.carhutapisr.repository.resourceprovider.CarHutCarRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarHutApiSRService {

    @Autowired
    private CarHutCarRepository carHutCarRepository;
    private final CarHutCarFilterModelConverter carHutCarFilterModelConverter = new CarHutCarFilterModelConverter();

    private static CarHutApiDatabaseResourceManager carHutApiDatabaseResourceManager =
            CarHutApiDatabaseResourceManager.getInstance();

    public CompletableFuture<ServiceStatus> removeOffer(String carId) {
        Function<Void, ServiceStatus> function = (unused) -> {
            try {
                carHutCarRepository.removeOffer(carId);
                return ServiceStatus.OK;
            } catch (Exception e) {
                return ServiceStatus.ERROR;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<List<CarHutCar>> getListingsForUserId(String userId) {
        Function<Void, List<CarHutCar>> function = (unused) -> {
            try {
                return carHutCarRepository.getListingsForUserId(userId);
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<String> addCarToDatabase(CarHutCar carHutCar) {
        Function<Void, String> function = (unused) -> {
            try {
                carHutCarRepository.save(carHutCar);
                return carHutCar.getId();
            } catch (Exception e) {
                return null;
            }
        };

        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<CarHutCar> getCarWithId(String carId) {
        Function<Void, CarHutCar> function = (unused) -> {
            try {
                return carHutCarRepository.getCarWithId(carId);
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<List<CarHutCar>> getCarsWithFilter(CarHutCarFilterModel carHutCarFilterModel,
                                                                String sortBy, String sortOrder, String offersPerPage,
                                                                String currentPage) {
        Function<Void, List<CarHutCar>> function = (unused) -> {
            try {
                TypeCorrectCarHutCarFilterDto dto = carHutCarFilterModelConverter
                        .convertToTypeCorrectCarHutCarFilterDto(carHutCarFilterModel);
//                Pageable pageable = PageRequest.of(currentPage, offersPerPage, Sort.unsorted());
                List<CarHutCar> result = carHutCarRepository.getCarsWithFilter(dto.getBrandId(), dto.getModelId(),
                        dto.getPriceFrom(), dto.getPriceTo(), dto.getMileageFrom(), dto.getMileageTo(),
                        dto.getRegistrationFrom(), dto.getRegistrationTo(), dto.getSeatingConfig(), dto.getDoors(),
                        dto.getLocation(), dto.getFuelType(), dto.getPowerFrom(), dto.getPowerTo(),
                        dto.getDisplacementFrom(), dto.getDisplacementTo(), dto.getGearbox(), dto.getPowertrain()/**,
                        dto.getCarTypes()**/);
                return result;
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<Integer> getNumberOfFilteredCars(CarHutCarFilterModel carHutCarFilterModel) {
        Function<Void, Integer> function = (unused) -> {
            try {
                TypeCorrectCarHutCarFilterDto dto = carHutCarFilterModelConverter
                        .convertToTypeCorrectCarHutCarFilterDto(carHutCarFilterModel);
                return carHutCarRepository.getNumberOfFilteredCars(dto.getBrandId(), dto.getModelId(),
                        dto.getPriceFrom(), dto.getPriceTo(), dto.getMileageFrom(), dto.getMileageTo(),
                        dto.getRegistrationFrom(), dto.getRegistrationTo(), dto.getSeatingConfig(), dto.getDoors(),
                        dto.getLocation(), dto.getFuelType(), dto.getPowerFrom(), dto.getPowerTo(),
                        dto.getDisplacementFrom(), dto.getDisplacementTo(), dto.getGearbox(), dto.getPowertrain()/**,
                        dto.getCarTypes()**/);
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<List<CarHutCar>> getCarsByIds(List<String> ids) {
        Function<Void, List<CarHutCar>> function = (unused) -> {
            try {
                return carHutCarRepository.getCarsWithIds(ids);
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }
}
