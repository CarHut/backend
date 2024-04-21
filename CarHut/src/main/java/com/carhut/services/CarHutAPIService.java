package com.carhut.services;

import com.carhut.database.repository.BrandRepository;
import com.carhut.database.repository.ModelRepository;
import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.models.AutobazarEUCarObject;
import com.carhut.models.Brand;
import com.carhut.models.Model;
import com.carhut.services.util.Filter;
import com.carhut.services.util.Sorter;
import com.carhut.temputils.models.ModelsPostModel;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class CarHutAPIService {

    private AutobazarEUCarRepository autobazarEUCarRepository;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;
    private TempCarRepository tempCarRepository;
    private Sorter sorter;

    @Autowired
    public CarHutAPIService(AutobazarEUCarRepository autobazarEUCarRepository, BrandRepository brandRepository, ModelRepository modelRepository, TempCarRepository tempCarRepository) {
        this.autobazarEUCarRepository = autobazarEUCarRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.tempCarRepository = tempCarRepository;
        this.sorter = new Sorter();
    }

    public List<AutobazarEUCarObject> getAllCars() {
        return autobazarEUCarRepository.getAllCars();
    }

    public List<Brand> getAllBrands() {
        return brandRepository.getAllBrands();
    }


    public List<Model> getModelsByBrand(String brand) {
        return modelRepository.getModelByBrandId(brand);
    }

    public List<Model> getModelsByBrandName(String brandName) {
        return modelRepository.getModelByBrandName(brandName);
    }

    public List<TempCarModel> getAllTempCars() {
        return tempCarRepository.getAllTempCars();
    }

    public List<TempCarModel> getAllTempCarsSorted(String sortBy, String sortOrder) {
        List<TempCarModel> cars = getAllTempCars();

        switch (sortBy) {
            case "PFL":
            case "PFH":
                return sorter.sortByPrice(cars, sortOrder);
            case "MFL":
            case "MFH":
                return sorter.sortByMileage(cars, sortOrder);
            case "SFL":
            case "SFH":
                return sorter.sortByPower(cars, sortOrder);
        }

        return cars;

    }

    public Integer getBrandIdFromBrandName(String brandName) {
        Integer result = brandRepository.getBrandIdFromBrandName(brandName);
        return result == null ? 136 : result;
    }

    public Integer getModelIdFromModelName(String modelName, int brandId) {
        Integer result = modelRepository.getModelIdByModelName(modelName, brandId);
        return result == null ? 2264 : result;
    }

    public List<TempCarModel> getTempCarsWithFilter(String brand, String model, String carType,
                                                    String priceFrom, String priceTo, String mileageFrom, String mileageTo,
                                                    String registration, String seatingConfig, String doors,
                                                    String location, String postalCode, String fuelType, String powerFrom,
                                                    String powerTo, String displacement, String gearbox, String powertrain,
                                                    String sortBy, String sortOrder, List<ModelsPostModel> models) {

        List<TempCarModel> filteredList = getAllTempCars();

        if (models != null) {
            if (!models.isEmpty()) {
                List<TempCarModel> resultList = new ArrayList<>();
                for (ModelsPostModel car : models) {
                    filteredList = getAllTempCars();
                    resultList.addAll(filterTempCarModels(car.getBrand(), car.getModel(), priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList));
                }

                return sortTempCars(sortBy, sortOrder, resultList);
            }
        }

        return filterTempCarModels(brand, model, priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList);
    }

    private List<TempCarModel> filterTempCarModels(String brand, String model, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType, String gearbox, String powertrain, String powerFrom, String powerTo, List<TempCarModel> filteredList) {
        Filter filter = new Filter();

        if (brand != null) {
            if (!brand.isEmpty()) {
                Integer brandId = getBrandIdFromBrandName(brand);
                filter.filterBrands(filteredList, brandId);
            }
        }

        if (model != null) {
            if (!model.isEmpty()) {
                Integer modelId = getModelIdFromModelName(model, getBrandIdFromBrandName(brand));
                filter.filterModels(filteredList, modelId);
            }
        }

        // carType coming soon

        if (priceFrom != null) {
            if (!priceFrom.isEmpty()) {
                filter.filterPriceFrom(filteredList, priceFrom);
            }
        }

        if (priceTo != null) {
            if (!priceTo.isEmpty()) {
                filter.filterPriceTo(filteredList, priceTo);
            }
        }

        if (mileageFrom != null) {
            if (!mileageFrom.isEmpty()) {
                filter.filterMileageFrom(filteredList, mileageFrom);
            }
        }

        if (mileageTo != null) {
            if (!mileageTo.isEmpty()) {
                filter.filterMileageTo(filteredList, mileageTo);
            }
        }

        if (fuelType != null) {
            if (!fuelType.isEmpty()) {
                filter.filterFuelType(filteredList, fuelType);
            }
        }

        if (gearbox != null) {
            if (!gearbox.isEmpty()) {
                filter.filterGearbox(filteredList, gearbox);
            }
        }

        if (powertrain != null) {
            if (!powertrain.isEmpty()) {
                filter.filterPowertrain(filteredList, powertrain);
            }
        }

        if (powerFrom != null) {
            if (!powerFrom.isEmpty()) {
                filter.filterPowerFrom(filteredList, powerFrom);
            }
        }

        if (powerTo != null) {
            if (!powerTo.isEmpty()) {
                filter.filterPowerTo(filteredList, powerTo);
            }
        }

        return filteredList;
    }

    private List<TempCarModel> sortTempCars(String sortBy, String sortOrder, List<TempCarModel> filteredList) {
        List<TempCarModel> sortedList = filteredList;

        if (sortBy != null && sortOrder != null) {
            if (!sortBy.isEmpty() && !sortOrder.isEmpty()) {
                sortedList = switch (sortBy) {
                    case "PFL", "PFH" -> sorter.sortByPrice(filteredList, sortOrder);
                    case "MFL", "MFH" -> sorter.sortByMileage(filteredList, sortOrder);
                    case "AFL", "AFH" -> sorter.sortByAlphabet(filteredList, sortOrder);
                    case "SFL", "SFH" -> sorter.sortByPower(filteredList, sortOrder);
                    default -> sortedList;
                };
            }
        }

        return sortedList;
    }

    public int getNumberOfTempFilteredCars(String brand, String model, String carType,
                                                    String priceFrom, String priceTo, String mileageFrom, String mileageTo,
                                                    String registration, String seatingConfig, String doors,
                                                    String location, String postalCode, String fuelType, String powerFrom,
                                                    String powerTo, String displacement, String gearbox, String powertrain)
    {

        List<TempCarModel> filteredList = getAllTempCars();

        return filterTempCarModels(brand, model, priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList).size();
    }




    public void updateBrand(String brand, String id) {
        tempCarRepository.updateBrand(getBrandIdFromBrandName(brand), id);
    }

    public void updateModel(String model, String id) {
        tempCarRepository.updateModel(getModelIdFromModelName(model, 6999999), id);
    }

    public TempCarModel getTempCarWithId(String carId) {
        return tempCarRepository.getTempCarWithId(carId);
    }
}
