package com.carhut.services.sr.carhutapisr.service;

import com.carhut.commons.model.CarHutCar;
import com.carhut.commons.model.CarHutCarFilterModel;
import com.carhut.commons.model.ModelsPostModel;
import com.carhut.services.sr.carhutapisr.dto.TypeCorrectCarHutCarFilterDto;
import com.carhut.services.sr.carhutapisr.dto.converter.CarHutCarFilterModelConverter;
import com.carhut.services.sr.carhutapisr.repository.resourceprovider.CarHutApiDatabaseResourceManager;
import com.carhut.services.sr.carhutapisr.repository.resourceprovider.CarHutCarRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                                                                String sortBy, String sortOrder, Integer offersPerPage,
                                                                Integer currentPage) {
        Function<Void, List<CarHutCar>> function = (unused) -> {
            try {
                TypeCorrectCarHutCarFilterDto dto = carHutCarFilterModelConverter
                        .convertToTypeCorrectCarHutCarFilterDto(carHutCarFilterModel);
                Integer[] brandIds = carHutCarFilterModel.getModels() == null
                        ? new Integer[0]
                        : carHutCarFilterModel.getModels()
                            .stream()
                            .map(ModelsPostModel::getBrand)
                            .filter(Objects::nonNull)
                            .map(Integer::parseInt)
                            .toArray(Integer[]::new);
                Integer[] modelIds = carHutCarFilterModel.getModels() == null
                        ? new Integer[0]
                        : carHutCarFilterModel.getModels()
                            .stream()
                            .map(ModelsPostModel::getModel)
                            .filter(Objects::nonNull)
                            .map(Integer::parseInt)
                            .toArray(Integer[]::new);

                String[] carTypes = carHutCarFilterModel.getCarTypes() == null
                        ? new String[0]
                        : carHutCarFilterModel.getCarTypes()
                            .stream()
                            .filter(Objects::nonNull)
                            .toArray(String[]::new);

                Sort sorter = createSorter(sortBy, sortOrder);

                Pageable pageable = PageRequest.of(currentPage, offersPerPage, sorter);
                return carHutCarRepository.getCarsWithFilter(dto.getBrandId(), dto.getModelId(),
                        dto.getPriceFrom(), dto.getPriceTo(), dto.getMileageFrom(), dto.getMileageTo(),
                        dto.getRegistrationFrom(), dto.getRegistrationTo(), dto.getSeatingConfig(), dto.getDoors(),
                        dto.getLocation(), dto.getFuelType(), dto.getPowerFrom(), dto.getPowerTo(),
                        dto.getDisplacementFrom(), dto.getDisplacementTo(), dto.getGearbox(), dto.getPowertrain(),
                        carTypes, brandIds, modelIds, pageable);
            } catch (Exception e) {
                return null;
            }
        };
        return carHutApiDatabaseResourceManager.acquireDatabaseResource(function);
    }

    private Sort createSorter(String sortBy, String sortOrder) {
        if (sortOrder == null || sortBy == null) {
            return null;
        }
        if (!sortOrder.equals("ASC") && !sortOrder.equals("DESC")) {
            return null;
        }
        if (!sortBy.equals("price") && !sortBy.equals("mileage") && !sortBy.equals("engine_power")
                && !sortBy.equals("header")) {
            return null;
        }

        return sortOrder.equals("ASC")
            ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }

    public CompletableFuture<Integer> getNumberOfFilteredCars(CarHutCarFilterModel carHutCarFilterModel) {
        Function<Void, Integer> function = (unused) -> {
            try {
                TypeCorrectCarHutCarFilterDto dto = carHutCarFilterModelConverter
                        .convertToTypeCorrectCarHutCarFilterDto(carHutCarFilterModel);
                Integer[] brandIds = carHutCarFilterModel.getModels() == null
                        ? new Integer[0]
                        : carHutCarFilterModel.getModels()
                        .stream()
                        .map(ModelsPostModel::getBrand)
                        .filter(Objects::nonNull)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);
                Integer[] modelIds = carHutCarFilterModel.getModels() == null
                        ? new Integer[0]
                        : carHutCarFilterModel.getModels()
                        .stream()
                        .map(ModelsPostModel::getModel)
                        .filter(Objects::nonNull)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);

                String[] carTypes = carHutCarFilterModel.getCarTypes() == null
                        ? new String[0]
                        : carHutCarFilterModel.getCarTypes()
                        .stream()
                        .filter(Objects::nonNull)
                        .toArray(String[]::new);

                return carHutCarRepository.getNumberOfFilteredCars(dto.getBrandId(), dto.getModelId(),
                        dto.getPriceFrom(), dto.getPriceTo(), dto.getMileageFrom(), dto.getMileageTo(),
                        dto.getRegistrationFrom(), dto.getRegistrationTo(), dto.getSeatingConfig(), dto.getDoors(),
                        dto.getLocation(), dto.getFuelType(), dto.getPowerFrom(), dto.getPowerTo(),
                        dto.getDisplacementFrom(), dto.getDisplacementTo(), dto.getGearbox(), dto.getPowertrain(),
                        carTypes, brandIds, modelIds);
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
