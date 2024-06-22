package com.carhut.services;

import com.carhut.database.repository.*;
import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.models.enums.BodyType;
import com.carhut.models.enums.Fuel;
import com.carhut.models.enums.Gearbox;
import com.carhut.models.enums.Powertrain;
import com.carhut.models.security.User;
import com.carhut.services.util.Filter;
import com.carhut.services.util.Sorter;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.temputils.repo.TempCarRepository;
import com.carhut.util.exceptions.carhutapi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Deprecated
    private TempCarRepository tempCarRepository;
    @Deprecated
    private AutobazarEUCarRepository autobazarEUCarRepository;

    @Autowired
    public CarHutAPIService(BrandRepository brandRepository, ModelRepository modelRepository) {
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.sorter = new Sorter();
    }

    public List<Brand> getAllBrands() throws CarHutAPIBrandsNotFoundException {
        try {
            return brandRepository.getAllBrands();
        }
        catch (Exception e) {
            throw new CarHutAPIBrandsNotFoundException("Error occurred while getting brands from database. Message: " + e.getMessage());
        }

    }

    public List<Model> getModelsByBrand(String brand) throws CarHutAPIModelsNotFoundException {
        try {
            return modelRepository.getModelByBrandId(brand);
        }
        catch (Exception e) {
            throw new CarHutAPIModelsNotFoundException("Error occurred while getting models from database. Message: " + e.getMessage());
        }
    }

    public List<Model> getModelsByBrandName(String brandName) throws CarHutAPIModelsNotFoundException {
        try {
            return modelRepository.getModelByBrandName(brandName);
        }
        catch (Exception e) {
            throw new CarHutAPIModelsNotFoundException("Error occurred while getting models from database. Message: " + e.getMessage());
        }
    }

    public Integer getBrandIdFromBrandName(String brandName) throws CarHutAPIBrandNotFoundException {
        try {
            Integer result = brandRepository.getBrandIdFromBrandName(brandName);
            return result == null ? 136 : result;
        }
        catch (Exception e) {
            throw new CarHutAPIBrandNotFoundException("Error occurred while getting brand id from database. Message: " + e.getMessage());
        }
    }

    public Integer getModelIdFromModelName(String modelName, int brandId) throws CarHutAPIModelNotFoundException {
        try {
            Integer result = modelRepository.getModelIdByModelName(modelName, brandId);
            return result == null ? 2264 : result;
        }
        catch (Exception e) {
            throw new CarHutAPIModelNotFoundException("Error occurred while getting model id from database. Message: " + e.getMessage());
        }
    }

    @Deprecated
    public void updateBrand(String brand, String id) throws CarHutAPIBrandNotFoundException {
        tempCarRepository.updateBrand(getBrandIdFromBrandName(brand), id);
    }

    @Deprecated
    public void updateModel(String model, String id) throws CarHutAPIModelNotFoundException {
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
                                                    String sortBy, String sortOrder, List<ModelsPostModel> models) throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException {

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
    private List<TempCarModel> filterTempCarModels(String brand, String model, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType, String gearbox, String powertrain, String powerFrom, String powerTo, List<TempCarModel> filteredList) throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException {
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
                                           String powerTo, String displacement, String gearbox, String powertrain) throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException {

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

    public CarHutCar getCarWithId(String carId) throws CarHutAPICarNotFoundException {
        try {
            return carHutCarRepository.getCarWithId(carId);
        }
        catch (Exception e) {
            throw new CarHutAPICarNotFoundException("Error occurred while getting car from database. Message: " + e.getMessage());
        }
    }

    public List<CarHutCar> getAllCars() throws CarHutAPICanNotGetCarsException {
        try {
            return carHutCarRepository.getAllCars();
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetCarsException("Error occurred while getting cars from database. Message: " + e.getMessage());
        }
    }

    public List<CarHutCar> getAllCarsSorted(String sortBy, String sortOrder) throws CarHutAPICanNotGetCarsException {
        List<CarHutCar> cars = getAllCars();

        return switch (sortBy) {
            case "PFL", "PFH" -> sorter.sortCarsByPrice(cars, sortOrder);
            case "MFL", "MFH" -> sorter.sortCarsByMileage(cars, sortOrder);
            case "SFL", "SFH" -> sorter.sortCarsByPower(cars, sortOrder);
            default -> cars;
        };

    }

    public List<CarHutCar> getCarsWithFilter(CarHutCarFilterModel carHutCarFilterModel, String sortBy, String sortOrder, Integer offersPerPage, Integer currentPage)
                                            throws CarHutAPICanNotGetCarsException, CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException {
        if (carHutCarFilterModel.getModels() != null && !carHutCarFilterModel.getModels().isEmpty()) {
            List<CarHutCar> resultList = new ArrayList<>();
            for (ModelsPostModel car : carHutCarFilterModel.getModels()) {
                List<CarHutCar> filteredList = getAllCars();
                resultList.addAll(filterCarModels(car.getBrand(), car.getModel(), carHutCarFilterModel, filteredList));
            }
            return stripCarHutCarList(sortCars(sortBy, sortOrder, resultList), offersPerPage, currentPage);
        }

        return stripCarHutCarList(sortCars(sortBy, sortOrder, filterCarModels(carHutCarFilterModel.getBrand(), carHutCarFilterModel.getModel(), carHutCarFilterModel, getAllCars())), offersPerPage, currentPage);
    }

    private List<CarHutCar> stripCarHutCarList(List<CarHutCar> list, Integer offersPerPage, Integer currentPage) {
        int startIndex = (currentPage - 1) * offersPerPage;
        int endIndex = Math.min(startIndex + offersPerPage, list.isEmpty() ? 0 : list.size() - 1) ;

        return list.subList(startIndex, endIndex);
    }

    private boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    // Firstly, it filters more obscure search params, so it filters faster
    private List<CarHutCar> filterCarModels(String brand, String model, CarHutCarFilterModel carHutCarFilterModel, List<CarHutCar> filteredList)
            throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException {
        Filter filter = new Filter();

        if (!isStringNullOrEmpty(carHutCarFilterModel.getSeatingConfig())) {
            filter.filterCarSeatingConfig(filteredList, carHutCarFilterModel.getSeatingConfig());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getDoors())) {
            filter.filterCarDoors(filteredList, carHutCarFilterModel.getDoors());
        }

        if (carHutCarFilterModel.getCarTypes() != null) {
            if (!carHutCarFilterModel.getCarTypes().isEmpty()) {
                filter.filterCarTypes(filteredList, carHutCarFilterModel.getCarTypes());
            }
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getRegistrationFrom())) {
            filter.filterCarRegistrationFrom(filteredList, carHutCarFilterModel.getRegistrationFrom());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getRegistrationTo())) {
            filter.filterCarRegistrationTo(filteredList, carHutCarFilterModel.getRegistrationTo());
        }

        // There might be a multiple models so check brand of models list first
        if (!isStringNullOrEmpty(brand)) {
            Integer brandId = getBrandIdFromBrandName(brand);
            filter.filterCarBrands(filteredList, brandId);
        } else if (!isStringNullOrEmpty(carHutCarFilterModel.getBrand())) {
            Integer brandId = getBrandIdFromBrandName(carHutCarFilterModel.getBrand());
            filter.filterCarBrands(filteredList, brandId);
        }

        // Same as with brand, check model of models list first
        if (!isStringNullOrEmpty(model)) {
            Integer modelId = getModelIdFromModelName(model, getBrandIdFromBrandName(brand)); // -> brand is certainly not null
            filter.filterCarModels(filteredList, modelId);
        } else if (!isStringNullOrEmpty(carHutCarFilterModel.getModel())) {
            Integer modelId = getModelIdFromModelName(carHutCarFilterModel.getModel(), getBrandIdFromBrandName(carHutCarFilterModel.getBrand()));
            filter.filterCarModels(filteredList, modelId);
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getPriceFrom())) {
            filter.filterCarPriceFrom(filteredList, carHutCarFilterModel.getPriceFrom());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getPriceTo())) {
            filter.filterCarPriceTo(filteredList, carHutCarFilterModel.getPriceTo());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getMileageFrom())) {
            filter.filterCarMileageFrom(filteredList, carHutCarFilterModel.getMileageFrom());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getMileageTo())) {
            filter.filterCarMileageTo(filteredList, carHutCarFilterModel.getMileageTo());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getFuelType())) {
            filter.filterCarFuelType(filteredList, carHutCarFilterModel.getFuelType());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getGearbox())) {
            filter.filterCarGearbox(filteredList, carHutCarFilterModel.getGearbox());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getPowertrain())) {
            filter.filterCarPowertrain(filteredList, carHutCarFilterModel.getPowertrain());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getPowerFrom())) {
            filter.filterCarPowerFrom(filteredList, carHutCarFilterModel.getPowerFrom());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getPowerTo())) {
            filter.filterCarPowerTo(filteredList, carHutCarFilterModel.getPowerTo());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getDisplacementFrom())) {
            filter.filterCarDisplacementFrom(filteredList, carHutCarFilterModel.getDisplacementFrom());
        }

        if (!isStringNullOrEmpty(carHutCarFilterModel.getDisplacementTo())) {
            filter.filterCarDisplacementTo(filteredList, carHutCarFilterModel.getDisplacementTo());
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
                    case "DAO", "DAN" -> sorter.sortCarsByDateAdded(filteredList, sortOrder);
                    default -> sortedList;
                };
            }
        }

        return sortedList;
    }

    public int getNumberOfFilteredCars(CarHutCarFilterModel carHutCarFilterModel)
            throws CarHutAPICanNotGetCarsException, CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException
    {
        return getCarsWithFilter(carHutCarFilterModel, null, null, Integer.MAX_VALUE, 1).size();
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

    public List<Color> getColors() throws CarHutAPICanNotGetColorsException {
        try {
            return colorRepository.getColors();
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetColorsException("Error occurred while getting colors from database. Message: " + e.getMessage());
        }
    }

    public List<Feature> getFeatures() throws CarHutAPICanNotGetFeaturesException {
        try {
            return featureRepository.getFeaturesAsc();
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetFeaturesException("Error occurred while getting features from database. Message: " + e.getMessage());
        }
    }

    public void addCarToDatabase(CarHutCar carHutCar) throws CarHutAPICarCanNotBeSavedException {
        CarHutCar newCar = new CarHutCar(userCredentialsRepository.findUserIdByUsername(carHutCar.getSellerId()), carHutCar.getSellerAddress(),
                carHutCar.getBrandId(), carHutCar.getModelId(), carHutCar.getHeader(),
                carHutCar.getPrice(), carHutCar.getMileage(), carHutCar.getRegistration(),
                carHutCar.getEnginePower(), carHutCar.getEngineDisplacement(), carHutCar.getFuel(),
                carHutCar.getFuelConsumptionAvg(), carHutCar.getFuelConsumptionCity(), carHutCar.getFuelConsumptionHighway(),
                carHutCar.getGearbox(), carHutCar.getGearboxGears(), carHutCar.getBodyType(),
                carHutCar.getPowertrain(), carHutCar.getDescription(), carHutCar.getBaseImgPath(),
                carHutCar.getPreviousOwners(), carHutCar.getEnergyEffClass(), carHutCar.getSeats(),
                carHutCar.getDoors(), carHutCar.getEmissionClass(), colorRepository.getColorIdByColorName(carHutCar.getExteriorColorId()),
                colorRepository.getColorIdByColorName(carHutCar.getInteriorColorId()), carHutCar.getDamageStatus(),
                carHutCar.isParkingSensors(), carHutCar.isParkingCameras(), carHutCar.getCountryOfOrigin(),
                carHutCar.getTechnicalInspectionDate(), carHutCar.getEmissionInspectionDate(), carHutCar.getFeatures(), new Date(System.currentTimeMillis()));

        try {
            carHutCarRepository.save(newCar);
            userCredentialsRepository.updateCountOfAddedOffers(newCar.getSellerId());
        }
        catch (Exception e) {
            throw new CarHutAPICarCanNotBeSavedException("Error occurred while saving the car. Message: " + e.getMessage());
        }
    }

    public Integer getFeatureIdByFeatureName(String feature) throws CarHutAPICanNotGetFeaturesException {
        try {
            return featureRepository.getFeatureIdByFeatureName(feature);
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetFeaturesException("Error occurred while getting features from database. Message: " + e.getMessage());
        }
    }

    public String getColorStringNameFromColorId(String colorId) throws CarHutAPICanNotGetColorsException {
        try {
            return colorRepository.getColorStringNameFromColorId(colorId);
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetColorsException("Error occurred while getting color name from database. Message: " + e.getMessage());
        }
    }

    public String getFeatureNameByFeatureId(Integer featureId) throws CarHutAPICanNotGetFeaturesException {
        try {
            return featureRepository.getFeatureNameByFeatureId(featureId);
        }
        catch (Exception e) {
            throw new CarHutAPICanNotGetFeaturesException("Error occurred while getting features from database. Message: " + e.getMessage());
        }
    }

    public List<String> getMultipleFeaturesByIds(List<Integer> featureIds) throws CarHutAPICanNotGetFeaturesException {
        List<String> featureNames = new ArrayList<>();
        for (Integer id : featureIds) {
            featureNames.add(getFeatureNameByFeatureId(id));
        }
        return featureNames;
    }

    public String getUsernameByUserId(String userId) {
        return userCredentialsRepository.getUsernameByUserId(userId);
    }

    public String getFirstNameAndSurnameByUserId(String userId) {
        return userCredentialsRepository.getFirstNameAndSurnameByUserId(userId);
    }

    public Integer getOffersNumByUserId(String userId) {
        return userCredentialsRepository.getOffersNumByUserId(userId);
    }

    public String getEmailByUserId(String userId) {
        return userCredentialsRepository.getEmailByUserId(userId);
    }

    public List<CarHutCar> getMyListings(String username) throws CarHutAPIException {
        try {
            User user = userCredentialsRepository.findUserByUsername(username);
            return carHutCarRepository.getMyListings(user.getId());
        }
        catch (Exception e) {
            throw new CarHutAPIException("Couldn't retrieve data from database.");
        }
    }

    public RequestStatusEntity removeOffer(String carId) throws CarHutAPIException {
        try {
            CarHutCar car = carHutCarRepository.getCarWithId(carId);
            if (car == null) {
                return RequestStatusEntity.ERROR;
            }

            car.setActive(false);
            carHutCarRepository.save(car);
            return RequestStatusEntity.SUCCESS;
        }
        catch (Exception e) {
            throw new CarHutAPIException("Something went wrong when deleting car from database.");
        }
    }

    public String getUserIdByUsername(String username) throws CarHutAPIException {
        try {
            return userCredentialsRepository.getUserIdByUsername(username);
        }
        catch (Exception e) {
            throw new CarHutAPIException("Couldn't retrieve user id.");
        }
    }
}
