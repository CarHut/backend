package com.carhut.services.carhutapi;

import com.carhut.database.repository.carhutapi.CarHutCarRepository;
import com.carhut.database.repository.carhutapi.SavedCarsByUsersRepository;
import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveCarRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.User;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.services.security.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    @Autowired
    private UserCredentialsService userCredentialsService;
    @Autowired
    private CarHutCarRepository carHutCarRepository;

    @UserAccessCheck
    public List<CarHutCar> getSavedCarsByUsername(PrincipalRequest<SimpleUsernameRequestModel> principalRequest) {
        if (principalRequest == null) {
            return null;
        }

        if (principalRequest.getDto().getUsername() == null) {
            return null;
        }

        User user;
        try {
            user = (User) userCredentialsService.loadUserByUsername(principalRequest.getDto().getUsername());
            if (user == null) {
                return List.of();
            }
        }
        catch (Exception e) {
            return List.of();
        }

        List<SavedCarByUser> savedCars;
        try {
            savedCars = savedCarsByUsersRepository.getSavedCarsByUserId(user.getId());
        }
        catch (Exception e) {
            return List.of();
        }

        List<CarHutCar> cars = new ArrayList<>();

        for (SavedCarByUser savedCarByUser : savedCars) {
            try {
                cars.add(carHutCarRepository.getCarWithId(savedCarByUser.getCarId()));
            }
            catch (Exception e) {
                return null;
            }
        }

        return cars;
    }

    @UserAccessCheck
    public ServiceStatusEntity addSavedCarByUser(PrincipalRequest<SaveCarRequestModel> savedCarByUser) {
        if (savedCarByUser == null) {
            return ServiceStatusEntity.ERROR;
        }

        User user;
        try {
            user = (User) userCredentialsService.loadUserByUsername(savedCarByUser.getDto().getUsername());

            if (user == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        try {
            savedCarsByUsersRepository.save(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getDto().getCarId()), user.getId(), savedCarByUser.getDto().getCarId()));
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
        }

        return ServiceStatusEntity.SUCCESS;
    }

    @UserAccessCheck
    public ServiceStatusEntity removeSavedCarByUsername(PrincipalRequest<SaveCarRequestModel> saveCarRequestModelPrincipalRequest) {
        if (saveCarRequestModelPrincipalRequest == null) {
            return ServiceStatusEntity.ERROR;
        }

        if (saveCarRequestModelPrincipalRequest.getDto().getUsername() == null) {
            return ServiceStatusEntity.ERROR;
        }

        User user;
        try {
            user = (User) userCredentialsService.loadUserByUsername(saveCarRequestModelPrincipalRequest.getDto().getUsername());

            if (user == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        try {
            savedCarsByUsersRepository.delete(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), saveCarRequestModelPrincipalRequest.getDto().getCarId()), user.getId(), saveCarRequestModelPrincipalRequest.getDto().getCarId()));
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY;
        }

        return ServiceStatusEntity.SUCCESS;
    }

    public void flush() {
        savedCarsByUsersRepository.deleteAll();
    }
}
