package com.carhut.model.enums;

import java.util.ArrayList;
import java.util.List;

public class Fuel {

    public static final String PETROL = "Petrol";
    public static final String DIESEL = "Diesel";
    public static final String HYBRID = "Hybrid";
    public static final String ELECTRIC = "Electric";
    public static final String LPG = "LPG";
    public static final String CNG = "CMG";
    public static final String NOT_STATED = "Not stated";

    private List<String> fuelTypes = new ArrayList<>();

    public Fuel() {
        fuelTypes.add(PETROL);
        fuelTypes.add(DIESEL);
        fuelTypes.add(HYBRID);
        fuelTypes.add(ELECTRIC);
        fuelTypes.add(LPG);
        fuelTypes.add(CNG);
        fuelTypes.add(NOT_STATED);
    }

    public List<String> getFuelTypes() {
        return fuelTypes;
    }

}
