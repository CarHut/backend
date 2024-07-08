package com.carhut.services.utils;

import com.carhut.CarHutApplication;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.services.util.Sorter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = CarHutApplication.class)
public class SorterTests {

    private Sorter sorter = new Sorter();

    @Test
    void sortCarsByPrice_ListIsNull() {
        try {
            List<CarHutCar> cars = sorter.sortCarsByPrice(null, "ASC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPrice_SortOrderIsNull() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByPrice(newArray, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPrice_ListIsNotNull_SortOrderIsNotValid() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByPrice(newArray, "NOT_VALID_SORT_ORDER");
            Assertions.assertEquals(1, cars.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPrice_ListIsNotNull_SortOrderIsValid_TestAscending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1");
        carHutCar2.setPrice("3");
        carHutCar3.setPrice("2");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPrice(cars, "ASC");

        Assertions.assertEquals("1", newList.get(0).getPrice());
        Assertions.assertEquals("2", newList.get(1).getPrice());
        Assertions.assertEquals("3", newList.get(2).getPrice());

    }

    @Test
    void sortCarsByPrice_ListIsNotNull_SortOrderIsValid_TestDescending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1");
        carHutCar2.setPrice("3");
        carHutCar3.setPrice("2");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPrice(cars, "DESC");

        Assertions.assertEquals("3", newList.get(0).getPrice());
        Assertions.assertEquals("2", newList.get(1).getPrice());
        Assertions.assertEquals("1", newList.get(2).getPrice());;

    }

    @Test
    void sortCarsByPower_ListIsNull() {
        try {
            List<CarHutCar> cars = sorter.sortCarsByPower(null, "ASC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPower_SortOrderIsNull() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByPower(newArray, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPower_ListIsNotNull_SortOrderIsNotValid() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByPower(newArray, "NOT_VALID_SORT_ORDER");
            Assertions.assertEquals(1, cars.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByPower_ListIsNotNull_SortOrderIsValid_TestAscending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("102.5");
        carHutCar2.setEnginePower("55");
        carHutCar3.setEnginePower("99.1");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPower(cars, "ASC");

        Assertions.assertEquals("55", newList.get(0).getEnginePower());
        Assertions.assertEquals("99.1", newList.get(1).getEnginePower());
        Assertions.assertEquals("102.5", newList.get(2).getEnginePower());
    }

    @Test
    void sortCarsByPower_ListIsNotNull_SortOrderIsValid_TestDescending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("102.5");
        carHutCar2.setEnginePower("55");
        carHutCar3.setEnginePower("99.1");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPower(cars, "DESC");

        Assertions.assertEquals("102.5", newList.get(0).getEnginePower());
        Assertions.assertEquals("99.1", newList.get(1).getEnginePower());
        Assertions.assertEquals("55", newList.get(2).getEnginePower());
    }

    @Test
    void sortCarsByMileage_ListIsNull() {
        try {
            List<CarHutCar> cars = sorter.sortCarsByMileage(null, "ASC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByMileage_SortOrderIsNull() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByMileage(newArray, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByMileage_ListIsNotNull_SortOrderIsNotValid() {
        try {
            List<CarHutCar> newArray = new ArrayList<>();
            newArray.add(new CarHutCar());
            List<CarHutCar> cars = sorter.sortCarsByMileage(newArray, "NOT_VALID_SORT_ORDER");
            Assertions.assertEquals(1, cars.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByMileage_ListIsNotNull_SortOrderIsValid_TestAscending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("100000");
        carHutCar2.setMileage("25000");
        carHutCar3.setMileage("2000000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByMileage(cars, "ASC");

        Assertions.assertEquals("25000", newList.get(0).getMileage());
        Assertions.assertEquals("100000", newList.get(1).getMileage());
        Assertions.assertEquals("2000000", newList.get(2).getMileage());
    }

    @Test
    void sortCarsByMileage_ListIsNotNull_SortOrderIsValid_TestDescending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("100000");
        carHutCar2.setMileage("25000");
        carHutCar3.setMileage("2000000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByMileage(cars, "DESC");

        Assertions.assertEquals("2000000", newList.get(0).getMileage());
        Assertions.assertEquals("100000", newList.get(1).getMileage());
        Assertions.assertEquals("25000", newList.get(2).getMileage());
    }

    @Test
    void sortCarsByAlphabet_ListIsNull() {
        try {
            List<CarHutCar> cars = sorter.sortCarsByAlphabet(null, "ASC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByAlphabet_SortOrderIsNull() {
        try {
            List<CarHutCar> cars = new ArrayList<>();
            CarHutCar car = new CarHutCar();
            cars.add(car);
            List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, null);
            Assertions.assertEquals(1, newList.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByAlphabet_ListIsNotNull_SortOrderIsInvalid() {
        try {
            List<CarHutCar> cars = new ArrayList<>();
            CarHutCar car = new CarHutCar();
            cars.add(car);
            List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, "INVALID_SORT_ORDER");
            Assertions.assertEquals(1, newList.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByAlphabet_ListIsNotNull_SortOrderIsValid_TestAscending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setHeader("Volkswagen");
        carHutCar2.setHeader("Audi");
        carHutCar3.setHeader("BMW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, "ASC");

        Assertions.assertEquals("Audi", newList.get(0).getHeader());
        Assertions.assertEquals("BMW", newList.get(1).getHeader());
        Assertions.assertEquals("Volkswagen", newList.get(2).getHeader());
    }

    @Test
    void sortCarsByAlphabet_ListIsNotNull_SortOrderIsValid_TestDescending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setHeader("Volkswagen");
        carHutCar2.setHeader("Audi");
        carHutCar3.setHeader("BMW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, "DESC");

        Assertions.assertEquals("Volkswagen", newList.get(0).getHeader());
        Assertions.assertEquals("BMW", newList.get(1).getHeader());
        Assertions.assertEquals("Audi", newList.get(2).getHeader());
    }

    @Test
    void sortCarsByAlphabet_ListIsNotNull_SortOrderIsValid_TestAscending_MultipleCarsWithSameStartingLetter() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setHeader("Abarth");
        carHutCar2.setHeader("Audi");
        carHutCar3.setHeader("Alfa Romeo");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, "ASC");

        Assertions.assertEquals("Abarth", newList.get(0).getHeader());
        Assertions.assertEquals("Alfa Romeo", newList.get(1).getHeader());
        Assertions.assertEquals("Audi", newList.get(2).getHeader());
    }

    @Test
    void sortCarsByAlphabet_ListIsNotNull_SortOrderIsValid_TestDescending_MultipleCarsWithSameStartingLetter() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setHeader("Abarth");
        carHutCar2.setHeader("Audi");
        carHutCar3.setHeader("Alfa Romeo");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByAlphabet(cars, "DESC");

        Assertions.assertEquals("Audi", newList.get(0).getHeader());
        Assertions.assertEquals("Alfa Romeo", newList.get(1).getHeader());
        Assertions.assertEquals("Abarth", newList.get(2).getHeader());
    }

    @Test
    void sortCarsByDateAdded_ListIsNull() {
        try {
            List<CarHutCar> cars = sorter.sortCarsByDateAdded(null, "ASC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByDateAdded_SortOrderIsNull() {
        try {
            List<CarHutCar> cars = new ArrayList<>();
            CarHutCar car = new CarHutCar();
            cars.add(car);
            List<CarHutCar> newList = sorter.sortCarsByDateAdded(cars, null);
            Assertions.assertEquals(1, newList.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByDateAdded_ListIsNotNull_SortOrderIsInvalid() {
        try {
            List<CarHutCar> cars = new ArrayList<>();
            CarHutCar car = new CarHutCar();
            cars.add(car);
            List<CarHutCar> newList = sorter.sortCarsByDateAdded(cars, "INVALID_SORT_ORDER");
            Assertions.assertEquals(1, newList.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void sortCarsByDateAdded_ListIsNotNull_SortOrderIsValid_TestAscending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setDateAdded(new Date(System.currentTimeMillis()));
        carHutCar2.setDateAdded(new Date(12345));
        carHutCar3.setDateAdded(new Date(54321));

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByDateAdded(cars, "ASC");

        Assertions.assertEquals("2", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        Assertions.assertEquals("1", newList.get(2).getId());
    }

    @Test
    void sortCarsByDateAdded_ListIsNotNull_SortOrderIsValid_TestDescending() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setDateAdded(new Date(System.currentTimeMillis()));
        carHutCar2.setDateAdded(new Date(12345));
        carHutCar3.setDateAdded(new Date(54321));

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByDateAdded(cars, "DESC");

        Assertions.assertEquals("1", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        Assertions.assertEquals("2", newList.get(2).getId());
    }

    @Test
    void sortCarsByPower_TwoValuesAreNotValid() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("");
        carHutCar2.setEnginePower("");
        carHutCar3.setEnginePower("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPower(cars, "ASC");

        Assertions.assertEquals("3", newList.get(0).getId());
        // Other values are not really necessary due to both not having a valid value so they are pushed to the end of the array
    }

    @Test
    void sortCarsByPower_OneValueIsNotValid_TestOne() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("");
        carHutCar2.setEnginePower("20");
        carHutCar3.setEnginePower("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPower(cars, "ASC");

        Assertions.assertEquals("2", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        // Other values are not really necessary due to both not having a valid value so they are pushed to the end of the array
    }

    @Test
    void sortCarsByPower_OneValueIsNotValid_TestTwo() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("20");
        carHutCar2.setEnginePower("");
        carHutCar3.setEnginePower("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByPower(cars, "ASC");

        Assertions.assertEquals("1", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        // Other values are not really necessary due to both not having a valid value so they are pushed to the end of the array
    }

    @Test
    void sortCarsByMileage_TwoValuesAreInvalid() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("");
        carHutCar2.setMileage("");
        carHutCar3.setMileage("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByMileage(cars, "ASC");

        Assertions.assertEquals("3", newList.get(0).getId());
        // Other values are not really necessary due to both not having a valid value, so they are pushed to the end of the array
    }

    @Test
    void sortCarsByMileage_OneValuesIsInvalid_TestOne() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("");
        carHutCar2.setMileage("20");
        carHutCar3.setMileage("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByMileage(cars, "DESC");

        Assertions.assertEquals("1", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        Assertions.assertEquals("2", newList.get(2).getId());
    }

    @Test
    void sortCarsByMileage_OneValuesIsInvalid_TestTwo() {
        List<CarHutCar> cars = new ArrayList<>();
        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("20");
        carHutCar2.setMileage("");
        carHutCar3.setMileage("123");

        carHutCar1.setId("1");
        carHutCar2.setId("2");
        carHutCar3.setId("3");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        List<CarHutCar> newList = sorter.sortCarsByMileage(cars, "DESC");

        Assertions.assertEquals("2", newList.get(0).getId());
        Assertions.assertEquals("3", newList.get(1).getId());
        Assertions.assertEquals("1", newList.get(2).getId());
    }
}
