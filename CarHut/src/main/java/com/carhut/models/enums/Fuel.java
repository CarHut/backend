package com.carhut.models.enums;

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

    public List<String> getFuelTypes() {
        List<String> fuelList = new ArrayList<>();
        fuelList.add(PETROL);
        fuelList.add(DIESEL);
        fuelList.add(HYBRID);
        fuelList.add(ELECTRIC);
        fuelList.add(LPG);
        fuelList.add(CNG);
        fuelList.add(NOT_STATED);
        return fuelList;
    }

}
