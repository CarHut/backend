package com.carhut.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.commons.model.CarHutCar;
import com.carhut.http.CarHutApiSRCaller;
import com.carhut.http.ImageServiceCaller;
import com.carhut.http.UserServiceCaller;
import com.carhut.repository.*;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.models.enums.BodyType;
import com.carhut.models.enums.Fuel;
import com.carhut.models.enums.Gearbox;
import com.carhut.models.enums.Powertrain;
import com.carhut.requests.requestmodels.CarHutCarFilterModel;
import com.carhut.requests.requestmodels.RemoveCarRequestModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpResponse;
import java.sql.Date;
import java.util.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;


@Service
public class CarHutAPIService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private FeatureRepository featureRepository;
    private final BodyType bodyType = new BodyType();
    private final Gearbox gearbox = new Gearbox();
    private final Powertrain powertrain = new Powertrain();
    private final Fuel fuel = new Fuel();
    private final RequestAuthenticationCaller requestAuthenticationCaller = new RequestAuthenticationCaller();
    private final CarHutApiSRCaller carHutApiSRCaller = new CarHutApiSRCaller();
    private final UserServiceCaller userServiceCaller = new UserServiceCaller();
    private final ImageServiceCaller imageServiceCaller = new ImageServiceCaller();

    public CarHutAPIService() {}

    public List<Brand> getAllBrands() {
        try {
            return brandRepository.getAllBrands();
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Model> getModelsByBrandName(String brandName) {
        try {
            return modelRepository.getModelByBrandName(brandName);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Integer getBrandIdFromBrandName(String brandName) {
        try {
            Integer result = brandRepository.getBrandIdFromBrandName(brandName);
            return result == null ? 136 : result;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Integer getModelIdFromModelName(String modelName, int brandId) {
        try {
            Integer result = modelRepository.getModelIdByModelName(modelName, brandId);
            return result == null ? 2264 : result;
        }
        catch (Exception e) {
            return null;
        }
    }

    public CompletableFuture<CarHutCar> getCarWithId(String carId) throws URISyntaxException {
        return carHutApiSRCaller.getCarWithId(carId);
    }

    public CompletableFuture<List<CarHutCar>> getCarsWithFilter(CarHutCarFilterModel carHutCarFilterModel,
                                                                String sortBy, String sortOrder, Integer offersPerPage,
                                                                Integer currentPage) throws URISyntaxException {
        return carHutApiSRCaller.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, currentPage);
    }

    public CompletableFuture<Integer> getNumberOfFilteredCars(CarHutCarFilterModel carHutCarFilterModel) throws URISyntaxException {
        return carHutApiSRCaller.getNumberOfFilteredCars(carHutCarFilterModel);
    }

    public List<String> getBodyTypes() {
        return bodyType.getBodyTypes();
    }

    public List<String> getFuelTypes() {
        return fuel.getFuelTypes();
    }

    public List<String> getPowertrainTypes() {
        return powertrain.getPowertrainTypes();
    }

    public List<String> getGearboxTypes() {
        return gearbox.getGearboxTypes();
    }

    public List<Color> getColors() {
        try {
            return colorRepository.getColors();
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Feature> getFeatures() {
        try {
            return featureRepository.getFeaturesAsc();
        }
        catch (Exception e) {
            return null;
        }
    }

    public CompletableFuture<String> addCarToDatabase(CarHutCar carHutCar, List<MultipartFile> images, String bearerToken) {
        CarHutCar newCar = new CarHutCar(carHutCar.getSellerId(), carHutCar.getSellerAddress(),
                carHutCar.getBrandId(), carHutCar.getModelId(), carHutCar.getHeader(),
                carHutCar.getPrice(), carHutCar.getMileage(), carHutCar.getRegistration(),
                carHutCar.getEnginePower(), carHutCar.getEngineDisplacement(), carHutCar.getFuel(),
                carHutCar.getFuelConsumptionAvg(), carHutCar.getFuelConsumptionCity(), carHutCar.getFuelConsumptionHighway(),
                carHutCar.getGearbox(), carHutCar.getGearboxGears(), carHutCar.getBodyType(),
                carHutCar.getPowertrain(), carHutCar.getDescription(), carHutCar.getBaseImgPath(),
                carHutCar.getPreviousOwners(), carHutCar.getEnergyEffClass(), carHutCar.getSeats(),
                carHutCar.getDoors(), carHutCar.getEmissionClass(), colorRepository.getColorIdByColorName(carHutCar.getExteriorColorId()),
                colorRepository.getColorIdByColorName(carHutCar.getInteriorColorId()), carHutCar.getDamageStatus(),
                carHutCar.isParkingSensors(), carHutCar.isParkingCameras(), carHutCar.getCountryOfOrigin(),
                carHutCar.getTechnicalInspectionDate(), carHutCar.getEmissionInspectionDate(), carHutCar.getFeatures(),
                new Date(System.currentTimeMillis()));

        CompletableFuture<String> resultCf = new CompletableFuture<>();

        try {
            CompletableFuture<HttpResponse<String>> authenticationCf =
                    isRequestAuthenticatedAsync(carHutCar.getSellerId(), bearerToken, resultCf);

            CompletableFuture<HttpResponse<String>> addCarToDatabaseCf =
                    authenticationCf.thenCompose(authResponse -> {
                        CompletableFuture<HttpResponse<String>> failedFuture = new CompletableFuture<>();
                        if (authResponse != null && authResponse.statusCode() == 200) {
                            try {
                                return carHutApiSRCaller.addCarToDatabaseAsync(newCar);
                            } catch (Exception e) {
                                resultCf.complete(null);
                                failedFuture.completeExceptionally(new RuntimeException("Exception occurred while saving CarHutCar model to database."));
                                return failedFuture;
                            }
                        } else {
                            resultCf.complete(null);
                            failedFuture.completeExceptionally(new RuntimeException("Authentication failed"));
                            return failedFuture;
                        }
                    });

            addCarToDatabaseCf.whenComplete((result, ex) -> {
                if (ex != null) {
                    resultCf.complete(null);
                } else if (result.statusCode() < 200 || result.statusCode() > 299) {
                    resultCf.complete(null);
                }
            });

            CompletableFuture<HttpResponse<String>> userServiceUpdateOffersCf =
                    addCarToDatabaseCf.thenCompose(carHutApiResponse -> {
                        CompletableFuture<HttpResponse<String>> failedFuture = new CompletableFuture<>();
                        if (carHutApiResponse != null && carHutApiResponse.statusCode() == 200) {
                            try {
                                return userServiceCaller.updateOffersCountForUserWithId(newCar.getSellerId(),
                                        bearerToken);
                            } catch (Exception e) {
                                resultCf.complete(null);
                                failedFuture.completeExceptionally(new RuntimeException("User service could not update number of offers."));
                                return failedFuture;
                            }
                        } else {
                            resultCf.complete(null);
                            failedFuture.completeExceptionally(new RuntimeException("CarHutApi did not add new car."));
                            return failedFuture;
                        }
            });

            userServiceUpdateOffersCf.whenComplete((result, ex) -> {
                if (ex != null) {
                    resultCf.complete(null);
                } else if (result.statusCode() < 200 || result.statusCode() > 299) {
                    resultCf.complete(null);
                }
            });

            CompletableFuture<HttpResponse<String>> saveImagesCf = userServiceUpdateOffersCf
                    .thenCompose(userServiceResponse -> {
                        CompletableFuture<HttpResponse<String>> failedFuture = new CompletableFuture<>();
                        if (userServiceResponse != null && userServiceResponse.statusCode() == 200) {
                            try {
                                return imageServiceCaller.saveImages(newCar, images);
                            } catch (Exception e) {
                                resultCf.complete(null);
                                failedFuture.completeExceptionally(new RuntimeException("Image service could not save images."));
                                return failedFuture;
                            }
                        } else {
                            resultCf.complete(null);
                            failedFuture.completeExceptionally(new RuntimeException("User service could not update car offers number for user."));
                            return failedFuture;
                        }
                    });

            saveImagesCf.whenComplete((result, ex) -> {
                if (ex != null) {
                    resultCf.complete(null);
                } else if (result.statusCode() < 200 || result.statusCode() > 299) {
                    resultCf.complete(null);
                } else {
                    resultCf.complete(newCar.getId());
                }
            });

            return resultCf;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Integer getFeatureIdByFeatureName(String feature) {
        if (feature == null) {
            return null;
        }

        try {
            return featureRepository.getFeatureIdByFeatureName(feature);
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getColorStringNameFromColorId(String colorId) {
        if (colorId == null) {
            return null;
        }

        try {
            return colorRepository.getColorStringNameFromColorId(colorId);
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getFeatureNameByFeatureId(Integer featureId) {
        if (featureId == null) {
            return null;
        }

        try {
            return featureRepository.getFeatureNameByFeatureId(featureId);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<String> getMultipleFeaturesByIds(List<Integer> featureIds) {
        if (featureIds == null) {
            return null;
        }

        List<String> featureNames = new ArrayList<>();
        for (Integer id : featureIds) {
            String featureName;
            try {
                featureName = getFeatureNameByFeatureId(id);
                if (featureName != null) {
                    featureNames.add(featureName);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return featureNames;
    }

    public CompletableFuture<List<CarHutCar>> getMyListings(String userId, String bearerToken) {
        if (userId == null || bearerToken == null) {
            return null;
        }

        CompletableFuture<List<CarHutCar>> resultCf = new CompletableFuture<>();

        CompletableFuture<HttpResponse<String>> authResponse = isRequestAuthenticatedAsync(userId, bearerToken,
                resultCf);

        CompletableFuture<HttpResponse<String>> carHutApiResponse =
                authResponse.thenCompose(response -> {
                    CompletableFuture<HttpResponse<String>> failedFuture = new CompletableFuture<>();
                    if (response != null && response.statusCode() == 200) {
                        try {
                            return carHutApiSRCaller.getListingsWithUserId(userId);
                        } catch (Exception e) {
                            resultCf.complete(null);
                            failedFuture.completeExceptionally(new RuntimeException("CarHutApi could not get listings with user id."));
                            return failedFuture;
                        }
                    } else {
                        resultCf.complete(null);
                        failedFuture.completeExceptionally(new RuntimeException("User not authenticated."));
                        return failedFuture;
                    }
                });

        carHutApiResponse.whenComplete((result, ex) -> {
            if (ex != null) {
                resultCf.complete(null);
            } else if (result.statusCode() < 200 || result.statusCode() > 299) {
                resultCf.complete(null);
            } else {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<CarHutCar> carListings = objectMapper.readValue(result.body(), new TypeReference<>() {});
                    resultCf.complete(carListings);
                } catch (Exception e) {
                    resultCf.complete(null);
                }
            }
        });

        return resultCf;
    }

    public CompletableFuture<ServiceStatusEntity> removeOffer(RemoveCarRequestModel removeCarRequestModel, String bearerToken) {

        CompletableFuture<ServiceStatusEntity> cf = new CompletableFuture<>();

        if (removeCarRequestModel == null) {
            cf.complete(ServiceStatusEntity.ERROR);
            return cf;
        }

        if (removeCarRequestModel.getCarId() == null) {
            cf.complete(ServiceStatusEntity.ERROR);
            return cf;
        }

        CompletableFuture<ServiceStatusEntity> resultCf = new CompletableFuture<>();

        CompletableFuture<HttpResponse<String>> authResponse = isRequestAuthenticatedAsync(removeCarRequestModel.getUserId(),
                bearerToken, resultCf);

        CompletableFuture<HttpResponse<String>> carHutApiResponse =
                authResponse.thenCompose(response -> {
                    CompletableFuture<HttpResponse<String>> failedFuture = new CompletableFuture<>();
                    if (response != null && response.statusCode() == 200) {
                        try {
                            return carHutApiSRCaller.removeOffer(removeCarRequestModel);
                        } catch (Exception e) {
                            resultCf.complete(null);
                            failedFuture.completeExceptionally(new RuntimeException("CarHutApi cannot remove car offer."));
                            return failedFuture;
                        }
                    } else {
                        resultCf.complete(null);
                        failedFuture.completeExceptionally(new RuntimeException("User not authenticated."));
                        return failedFuture;
                    }
                });

        carHutApiResponse.whenComplete((result, ex) -> {
            if (ex != null) {
                resultCf.complete(null);
            } else if (result.statusCode() < 200 || result.statusCode() > 299) {
                resultCf.complete(null);
            } else {
                resultCf.complete(ServiceStatusEntity.SUCCESS);
            }
        });

        return resultCf;
    }

    private <T> CompletableFuture<HttpResponse<String>> isRequestAuthenticatedAsync(String userId, String bearerToken,
                                                                                CompletableFuture<T> finalizable) {
        CompletableFuture<HttpResponse<String>> response = requestAuthenticationCaller
                .isRequestAuthenticatedAsync(userId, bearerToken);

        response.whenComplete((result, ex) -> {
           if (ex != null) {
               finalizable.complete(null);
           } else if (result.statusCode() < 200 || result.statusCode() > 299){
               finalizable.complete(null);
           }
        });

        return response;
    }

}
