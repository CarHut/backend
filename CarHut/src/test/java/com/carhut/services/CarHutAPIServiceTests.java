package com.carhut.services;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarHutAPIServiceTests {

    @Autowired
    private CarHutAPIService carHutAPIService;
    @Autowired
    private CarHutCarRepository carHutCarRepository;
    private CarHutJSONConverter carHutJSONConverter = new CarHutJSONConverter();

    @Test
    void getAllBrands_ValidCaseShouldReturnAListOfBrands() {
        try {
            List<Brand> brands = carHutAPIService.getAllBrands();
            Assertions.assertFalse(brands.isEmpty());
        } catch (CarHutAPIBrandsNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getAllBrands_MockedBrandRepository_ShouldThrowError() {
        CarHutAPIService carHutAPIService1 = new CarHutAPIService(null, null);

        try {
            List<Brand> brands = carHutAPIService1.getAllBrands();
            Assertions.assertNull(brands);
        } catch (CarHutAPIBrandsNotFoundException e) {
            System.out.println(e.getMessage());
            // Expected ending
        }
    }

    @Test
    void getModelsByBrandName_ValidCaseShouldReturnAListOfModels() {
        try {
            List<Model> models = carHutAPIService.getModelsByBrandName("Audi");
            Assertions.assertFalse(models.isEmpty());
        } catch (CarHutAPIModelsNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getModelsByBrandName_MockedModelRepository_ShouldThrowError() {
        CarHutAPIService carHutAPIService1 = new CarHutAPIService(null, null);

        try {
            List<Model> models = carHutAPIService1.getModelsByBrandName("Audi");
            Assertions.assertNull(models);
        } catch (CarHutAPIModelsNotFoundException e) {
            System.out.println(e.getMessage());
            // Expected ending
        }
    }

    @Test
    void getBrandIdFromBrandName_BrandRepositoryIsNotNull_ShouldReturnId() {
        try {
            Integer id = carHutAPIService.getBrandIdFromBrandName("Audi");
            Assertions.assertNotNull(id);
        } catch (CarHutAPIBrandNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getBrandIdFromBrandName_BrandRepositoryIsNull_ShouldThrowException() {
        CarHutAPIService carHutAPIService1 = new CarHutAPIService(null, null);

        try {
            Integer id = carHutAPIService1.getBrandIdFromBrandName("Audi");
            Assertions.assertNull(id);
        } catch (CarHutAPIBrandNotFoundException e) {
            System.out.println(e.getMessage());
            // Expected ending
        }
    }

    @Test
    void getBrandIdFromBrandName_BrandRepositoryIsNotNull_ValueIsNotInDatabase() {
        try {
            Integer id = carHutAPIService.getBrandIdFromBrandName("Random brand");
            Assertions.assertEquals(136, id);
        } catch (CarHutAPIBrandNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getModelIdFroModelName_ShouldReturnNotNullInteger() {
        try {
            Integer id = carHutAPIService.getModelIdFromModelName("A4", 1);
            Assertions.assertNotNull(id);
        } catch (CarHutAPIModelNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getModelIdFroModelName_ShouldThrowException() {
        CarHutAPIService carHutAPIService1 = new CarHutAPIService(null, null);
        try {
            Integer id = carHutAPIService1.getModelIdFromModelName("A4", 1);
            Assertions.assertNull(id);
        } catch (CarHutAPIModelNotFoundException e) {
            System.out.println(e.getMessage());
            // Expected ending
        }
    }

    @Test
    void getModelIdFromModelName_ShouldReturnDefaultId_ModelNameIsNotInDatabase() {
        try {
            Integer id = carHutAPIService.getModelIdFromModelName("Random brand", 1);
            Assertions.assertEquals(2264, id);
        } catch (CarHutAPIModelNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void carHutCarDataTransfer_AddNewCarToDatabase_GetCarWithId_ThenDeleteCar_SuccessfulScenario() {
        CarHutCar car = new CarHutCar(
                "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, false, false,
                null, null, null, null, null
        );

        String newId = null;

        try {
            newId = carHutAPIService.addCarToDatabase(car);
        } catch (CarHutAPICarCanNotBeSavedException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }

        // timeout
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            CarHutCar car1 = carHutAPIService.getCarWithId(newId);
            Assertions.assertEquals("user0", car1.getSellerId());
            Assertions.assertEquals(car.getSellerAddress(), car1.getSellerAddress());
            Assertions.assertEquals(car.getBrandId(), car1.getBrandId());
            Assertions.assertEquals(car.getModelId(), car1.getModelId());
            Assertions.assertEquals(car.getHeader(), car1.getHeader());
            Assertions.assertEquals(car.getPrice(), car1.getPrice());

            carHutCarRepository.delete(car1);
        } catch (CarHutAPICarNotFoundException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void addCarToDatabase_InvalidUserId() {
        CarHutCar car = new CarHutCar(
                "INVALID_USER_ID", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, false, false,
                null, null, null, null, null
        );

        try {
            String id = carHutAPIService.addCarToDatabase(car);
            Assertions.assertNull(id);
        } catch (CarHutAPICarCanNotBeSavedException e) {
            System.out.println(e.getMessage());
            // Expected ending
        }

    }

    @Test
    void getCarWithId_InvalidCarId_ShouldReturnNull_NoException() {
        try {
            CarHutCar car = carHutAPIService.getCarWithId("INVALID_CAR_ID");
            Assertions.assertNull(car);
        } catch (CarHutAPICarNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getAllCars_ShouldReturnListOfSizeGreaterThanZero() {
        try {
            List<CarHutCar> cars = carHutAPIService.getAllCars();
            Assertions.assertNotNull(cars);
            Assertions.assertFalse(cars.isEmpty());
        } catch (CarHutAPICanNotGetCarsException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getCarsWithFilter_ValidScenario_ShouldShowAllCarsAvailable() {
        CarHutCarFilterModel carHutCarFilterModel = new CarHutCarFilterModel(
                "", "", Collections.emptyList(),
                "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", Collections.emptyList()
        );

        String sortBy = "";
        String sortOrder = "";
        Integer offersPerPage = Integer.MAX_VALUE;

        try {
            List<CarHutCar> filteredCars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
            List<CarHutCar> allCars = carHutAPIService.getAllCars();
            Assertions.assertEquals(allCars.size(), filteredCars.size());
        } catch (CarHutAPIBrandNotFoundException | CarHutAPIModelNotFoundException |
                 CarHutAPICanNotGetCarsException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getCarsWithFilter_MultipleModels() {

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

        try {
            List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
            Assertions.assertFalse(cars.isEmpty());
            Assertions.assertTrue(cars.size() <= carHutAPIService.getAllCars().size());
        } catch (CarHutException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getCarsWithFilter_AllFilterParametersAreSet() {
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

        try {
            List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
            Assertions.assertTrue(cars.size() <= carHutAPIService.getAllCars().size());
        } catch (CarHutException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getNumberOfFilteredCars_ValidScenario() {
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

        try {
            List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, 1);
            Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel);
            Assertions.assertEquals(cars.size(), size);
        } catch (CarHutException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }

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
    void getColors_ValidScenario() {
        try {
            List<Color> colors = carHutAPIService.getColors();
            Assertions.assertNotNull(colors);
            Assertions.assertFalse(colors.isEmpty());
        } catch (CarHutAPICanNotGetColorsException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getFeatures_ValidScenario() {
        try {
            List<Feature> features = carHutAPIService.getFeatures();
            Assertions.assertNotNull(features);
            Assertions.assertFalse(features.isEmpty());
        } catch (CarHutAPICanNotGetFeaturesException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getFeatureById_FeatureIsInvalid() {
        try {
            Integer id = carHutAPIService.getFeatureIdByFeatureName("INVALID_FEATURE_NAME");
            Assertions.assertNull(id);
        } catch (CarHutAPICanNotGetFeaturesException e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS EXPECTED ENDING.");
        }
    }

    @Test
    void getFeatureById_ValidFeatureName() {
        try {
            Integer id = carHutAPIService.getFeatureIdByFeatureName("Parking cameras");
            Assertions.assertEquals(2, id);
        } catch (CarHutAPICanNotGetFeaturesException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getColorStringNameFromColorId_InvalidId() {
        try {
            String colorName = carHutAPIService.getColorStringNameFromColorId("INVALID_ID");
            Assertions.assertNull(colorName);
        } catch (CarHutAPICanNotGetColorsException e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS ONE OF THE EXPECTED ENDINGS.");
        }
    }

    @Test
    void getColorStringNameFromColorId_ValidId() {
        try {
            String colorName = carHutAPIService.getColorStringNameFromColorId("1");
            Assertions.assertEquals("red", colorName);
        } catch (CarHutAPICanNotGetColorsException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void getFeatureNameByFeatureId_InvalidId() {
        try {
            String featureName = carHutAPIService.getFeatureNameByFeatureId(-1);
            Assertions.assertNull(featureName);
        } catch (CarHutAPICanNotGetFeaturesException e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS ONE OF THE EXPECTED ENDINGS.");
        }
    }

    @Test
    void getFeatureNameByFeatureId_ValidId() {
        try {
            String featureName = carHutAPIService.getFeatureNameByFeatureId(1);
            Assertions.assertEquals("LED lights", featureName);
        } catch (CarHutAPICanNotGetFeaturesException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
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
    void getMyListing_UsernameIsNull() {
        try {
            List<CarHutCar> cars = carHutAPIService.getMyListings(null);
            Assertions.assertNull(cars);
        } catch (CarHutAPIException | CarHutAuthenticationException e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS ONE OF THE EXPECTED ENDINGS.");
        }
    }

    @Test
    void getMyListing_UsernameIsInvalid() {
        try {
            List<CarHutCar> cars = carHutAPIService.getMyListings("INVALID_USERNAME");
            Assertions.assertNull(cars);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS ONE OF THE EXPECTED ENDINGS.");
        }
    }

    @Test
    void getMyListing_UsernameIsValid_UserIsNotLoggedIn() {
        try {
            List<CarHutCar> cars = carHutAPIService.getMyListings("user0");
            Assertions.fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS EXPECTED ENDING.");
        }
    }

    @Test
    void getMyListing_UsernameIsValid_UserIsLoggedIn() {
        try {
            Thread.sleep(1000);
            String username = "admin";
            String password = "admin";

            // Login
            URL url = new URL(NetworkPaths.publicIPAddress + ":8080/api/auth/authenticate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            Assertions.assertEquals(200, responseCode);

            InputStream stream = conn.getInputStream();
            String bearerToken = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                bearerToken = br.readLine();
            }

            Assertions.assertNotNull(bearerToken);

            System.out.println(bearerToken);

            Thread.sleep(2000);

            // Send request to getMyListings
            URL url1 = new URL(NetworkPaths.publicIPAddress + ":8080/api/carhut/getMyListings?username=" + username);
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            conn1.setRequestMethod("POST");
            conn1.setRequestProperty("Content-Type", "application/json");
            conn1.setRequestProperty("Authorization", "Bearer " + bearerToken);
            conn1.setDoOutput(true);

            responseCode = conn1.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            List<CarHutCar> cars;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn1.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                cars = carHutJSONConverter.deserializeListOfCarHutCars(response.toString());
            }

            Assertions.assertNotNull(cars);
            Assertions.assertFalse(cars.isEmpty());
            cars.forEach(car -> Assertions.assertEquals("user0", car.getSellerId()));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeOffer_IdIsNull() {
        try {
            RequestStatusEntity status = carHutAPIService.removeOffer(null);
            Assertions.assertEquals(RequestStatusEntity.ERROR, status);
        } catch (CarHutAPIException | CarHutAuthenticationException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void removeOffer_IdIsInvalid() {
        try {
            RequestStatusEntity status = carHutAPIService.removeOffer("INVALID_ID");
            Assertions.assertEquals(RequestStatusEntity.ERROR, status);
        } catch (CarHutAPIException | CarHutAuthenticationException | NullPointerException e) {
            System.out.println(e.getMessage());
            System.out.println("THIS IS ONE OF THE EXPECTED ENDINGS.");
        }
    }

    @Test
    void removeOffer_IdIsValid() throws InterruptedException {
        String bearerToken;
        try {
            // First user logs in
            Thread.sleep(1000);
            String username = "admin";
            String password = "admin";

            // Login
            URL url = new URL(NetworkPaths.publicIPAddress + ":8080/api/auth/authenticate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            Assertions.assertEquals(200, responseCode);

            InputStream stream = conn.getInputStream();
            bearerToken = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                bearerToken = br.readLine();
            }

            Assertions.assertNotNull(bearerToken);

            System.out.println(bearerToken);

            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        CarHutCar car = new CarHutCar(
                "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, false, false,
                null, null, null, null, null
        );

        String id = null;

        try {
            id = carHutAPIService.addCarToDatabase(car);
            Assertions.assertNotNull(id);
        } catch (CarHutAPICarCanNotBeSavedException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(1000);

        try {
            // Send request to remove offer
            URL url1 = new URL(NetworkPaths.publicIPAddress + ":8080/api/carhut/removeOffer?carId=" + id);
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            conn1.setRequestMethod("POST");
            conn1.setRequestProperty("Content-Type", "application/json");
            conn1.setRequestProperty("Authorization", "Bearer " + bearerToken);
            conn1.setDoOutput(true);

            int responseCode = conn1.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            List<CarHutCar> cars;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn1.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response);
            }

            Thread.sleep(2000);

            CarHutCar addedCar = carHutCarRepository.getCarWithId(id);
            Assertions.assertNotNull(addedCar);
            Assertions.assertFalse(addedCar.isActive());

            // Flush data
            carHutCarRepository.delete(addedCar);

            Thread.sleep(1000);

            // Flushed data
            CarHutCar deletedCar = carHutCarRepository.getCarWithId(id);
            Assertions.assertNull(deletedCar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
