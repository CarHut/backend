package com.carhut.models.enums;

import java.util.ArrayList;
import java.util.List;

public class BodyType {

    public static final String SUV = "SUV";
    public static final String OTHER = "Other";
    public static final String TARGA = "Targa";
    public static final String VAN = "Van";
    public static final String BUS = "Bus";
    public static final String COUPE = "Coupé";
    public static final String TRUCK = "Truck";
    public static final String HATCHBACK = "Hatchback";
    public static final String LIMOUSINE = "Limousine";
    public static final String MINIBUS = "Minibus";
    public static final String CABRIO = "Cabrio";
    public static final String PICKUP = "Pickup";
    public static final String SEDAN = "Sedan";
    public static final String COMBI = "Combi";
    public static final String NOT_STATED = "Not stated";

    public List<String> getBodyTypes() {
        List<String> bodyTypesList = new ArrayList<>();
        bodyTypesList.add(SUV);
        bodyTypesList.add(OTHER);
        bodyTypesList.add(TARGA);
        bodyTypesList.add(VAN);
        bodyTypesList.add(BUS);
        bodyTypesList.add(COUPE);
        bodyTypesList.add(TRUCK);
        bodyTypesList.add(HATCHBACK);
        bodyTypesList.add(LIMOUSINE);
        bodyTypesList.add(MINIBUS);
        bodyTypesList.add(CABRIO);
        bodyTypesList.add(PICKUP);
        bodyTypesList.add(SEDAN);
        bodyTypesList.add(COMBI);
        bodyTypesList.add(NOT_STATED);
        return bodyTypesList;
    }
}
