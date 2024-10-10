package com.carhut.savedcarsservice.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import com.carhut.savedcarsservice.repository.SavedCarsByUsersRepository;
import com.carhut.savedcarsservice.repository.resourceprovider.SavedCarsDatabaseResourceManager;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carhut.savedcarsservice.requests.SaveCarRequestModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    private SavedCarsDatabaseResourceManager savedCarsDatabaseResourceManager =
            SavedCarsDatabaseResourceManager.getInstance();
    private RequestAuthenticationCaller authCaller = new RequestAuthenticationCaller();

    public CompletableFuture<List<CarHutCar>> getSavedCarsByUsername(String userId, String bearerToken) throws IOException, InterruptedException {
        CompletableFuture<List<CarHutCar>> cf = new CompletableFuture<>();
        if (userId == null) {
            cf.complete(null);
            return cf;
        }

        if (!isRequestAuthenticated(userId, bearerToken)) {
            cf.complete(null);
            return cf;
        }

        Function<Void, List<CarHutCar>> function = (unused -> {
           List<CarHutCar> list = savedCarsByUsersRepository.getSavedCarsByUserId(userId);
           return list;
        });

        return savedCarsDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<SavedCarsServiceStatus> addSavedCarByUser(SaveCarRequestModel savedCarByUser,
                                                                       String bearerToken) throws IOException, InterruptedException {
        CompletableFuture<SavedCarsServiceStatus> cf = new CompletableFuture<>();
        if (savedCarByUser == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if (!isRequestAuthenticated(savedCarByUser.getUserId(), bearerToken)) {
            cf.complete(SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED);
            return cf;
        }

        try {
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
        }
        catch (Exception e) {
            cf.complete(SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY);
            return cf;
        }

    }

    public CompletableFuture<SavedCarsServiceStatus> removeSavedCarByUsername(SaveCarRequestModel saveCarRequestModel,
                                                                              String bearerToken) throws IOException, InterruptedException {
        CompletableFuture<SavedCarsServiceStatus> cf = new CompletableFuture<>();
        if (saveCarRequestModel == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if (saveCarRequestModel.getCarId() == null || saveCarRequestModel.getUserId() == null) {
            cf.complete(SavedCarsServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if (!isRequestAuthenticated(saveCarRequestModel.getUserId(), bearerToken)) {
            cf.complete(SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED);
        }

        try {
            Function<Void, SavedCarsServiceStatus> function = (unused) -> {
                try {
                    savedCarsByUsersRepository.save(
                            new SavedCarByUser(
                                    SavedCarByUser.generateId(saveCarRequestModel.getUserId(), saveCarRequestModel.getCarId()),
                                    saveCarRequestModel.getUserId(), saveCarRequestModel.getCarId()
                            )
                    );
                } catch (Exception e) {
                    return SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_REMOVED_EXCEPTIONALLY;
                }
                return SavedCarsServiceStatus.SUCCESS;
            };
            return savedCarsDatabaseResourceManager.acquireDatabaseResource(function);
        }
        catch (Exception e) {
            cf.complete(SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_REMOVED_EXCEPTIONALLY);
            return cf;
        }
    }

    private boolean isRequestAuthenticated(String userId, String bearerToken) throws IOException, InterruptedException {
        return authCaller.isRequestAuthenticated(userId, bearerToken);
    }

    @VisibleForTesting
    void flush() {
        savedCarsByUsersRepository.deleteAll();
    }
}
