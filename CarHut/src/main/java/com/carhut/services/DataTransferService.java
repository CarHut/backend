package com.carhut.services;

import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Deprecated
@Service
public class DataTransferService {

    private AutobazarEUCarRepository repository;
    private TempCarRepository tempCarRepository;

    @Autowired
    public DataTransferService(AutobazarEUCarRepository repository, TempCarRepository tempCarRepository) {
        this.repository = repository;
        this.tempCarRepository = tempCarRepository;
    }

    public void saveCar(AutobazarEUCarObject carObject) {
        repository.save(carObject);
    }

    public List<AutobazarEUCarObject> getAllCars() { return repository.getAllCars(); }

    public void transferCarsFromAutobazarEUToTmpCarList() {
        List<AutobazarEUCarObject> list = getAllCars();

        for (AutobazarEUCarObject car : list) {
            tempCarRepository.save(new TempCarModel(car.getId(), car.getMileage(), car.getModelYear(),
                    car.getEnginePower(), car.getFuel(), car.getBodywork(), car.getTransmission(),
                    car.getPowerTrain(), "ca. 1.0l/100km", "Default owner", car.getLocation(),
                    car.getPrice(), "/null", 136,1, car.getHeader()));
            System.out.println("Saving car: " + car.getHeader());
        }

        System.out.println("Successfully saved all cars to new table.");
    }



}