package com.carhut.services;

import com.carhut.database.repository.SavedCarsByUsersRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.SavedCarByUser;
import com.carhut.models.User;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedCarsByUsersService {

    @Autowired
    private SavedCarsByUsersRepository savedCarsByUsersRepository;
    @Autowired
    private TempCarRepository tempCarRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public List<TempCarModel> getSavedCarsByUserUsername(String username) {
        User user = userCredentialsRepository.findUserByUsername(username);
        List<SavedCarByUser> savedCars = savedCarsByUsersRepository.getSavedCarsByUserId(user.getId());
        List<TempCarModel> cars = new ArrayList<>();

        for (SavedCarByUser savedCarByUser : savedCars) {
            cars.add(tempCarRepository.getTempCarWithId(savedCarByUser.getCarId()));
        }

        return cars;
    }

    private int getSizeOfSavedCars() {
        return savedCarsByUsersRepository.getSizeOfSavedCars();
    }

    public RequestStatusEntity addSavedCarByUser(SavedCarByUser savedCarByUser) {
        User user = userCredentialsRepository.findUserByUsername(savedCarByUser.getUserId());
        savedCarsByUsersRepository.save(new SavedCarByUser(getSizeOfSavedCars() + 1, user.getId(), savedCarByUser.getCarId()));
        return RequestStatusEntity.SUCCESS;
    }
}
