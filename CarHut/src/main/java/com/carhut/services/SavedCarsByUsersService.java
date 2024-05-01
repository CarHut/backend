package com.carhut.services;

import com.carhut.database.repository.CarHutCarRepository;
import com.carhut.database.repository.SavedCarsByUsersRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.models.security.User;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    @Autowired
    private TempCarRepository tempCarRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private CarHutCarRepository carHutCarRepository;

    @Deprecated
    public List<TempCarModel> getSavedTempCarsByUserUsername(String username) {
        User user = userCredentialsRepository.findUserByUsername(username);
        List<SavedCarByUser> savedCars = savedCarsByUsersRepository.getSavedCarsByUserId(user.getId());
        List<TempCarModel> cars = new ArrayList<>();

        for (SavedCarByUser savedCarByUser : savedCars) {
            cars.add(tempCarRepository.getTempCarWithId(savedCarByUser.getCarId()));
        }

        return cars;
    }

    public List<CarHutCar> getSavedCarsByUsername(String username) {
        User user = userCredentialsRepository.findUserByUsername(username);
        if (user == null) {
            return List.of();
        }
        List<SavedCarByUser> savedCars = savedCarsByUsersRepository.getSavedCarsByUserId(user.getId());
        List<CarHutCar> cars = new ArrayList<>();

        for (SavedCarByUser savedCarByUser : savedCars) {
            cars.add(carHutCarRepository.getCarWithId(savedCarByUser.getCarId()));
        }

        return cars;
    }

    public RequestStatusEntity addSavedCarByUser(SavedCarByUser savedCarByUser) {
        User user = userCredentialsRepository.findUserByUsername(savedCarByUser.getUserId());
        savedCarsByUsersRepository.save(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getCarId()), user.getId(), savedCarByUser.getCarId()));
        return RequestStatusEntity.SUCCESS;
    }


    public RequestStatusEntity removeSavedCarByUsername(SavedCarByUser savedCarByUser) {
        User user = userCredentialsRepository.findUserByUsername(savedCarByUser.getUserId());
        savedCarsByUsersRepository.delete(new SavedCarByUser(SavedCarByUser.generateId(user.getId(), savedCarByUser.getCarId()), user.getId(), savedCarByUser.getCarId()));
        return RequestStatusEntity.SUCCESS;
    }
}
