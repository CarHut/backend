package com.carhut.services;

import com.carhut.CarHutApplication;
import com.carhut.database.repository.CarHutCarRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.models.requestmodels.CarHutCarFilterModel;
import com.carhut.models.requestmodels.ModelsPostModel;
import com.carhut.paths.NetworkPaths;
import com.carhut.util.converters.CarHutJSONConverter;
import com.carhut.util.exceptions.CarHutException;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.carhutapi.*;
import com.carhut.utils.AuthLogger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarHutAPIServiceTests {

    @Autowired
    private CarHutAPIService carHutAPIService;
    @Autowired
    private CarHutCarRepository carHutCarRepository;
    private CarHutJSONConverter carHutJSONConverter = new CarHutJSONConverter();

    @Test
    void getAllBrands_ValidCaseShouldReturnAListOfBrands() throws CarHutAPIBrandsNotFoundException {
        List<Brand> brands = carHutAPIService.getAllBrands();
        Assertions.assertNotNull(brands);
        Assertions.assertFalse(brands.isEmpty());
    }

    @Test
    void getModelsByBrandName_ValidCaseShouldReturnAListOfModels() throws CarHutAPIModelsNotFoundException {
        List<Model> models = carHutAPIService.getModelsByBrandName("Audi");
        Assertions.assertFalse(models.isEmpty());
    }

    @Test
    void getBrandIdFromBrandName_BrandRepositoryIsNotNull_ShouldReturnId() throws CarHutAPIBrandNotFoundException {
        Integer id = carHutAPIService.getBrandIdFromBrandName("Audi");
        Assertions.assertNotNull(id);
    }

    @Test
    void getBrandIdFromBrandName_BrandRepositoryIsNotNull_ValueIsNotInDatabase() throws CarHutAPIBrandNotFoundException {
        Integer id = carHutAPIService.getBrandIdFromBrandName("Random brand");
        Assertions.assertEquals(136, id);
    }

    @Test
    void getModelIdFroModelName_ShouldReturnNotNullInteger() throws CarHutAPIModelNotFoundException {
        Integer id = carHutAPIService.getModelIdFromModelName("A4", 1);
        Assertions.assertNotNull(id);
    }

     @Test
    void getModelIdFromModelName_ShouldReturnDefaultId_ModelNameIsNotInDatabase() throws CarHutAPIModelNotFoundException {
        Integer id = carHutAPIService.getModelIdFromModelName("Random brand", 1);
        Assertions.assertEquals(2264, id);
    }

    @Test
    void carHutCarDataTransfer_AddNewCarToDatabase_GetCarWithId_ThenDeleteCar_SuccessfulScenario() throws CarHutAPICarCanNotBeSavedException, InterruptedException, CarHutAPICarNotFoundException {
        CarHutCar car = new CarHutCar(
                "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, "color0", "color0", null, false, false,
                null, null, null, null, null
        );

        String newId = carHutAPIService.addCarToDatabase(car);
        Thread.sleep(1000);

        CarHutCar car1 = carHutAPIService.getCarWithId(newId);
        Assertions.assertEquals("user0", car1.getSellerId());
        Assertions.assertEquals(car.getSellerAddress(), car1.getSellerAddress());
        Assertions.assertEquals(car.getBrandId(), car1.getBrandId());
        Assertions.assertEquals(car.getModelId(), car1.getModelId());
        Assertions.assertEquals(car.getHeader(), car1.getHeader());
        Assertions.assertEquals(car.getPrice(), car1.getPrice());

        carHutCarRepository.delete(car1);
    }

    @Test
    void addCarToDatabase_InvalidUserId() throws CarHutAPICarCanNotBeSavedException {
        CarHutCar car = new CarHutCar(
                "INVALID_USER_ID", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, "color0", "color0", null, false, false,
                null, null, null, null, null
        );

        String id = carHutAPIService.addCarToDatabase(car);
        Assertions.assertNull(id);
    }

    @Test
    void getCarWithId_InvalidCarId_ShouldReturnNull_NoException() throws CarHutAPICarNotFoundException {
        CarHutCar car = carHutAPIService.getCarWithId("INVALID_CAR_ID");
        Assertions.assertNull(car);
    }

    @Test
    void getAllCars_ShouldReturnListOfSizeGreaterThanZero() throws CarHutAPICanNotGetCarsException {
        List<CarHutCar> cars = carHutAPIService.getAllCars();
        Assertions.assertNotNull(cars);
        Assertions.assertFalse(cars.isEmpty());
    }

    @Test
    void getCarsWithFilter_ValidScenario_ShouldShowAllCarsAvailable() throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException, CarHutAPICanNotGetCarsException {
        CarHutCarFilterModel carHutCarFilterModel = new CarHutCarFilterModel(
                "", "", Collections.emptyList(),
                "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", Collections.emptyList()
        );

        String sortBy = "";
        String sortOrder = "";
        Integer offersPerPage = Integer.MAX_VALUE;

        List<CarHutCar> filteredCars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
        List<CarHutCar> allCars = carHutAPIService.getAllCars();
        Assertions.assertEquals(allCars.size(), filteredCars.size());
    }

    @Test
    void getCarsWithFilter_MultipleModels() throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException, CarHutAPICanNotGetCarsException {

        List<ModelsPostModel> models = new ArrayList<>();
        models.add(new ModelsPostModel("BMW", "520"));
        models.add(new ModelsPostModel("Audi", "A4"));
        models.add(new ModelsPostModel("BMW", "M3"));

        CarHutCarFilterModel carHutCarFilterModel = new CarHutCarFilterModel(
                "", "", Collections.emptyList(),
                "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", models
        );

        String sortBy = "";
        String sortOrder = "";
        Integer offersPerPage = Integer.MAX_VALUE;

        List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
        Assertions.assertFalse(cars.isEmpty());
        Assertions.assertTrue(cars.size() <= carHutAPIService.getAllCars().size());
    }

    @Test
    void getCarsWithFilter_AllFilterParametersAreSet() throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException, CarHutAPICanNotGetCarsException {
        List<ModelsPostModel> models = new ArrayList<>();
        models.add(new ModelsPostModel("BMW", "520"));
        models.add(new ModelsPostModel("Audi", "A4"));

        List<String> carTypes = new ArrayList<>();
        carTypes.add("Sedan");
        carTypes.add("SUV");

        String sortBy = "";
        String sortOrder = "";
        Integer offersPerPage = Integer.MAX_VALUE;

        CarHutCarFilterModel carHutCarFilterModel = new CarHutCarFilterModel(
                "", "", carTypes,
                "5000", "10000", "105000", "260000", "2000", "2018", "5", "5", "",
                "21345", "Petrol", "150", "500", "2000", "5000", "Automatic", "All-wheel", models
        );

        List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
        Assertions.assertTrue(cars.size() <= carHutAPIService.getAllCars().size());
    }

    @Test
    void getNumberOfFilteredCars_ValidScenario() throws CarHutAPIBrandNotFoundException, CarHutAPIModelNotFoundException, CarHutAPICanNotGetCarsException {
        List<ModelsPostModel> models = new ArrayList<>();
        models.add(new ModelsPostModel("BMW", "520"));
        models.add(new ModelsPostModel("Audi", "A4"));

        CarHutCarFilterModel carHutCarFilterModel = new CarHutCarFilterModel(
                "", "", Collections.emptyList(),
                "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", models
        );

        String sortBy = "";
        String sortOrder = "";
        Integer offersPerPage = Integer.MAX_VALUE;

        List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
        Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel);
        Assertions.assertEquals(cars.size(), size);
    }

    @Test
    void getBodyTypes_ValidScenario() {
        List<String> bodyTypes = carHutAPIService.getBodyTypes();
        Assertions.assertEquals(15, bodyTypes.size());
    }

    @Test
    void getFuelTypes_ValidScenario() {
        List<String> fuelTypes = carHutAPIService.getFuelTypes();
        Assertions.assertEquals(7, fuelTypes.size());
    }

    @Test
    void getPowertrainTypes_ValidScenario() {
        List<String> powertrainTypes = carHutAPIService.getPowertrainTypes();
        Assertions.assertEquals(4, powertrainTypes.size());
    }

    @Test
    void getGearboxTypes_ValidScenario() {
        List<String> gearboxTypes = carHutAPIService.getGearboxTypes();
        Assertions.assertEquals(4, gearboxTypes.size());
    }

    @Test
    void getColors_ValidScenario() throws CarHutAPICanNotGetColorsException {
        List<Color> colors = carHutAPIService.getColors();
        Assertions.assertNotNull(colors);
        Assertions.assertFalse(colors.isEmpty());
    }

    @Test
    void getFeatures_ValidScenario() throws CarHutAPICanNotGetFeaturesException {
        List<Feature> features = carHutAPIService.getFeatures();
        Assertions.assertNotNull(features);
        Assertions.assertFalse(features.isEmpty());
    }

    @Test
    void getFeatureById_FeatureIsInvalid() throws CarHutAPICanNotGetFeaturesException {
        Integer id = carHutAPIService.getFeatureIdByFeatureName("INVALID_FEATURE_NAME");
        Assertions.assertNull(id);
    }

    @Test
    void getFeatureById_ValidFeatureName() throws CarHutAPICanNotGetFeaturesException {
        Integer id = carHutAPIService.getFeatureIdByFeatureName("Parking cameras");
        Assertions.assertEquals(2, id);
    }

    @Test
    void getColorStringNameFromColorId_InvalidId() throws CarHutAPICanNotGetColorsException {
        String colorName = carHutAPIService.getColorStringNameFromColorId("INVALID_ID");
        Assertions.assertNull(colorName);
    }

    @Test
    void getColorStringNameFromColorId_ValidId() throws CarHutAPICanNotGetColorsException {
        String colorName = carHutAPIService.getColorStringNameFromColorId("1");
        Assertions.assertEquals("red", colorName);
    }

    @Test
    void getFeatureNameByFeatureId_InvalidId() throws CarHutAPICanNotGetFeaturesException {
        String featureName = carHutAPIService.getFeatureNameByFeatureId(-1);
        Assertions.assertNull(featureName);
    }

    @Test
    void getFeatureNameByFeatureId_ValidId() throws CarHutAPICanNotGetFeaturesException {
        String featureName = carHutAPIService.getFeatureNameByFeatureId(1);
        Assertions.assertEquals("LED lights", featureName);
    }

    @Test
    void getMultipleFeaturesByIds_ListIsNull() {
        List<String> featureNames = carHutAPIService.getMultipleFeaturesByIds(null);
        Assertions.assertNull(featureNames);
    }

    @Test
    void getMultipleFeaturesByIds_ListIsNotNull_ContainsAlsoInvalidIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(null);
        ids.add(150000);
        ids.add(2);

        List<String> featureNames = carHutAPIService.getMultipleFeaturesByIds(ids);
        Assertions.assertEquals(2, featureNames.size());
    }

    @Test
    void getUsernameByUserId_IdIsNull() {
        String username = carHutAPIService.getUsernameByUserId(null);
        Assertions.assertNull(username);
    }

    @Test
    void getUsernameByUserId_IdIsInvalid() {
        String username = carHutAPIService.getUsernameByUserId("INVALID_USERNAME_FOR_TEST");
        Assertions.assertNull(username);
    }

    @Test
    void getUsernameByUserId_IdIsValid() {
        String username = carHutAPIService.getUsernameByUserId("user0");
        Assertions.assertEquals("admin", username);
    }

    @Test
    void getFirstNameAndSurnameById_IdIsNull() {
        String fullName = carHutAPIService.getFirstNameAndSurnameByUserId(null);
        Assertions.assertNull(fullName);
    }

    @Test
    void getFirstNameAndSurnameById_IdIsInvalid() {
        String fullName = carHutAPIService.getFirstNameAndSurnameByUserId("INVALID_USERNAME_FOR_TEST");
        Assertions.assertNull(fullName);
    }

    @Test
    void getFirstNameAndSurnameById_IdIsValid() {
        String fullName = carHutAPIService.getFirstNameAndSurnameByUserId("user0");
        Assertions.assertEquals("Johny Johny", fullName);
    }

    @Test
    void getOffersNumByUserId_IdIsNull() {
        Integer value = carHutAPIService.getOffersNumByUserId(null);
        Assertions.assertNull(value);
    }

    @Test
    void getOffersNumByUserId_IdIsInvalid() {
        Integer value = carHutAPIService.getOffersNumByUserId("INVALID_USER_ID_FOR_TEST");
        Assertions.assertNull(value);
    }

    @Test
    void getOffersNumByUserId_IdIsValid() {
        Integer value = carHutAPIService.getOffersNumByUserId("user0");
        Assertions.assertNotNull(value);
    }

    @Test
    void getEmailByUserId_IdIsNull() {
        String email = carHutAPIService.getEmailByUserId(null);
        Assertions.assertNull(email);
    }

    @Test
    void getEmailByUserId_IdIsInvalid() {
        String email = carHutAPIService.getEmailByUserId("INVALID_USER_ID_FOR_TEST");
        Assertions.assertNull(email);
    }

    @Test
    void getEmailByUserId_IdIsValid() {
        String email = carHutAPIService.getEmailByUserId("user0");
        Assertions.assertEquals("admin@admin.com", email);
    }

    @Test
    void getMyListing_UsernameIsNull() throws CarHutAPIException, CarHutAuthenticationException {
        List<CarHutCar> cars = carHutAPIService.getMyListings(null);
        Assertions.assertNull(cars);
    }

    @Test
    void getMyListing_UsernameIsInvalid() throws CarHutAPIException, CarHutAuthenticationException {
        List<CarHutCar> cars = carHutAPIService.getMyListings("INVALID_USERNAME");
        Assertions.assertNull(cars);
    }

    @Test
    void getMyListing_UsernameIsValid() throws CarHutAPIException, CarHutAuthenticationException {
        List<CarHutCar> cars = carHutAPIService.getMyListings("admin");
        Assertions.assertNotNull(cars);
        Assertions.assertFalse(cars.isEmpty());
        cars.forEach(car -> Assertions.assertEquals("user0", car.getSellerId()));
    }

    @Test
    void removeOffer_IdIsNull() throws CarHutAPIException, CarHutAuthenticationException {
        RequestStatusEntity status = carHutAPIService.removeOffer(null);
        Assertions.assertEquals(RequestStatusEntity.ERROR, status);
    }

    @Test
    void removeOffer_IdIsInvalid() throws CarHutAPIException, CarHutAuthenticationException {
        RequestStatusEntity status = carHutAPIService.removeOffer("INVALID_ID");
        Assertions.assertEquals(RequestStatusEntity.ERROR, status);
    }

    @Test
    void removeOffer_IdIsValid() throws InterruptedException, CarHutAPIException, CarHutAuthenticationException {
        CarHutCar car = new CarHutCar(
                "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, "color0", "color0", null, false, false,
                null, null, null, null, null
        );

        String id = carHutAPIService.addCarToDatabase(car);
        Assertions.assertNotNull(id);
        Thread.sleep(1000);

        RequestStatusEntity status = carHutAPIService.removeOffer(id); // Remove offer
        Assertions.assertEquals(RequestStatusEntity.SUCCESS, status);
    }

}
