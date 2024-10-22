package com.carhut.model.enums;

import java.util.ArrayList;
import java.util.List;

public class Gearbox {

    public static final String SEQUENTIAL = "Sequential";
    public static final String MANUAL = "Manual";
    public static final String AUTOMATIC = "Automatic";
    public static final String NOT_STATED = "Not stated";
    private List<String> gearboxList = new ArrayList<>();

    public Gearbox() {
        gearboxList.add(SEQUENTIAL);
        gearboxList.add(MANUAL);
        gearboxList.add(AUTOMATIC);
        gearboxList.add(NOT_STATED);
    }

    public List<String> getGearboxTypes() {
        return gearboxList;
    }

}
