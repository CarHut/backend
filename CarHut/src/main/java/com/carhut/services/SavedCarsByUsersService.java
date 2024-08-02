package com.carhut.services;

import com.carhut.database.repository.CarHutCarRepository;
import com.carhut.database.repository.SavedCarsByUsersRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.models.security.User;
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

    public List<CarHutCar> getSavedCarsByUsername(String username) throws UserCredentialsNotFoundException, SavedCarsNotFoundException, CarHutAPICarNotFoundException, CarHutAuthenticationException {
        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(username);
        }
        catch (Exception e) {
            throw new UserCredentialsNotFoundException("Error occurred while getting user credentials. Message: " + e.getMessage());
        }

        if (user == null) {
            return List.of();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurityContextHolder = ((User)authentication.getPrincipal());

        if (!userSecurityContextHolder.getUsername().equals(username)) {
            throw new CarHutAuthenticationException("Unauthorized access to user data.");
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

    public RequestStatusEntity addSavedCarByUser(SavedCarByUser savedCarByUser) throws UserCredentialsNotFoundException, SavedCarsCanNotBeSavedException, CarHutAuthenticationException {
        if (savedCarByUser == null) {
            return RequestStatusEntity.ERROR;
        }

        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(savedCarByUser.getUserId());
        }
        catch (Exception e) {
            throw new UserCredentialsNotFoundException("Error occurred while getting user credentials. Message: " + e.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurityContextHolder = ((User)authentication.getPrincipal());

        if (!userSecurityContextHolder.getUsername().equals(user.getUsername())) {
            throw new CarHutAuthenticationException("Unauthorized access to user data.");
        }

        try {
            savedCarsByUsersRepository.save(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getCarId()), user.getId(), savedCarByUser.getCarId()));
        }
        catch (Exception e) {
            throw new SavedCarsCanNotBeSavedException("Error occurred while saving car to database. Message: " + e.getMessage());
        }

        return RequestStatusEntity.SUCCESS;
    }


    public RequestStatusEntity removeSavedCarByUsername(SavedCarByUser savedCarByUser) throws UserCredentialsNotFoundException, SavedCarsCanNotBeDeletedException, CarHutAuthenticationException {
        User user;
        try {
            user = userCredentialsRepository.findUserByUsername(savedCarByUser.getUserId());
        }
        catch (Exception e) {
            throw new UserCredentialsNotFoundException("Error occurred while getting user credentials. Message: " + e.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurityContextHolder = ((User)authentication.getPrincipal());

        if (!userSecurityContextHolder.getUsername().equals(user.getUsername())) {
            throw new CarHutAuthenticationException("Unauthorized access to user data.");
        }

        try {
            savedCarsByUsersRepository.delete(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getCarId()), user.getId(), savedCarByUser.getCarId()));
        }
        catch (Exception e) {
            throw new SavedCarsCanNotBeDeletedException("Error occurred while deleting saved car from database. Message: " + e.getMessage());
        }

        return RequestStatusEntity.SUCCESS;
    }
}
