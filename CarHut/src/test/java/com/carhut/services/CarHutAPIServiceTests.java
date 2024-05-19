package com.carhut.services;
//
//import com.carhut.models.carhut.*;
//import com.carhut.services.CarHutAPIService;
//import com.carhut.util.exceptions.CarHutException;
//import com.carhut.util.exceptions.carhutapi.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
@SpringBootTest
public class CarHutAPIServiceTests {
//
//    @Autowired
//    private CarHutAPIService carHutAPIService;
//
//    @Test
//    public void testGetAllBrands_arrayLengthShouldNotEqualZeroOrNull() {
//        List<Brand> brands = null;
//        try {
//            brands = carHutAPIService.getAllBrands();
//        } catch (CarHutAPIBrandsNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(brands, "Brands are null.");
//        Assertions.assertNotEquals(brands.size(), 0, "Brands size is 0.");
//    }
//
//    @Test
//    public void testGetModelsByBrand_shouldReturnSomethingBecauseBrandIsValidAndHasModels() {
//        List<Model> models = null;
//        try {
//            models = carHutAPIService.getModelsByBrand("BMW");
//        } catch (CarHutAPIModelsNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(models, "Brands are null.");
//        Assertions.assertNotEquals(0, models.size(), "Brands size is 0.");
//    }
//
//    @Test
//    public void testGetModelsByBrand_badInputShouldReturnArrayOfSizeZero() {
//        List<Model> models = null;
//        try {
//            models = carHutAPIService.getModelsByBrand("RANDOM_BRAND");
//        } catch (CarHutAPIModelsNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(models, "Brands are null.");
//        Assertions.assertEquals(0, models.size());
//    }
//
//    @Test
//    public void testGetModelsByBrandName_shouldReturnSomethingBecauseBrandIsValidAndHasModels() {
//        List<Model> models = null;
//        try {
//            models = carHutAPIService.getModelsByBrandName("BMW");
//        } catch (CarHutAPIModelsNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(models, "Brands are null.");
//        Assertions.assertNotEquals(0, models.size(), "Brands size is 0.");
//    }
//
//    @Test
//    public void testGetModelsByBrandName_badInputShouldReturnArrayOfSizeZero() {
//        List<Model> models = null;
//        try {
//            models = carHutAPIService.getModelsByBrandName("RANDOM_BRAND");
//        } catch (CarHutAPIModelsNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(models, "Brands are null.");
//        Assertions.assertEquals(0, models.size());
//    }
//
//    @Test
//    public void testGetBrandIdFromBrandName_existingBrand() {
//        // First in database with id 1
//        Integer brandId = null;
//        try {
//            brandId = carHutAPIService.getBrandIdFromBrandName("Audi");
//        } catch (CarHutAPIBrandNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(brandId, "brandId is null");
//        Assertions.assertEquals(1, brandId, "BrandId for Audi is not 1");
//    }
//
//    @Test
//    public void testGetBrandIdFromBrandName_imaginaryBrand_shouldReturnDefaultValue() {
//        // First in database with id 1
//        Integer brandId = null;
//        try {
//            brandId = carHutAPIService.getBrandIdFromBrandName("CAR_BRAND");
//        } catch (CarHutAPIBrandNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(brandId, "brandId is null");
//        Assertions.assertEquals(136, brandId);
//    }
//
//    @Test
//    public void testModelIfFromModelName_rightBrandId_wrongModel() {
//        Integer modelId = null;
//        try {
//            modelId = carHutAPIService.getModelIdFromModelName("BRAND_MODEL", 1);
//        } catch (CarHutAPIModelNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(modelId, "modelId is null");
//        Assertions.assertEquals(2264, modelId, "modelId is not default value");
//    }
//
//    @Test
//    public void testModelIfFromModelName_wrongBrandId_rightModel() {
//        Integer modelId = null;
//        try {
//            modelId = carHutAPIService.getModelIdFromModelName("Golf", 136);
//        } catch (CarHutAPIModelNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(modelId, "modelId is null");
//        Assertions.assertEquals(2264, modelId, "modelId is not default value");
//    }
//
//    @Test
//    public void testGetAllCars() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getAllCars();
//        } catch (CarHutAPICanNotGetCarsException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetAllCarsSorted_sortByPrice() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getAllCarsSorted("PFL", "ASC");
//        } catch (CarHutAPICanNotGetCarsException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetAllCarsSorted_sortByMileage() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getAllCarsSorted("MFL", "ASC");
//        } catch (CarHutAPICanNotGetCarsException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetAllCarsSorted_sortByPower() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getAllCarsSorted("SFL", "ASC");
//        } catch (CarHutAPICanNotGetCarsException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetAllCarsSorted_invalidSortBy_shouldReturnDefaultCarList() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getAllCarsSorted("RANDOM_SORT_BY", "ASC");
//        } catch (CarHutAPICanNotGetCarsException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetCarsWithFilters_noMultipleCarModels() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getCarsWithFilter("Audi", "A4", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null);
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetCarsWithFilters_multipleModels() {
//        List<ModelsPostModel> models = new ArrayList<>();
//        models.add(new ModelsPostModel("Audi", "A4"));
//        models.add(new ModelsPostModel("Audi", "A6"));
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getCarsWithFilter("Audi", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", models);
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetCarsWithFilters_everyParamIsNotEmptyStringApartFromNotImplementedSortingParams() {
//        List<CarHutCar> cars = null;
//        try {
//            cars = carHutAPIService.getCarsWithFilter("Audi", "A4", "", "0", "100000", "0", "500000", "", "", "", "", "", "Diesel", "0", "500", "", "Automatic", "AllWheel", "", "", null);
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNotNull(cars, "Car list is null");
//        Assertions.assertNotEquals(0, cars.size(), "car list is of size 0");
//    }
//
//    @Test
//    public void testGetNumberOfFilteredCars_notEmptyParams() {
//        int numberOfCars = 0;
//        try {
//            numberOfCars = carHutAPIService.getNumberOfFilteredCars("Audi", "A4", "", "0", "100000", "0", "500000", "", "", "", "", "", "Diesel", "0", "500", "", "Automatic", "AllWheel");
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNotEquals(0, numberOfCars, "car list is of size 0");
//    }
//
//    @Test
//    public void testGetNumberOfFilteredCars_emptyParams_shouldReturnAllCars() {
//        int numberOfCars = 0;
//        try {
//            numberOfCars = carHutAPIService.getNumberOfFilteredCars("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNotEquals(0, numberOfCars, "car list is of size 0");
//    }
//
//    @Test
//    public void testGetNumberOfFilteredCars_paramsThatWillGiveAZeroCarsFound() {
//        int numberOfCars = 0;
//        try {
//            numberOfCars = carHutAPIService.getNumberOfFilteredCars("RANDOM_BRAND", "RANDOM_MODEL", "", "99999999", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//        } catch (CarHutException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertEquals(0, numberOfCars, "car list is not of size 0");
//    }
//
//    @Test
//    public void testGetBodyTypes() {
//        List<String> bodyTypes = carHutAPIService.getBodyTypes();
//        Assertions.assertNotNull(bodyTypes, "Body types are null");
//        Assertions.assertNotEquals(0, bodyTypes.size(), "Size of body types list is 0");
//    }
//
//    @Test
//    public void testGetFuelTypes() {
//        List<String> fuelTypes = carHutAPIService.getFuelTypes();
//        Assertions.assertNotNull(fuelTypes, "Fuel types are null");
//        Assertions.assertNotEquals(0, fuelTypes.size(), "Size of fuel types list is 0");
//    }
//
//    @Test
//    public void testGetPowertrainTypes() {
//        List<String> powertrainTypes = carHutAPIService.getPowertrainTypes();
//        Assertions.assertNotNull(powertrainTypes, "powertrainTypes are null");
//        Assertions.assertNotEquals(0, powertrainTypes.size(), "Size of powertrainTypes list is 0");
//    }
//
//    @Test
//    public void testGetGearboxTypes() {
//        List<String> gearboxTypes = carHutAPIService.getGearboxTypes();
//        Assertions.assertNotNull(gearboxTypes, "Gearbox types are null");
//        Assertions.assertNotEquals(0, gearboxTypes.size(), "Size of gearboxTypes types list is 0");
//    }
//
//    @Test
//    public void testGetColors() {
//        List<Color> colors = null;
//        try {
//            colors = carHutAPIService.getColors();
//        } catch (CarHutAPICanNotGetColorsException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNotNull(colors, "colors are null");
//        Assertions.assertNotEquals(0, colors.size(), "Size of colors list is 0");
//    }
//
//    @Test
//    public void testGetFeatures() {
//        List<Feature> features = null;
//        try {
//            features = carHutAPIService.getFeatures();
//        } catch (CarHutAPICanNotGetFeaturesException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNotNull(features, "Features are null");
//        Assertions.assertNotEquals(0, features.size(), "Size of features list is 0");
//    }
//
//    @Test
//    public void testGetFeatureIdByFeatureName_invalidFeatureName() {
//        Integer id = null;
//        try {
//            id = carHutAPIService.getFeatureIdByFeatureName("INVALID_FEATURE");
//        } catch (CarHutAPICanNotGetFeaturesException e) {
//            throw new RuntimeException(e);
//        }
//        Assertions.assertNull(id, "Feature id is not null");
//    }
//
}
