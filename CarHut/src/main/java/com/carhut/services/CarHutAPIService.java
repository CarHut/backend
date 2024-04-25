package com.carhut.services;

import com.carhut.database.repository.BrandRepository;
import com.carhut.database.repository.CarHutCarRepository;
import com.carhut.database.repository.ColorRepository;
import com.carhut.database.repository.ModelRepository;
import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.models.carhut.*;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.models.enums.BodyType;
import com.carhut.models.enums.Fuel;
import com.carhut.models.enums.Gearbox;
import com.carhut.models.enums.Powertrain;
import com.carhut.services.util.Filter;
import com.carhut.services.util.Sorter;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CarHutAPIService {

    private final BodyType bodyType = new BodyType();
    private final Gearbox gearbox = new Gearbox();
    private final Powertrain powertrain = new Powertrain();
    private final Fuel fuel = new Fuel();
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;
    private Sorter sorter;
    @Autowired
    private CarHutCarRepository carHutCarRepository;
    @Autowired
    private ColorRepository colorRepository;

    @Deprecated
    private TempCarRepository tempCarRepository;
    @Deprecated
    private AutobazarEUCarRepository autobazarEUCarRepository;

    @Autowired
    public CarHutAPIService(BrandRepository brandRepository, ModelRepository modelRepository) {
        this.autobazarEUCarRepository = autobazarEUCarRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.tempCarRepository = tempCarRepository;
        this.sorter = new Sorter();
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

    public Integer getBrandIdFromBrandName(String brandName) {
        Integer result = brandRepository.getBrandIdFromBrandName(brandName);
        return result == null ? 136 : result;
    }

    public Integer getModelIdFromModelName(String modelName, int brandId) {
        Integer result = modelRepository.getModelIdByModelName(modelName, brandId);
        return result == null ? 2264 : result;
    }

    public void updateBrand(String brand, String id) {
        tempCarRepository.updateBrand(getBrandIdFromBrandName(brand), id);
    }

    public void updateModel(String model, String id) {
        tempCarRepository.updateModel(getModelIdFromModelName(model, 6999999), id);
    }

    @Deprecated
    public TempCarModel getTempCarWithId(String carId) {
        return tempCarRepository.getTempCarWithId(carId);
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
    public int getNumberOfTempFilteredCars(String brand, String model, String carType,
                                           String priceFrom, String priceTo, String mileageFrom, String mileageTo,
                                           String registration, String seatingConfig, String doors,
                                           String location, String postalCode, String fuelType, String powerFrom,
                                           String powerTo, String displacement, String gearbox, String powertrain)
    {

        List<TempCarModel> filteredList = getAllTempCars();

        return filterTempCarModels(brand, model, priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList).size();
    }

    @Deprecated
    public List<TempCarModel> getAllTempCars() {
        return tempCarRepository.getAllTempCars();
    }

    @Deprecated
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

    @Deprecated
    public List<AutobazarEUCarObject> getAllAutobazarCars() {
        return autobazarEUCarRepository.getAllCars();
    }

    public CarHutCar getCarWithId(String carId) {
        return carHutCarRepository.getCarWithId(carId);
    }

    public List<CarHutCar> getAllCars() {
        return carHutCarRepository.getAllCars();
    }

    public List<CarHutCar> getAllCarsSorted(String sortBy, String sortOrder) {
        List<CarHutCar> cars = getAllCars();

        switch (sortBy) {
            case "PFL":
            case "PFH":
                return sorter.sortCarsByPrice(cars, sortOrder);
            case "MFL":
            case "MFH":
                return sorter.sortCarsByMileage(cars, sortOrder);
            case "SFL":
            case "SFH":
                return sorter.sortCarsByPower(cars, sortOrder);
        }

        return cars;
    }

    public List<CarHutCar> getCarsWithFilter(String brand, String model, String carType, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String registration, String seatingConfig, String doors, String location, String postalCode, String fuelType, String powerFrom, String powerTo, String displacement, String gearbox, String powertrain, String sortBy, String sortOrder, List<ModelsPostModel> models) {
        List<CarHutCar> filteredList = getAllCars();

        if (models != null && !models.isEmpty()) {
            List<CarHutCar> resultList = new ArrayList<>();
            for (ModelsPostModel car : models) {
                resultList.addAll(filterCarModels(car.getBrand(), car.getModel(), priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList));
            }

            return sortCars(sortBy, sortOrder, resultList);
        }

        return filterCarModels(brand, model, priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList);
    }

    private List<CarHutCar> filterCarModels(String brand, String model, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType, String gearbox, String powertrain, String powerFrom, String powerTo, List<CarHutCar> filteredList) {
        Filter filter = new Filter();

        if (brand != null) {
            if (!brand.isEmpty()) {
                Integer brandId = getBrandIdFromBrandName(brand);
                filter.filterCarBrands(filteredList, brandId);
            }
        }

        if (model != null) {
            if (!model.isEmpty()) {
                Integer modelId = getModelIdFromModelName(model, getBrandIdFromBrandName(brand));
                filter.filterCarModels(filteredList, modelId);
            }
        }

        // carType coming soon

        if (priceFrom != null) {
            if (!priceFrom.isEmpty()) {
                filter.filterCarPriceFrom(filteredList, priceFrom);
            }
        }

        if (priceTo != null) {
            if (!priceTo.isEmpty()) {
                filter.filterCarPriceTo(filteredList, priceTo);
            }
        }

        if (mileageFrom != null) {
            if (!mileageFrom.isEmpty()) {
                filter.filterCarMileageFrom(filteredList, mileageFrom);
            }
        }

        if (mileageTo != null) {
            if (!mileageTo.isEmpty()) {
                filter.filterCarMileageTo(filteredList, mileageTo);
            }
        }

        if (fuelType != null) {
            if (!fuelType.isEmpty()) {
                filter.filterCarFuelType(filteredList, fuelType);
            }
        }

        if (gearbox != null) {
            if (!gearbox.isEmpty()) {
                filter.filterCarGearbox(filteredList, gearbox);
            }
        }

        if (powertrain != null) {
            if (!powertrain.isEmpty()) {
                filter.filterCarPowertrain(filteredList, powertrain);
            }
        }

        if (powerFrom != null) {
            if (!powerFrom.isEmpty()) {
                filter.filterCarPowerFrom(filteredList, powerFrom);
            }
        }

        if (powerTo != null) {
            if (!powerTo.isEmpty()) {
                filter.filterCarPowerTo(filteredList, powerTo);
            }
        }

        return filteredList;
    }

    private List<CarHutCar> sortCars(String sortBy, String sortOrder, List<CarHutCar> filteredList) {
        List<CarHutCar> sortedList = filteredList;

        if (sortBy != null && sortOrder != null) {
            if (!sortBy.isEmpty() && !sortOrder.isEmpty()) {
                sortedList = switch (sortBy) {
                    case "PFL", "PFH" -> sorter.sortCarsByPrice(filteredList, sortOrder);
                    case "MFL", "MFH" -> sorter.sortCarsByMileage(filteredList, sortOrder);
                    case "AFL", "AFH" -> sorter.sortCarsByAlphabet(filteredList, sortOrder);
                    case "SFL", "SFH" -> sorter.sortCarsByPower(filteredList, sortOrder);
                    default -> sortedList;
                };
            }
        }

        return sortedList;
    }

    public int getNumberOfFilteredCars(String brand, String model, String carType, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String registration, String seatingConfig, String doors, String location, String postalCode, String fuelType, String powerFrom, String powerTo, String displacement, String gearbox, String powertrain) {
        List<CarHutCar> filteredList = getAllCars();

        return filterCarModels(brand, model, priceFrom, priceTo, mileageFrom, mileageTo, fuelType, gearbox, powertrain, powerFrom, powerTo, filteredList).size();

    }

    public List<String> getBodyTypes() {
        return bodyType.getBodyTypes();
    }

    public List<String> getFuelTypes() {
        return fuel.getFuelTypes();
    }

    public List<String> getPowertrainTypes() {
        return powertrain.getPowertrainTypes();
    }

    public List<String> getGearboxTypes() {
        return gearbox.getGearboxTypes();
    }

    public List<Color> getColors() {
        return colorRepository.getColors();
    }
}
