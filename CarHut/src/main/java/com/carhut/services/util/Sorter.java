package com.carhut.services.util;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.temputils.models.TempCarModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Sorter {

    public List<CarHutCar> sortCarsByPrice(List<CarHutCar> cars, String sortOrder) {
        cars.sort(new Comparator<CarHutCar>() {
            @Override
            public int compare(CarHutCar car1, CarHutCar car2) {
                int price1 = extractNumberFromString(car1.getPrice());
                int price2 = extractNumberFromString(car2.getPrice());
                return sortOrder.equals("ASC") ? Integer.compare(price1, price2) : Integer.compare(price2, price1);
            }
        });

        return cars;
    }

    @Deprecated
    public List<TempCarModel> sortByPrice(List<TempCarModel> cars, String sortOrder) {
        cars.sort(new Comparator<TempCarModel>() {
            @Override
            public int compare(TempCarModel car1, TempCarModel car2) {
                int price1 = extractNumberFromString(car1.getPrice());
                int price2 = extractNumberFromString(car2.getPrice());
                return sortOrder.equals("ASC") ? Integer.compare(price1, price2) : Integer.compare(price2, price1);
            }
        });

        return cars;
    }

    @Deprecated
    public List<TempCarModel> sortByPower(List<TempCarModel> cars, String sortOrder) {

        cars.sort((o1, o2) -> {
            String powerStr1 = o1.getEnginePower();
            String powerStr2 = o2.getEnginePower();

            // Handling empty strings
            if (isStringFaulty(powerStr1) && isStringFaulty(powerStr2)) {
                return 0;
            } else if (isStringFaulty(powerStr1) && !isStringFaulty(powerStr2)) {
                return sortOrder.equals("ASC") ? 1 : -1; // Empty string should appear after non-empty string
            } else if (!isStringFaulty(powerStr1) && isStringFaulty(powerStr2)) {
                return sortOrder.equals("DESC") ? 1 : -1; // Empty string should appear after non-empty string
            }

            double power1 = extractDoubleNumberFromString(o1.getEnginePower());
            double power2 = extractDoubleNumberFromString(o2.getEnginePower());
            return sortOrder.equals("ASC") ? Double.compare(power1, power2) : Double.compare(power2, power1);
        });

        return cars;
    }

    public List<CarHutCar> sortCarsByPower(List<CarHutCar> cars, String sortOrder) {

        cars.sort((o1, o2) -> {
            String powerStr1 = o1.getEnginePower();
            String powerStr2 = o2.getEnginePower();

            // Handling empty strings
            if (isStringFaulty(powerStr1) && isStringFaulty(powerStr2)) {
                return 0;
            } else if (isStringFaulty(powerStr1) && !isStringFaulty(powerStr2)) {
                return sortOrder.equals("ASC") ? 1 : -1; // Empty string should appear after non-empty string
            } else if (!isStringFaulty(powerStr1) && isStringFaulty(powerStr2)) {
                return sortOrder.equals("DESC") ? 1 : -1; // Empty string should appear after non-empty string
            }

            double power1 = extractDoubleNumberFromString(o1.getEnginePower());
            double power2 = extractDoubleNumberFromString(o2.getEnginePower());
            return sortOrder.equals("ASC") ? Double.compare(power1, power2) : Double.compare(power2, power1);
        });

        return cars;
    }

    private int extractNumberFromString(String string) {
        if (isStringFaulty(string)) {
            return Integer.MAX_VALUE; // Assigning a large value to represent empty strings
        }

        try {
            return Integer.parseInt(string.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private double extractDoubleNumberFromString(String string) {
        if (isStringFaulty(string)) {
            return Double.MAX_VALUE;
        }

        try {
            return Double.valueOf(string.replaceAll("kW", ""));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    @Deprecated
    public List<TempCarModel> sortByMileage(List<TempCarModel> cars, String sortOrder) {
        cars.sort((car1, car2) -> {
            String mileageStr1 = car1.getMileage();
            String mileageStr2 = car2.getMileage();

            // Handling empty strings
            if (isStringFaulty(mileageStr1) && isStringFaulty(mileageStr2)) {
            } else if (isStringFaulty(mileageStr1)) {
                return sortOrder.equals("ASC") ? 1 : -1; // Empty string should appear after non-empty string
            } else if (isStringFaulty(mileageStr2)) {
                return sortOrder.equals("ASC") ? -1 : 1; // Empty string should appear after non-empty string
            }

            int mileage1 = extractNumberFromString(mileageStr1);
            int mileage2 = extractNumberFromString(mileageStr2);

            return sortOrder.equals("ASC") ? Integer.compare(mileage1, mileage2) : Integer.compare(mileage2, mileage1);
        });

        return cars;
    }

    public List<CarHutCar> sortCarsByMileage(List<CarHutCar> cars, String sortOrder) {
        cars.sort((car1, car2) -> {
            String mileageStr1 = car1.getMileage();
            String mileageStr2 = car2.getMileage();

            // Handling empty strings
            if (isStringFaulty(mileageStr1) && isStringFaulty(mileageStr2)) {
            } else if (isStringFaulty(mileageStr1)) {
                return sortOrder.equals("ASC") ? 1 : -1; // Empty string should appear after non-empty string
            } else if (isStringFaulty(mileageStr2)) {
                return sortOrder.equals("ASC") ? -1 : 1; // Empty string should appear after non-empty string
            }

            int mileage1 = extractNumberFromString(mileageStr1);
            int mileage2 = extractNumberFromString(mileageStr2);

            return sortOrder.equals("ASC") ? Integer.compare(mileage1, mileage2) : Integer.compare(mileage2, mileage1);
        });

        return cars;
    }

    public boolean isStringFaulty(String string) {
        return string.equals("neuvedené") || string.isEmpty() || string.equals("NotStated");
    }

    @Deprecated
    public List<TempCarModel> sortByAlphabet(List<TempCarModel> cars, String sortOrder) {
        cars.sort((car1, car2) -> {
            String header1 = car1.getHeader();
            String header2 = car2.getHeader();

            return sortOrder.equals("ASC") ? header1.compareTo(header2) : header2.compareTo(header1);
        });

        return cars;
    }

    public List<CarHutCar> sortCarsByAlphabet(List<CarHutCar> cars, String sortOrder) {
        if (sortOrder.equalsIgnoreCase("ASC")) {
            cars.sort(Comparator.comparing(CarHutCar::getHeader));
        } else if (sortOrder.equalsIgnoreCase("DESC")) {
            cars.sort(Comparator.comparing(CarHutCar::getHeader).reversed());
        } else {
            throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }

        return cars;
    }

    public List<CarHutCar> sortCarsByDateAdded(List<CarHutCar> cars, String sortOrder) {
        cars.sort((car1, car2) -> {
            Date date1 = car1.getDateAdded();
            Date date2 = car2.getDateAdded();

            return sortOrder.equals("ASC") ? date1.compareTo(date2) : date2.compareTo(date1);
        });

        return cars;
    }
}
