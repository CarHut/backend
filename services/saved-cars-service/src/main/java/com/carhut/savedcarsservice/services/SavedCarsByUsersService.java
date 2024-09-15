package com.carhut.savedcarsservice.services;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import com.carhut.savedcarsservice.repositories.SavedCarsByUsersRepository;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.carhut.security.annotations.UserAccessCheck;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carhut.savedcarsservice.requests.GetSavedCarsByUserIdModel;
import com.carhut.savedcarsservice.requests.SaveCarRequestModel;

import java.util.List;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;

    @UserAccessCheck
    public List<CarHutCar> getSavedCarsByUsername(GetSavedCarsByUserIdModel idModel) {
        if (idModel == null) {
            return null;
        }

        return savedCarsByUsersRepository.getSavedCarsByUserId(idModel.getId());
    }

    @UserAccessCheck
    public SavedCarsServiceStatus addSavedCarByUser(SaveCarRequestModel savedCarByUser) {
        if (savedCarByUser == null) {
            return SavedCarsServiceStatus.OBJECT_IS_NULL;
        }

        try {
            savedCarsByUsersRepository.save(
                    new SavedCarByUser(
                            SavedCarByUser.generateId(savedCarByUser.getUserId(), savedCarByUser.getCarId()),
                            savedCarByUser.getUserId(), savedCarByUser.getCarId()
                    )
            );
        }
        catch (Exception e) {
            return SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
        }

        return SavedCarsServiceStatus.SUCCESS;
    }

    @UserAccessCheck
    public SavedCarsServiceStatus removeSavedCarByUsername(SaveCarRequestModel saveCarRequestModel) {
        if (saveCarRequestModel == null) {
            return SavedCarsServiceStatus.OBJECT_IS_NULL;
        }

        if (saveCarRequestModel.getCarId() == null || saveCarRequestModel.getUserId() == null) {
            return SavedCarsServiceStatus.OBJECT_IS_NULL;
        }

        try {
            savedCarsByUsersRepository.save(
                    new SavedCarByUser(
                            SavedCarByUser.generateId(saveCarRequestModel.getUserId(), saveCarRequestModel.getCarId()),
                            saveCarRequestModel.getUserId(), saveCarRequestModel.getCarId()
                    )
            );
        }
        catch (Exception e) {
            return SavedCarsServiceStatus.OBJECT_COULD_NOT_BE_REMOVED_EXCEPTIONALLY;
        }

        return SavedCarsServiceStatus.SUCCESS;
    }

    @VisibleForTesting
    void flush() {
        savedCarsByUsersRepository.deleteAll();
    }
}
