package com.carhut.services.util;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.temputils.models.TempCarModel;

import java.util.List;

public class Filter {

    @Deprecated
    public void filterBrands(List<TempCarModel> cars, Integer brand) {
        cars.removeIf(car -> car.getBrandId() != brand);
    }

    @Deprecated
    public void filterModels(List<TempCarModel> cars, Integer model) {
        cars.removeIf(car -> car.getModelId() != model);
    }

    @Deprecated
    public void filterPriceFrom(List<TempCarModel> cars, String priceFrom) {
        if (priceFrom.equals("priceMore")) {
            cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) < 200000);
            return;
        }
        int priceFromInt = Integer.parseInt(priceFrom);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) < priceFromInt);
    }

    @Deprecated
    public void filterPriceTo(List<TempCarModel> cars, String priceTo) {
        if (priceTo.equals("priceMore")) {
            cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) > 200000);
            return;
        }
        int priceToInt = Integer.parseInt(priceTo);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) > priceToInt);
    }

    @Deprecated
    public void filterMileageFrom(List<TempCarModel> cars, String mileageFrom) {
        if (mileageFrom.equals("mileageMore")) {
            cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) < 200000);
        }
        int mileageFromInt = Integer.parseInt(mileageFrom);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) < mileageFromInt);
    }

    @Deprecated
    public void filterMileageTo(List<TempCarModel> cars, String mileageTo) {
        if (mileageTo.equals("mileageMore")) {
            cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) > 200000);
        }
        int mileageToInt = Integer.parseInt(mileageTo);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) > mileageToInt);
    }

    @Deprecated
    public void filterFuelType(List<TempCarModel> cars, String fuelType) {
        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    @Deprecated
    public void filterGearbox(List<TempCarModel> cars, String gearbox) {
        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    @Deprecated
    public void filterPowertrain(List<TempCarModel> cars, String powertrain) {
        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    @Deprecated
    public void filterPowerFrom(List<TempCarModel> cars, String powerFrom) {
        int powerFromToInt = Integer.parseInt(powerFrom);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) < powerFromToInt);
    }
    @Deprecated
    public void filterPowerTo(List<TempCarModel> cars, String powerTo) {
        int powerToToInt = Integer.parseInt(powerTo);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) > powerToToInt);
    }

    public void filterCarBrands(List<CarHutCar> cars, Integer brand) {
        cars.removeIf(car -> car.getBrandId() != brand);
    }

    public void filterCarModels(List<CarHutCar> cars, Integer model) {
        cars.removeIf(car -> car.getModelId() != model);
    }

    public void filterCarPriceFrom(List<CarHutCar> cars, String priceFrom) {
        if (priceFrom.equals("priceMore")) {
            cars.removeIf(car -> {
                try {
                    int price = Integer.parseInt(car.getPrice().replaceAll("€", "").trim());
                    return price < 200000;
                } catch (Exception e) {
                    return true;
                }
            });
            return;
        }
        int priceFromInt = Integer.parseInt(priceFrom);
        cars.removeIf(car -> {
            try {
                int price = Integer.parseInt(car.getPrice().replaceAll("€", "").trim());
                return price < priceFromInt;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarPriceTo(List<CarHutCar> cars, String priceTo) {
        // No need to filter out because there isn't upper limit
        if (priceTo.equals("priceMore")) {
            return;
        }
        int priceToInt = Integer.parseInt(priceTo);
        cars.removeIf(car -> {
            try {
                int price = Integer.parseInt(car.getPrice().replaceAll("€", "").trim());
                return price > priceToInt;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarMileageFrom(List<CarHutCar> cars, String mileageFrom) {
        if (mileageFrom.equals("mileageMore")) {
            cars.removeIf(car -> {
                try {
                    int mileage = Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "").trim());
                    return mileage < 200000;
                } catch (Exception e) {
                    return true;
                }
            });
            return;
        }
        int mileageFromInt = Integer.parseInt(mileageFrom);
        cars.removeIf(car -> {
            try {
                int mileage = Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "").trim());
                return mileage < mileageFromInt;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarMileageTo(List<CarHutCar> cars, String mileageTo) {
        // No need to filter out because there isn't upper limit
        if (mileageTo.equals("mileageMore")) {
            return;
        }
        int mileageToInt = Integer.parseInt(mileageTo);
        cars.removeIf(car -> {
            try {
                int mileage = Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "").trim());
                return mileage > mileageToInt;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarFuelType(List<CarHutCar> cars, String fuelType) {
        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    public void filterCarGearbox(List<CarHutCar> cars, String gearbox) {
        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    public void filterCarPowertrain(List<CarHutCar> cars, String powertrain) {
        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    public void filterCarPowerFrom(List<CarHutCar> cars, String powerFrom) {
        if (powerFrom.equals("powerMore")) {
            cars.removeIf(car -> {
                try {
                    double power = Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "").trim());
                    return power < 300;
                } catch (Exception e) {
                    return true;
                }
            });
            return;
        }
        int powerFromToInt = Integer.parseInt(powerFrom);
        cars.removeIf(car -> {
            try {
                double power = Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "").trim());
                return power < powerFromToInt;
            } catch (Exception e) {
                return true;
            }
        });
    }
    public void filterCarPowerTo(List<CarHutCar> cars, String powerTo) {
        // No need to filter out because there isn't upper limit
        if (powerTo.equals("powerMore")) {
            return;
        }
        int powerToToInt = Integer.parseInt(powerTo);
        cars.removeIf(car -> {
            try {
                double power = Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "").trim());
                return power > powerToToInt;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarTypes(List<CarHutCar> cars, List<String> carTypes) {
        cars.removeIf(car -> !carTypes.contains(car.getBodyType()));
    }

    public void filterCarRegistrationFrom(List<CarHutCar> cars, String registrationFrom) {
        // No lower limit
        if (registrationFrom.equals("registrationOlder")) {
            return;
        }
        cars.removeIf(car -> {
            try {
                return !car.getRegistration().equals("NotStated") ? Integer.parseInt(registrationFrom) > Integer.parseInt(car.getRegistration()) : false;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarRegistrationTo(List<CarHutCar> cars, String registrationTo) {
        if (registrationTo.equals("registrationOlder")) {
            cars.removeIf(car -> 1960 < Integer.parseInt(car.getRegistration()));
            return;
        }
        cars.removeIf(car -> {
            try {
                return !car.getRegistration().equals("NotStated") ? Integer.parseInt(registrationTo) < Integer.parseInt(car.getRegistration()) : false;
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarSeatingConfig(List<CarHutCar> cars, String seatingConfig) {
        cars.removeIf(car -> !car.getSeats().equals(seatingConfig));
    }

    public void filterCarDoors(List<CarHutCar> cars, String doors) {
        cars.removeIf(car -> !car.getDoors().equals(doors));
    }

    public void filterCarDisplacementFrom(List<CarHutCar> cars, String displacementFrom) {
        if (displacementFrom.equals("displacementMore")) {
            cars.removeIf(car -> {
                try {
                    return 200000 > Integer.parseInt(car.getEngineDisplacement());
                } catch (Exception e) {
                    return true;
                }
            });
            return;
        }
        cars.removeIf(car -> {
            try {
                return Integer.parseInt(displacementFrom) > Integer.parseInt(car.getEngineDisplacement());
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void filterCarDisplacementTo(List<CarHutCar> cars, String displacementTo) {
        if (displacementTo.equals("displacementMore")) {
            return;
        }
        cars.removeIf(car -> {
            try {
                return Integer.parseInt(displacementTo) < Integer.parseInt(car.getEngineDisplacement());
            } catch (Exception e) {
                return true;
            }
        });
    }
}
