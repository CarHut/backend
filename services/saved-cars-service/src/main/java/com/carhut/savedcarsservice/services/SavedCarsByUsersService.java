package com.carhut.savedcarsservice.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.commons.model.CarHutCar;
import com.carhut.savedcarsservice.http.caller.CarHutApiCaller;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import com.carhut.savedcarsservice.repository.SavedCarsByUsersRepository;
import com.carhut.savedcarsservice.repository.resourceprovider.SavedCarsDatabaseResourceManager;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carhut.savedcarsservice.requests.SaveCarRequestModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    private SavedCarsDatabaseResourceManager savedCarsDatabaseResourceManager =
            SavedCarsDatabaseResourceManager.getInstance();
    private RequestAuthenticationCaller requestAuthenticationCaller = new RequestAuthenticationCaller();
    private final CarHutApiCaller carHutApiCaller = new CarHutApiCaller();

    public CompletableFuture<List<CarHutCar>> getSavedCarsByUserId(String userId, String bearerToken) {
        CompletableFuture<List<CarHutCar>> cf = new CompletableFuture<>();
        if (userId == null) {
            cf.complete(null);
            return cf;
        }

        // Check if user is authorized
        CompletableFuture<HttpResponse<String>> authRequest = isRequestAuthenticatedAsync(userId, bearerToken, cf);
        // Fetch saved cars by users
        CompletableFuture<List<SavedCarByUser>> serviceRequest = authRequest.thenCompose(authResponse -> {
            Function<Void, List<SavedCarByUser>> function = (unused -> {
                List<SavedCarByUser> list = savedCarsByUsersRepository.getSavedCarsByUserId(userId);
                return list;
            });

            return savedCarsDatabaseResourceManager.acquireDatabaseResource(function);
        });
        serviceRequest.whenComplete((result, ex) -> {
           if (ex != null) {
               cf.complete(null);
           } else if (result == null) {
               cf.complete(null);
           }
        });

        CompletableFuture<HttpResponse<String>> carHutApiRequest = serviceRequest.thenCompose(serviceResponse -> {
            try {
                return carHutApiCaller.getCarsByCarIds(serviceResponse, userId, bearerToken);
            } catch (URISyntaxException e) {
                return null;
            }
        });

        carHutApiRequest.whenComplete((result, ex) -> {
            if (ex != null) {
                cf.complete(null);
            } else if (result == null) {
                cf.complete(null);
            } else {
                if (result.statusCode() != 200) {
                    cf.complete(null);
                } else {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<CarHutCar> cars = mapper.readValue(result.body(), new TypeReference<>() {});
                        cf.complete(cars);
                    } catch (Exception e) {
                        cf.complete(null);
                    }
                }
            }
        });

        return cf;
    }

    public CompletableFuture<SavedCarsServiceStatus> addSavedCarByUser(SaveCarRequestModel savedCarByUser,
                                                                       String bearerToken) throws IOException, InterruptedException {
        CompletableFuture<SavedCarsServiceStatus> cf = new CompletableFuture<>();
        if (savedCarByUser == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        CompletableFuture<HttpResponse<String>> authRequest =
                isRequestAuthenticatedAsync(savedCarByUser.getUserId(), bearerToken, cf);
        CompletableFuture<SavedCarsServiceStatus> serviceRequest = authRequest.thenCompose(authResponse -> {
            Function<Void, SavedCarsServiceStatus> function = (unused) -> {
                try {
                    savedCarsByUsersRepository.save(
                            new SavedCarByUser(
                                    SavedCarByUser.generateId(savedCarByUser.getUserId(), savedCarByUser.getCarId()),
                                    savedCarByUser.getUserId(), savedCarByUser.getCarId()
                            )
                    );
                } catch (Exception e) {
                    return SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
                }
                return SavedCarsServiceStatus.SUCCESS;
            };
            return savedCarsDatabaseResourceManager.acquireDatabaseResource(function);
        });

        serviceRequest.whenComplete((result, ex) -> {
            if (ex != null) {
                cf.complete(null);
            } else {
                cf.complete(result);
            }
        });

        return cf;
    }

    public CompletableFuture<SavedCarsServiceStatus> removeSavedCarByUsername(SaveCarRequestModel saveCarRequestModel,
                                                                              String bearerToken) {
        CompletableFuture<SavedCarsServiceStatus> cf = new CompletableFuture<>();
        if (saveCarRequestModel == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if (saveCarRequestModel.getCarId() == null || saveCarRequestModel.getUserId() == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        CompletableFuture<HttpResponse<String>> authRequest =
                isRequestAuthenticatedAsync(saveCarRequestModel.getUserId(), bearerToken, cf);
        CompletableFuture<SavedCarsServiceStatus> serviceRequest = authRequest.thenCompose(authResponse -> {
            Function<Void, SavedCarsServiceStatus> function = (unused) -> {
                try {
                    SavedCarByUser savedCarByUser = savedCarsByUsersRepository
                            .getSavedCarByByCarId(saveCarRequestModel.getCarId());
                    if (savedCarByUser == null) {
                        return SavedCarsServiceStatus.OBJECT_IS_NULL;
                    }
                    savedCarsByUsersRepository.delete(savedCarByUser);
                } catch (Exception e) {
                    return SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_REMOVED_EXCEPTIONALLY;
                }
                return SavedCarsServiceStatus.SUCCESS;
            };

            return savedCarsDatabaseResourceManager.acquireDatabaseResource(function);
        });

        serviceRequest.whenComplete((result, ex) -> {
            if (ex != null) {
                cf.complete(null);
            } else {
                cf.complete(result);
            }
        });

        return cf;
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
