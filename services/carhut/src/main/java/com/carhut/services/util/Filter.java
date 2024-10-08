package com.carhut.services.util;

import com.carhut.models.carhut.CarHutCar;

import java.util.List;

public class Filter {

    public void filterCarBrands(List<CarHutCar> cars, Integer brand) {
        if (cars == null || brand == null) {
            return;
        }

        cars.removeIf(car -> car.getBrandId() != brand);
    }

    public void filterCarModels(List<CarHutCar> cars, Integer model) {
        if (cars == null || model == null) {
            return;
        }

        cars.removeIf(car -> car.getModelId() != model);
    }

    public void filterCarPriceFrom(List<CarHutCar> cars, String priceFrom) {
        if (cars == null || priceFrom == null) {
            return;
        }

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

        if (cars == null || priceTo == null) {
            return;
        }

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
        if (cars == null || mileageFrom == null) {
            return;
        }

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
        if (cars == null || mileageTo == null) {
            return;
        }

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
        if (cars == null || fuelType == null) {
            return;
        }

        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    public void filterCarGearbox(List<CarHutCar> cars, String gearbox) {
        if (cars == null || gearbox == null) {
            return;
        }

        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    public void filterCarPowertrain(List<CarHutCar> cars, String powertrain) {
        if (cars == null || powertrain == null) {
            return;
        }

        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    public void filterCarPowerFrom(List<CarHutCar> cars, String powerFrom) {
        if (cars == null || powerFrom == null) {
            return;
        }

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
        if (cars == null || powerTo == null) {
            return;
        }

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
        if (cars == null || carTypes == null) {
            return;
        }

        cars.removeIf(car -> !carTypes.contains(car.getBodyType()));
    }

    public void filterCarRegistrationFrom(List<CarHutCar> cars, String registrationFrom) {
        if (cars == null || registrationFrom == null) {
            return;
        }

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
        if (cars == null || registrationTo == null) {
            return;
        }

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
        if (cars == null || seatingConfig == null) {
            return;
        }

        cars.removeIf(car -> !car.getSeats().equals(seatingConfig));
    }

    public void filterCarDoors(List<CarHutCar> cars, String doors) {
        if (cars == null || doors == null) {
            return;
        }

        cars.removeIf(car -> !car.getDoors().equals(doors));
    }

    public void filterCarDisplacementFrom(List<CarHutCar> cars, String displacementFrom) {
        if (cars == null || displacementFrom == null) {
            return;
        }

        if (displacementFrom.equals("displacementMore")) {
            cars.removeIf(car -> {
                try {
                    return 6000 > Integer.parseInt(car.getEngineDisplacement());
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
        if (cars == null || displacementTo == null) {
            return;
        }

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
