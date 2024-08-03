package com.carhut.services;

import com.carhut.database.repository.CarHutCarRepository;
import com.carhut.database.repository.SavedCarsByUsersRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveCarRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.User;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.carhutapi.CarHutAPICarNotFoundException;
import com.carhut.util.exceptions.savedcars.SavedCarsCanNotBeDeletedException;
import com.carhut.util.exceptions.savedcars.SavedCarsCanNotBeSavedException;
import com.carhut.util.exceptions.savedcars.SavedCarsNotFoundException;
import com.carhut.util.exceptions.usercredentials.UserCredentialsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private CarHutCarRepository carHutCarRepository;

    @UserAccessCheck
    public List<CarHutCar> getSavedCarsByUsername(PrincipalRequest<SimpleUsernameRequestModel> principalRequest) throws UserCredentialsNotFoundException, SavedCarsNotFoundException, CarHutAPICarNotFoundException, CarHutAuthenticationException {
        if (principalRequest.getDto().getUsername() == null) {
            return null;
        }

        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(principalRequest.getDto().getUsername());
        }
        catch (Exception e) {
            return null;
        }

        if (user == null) {
            return List.of();
        }

        List<SavedCarByUser> savedCars;
        try {
            savedCars = savedCarsByUsersRepository.getSavedCarsByUserId(user.getId());
        }
        catch (Exception e) {
            throw new SavedCarsNotFoundException("Error occurred while getting saved cars. Message: " + e.getMessage());
        }

        List<CarHutCar> cars = new ArrayList<>();

        for (SavedCarByUser savedCarByUser : savedCars) {
            try {
                cars.add(carHutCarRepository.getCarWithId(savedCarByUser.getCarId()));
            }
            catch (Exception e) {
                throw new CarHutAPICarNotFoundException("Error occurred while getting car from database. Message: " + e.getMessage());
            }
        }

        return cars;
    }

    @UserAccessCheck
    public RequestStatusEntity addSavedCarByUser(PrincipalRequest<SaveCarRequestModel> savedCarByUser) throws UserCredentialsNotFoundException, SavedCarsCanNotBeSavedException, CarHutAuthenticationException {
        if (savedCarByUser == null) {
            return RequestStatusEntity.ERROR;
        }

        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(savedCarByUser.getDto().getUsername());

            if (user == null) {
                return RequestStatusEntity.ERROR;
            }
        }
        catch (Exception e) {
            return RequestStatusEntity.ERROR;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userSecurityContextHolder = ((User)authentication.getPrincipal());

            if (!userSecurityContextHolder.getUsername().equals(user.getUsername())) {
                return RequestStatusEntity.ERROR;
            }
        } catch (Exception e) {
            return RequestStatusEntity.ERROR;
        }

        try {
            savedCarsByUsersRepository.save(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getDto().getCarId()), user.getId(), savedCarByUser.getDto().getCarId()));
        }
        catch (Exception e) {
            throw new SavedCarsCanNotBeSavedException("Error occurred while saving car to database. Message: " + e.getMessage());
        }

        return RequestStatusEntity.SUCCESS;
    }

    @UserAccessCheck
    public RequestStatusEntity removeSavedCarByUsername(PrincipalRequest<SaveCarRequestModel> saveCarRequestModelPrincipalRequest) throws UserCredentialsNotFoundException, SavedCarsCanNotBeDeletedException, CarHutAuthenticationException {
        if (saveCarRequestModelPrincipalRequest.getDto().getUsername() == null) {
            return RequestStatusEntity.ERROR;
        }

        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(saveCarRequestModelPrincipalRequest.getDto().getUsername());
        }
        catch (Exception e) {
            return RequestStatusEntity.ERROR;
        }

        try {
            savedCarsByUsersRepository.delete(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), saveCarRequestModelPrincipalRequest.getDto().getCarId()), user.getId(), saveCarRequestModelPrincipalRequest.getDto().getCarId()));
        }
        catch (Exception e) {
            throw new SavedCarsCanNotBeDeletedException("Error occurred while deleting saved car from database. Message: " + e.getMessage());
        }

        return RequestStatusEntity.SUCCESS;
    }

    public void flush() {
        savedCarsByUsersRepository.deleteAll();
    }
}
