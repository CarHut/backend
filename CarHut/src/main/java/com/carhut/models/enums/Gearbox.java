package com.carhut.models.enums;

import java.util.ArrayList;
import java.util.List;

public class Gearbox {

    public static final String SEQUENTIAL = "Sequential";
    public static final String MANUAL = "Manual";
    public static final String AUTOMATIC = "Automatic";
    public static final String NOT_STATED = "Not stated";

    public List<String> getGearboxTypes() {
        List<String> gearboxList = new ArrayList<>();
        gearboxList.add(SEQUENTIAL);
        gearboxList.add(MANUAL);
        gearboxList.add(AUTOMATIC);
        gearboxList.add(NOT_STATED);
        return gearboxList;
    }

}
